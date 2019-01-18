package com.jsjrobotics.testmirror.connectToMirror

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.MirrorStateDispatcher
import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.network.DnsServiceListener
import com.jsjrobotics.testmirror.network.ProtoBufMessageDispatcher
import com.jsjrobotics.testmirror.service.websocket.ClientStateDispatcher
import com.jsjrobotics.testmirror.service.websocket.tasks.ClientState
import com.mirror.proto.oobe.PairResponse
import com.mirror.proto.user.IdentifyResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConnectToMirrorModel @Inject constructor(private val application: Application,
                                               private val dnsServiceListener: DnsServiceListener,
                                               private val clientStateDispatcher: ClientStateDispatcher,
                                               private val navigationController: NavigationController,
                                               private val mirrorStateDispatcher: MirrorStateDispatcher,
                                               private val protoBufMessageDispatcher: ProtoBufMessageDispatcher) {

    private var connectDisposable: Disposable? = null
    private var mirrorSubscription: Disposable? = null
    private val connectingDisposables : CompositeDisposable = CompositeDisposable()

    private val savedUnconnectedMirrors : BehaviorSubject<Set<ResolvedMirrorData>> = BehaviorSubject.create()
    val onMirrorDiscovered : Observable<Set<ResolvedMirrorData>> = savedUnconnectedMirrors

    private val pairingNeeded : PublishSubject<Boolean> = PublishSubject.create()
    val onPairingNeeded : Observable<Boolean> = pairingNeeded

    private val pairingError : PublishSubject<Boolean> = PublishSubject.create()
    val onPairingError : Observable<Boolean> = pairingError

    private val connectionSuccess : PublishSubject<Boolean> = PublishSubject.create()
    val onConnectionSuccess : Observable<Boolean> = connectionSuccess

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

        connectingDisposables.clear()
    }

    private fun handleError(error: Throwable) {
        ERROR("OnServiceInfoDiscovered threw error: ${error.message}")
    }

    private fun saveDiscoveredMirrors(mirrors : Set<ResolvedMirrorData>) {
        val connectedMirrors = application.webSocketService?.connectedMirrors?.keys as Set<NsdServiceInfo>? ?: emptySet()
        val unconnectedMirrors = mirrors.filter { !connectedMirrors.contains(it.serviceInfo) }
        savedUnconnectedMirrors.onNext(unconnectedMirrors.toSet())
    }

    fun connectToClient(info: NsdServiceInfo) {
        val mirrorToConnect = savedUnconnectedMirrors.value.firstOrNull{ it.serviceInfo == info}
        if (mirrorToConnect == null) {
            ERROR("Unable to find saved mirror to connect to")
            return
        }
        connectDisposable = clientStateDispatcher.onOpenEvent
                .filter{ info == it.socketManager.serviceInfo }
                .subscribe { state ->
                    if (state.isConnected) {
                        handleConnected(state)
                    } else {
                        handleDisconnected()
                    }
        }
        application.webSocketService?.connectToClient(mirrorToConnect)
    }

    private fun handleDisconnected() {
        unsubscribe()
    }

    private fun handleConnected(state: ClientState) {
        val identityDisposable = protoBufMessageDispatcher.onIdentifyResponse.subscribe(this::onIdentityResponse)
        val pairResponseDisposable = protoBufMessageDispatcher.onPairResponseEvent.subscribe(this::onPairResponse)
        connectingDisposables.addAll(identityDisposable, pairResponseDisposable)
        application.webSocketService?.sendIdentifyRequest(state.socketManager.serviceInfo)
    }

    private fun onIdentityResponse(response: IdentifyResponse) {
        if (response.pairingRequired) {
            pairingNeeded.onNext(true)
        } else {
            notifyConnected()
        }
    }

    fun sendPairingCode(serviceInfo: NsdServiceInfo, code: String) {
        application.webSocketService?.sendPairingCode(serviceInfo, code)
    }

    private fun onPairResponse(response: PairResponse) {
        if (response.success) {
            notifyConnected()
        } else {
            pairingError.onNext(true)
        }
    }

    private fun notifyConnected() {
        connectionSuccess.onNext(true)
        mirrorStateDispatcher.onConnectedToMirror()
        navigationController.removeConnectToMirrorFragment()
    }


}