package com.jsjrobotics.testmirror

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.connectToMirror.ConnectToMirrorFragment
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.profile.ProfileFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment

enum class FragmentId {
    LOGIN,
    SIGNUP,
    UPDATE_INFO,
    WELCOME,
    CONNECT_TO_MIRROR,
    PROFILE;

    fun instantiate(): Fragment {
        return when(this) {
            LOGIN -> LoginFragment()
            SIGNUP -> SignUpFragment()
            WELCOME -> WelcomeFragment()
            UPDATE_INFO -> UpdateInfoFragment()
            PROFILE -> ProfileFragment()
            CONNECT_TO_MIRROR -> ConnectToMirrorFragment()
        }
    }

    fun tag(): String {
        return when(this) {
            LOGIN -> LoginFragment.TAG
            SIGNUP -> SignUpFragment.TAG
            WELCOME -> WelcomeFragment.TAG
            UPDATE_INFO -> UpdateInfoFragment.TAG
            PROFILE -> ProfileFragment.TAG
            CONNECT_TO_MIRROR -> ConnectToMirrorFragment.TAG
        }
    }

    fun isNavBarVisible(): Boolean {
        return when(this) {
            PROFILE -> true
            else -> false
        }
    }
}
