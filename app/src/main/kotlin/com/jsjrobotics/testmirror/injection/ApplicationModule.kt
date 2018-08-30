package com.jsjrobotics.testmirror.injection

import android.content.res.Resources
import com.jsjrobotics.testmirror.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val application: Application) {
    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    fun provideResources(application: Application) : Resources = application.resources
}