package com.jsjrobotics.testmirror

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.os.SystemClock
import com.jsjrobotics.testmirror.dataStructures.*
import java.util.concurrent.Executors
import javax.inject.Inject

class DataPersistenceService : Service() {

    companion object {
        const val SHARED_PREFERENCES_FILE : String = "DataPersistenceService.SharedPreferences"
    }
    private val SERVICE_THREAD_COUNT: Int = Runtime.getRuntime().availableProcessors() *2
    private val SOFT_TIME_TO_LIVE: Long = 10
    private val HARD_TIME_TO_LIVE: Long = 30
    private val cacheSettings = CacheSettings(SOFT_TIME_TO_LIVE, HARD_TIME_TO_LIVE)
    private val listeners: MutableList<IProfileCallback> = mutableListOf()
    private val dataStore: MutableMap<String, CachedProfile> = hashMapOf()
    private val dataToServe: MutableList<String> = mutableListOf()
    private val timeSource: () -> Long = { SystemClock.uptimeMillis() }
    private val executor =  Executors.newFixedThreadPool(SERVICE_THREAD_COUNT)

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Application.inject(this)
    }

    private val binder = object : IDataPersistence.Stub() {

        override fun attemptSignup(callback: IProfileCallback?, data: SignUpData?) {
            if (data == null || callback == null) {
                callback?.signUpFailure(application.getString(R.string.invalid_credentials))
                return
            }
            executor.submit{ performSignUp(callback, data)}
        }

        override fun attemptLogin(callback: IProfileCallback?, data: LoginData?) {
            if (data == null || callback == null) {
                callback?.loginFailure()
                return
            }
            executor.submit{ performLogin(callback, data)}
        }

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

    private fun performSignUp(callback: IProfileCallback, data: SignUpData) {
        if (!data.isValid()) {
            callback.signUpFailure(application.getString(R.string.invalid_credentials))
            return
        }
        if (getPersistentData(data.email) != null) {
            callback.signUpFailure(application.getString(R.string.email_already_exists))
            return
        }
        val profile = writePersistentData(data)
        dataStore[data.email] = profile
        callback.loginSuccess(profile.account)
    }

    private fun performLogin(callback: IProfileCallback, data: LoginData) {
        val userEmail = data.userEmail
        val password = data.password
        val savedData : CachedProfile? = getPersistentData(userEmail)
        if (savedData == null) {
            callback.loginFailure()
            return
        }
        if (savedData.account.userPassword == password) {
            callback.loginSuccess(savedData.account)
        }
    }

    private fun getPersistentData(loginEmail: String): CachedProfile? {
        val data = sharedPreferences.getStringSet(loginEmail, emptySet())
        if (data.isEmpty() || data.size != Account.ATTRIBUTES_IN_ACCOUNT) {
            return null
        }
        val listData = data.toList();
        val userEmail = listData[0]
        val userPassword = listData[1]
        val fullName = listData[2]
        val birthday = listData[3]
        val location = listData[4]
        val account = Account(userEmail,
                              userPassword,
                              fullName,
                              birthday.toLong(),
                              location)
        val profile = CachedProfile(account, timeSource.invoke())
        dataStore[loginEmail] = profile
        return profile
    }

    private fun writePersistentData(data: SignUpData): CachedProfile {
        val account = Account(data.email,
                              data.password,
                              data.fullName,
                              Account.UNKNOWN_BIRTHDAY,
                              Account.UNKNOWN_LOCATION)
        val profile = CachedProfile(account, timeSource.invoke())
        dataStore[data.email] = profile
        return profile
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