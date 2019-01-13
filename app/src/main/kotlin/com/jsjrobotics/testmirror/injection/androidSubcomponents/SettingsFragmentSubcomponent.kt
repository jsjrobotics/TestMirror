package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.settings.SettingsFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SettingsFragmentSubcomponent : AndroidInjector<SettingsFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SettingsFragment>()
}