package com.jsjrobotics.testmirror.connectToMirror

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import javax.inject.Inject

class ConnectToMirrorPresenter @Inject constructor(val model: ConnectToMirrorModel): DefaultPresenter() {
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
        displayedMirrors.clear()
        displayedMirrors.addAll(mirrors)
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
        disposables.addAll(mirrorSelectedDisposable, connectDisposable)
    }
}
