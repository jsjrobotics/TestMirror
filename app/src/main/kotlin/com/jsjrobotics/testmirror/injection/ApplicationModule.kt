package com.jsjrobotics.testmirror.injection

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.service.DataPersistenceService
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

    @Provides
    fun provideSharedPreferences(application: Application) : SharedPreferences = application.getSharedPreferences(DataPersistenceService.SHARED_PREFERENCES_FILE, MODE_PRIVATE)

}