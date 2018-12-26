package com.jsjrobotics.testmirror

import android.os.Handler
import android.os.Looper
import android.util.Log

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