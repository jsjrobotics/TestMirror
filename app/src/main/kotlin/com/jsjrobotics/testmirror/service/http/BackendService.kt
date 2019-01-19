package com.jsjrobotics.testmirror.service.http

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.IBackend
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.*
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.service.http.Paths.DOMAIN
import com.jsjrobotics.testmirror.service.http.tasks.LoginTask
import com.jsjrobotics.testmirror.service.http.tasks.OnDemandWorkoutsTask
import com.jsjrobotics.testmirror.service.http.tasks.SignUpTask
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.Executors
import javax.inject.Inject

class BackendService : Service() {

    companion object {
        const val SHARED_PREFERENCES_FILE : String = "BackendService.SharedPreferences"
    }
    private val SERVICE_THREAD_COUNT: Int = Runtime.getRuntime().availableProcessors() *2
    private val SOFT_TIME_TO_LIVE: Long = 10 * 1000
    private val HARD_TIME_TO_LIVE: Long = 30 * 1000
    private val cacheSettings = CacheSettings(SOFT_TIME_TO_LIVE, HARD_TIME_TO_LIVE)
    private val profileCallbackListeners: MutableList<IProfileCallback> = mutableListOf()
    private val dataStore: MutableMap<String, CachedProfile> = hashMapOf()
    private val dataToServe: MutableList<String> = mutableListOf()
    private val timeSource: () -> Long = { SystemClock.uptimeMillis() }
    private val executor =  Executors.newFixedThreadPool(SERVICE_THREAD_COUNT)
    private lateinit var backendApi: RefineMirrorApi

    @Inject
    protected lateinit var sharedPreferences: SharedPreferences

    @Inject
    protected lateinit var localBroadcastManager: LocalBroadcastManager

    @Inject
    protected lateinit var loginModel: LoginModel

    override fun onCreate() {
        super.onCreate()
        Application.inject(this)
        val retrofit = Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        backendApi = retrofit.create(RefineMirrorApi::class.java)
    }

    private val binder = object : IBackend.Stub() {
        private fun updateDataStore(key: String, value: CachedProfile) {
            dataStore[key] = value
        }

        override fun attemptSignup(callback: IProfileCallback?, data: SignUpData?) {
            if (data == null || callback == null) {
                callback?.signUpFailure(application.getString(R.string.invalid_credentials))
                return
            }
            val task = SignUpTask(::getPersistentData,
                    ::writePersistentData,
                    ::updateDataStore,
                    backendApi,
                    application.resources,
                    callback,
                    data)
            executor.submit(task)
        }


        override fun attemptLogin(callback: IProfileCallback?, data: LoginData?) {
            if (data == null || callback == null) {
                callback?.loginFailure()
                return
            }
            val task = LoginTask(::getPersistentData,
                    backendApi,
                    callback,
                    data,
                    loginModel)
            executor.submit(task)
        }

        override fun getOnDemandWorkout() {
            val task = OnDemandWorkoutsTask(backendApi, loginModel, localBroadcastManager)
            executor.submit(task)
        }
        override fun getProfileData(profile: String?) {
            profile?.let{
                executor.submit{requestData(it)}
            }
        }

        override fun registerCallback(callback: IProfileCallback?) {
            callback?.let {
                profileCallbackListeners.add(it)
            }
        }

        override fun unregisterCallback(callback: IProfileCallback?) {
            callback?.let {
                profileCallbackListeners.remove(it)
            }
        }

    }


    private fun updateDataStore(email: String, profile: CachedProfile) {
        dataStore[email] = profile
        executor.submit{
            profileCallbackListeners.forEach {
                it.update(profile.account)
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    fun writePersistentData(cached: CachedProfile) {
        val account = cached.account
        dataStore[account.userEmail] = cached
        val serializedData = Account.toSharedPreferences(account)
        sharedPreferences.edit()
                .putStringSet(account.userEmail, serializedData)
                .commit()
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
        profileCallbackListeners.forEach {
            it.update(cached.account)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun getPersistentData(loginEmail: String): CachedProfile? {
        val data = sharedPreferences.getStringSet(loginEmail, emptySet())!!
        if (data.isEmpty() || data.size != Account.ATTRIBUTES_IN_ACCOUNT) {
            return null
        }
        val account = Account.fromSharedPreferences(data)
        val profile = CachedProfile(account, timeSource.invoke())
        updateDataStore(loginEmail, profile)
        return profile
    }

    @SuppressLint("ApplySharedPref")
    fun writePersistentData(data: SignUpData): CachedProfile {
        val newAccount = Account(data.email,
                                 data.password,
                                 data.fullName,
                                 Account.UNKNOWN_BIRTHDAY,
                                 Account.UNKNOWN_LOCATION,
                                 Account.UNKNOWN_UUID)
        val profile = CachedProfile(newAccount, timeSource.invoke())
        updateDataStore(data.email, profile)
        val serializedData = Account.toSharedPreferences(newAccount)
        sharedPreferences.edit()
                .putStringSet(data.email, serializedData)
                .commit()
        return profile
    }
}
