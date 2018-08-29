package com.jsjrobotics.testmirror

import android.app.Activity
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

    override fun onCreate() {
        super.onCreate()
        instance = this
        injector = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        injector!!.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = fragmentInjector


}