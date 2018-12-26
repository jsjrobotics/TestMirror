package com.jsjrobotics.testmirror.network

import android.annotation.SuppressLint
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.jsjrobotics.testmirror.DEBUG
import com.jsjrobotics.testmirror.ERROR
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A class for receiving found Mirror Services.
 * Subscribing to onServiceInfoDiscovered gives all resolved Mirror services at burst intervals.
 * The set of resolved services is broadcast each time one is added or removed.
 */
@Singleton
class DnsServiceListener @Inject constructor(val nsdManager: NsdManager) {

    private val serviceInfoDiscovered : PublishSubject<Set<NsdServiceInfo>> = PublishSubject.create()
    val onServiceInfoDiscovered : Observable<Set<NsdServiceInfo>> = serviceInfoDiscovered
    private val TAG = javaClass.simpleName
    private val serviceType = "_mirror._tcp."
    private val serviceNameMatcher = Regex("^mirror-.*|^Android$")
    private val resolvedServiceInfo: MutableSet<NsdServiceInfo> = mutableSetOf()
    private val THREE_SECONDS_MS: Long = 3 * 1000
    private var reportResultsDisposable: Disposable? = null

    private var resolving: Boolean = false
    private val servicesToResolve: MutableList<NsdServiceInfo> = mutableListOf()
    private val discoveryListener : NsdManager.DiscoveryListener = object : NsdManager.DiscoveryListener {
        override fun onServiceFound(service: NsdServiceInfo) {
            if (service.serviceType == serviceType && serviceNameMatcher.matches(service.serviceName)) {
                if (!resolving) {
                    nsdManager.resolveService(service, buildResolveListener())
                    resolving = true
                } else {
                    synchronized(servicesToResolve) { servicesToResolve.add(service) }

                }
            }
        }


        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }


        override fun onDiscoveryStarted(serviceType: String) { Log.e(TAG, "Discovery started") }

        override fun onDiscoveryStopped(serviceType: String) { /* Do Nothing */ }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) { /* Do Nothing */ }
    }


    private fun buildResolveListener(): NsdManager.ResolveListener {
        return object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Resolve failed: $errorCode")
                resolvedServiceInfo.remove(serviceInfo)
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                resolvedServiceInfo.add(serviceInfo)
                synchronized(servicesToResolve) {
                    if (servicesToResolve.size > 0) {
                        val nextService = servicesToResolve.removeAt(0)
                        nsdManager.resolveService(nextService, buildResolveListener())
                    } else {
                        resolving = false
                        startReportResultsTimer()
                    }
                }
            }
        }
    }

    private fun startReportResultsTimer() {
        reportResultsDisposable?.dispose()
        reportResultsDisposable = Observable.timer(THREE_SECONDS_MS, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe{
                    DEBUG("Notifying of Mirrors found: ${resolvedServiceInfo.size}")
                    stopDiscovery()
                    serviceInfoDiscovered.onNext(resolvedServiceInfo)
                }
    }

    @SuppressLint("CheckResult")
    fun discoverServices() {
        resolvedServiceInfo.clear()
        servicesToResolve.clear()
        resolving = false
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener )

    }

    fun stopDiscovery() {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }
}