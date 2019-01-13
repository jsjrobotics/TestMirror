package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.progress.ProgressFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ProgressFragmentSubcomponent : AndroidInjector<ProgressFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ProgressFragment>()
}