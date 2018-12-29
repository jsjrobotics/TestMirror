package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.service.WebSocketService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface WebSocketSubcomponent : AndroidInjector<WebSocketService>{
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<WebSocketService>()
}
