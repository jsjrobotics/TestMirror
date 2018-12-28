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
        val serviceNames = displayedMirrors.map {
            val mirrorName = it.mirrorData?.name ?: ""
            if (mirrorName.isNotEmpty()) {
                return@map mirrorName
            }
            return@map it.serviceInfo.serviceName
        }
        view.displayMirrors(serviceNames)

    }

    fun bind(v: ConnectToMirrorView) {
        view = v
        val mirrorSelectedDisposable = view.onMirrorSelected.subscribe { selectedMirrorIndex ->
            selectedMirror = displayedMirrors[selectedMirrorIndex]
            view.setMirrorSelected(selectedMirrorIndex)
            view.enableConnectButton()
        }
        disposables.add(mirrorSelectedDisposable)

    }
}
