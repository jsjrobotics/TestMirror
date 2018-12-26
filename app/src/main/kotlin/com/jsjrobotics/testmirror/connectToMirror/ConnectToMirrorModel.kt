package com.jsjrobotics.testmirror.connectToMirror

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.network.DnsServiceListener
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConnectToMirrorModel @Inject constructor(val dnsServiceListener: DnsServiceListener) {

    private var mirrorSubscription: Disposable? = null
    private val savedMirrors : BehaviorSubject<Set<NsdServiceInfo>> = BehaviorSubject.create()
    val onMirrorDisocvered : Observable<Set<NsdServiceInfo>> = savedMirrors
    fun startDiscovery() {
        if (mirrorSubscription == null) {
            mirrorSubscription = dnsServiceListener.onServiceInfoDiscovered.subscribe(this::saveDiscoveredMirrors, this::handleError)
        }
        dnsServiceListener.discoverServices()
    }

    fun stopDiscovery() {
        mirrorSubscription?.dispose()
        mirrorSubscription = null
        dnsServiceListener.stopDiscovery()
    }

    private fun handleError(error: Throwable) {
        ERROR("OnServiceInfoDiscovered threw error: ${error.message}")
    }

    private fun saveDiscoveredMirrors(mirrors :Set<NsdServiceInfo>) {
        savedMirrors.onNext(mirrors)
    }
}