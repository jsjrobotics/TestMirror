package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.login.LoginFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface LoginFragmentSubcomponent : AndroidInjector<LoginFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<LoginFragment>()

}
