package com.jsjrobotics.testmirror.settings

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.service.RemoteMirrorState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsModel @Inject constructor(val application: Application) {
    private val connectedMirrorsReceived : PublishSubject<Map<NsdServiceInfo, RemoteMirrorState>> = PublishSubject.create()
    val onConnectedMirrorsReceived : Observable<Map<NsdServiceInfo, RemoteMirrorState>> = connectedMirrorsReceived

    @Suppress("UNCHECKED_CAST")
    fun requestConnectedMirrors() {
        val connectedMirrors : Map<NsdServiceInfo, RemoteMirrorState>? = application.webSocketService?.connectedMirrors as Map<NsdServiceInfo, RemoteMirrorState>?
        connectedMirrors?.let (connectedMirrorsReceived::onNext )
    }

}
