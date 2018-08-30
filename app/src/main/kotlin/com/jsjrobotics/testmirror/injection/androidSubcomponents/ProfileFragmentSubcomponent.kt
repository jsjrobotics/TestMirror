package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.profile.ProfileFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ProfileFragmentSubcomponent : AndroidInjector<ProfileFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ProfileFragment>()

}
