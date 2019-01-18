package com.jsjrobotics.testmirror

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.injection.ApplicationComponent
import com.jsjrobotics.testmirror.injection.ApplicationModule
import com.jsjrobotics.testmirror.injection.DaggerApplicationComponent
import com.jsjrobotics.testmirror.service.http.BackendService
import com.jsjrobotics.testmirror.service.websocket.WebSocketService
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class Application : android.app.Application(), HasActivityInjector, HasSupportFragmentInjector {

    companion object {
        private lateinit var instance: Application

        fun instance(): Application {
            return instance
        }

        fun inject(activity: Activity) {
            instance().activityInjector.inject(activity)
        }

        fun inject(fragment: Fragment) {
            instance().fragmentInjector.inject(fragment)
        }

        fun inject(service: Service) {
            instance().serviceInjector.inject(service)
        }

    }


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    private val serviceConnectedMap : MutableMap <ComponentName, ((IBinder) -> Unit)> = mutableMapOf()
    private val serviceDisconnectedMap : MutableMap <ComponentName, (() -> Unit)> = mutableMapOf()

    var injector: ApplicationComponent? = null ; private set
    var backendService: IBackend? = null; private set
    var webSocketService: IWebSocket? = null; private set

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            serviceConnectedMap[componentName]?.invoke(service)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            serviceDisconnectedMap[componentName]?.invoke()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        injector = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        injector!!.inject(this)
        addServiceConnectionListeners()
        startDataPersistence()
        startWebSocketService()
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = fragmentInjector

    private fun addServiceConnectionListeners() {
        val dataComponent = ComponentName(this, BackendService::class.java)
        serviceConnectedMap[dataComponent] = ::onDataServiceConnected
        serviceDisconnectedMap[dataComponent] = ::onDataServiceDisconnected
        val webComponent = ComponentName(this, WebSocketService::class.java)
        serviceConnectedMap[webComponent] = ::onWebSocketServiceConnected
        serviceDisconnectedMap[webComponent] = ::onWebSocketServiceDisconnected
    }

    private fun startDataPersistence() {
        val intent = Intent(this, BackendService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun startWebSocketService() {
        val intent = Intent(this, WebSocketService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun onWebSocketServiceConnected(service: IBinder) {
        webSocketService = IWebSocket.Stub.asInterface(service)
    }

    private fun onDataServiceConnected(service: IBinder) {
        backendService = IBackend.Stub.asInterface(service)
    }

    private fun onWebSocketServiceDisconnected() {
        webSocketService = null
    }

    private fun onDataServiceDisconnected() {
        backendService = null
    }
}