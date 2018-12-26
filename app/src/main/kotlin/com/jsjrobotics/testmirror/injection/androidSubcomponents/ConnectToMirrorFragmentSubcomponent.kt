package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.connectToMirror.ConnectToMirrorFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ConnectToMirrorFragmentSubcomponent : AndroidInjector<ConnectToMirrorFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ConnectToMirrorFragment>()

}