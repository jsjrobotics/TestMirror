package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.signup.SignUpFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SignUpFragmentSubcomponent : AndroidInjector<SignUpFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SignUpFragment>()

}
