package com.jsjrobotics.testmirror.injection

import android.app.Activity
import com.jsjrobotics.testmirror.MainActivity
import com.jsjrobotics.testmirror.injection.androidSubcomponents.MainActivitySubcomponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        MainActivitySubcomponent::class
))
abstract class ActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}