package com.jsjrobotics.testmirror

import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.jsjrobotics.testmirror.service.RemoteMirrorState

typealias ConnectedMirrorMap = Map<NsdServiceInfo, RemoteMirrorState>

fun runOnUiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper())
            .post(action)
}

fun Any.ERROR(message: String) {
    Log.e(javaClass.simpleName, message)
}

fun Any.DEBUG(message: String) {
    Log.d(javaClass.simpleName, message)
}