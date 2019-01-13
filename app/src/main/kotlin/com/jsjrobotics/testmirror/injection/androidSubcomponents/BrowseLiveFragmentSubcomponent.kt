package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.browseLive.BrowseLiveFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface BrowseLiveFragmentSubcomponent : AndroidInjector<BrowseLiveFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BrowseLiveFragment>()
}