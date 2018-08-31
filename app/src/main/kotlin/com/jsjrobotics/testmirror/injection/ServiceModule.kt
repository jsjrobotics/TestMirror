package com.jsjrobotics.testmirror.injection

import android.app.Service
import com.jsjrobotics.testmirror.service.DataPersistenceService
import com.jsjrobotics.testmirror.injection.androidSubcomponents.DataPersistenceSubcomponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        DataPersistenceSubcomponent::class
))
abstract class ServiceModule {
    @Binds
    @IntoMap
    @ServiceKey(DataPersistenceService::class)
    internal abstract fun bindDataPersistenceInjectorFactory(builder: DataPersistenceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}