package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.activities.WelcomeActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface WelcomeActivitySubcomponent : AndroidInjector<WelcomeActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<WelcomeActivity>()
}
