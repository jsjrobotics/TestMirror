package com.jsjrobotics.testmirror.connectToMirror

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.network.ProtoBufMessageBroker
import com.mirror.proto.user.IdentifyResponse
import javax.inject.Inject

class ConnectToMirrorPresenter @Inject constructor(
        private val model: ConnectToMirrorModel,
        private val protoBufMessageBroker: ProtoBufMessageBroker,
        private val navigationController: NavigationController): DefaultPresenter() {
    private lateinit var view: ConnectToMirrorView
    private var displayedMirrors: MutableList<ResolvedMirrorData> = mutableListOf()
    private var selectedMirror: ResolvedMirrorData? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun startMirrorDiscovery() {
        val displayMirrorDisposable = model.onMirrorDiscovered.subscribe(this::displayMirrors, { ERROR("Failed to load mirrors")})
        disposables.add(displayMirrorDisposable)
        model.startDiscovery()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun stopMirrorDiscovery () {
        model.stopDiscovery()
        disposables.dispose()
    }

    private fun displayMirrors(mirrors : Set<ResolvedMirrorData>) {
        displayedMirrors = mirrors.toMutableList()
        val serviceNames = displayedMirrors.map ( this::getMirrorName )
        view.displayMirrors(serviceNames)

    }

    private fun getMirrorName(resolvedMirror: ResolvedMirrorData) : String {
        val mirrorName = resolvedMirror.mirrorData?.name ?: ""
        if (mirrorName.isNotEmpty()) {
            return mirrorName
        }
        return resolvedMirror.serviceInfo.serviceName
    }

    fun bind(v: ConnectToMirrorView) {
        view = v
        val mirrorSelectedDisposable = view.onMirrorSelected.subscribe { selectedMirrorIndex ->
            val chosenMirror = displayedMirrors[selectedMirrorIndex]
            if (chosenMirror == selectedMirror) {
                view.unselectMirror(selectedMirrorIndex)
                view.disableConnectButton()
                selectedMirror = null
            } else {
                selectedMirror = chosenMirror
                view.setMirrorSelected(selectedMirrorIndex)
                view.enableConnectButton()
            }
        }
        val connectDisposable = view.onConnectButtonClicked.subscribe { ignored ->
            selectedMirror?.let {
                view.showConnecting(getMirrorName(it))
                model.connectToClient(it.serviceInfo.host)
            }

        }
        val identityDisposable = protoBufMessageBroker.onIdentifyResponse.subscribe(this::onIdentityResponse)
        val pariingCodeDisposable = view.onSendPairingButtonClicked.subscribe(this::receivePairingCode)
        disposables.addAll(mirrorSelectedDisposable, connectDisposable, identityDisposable, pariingCodeDisposable)
    }

    private fun receivePairingCode(code: String) {
        model.sendPairingCode(code)
    }

    private fun onIdentityResponse(response: IdentifyResponse) {
        if (response.pairingRequired) {
            view.showPairingInput()
        } else {
            navigationController.showProfile()
        }
    }
}
