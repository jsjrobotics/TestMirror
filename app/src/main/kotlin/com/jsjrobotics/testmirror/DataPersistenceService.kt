package com.jsjrobotics.testmirror

import android.app.Service
import android.content.Intent
import android.os.IBinder

class DataPersistenceService : Service() {
    private val binder = object : IDataPersistence.Stub() {
        override fun getProfileData(profile: String?): String {
            return ""
        }

        override fun registerCallback(callback: IProfileCallback?) {}

        override fun unregisterCallback(callback: IProfileCallback?) {}

    }
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}