package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.service.DataPersistenceService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface DataPersistenceSubcomponent : AndroidInjector<DataPersistenceService>{
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<DataPersistenceService>()


}
