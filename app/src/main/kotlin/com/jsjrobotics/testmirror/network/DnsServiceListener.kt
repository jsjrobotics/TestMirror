package com.jsjrobotics.testmirror.network

import android.annotation.SuppressLint
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.service.networking.MirrorPeerToPeerApi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit



/**
 * A class for receiving found Mirror Services.
 * Subscribing to onServiceInfoDiscovered gives all resolved Mirror services at burst intervals.
 * The set of resolved services is broadcast each time one is added or removed.
 */
@Singleton
class DnsServiceListener @Inject constructor(val nsdManager: NsdManager) {

    private val serviceInfoDiscovered : PublishSubject<Set<ResolvedMirrorData>> = PublishSubject.create()
    val onServiceInfoDiscovered : Observable<Set<ResolvedMirrorData>> = serviceInfoDiscovered
    private val TAG = javaClass.simpleName
    private val serviceType = "_mirror._tcp."
    private val serviceNameMatcher = Regex("^mirror-.*|^Android$")
    private val resolvedServiceInfo: MutableSet<NsdServiceInfo> = mutableSetOf()
    private val THREE_SECONDS_MS: Long = 3 * 1000
    private var reportResultsDisposable: Disposable? = null
    private var isDiscovering: Boolean = true

    private val servicesToResolve: MutableList<NsdServiceInfo> = mutableListOf()
    private val discoveryListener : NsdManager.DiscoveryListener = object : NsdManager.DiscoveryListener {
        override fun onServiceFound(service: NsdServiceInfo) {
            if (service.serviceType == serviceType && serviceNameMatcher.matches(service.serviceName)) {
                servicesToResolve.add(service)
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
                resolveServices()
            }
        }
    }

    private fun resolveServices() {
        if (servicesToResolve.size > 0) {
            val nextService = servicesToResolve.removeAt(0)
            nsdManager.resolveService(nextService, buildResolveListener())
        } else {
            resolveMirrorData()
        }
    }

    private fun resolveMirrorData() {
        val mirrorDataToResolve : MutableSet<NsdServiceInfo> = mutableSetOf<NsdServiceInfo>().apply {
            addAll(resolvedServiceInfo)
        }
        mirrorDataToResolve.forEach{ requestMirrorData(it) }
        serviceInfoDiscovered.onNext(mirrorDataResolved.values.toSet())

    }

    private val mirrorDataResolved = mutableMapOf<NsdServiceInfo, ResolvedMirrorData>()

    private fun requestMirrorData(serviceInfo: NsdServiceInfo) {
        val baseUrl = "http://${serviceInfo.host.hostAddress}:8080/"
        try {
            val result = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MirrorPeerToPeerApi::class.java)
                    .getMirrorData()
                    .execute()
            val bodyResponse = result.body()
            mirrorDataResolved[serviceInfo] = ResolvedMirrorData(serviceInfo, bodyResponse)
        } catch (e: Exception) {
            ERROR("Failed to download login data: $e")
            mirrorDataResolved[serviceInfo] = ResolvedMirrorData(serviceInfo)
        }
    }

    @SuppressLint("CheckResult")
    fun discoverServices() {
        resolvedServiceInfo.clear()
        servicesToResolve.clear()
        reportResultsDisposable?.dispose()
        isDiscovering = true
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener )
        reportResultsDisposable = Observable.timer(THREE_SECONDS_MS, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe{
                    isDiscovering = false
                    stopDiscovery()
                    resolveServices()
                }

    }

    fun stopDiscovery() {
        if (isDiscovering) {
            nsdManager.stopServiceDiscovery(discoveryListener)
            isDiscovering = false
        }
    }
}