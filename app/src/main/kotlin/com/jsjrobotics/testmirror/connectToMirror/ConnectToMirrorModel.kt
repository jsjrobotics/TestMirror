package com.jsjrobotics.testmirror.connectToMirror

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.network.DnsServiceListener
import com.jsjrobotics.testmirror.service.websocket.ClientStateDispatcher
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.net.InetAddress
import javax.inject.Inject

class ConnectToMirrorModel @Inject constructor(private val application: Application,
                                               private val dnsServiceListener: DnsServiceListener,
                                               private val clientStateDispatcher: ClientStateDispatcher) {

    private var connectDisposable: Disposable? = null
    private var mirrorSubscription: Disposable? = null
    private val savedMirrors : BehaviorSubject<Set<ResolvedMirrorData>> = BehaviorSubject.create()
    val onMirrorDiscovered : Observable<Set<ResolvedMirrorData>> = savedMirrors

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

    fun unsubscribe() {
        connectDisposable?.dispose()
        connectDisposable = null
        mirrorSubscription?.dispose()
        mirrorSubscription = null
    }

    private fun handleError(error: Throwable) {
        ERROR("OnServiceInfoDiscovered threw error: ${error.message}")
    }

    private fun saveDiscoveredMirrors(mirrors : Set<ResolvedMirrorData>) {
        savedMirrors.onNext(mirrors)
    }

    fun connectToClient(host: InetAddress) {
        connectDisposable = clientStateDispatcher.onOpenEvent.subscribe { connected ->
            if (connected) {
                handleConnected()
            } else {
                handleDisconnected()
            }
        }
        application.webSocketService?.connectToClient(host.hostAddress)
    }

    private fun handleDisconnected() {

    }

    private fun handleConnected() {
        application.webSocketService?.sendIdentifyRequest()
    }


    fun sendPairingCode(code: String) {
        application.webSocketService?.sendPairingCode(code)
    }
}