package com.jsjrobotics.testmirror.injection

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.LoginFragment
import com.jsjrobotics.testmirror.injection.androidSubcomponents.LoginFragmentSubcomponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        LoginFragmentSubcomponent::class
))
abstract class FragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    internal abstract fun bindLoginFragmentInjectorFactory(builder: LoginFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}