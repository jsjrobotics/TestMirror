package com.jsjrobotics.testmirror.injection

import android.app.Service
import com.jsjrobotics.testmirror.injection.androidSubcomponents.DataPersistenceSubcomponent
import com.jsjrobotics.testmirror.injection.androidSubcomponents.WebSocketSubcomponent
import com.jsjrobotics.testmirror.service.http.BackendService
import com.jsjrobotics.testmirror.service.websocket.WebSocketService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [
    DataPersistenceSubcomponent::class,
    WebSocketSubcomponent::class
])
abstract class ServiceModule {
    @Binds
    @IntoMap
    @ServiceKey(BackendService::class)
    internal abstract fun bindDataPersistenceInjectorFactory(builder: DataPersistenceSubcomponent.Builder): AndroidInjector.Factory<out Service>

    @Binds
    @IntoMap
    @ServiceKey(WebSocketService::class)
    internal abstract fun bindWebSocketServiceInjectorFactory(builder: WebSocketSubcomponent.Builder): AndroidInjector.Factory<out Service>

}