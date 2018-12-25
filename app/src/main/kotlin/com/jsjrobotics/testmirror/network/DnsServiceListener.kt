package com.jsjrobotics.testmirror.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log


class DnsServiceListener(val nsdManager: NsdManager) : NsdManager.DiscoveryListener {
    private val TAG = javaClass.simpleName

    override fun onServiceFound(service: NsdServiceInfo) {
        // A service was found! Do something with it.
        Log.d(TAG, "Service discovery success$service")
        when {
            service.serviceType != SERVICE_TYPE -> // Service type is the string containing the protocol and
                // transport layer for this service.
                Log.d(TAG, "Unknown Service Type: ${service.serviceType}")
            service.serviceName == mServiceName -> // The name of the service tells the user what they'd be
                // connecting to. It could be "Bob's Chat App".
                Log.d(TAG, "Same machine: $mServiceName")
            service.serviceName.contains("NsdChat") -> mNsdManager.resolveService(service, mResolveListener)
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


    override fun onDiscoveryStarted(serviceType: String) { /* Do Nothing */}

    override fun onDiscoveryStopped(serviceType: String) { /* Do Nothing */}

    override fun onServiceLost(serviceInfo: NsdServiceInfo) { /* Do Nothing */}
}