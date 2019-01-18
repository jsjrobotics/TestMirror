package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.service.http.BackendService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface DataPersistenceSubcomponent : AndroidInjector<BackendService>{
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BackendService>()
}
