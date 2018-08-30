package com.jsjrobotics.testmirror

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import com.jsjrobotics.testmirror.dataStructures.CacheSettings
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import java.util.concurrent.Executors

class DataPersistenceService : Service() {
    private val SERVICE_THREAD_COUNT: Int = Runtime.getRuntime().availableProcessors() *2
    private val SOFT_TIME_TO_LIVE: Long = 10
    private val HARD_TIME_TO_LIVE: Long = 30
    private val cacheSettings = CacheSettings(SOFT_TIME_TO_LIVE, HARD_TIME_TO_LIVE)
    private val listeners: MutableList<IProfileCallback> = mutableListOf()
    private val dataStore: MutableMap<String, CachedProfile> = hashMapOf()
    private val dataToServe: MutableList<String> = mutableListOf()
    private val timeSource: () -> Long = { SystemClock.uptimeMillis() }
    private val executor =  Executors.newFixedThreadPool(SERVICE_THREAD_COUNT)
    private val binder = object : IDataPersistence.Stub() {
        override fun getProfileData(profile: String?) {
            profile?.let{
                executor.submit{requestData(it)}
            }
        }

        override fun registerCallback(callback: IProfileCallback?) {
            callback?.let {
                listeners.add(it)
            }
        }

        override fun unregisterCallback(callback: IProfileCallback?) {
            callback?.let {
                listeners.remove(it)
            }
        }

    }

    private fun requestData(email: String) {
        dataStore[email]?.let { profile ->
            val cacheTime = profile.cacheTime
            if (!cacheSettings.softTtlExpired(timeSource, cacheTime)) {
                serveData(profile)
            } else if (!cacheSettings.hardTtlExpired(timeSource, cacheTime)) {
                serveData(profile)
                refreshCache(profile.cacheKey())
            } else {
                removeFromCache(profile)
                downloadAndServe(email)
            }
            return
        }
        downloadAndServe(email)
    }

    private fun downloadAndServe(email: String) {
        dataToServe.add(email)
        refreshCache(email)
    }

    private fun removeFromCache(profile: CachedProfile) {
        dataStore.remove(profile.cacheKey())
    }

    private fun refreshCache(email: String) {

    }

    private fun serveData(cached: CachedProfile) {
        listeners.forEach {
            it.update(cached.account)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}