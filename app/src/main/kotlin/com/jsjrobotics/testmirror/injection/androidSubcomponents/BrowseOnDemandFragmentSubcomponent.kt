package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.browseOnDemand.BrowseOnDemandFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface BrowseOnDemandFragmentSubcomponent : AndroidInjector<BrowseOnDemandFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BrowseOnDemandFragment>()
}