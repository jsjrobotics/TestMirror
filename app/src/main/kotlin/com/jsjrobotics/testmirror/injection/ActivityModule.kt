package com.jsjrobotics.testmirror.injection

import android.app.Activity
import com.jsjrobotics.testmirror.activities.MainActivity
import com.jsjrobotics.testmirror.activities.WelcomeActivity
import com.jsjrobotics.testmirror.injection.androidSubcomponents.MainActivitySubcomponent
import com.jsjrobotics.testmirror.injection.androidSubcomponents.WelcomeActivitySubcomponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [
    MainActivitySubcomponent::class,
    WelcomeActivitySubcomponent::class
])
abstract class ActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(WelcomeActivity::class)
    internal abstract fun bindWelcomeActivityInjectorFactory(builder: WelcomeActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>

}