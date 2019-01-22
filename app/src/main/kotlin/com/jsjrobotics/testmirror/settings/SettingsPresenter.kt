package com.jsjrobotics.testmirror.settings

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.service.RemoteMirrorState
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(private val application: Application){
    private lateinit var view: SettingsView

    private val disposables = CompositeDisposable()

    fun init(v: SettingsView, launchConnectMirror : () -> Unit) {
        view = v
        val connectAnotherDisposable = view.onConnectAnother.subscribe {
            launchConnectMirror.invoke()
        }
        disposables.addAll(connectAnotherDisposable)
    }

    @Suppress("UNCHECKED_CAST")
    fun loadData() {
        Thread {
            // Running on the main thread freezes ui
            val connectedMirrors = application.webSocketService?.connectedMirrors as Map<NsdServiceInfo, RemoteMirrorState>?
            view.displayConnectedMirrors(connectedMirrors ?: emptyMap())
        }.start()

    }

}
