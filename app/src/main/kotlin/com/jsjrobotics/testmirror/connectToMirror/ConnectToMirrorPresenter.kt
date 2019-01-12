package com.jsjrobotics.testmirror.connectToMirror

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConnectToMirrorPresenter @Inject constructor(
        private val model: ConnectToMirrorModel): DefaultPresenter() {

    private lateinit var view: ConnectToMirrorView
    private var displayedMirrors: MutableList<ResolvedMirrorData> = mutableListOf()
    private var selectedMirror: ResolvedMirrorData? = null
    private val disposables = CompositeDisposable()


    private fun displayMirrors(mirrors : Set<ResolvedMirrorData>) {
        displayedMirrors = mirrors.toMutableList()
        val serviceNames = displayedMirrors.map ( this::getMirrorName )
        view.displayMirrorsScreen(serviceNames)

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
        val displayPairingDisposable = model.onPairingNeeded.subscribe { view.showPairingInput() }
        val pairingErrorDisposable = model.onPairingError.subscribe { view.showPairingError()}
        val displayMirrorDisposable = model.onMirrorDiscovered.subscribe(this::displayMirrors, { ERROR("Failed to load mirrors")})
        val rescanDisposable = view.onRescanButtonClicked.subscribe {
            startMirrorSearch()
        }
        val mirrorSelectedDisposable = view.onMirrorSelected.subscribe { selectedMirrorIndex ->
            val chosenMirror = displayedMirrors[selectedMirrorIndex]
            if (chosenMirror == selectedMirror) {
                view.unselectMirror(selectedMirrorIndex)
                view.toggleConnectRescanButton(true)
                selectedMirror = null
            } else {
                selectedMirror = chosenMirror
                view.setMirrorSelected(selectedMirrorIndex)
                view.toggleConnectRescanButton(false)
            }
        }
        val connectDisposable = view.onConnectButtonClicked.subscribe { ignored ->
            selectedMirror?.let {
                view.showConnecting(getMirrorName(it))
                model.connectToClient(it.serviceInfo.host)
            }
        }
        val pariingCodeDisposable = view.onSendPairingButtonClicked.subscribe(model::sendPairingCode)
        disposables.addAll(displayMirrorDisposable,
                mirrorSelectedDisposable,
                connectDisposable,
                displayPairingDisposable,
                pariingCodeDisposable,
                pairingErrorDisposable,
                rescanDisposable)
        startMirrorSearch()
    }

    private fun startMirrorSearch() {
        model.startDiscovery()
        view.displayLoadingScreen()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
        model.unsubscribe()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun stopMirrorDiscovery () {
        model.stopDiscovery()
    }
}
