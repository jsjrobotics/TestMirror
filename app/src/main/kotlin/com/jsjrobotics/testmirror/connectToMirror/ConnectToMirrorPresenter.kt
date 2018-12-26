package com.jsjrobotics.testmirror.connectToMirror

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import javax.inject.Inject

class ConnectToMirrorPresenter
    @Inject constructor(val model: ConnectToMirrorModel): DefaultPresenter() {
    private lateinit var view: ConnectToMirrorView

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
        val serviceNames = mirrors.map {it.serviceInfo.serviceName }
        view.displayMirrors(serviceNames)

    }

    fun bind(view: ConnectToMirrorView) {
        this.view = view
    }
}
