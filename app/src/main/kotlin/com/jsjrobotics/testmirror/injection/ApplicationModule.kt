package com.jsjrobotics.testmirror.injection

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.nsd.NsdManager
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.service.http.DataPersistenceService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val application: Application) {
    @Provides
    @Singleton
    fun provideApplication() : Application = application

    @Provides
    fun provideResources(application: Application) : Resources = application.resources

    @Provides
    fun provideSharedPreferences(application: Application) : SharedPreferences = application.getSharedPreferences(DataPersistenceService.SHARED_PREFERENCES_FILE, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideNsdManager(application: Application) : NsdManager = application.getSystemService(Context.NSD_SERVICE) as NsdManager
}