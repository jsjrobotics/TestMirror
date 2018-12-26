package com.jsjrobotics.testmirror.injection

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.connectToMirror.ConnectToMirrorFragment
import com.jsjrobotics.testmirror.injection.androidSubcomponents.*
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.profile.ProfileFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        LoginFragmentSubcomponent::class,
        WelcomeFragmentSubcomponent::class,
        SignUpFragmentSubcomponent::class,
        ProfileFragmentSubcomponent::class,
        UpdateInfoFragmentSubcomponent::class,
        ConnectToMirrorFragmentSubcomponent::class
))
abstract class FragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    internal abstract fun bindLoginFragmentInjectorFactory(builder: LoginFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(WelcomeFragment::class)
    internal abstract fun bindWelcomeFragmentInjectorFactory(builder: WelcomeFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SignUpFragment::class)
    internal abstract fun bindSignUpFragmentInjectorFactory(builder: SignUpFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(ProfileFragment::class)
    internal abstract fun bindProfileFragmentInjectorFactory(builder: ProfileFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(UpdateInfoFragment::class)
    internal abstract fun bindUpdateInfoFragmentInjectorFactory(builder: UpdateInfoFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(ConnectToMirrorFragment::class)
    internal abstract fun bindConnectToMirrorFragmentInjectorFactory(builder: ConnectToMirrorFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

}