package com.jsjrobotics.testmirror.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A class for receiving found Mirror Services.
 * Subscribing to onServiceInfoDiscovered gives all resolved Mirror services at burst intervals.
 * The set of resolved services is broadcast each time one is added or removed.
 */
@Singleton
class DnsServiceListener(val nsdManager: NsdManager) {

    private val serviceInfoDiscovered : PublishSubject<Set<NsdServiceInfo>> = PublishSubject.create()
    val onServiceInfoDiscovered : Observable<Set<NsdServiceInfo>> = serviceInfoDiscovered.throttleLast(200, TimeUnit.MILLISECONDS, Schedulers.io())
    private val TAG = javaClass.simpleName
    private val serviceType = "_mirror._tcp."
    private val serviceNameMatcher = Regex("^mirror-|^Android$")
    private val resolvedServiceInfo: Set<NsdServiceInfo> = mutableSetOf()

    var resolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e(TAG, "Resolve failed: $errorCode")
            resolvedServiceInfo.minus(serviceInfo)
            serviceInfoDiscovered.onNext(resolvedServiceInfo)
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            resolvedServiceInfo.plus(serviceInfo)
            serviceInfoDiscovered.onNext(resolvedServiceInfo)
        }
    }

    private val discoveryListener : NsdManager.DiscoveryListener = object : NsdManager.DiscoveryListener {
        override fun onServiceFound(service: NsdServiceInfo) {
            if (service.serviceType == serviceType && serviceNameMatcher.matches(service.serviceName)) {
                nsdManager.resolveService(service, resolveListener)
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


        override fun onDiscoveryStarted(serviceType: String) { /* Do Nothing */ }

        override fun onDiscoveryStopped(serviceType: String) { /* Do Nothing */ }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) { /* Do Nothing */ }
    }


    fun discoverServices() {
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener )
    }

    fun stopDiscovery() {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }
}