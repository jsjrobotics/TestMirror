package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.welcome.WelcomeFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface WelcomeFragmentSubcomponent : AndroidInjector<WelcomeFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<WelcomeFragment>()

}
