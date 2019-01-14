package com.jsjrobotics.testmirror.settings

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.service.RemoteMirrorState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsModel @Inject constructor(val application: Application) {
    fun requestConnectedMirrors() {
        val connectedMirrors : Map<NsdServiceInfo, RemoteMirrorState>? = application.webSocketService?.connectedMirrors as Map<NsdServiceInfo, RemoteMirrorState>?
        // TODO DISPLAY mirrors
    }

}
