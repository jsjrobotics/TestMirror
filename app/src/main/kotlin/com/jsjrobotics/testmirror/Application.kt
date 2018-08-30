package com.jsjrobotics.testmirror

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.injection.ApplicationComponent
import com.jsjrobotics.testmirror.injection.ApplicationModule
import com.jsjrobotics.testmirror.injection.DaggerApplicationComponent
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

    }

    var injector: ApplicationComponent? = null ; private set

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val serviceConnection = buildServiceConnection()

    private var dataPersistenceService: IDataPersistence? = null

    private fun buildServiceConnection(): ServiceConnection {
        return object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                dataPersistenceService = IDataPersistence.Stub.asInterface(service)

            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                dataPersistenceService = null
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        injector = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        injector!!.inject(this)
        startDataPersistence()
    }

    private fun startDataPersistence() {
        val intent = Intent(this, DataPersistenceService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = fragmentInjector


}