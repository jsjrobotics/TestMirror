package com.jsjrobotics.testmirror.injection.androidSubcomponents

import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface UpdateInfoFragmentSubcomponent : AndroidInjector<UpdateInfoFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<UpdateInfoFragment>()

}
