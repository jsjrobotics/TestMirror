package com.jsjrobotics.testmirror.injection

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.browseLive.BrowseLiveFragment
import com.jsjrobotics.testmirror.browseOnDemand.BrowseOnDemandFragment
import com.jsjrobotics.testmirror.connectToMirror.ConnectToMirrorFragment
import com.jsjrobotics.testmirror.injection.androidSubcomponents.*
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.profile.ProfileFragment
import com.jsjrobotics.testmirror.progress.ProgressFragment
import com.jsjrobotics.testmirror.settings.SettingsFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [
    LoginFragmentSubcomponent::class,
    WelcomeFragmentSubcomponent::class,
    SignUpFragmentSubcomponent::class,
    ProfileFragmentSubcomponent::class,
    ConnectToMirrorFragmentSubcomponent::class,
    BrowseLiveFragmentSubcomponent::class,
    BrowseOnDemandFragmentSubcomponent::class,
    SettingsFragmentSubcomponent::class,
    ProgressFragmentSubcomponent::class
])

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
    @FragmentKey(ProgressFragment::class)
    internal abstract fun bindProgressFragmentInjectorFactory(builder: ProgressFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(BrowseLiveFragment::class)
    internal abstract fun bindBrowseLiveFragmentInjectorFactory(builder: BrowseLiveFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(BrowseOnDemandFragment::class)
    internal abstract fun bindBrowseOnDemandFragmentInjectorFactory(builder: BrowseOnDemandFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    internal abstract fun bindSettingsFragmentInjectorFactory(builder: SettingsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(ConnectToMirrorFragment::class)
    internal abstract fun bindConnectToMirrorFragmentInjectorFactory(builder: ConnectToMirrorFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}