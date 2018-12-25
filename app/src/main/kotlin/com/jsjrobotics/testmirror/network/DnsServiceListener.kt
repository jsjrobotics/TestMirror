package com.jsjrobotics.testmirror.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Singleton
class DnsServiceListener(val nsdManager: NsdManager) {

    private val serviceInfoDiscovered : PublishSubject<NsdServiceInfo> = PublishSubject.create()
    val onServiceInfoDiscovered : Observable<NsdServiceInfo> = serviceInfoDiscovered
    private val TAG = javaClass.simpleName
    private val serviceType = "_mirror._tcp."
    private val serviceNameMatcher = Regex("^mirror-|^Android$")
    private var resolvedServiceInfo: NsdServiceInfo? = null

    private val discoveryListener : NsdManager.DiscoveryListener = object : NsdManager.DiscoveryListener {
        override fun onServiceFound(service: NsdServiceInfo) {
            if (service.serviceType == serviceType && serviceNameMatcher.matches(service.serviceName)) {
                nsdManager.resolveService(service, buildResolveListener())
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

    private fun buildResolveListener() : NsdManager.ResolveListener {
        return object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                resolvedServiceInfo = serviceInfo
            }
        }
    }

    fun discoverServices() {
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener )
    }
}