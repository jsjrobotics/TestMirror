package com.jsjrobotics.testmirror

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment

enum class FragmentId {
    LOGIN,
    SIGNUP,
    UPDATE_INFO,
    WELCOME;

    fun instantiate(): Fragment {
        return when(this) {
            LOGIN -> LoginFragment()
            SIGNUP -> SignUpFragment()
            WELCOME -> WelcomeFragment()
            UPDATE_INFO -> UpdateInfoFragment()
        }
    }

    fun tag(): String {
        return when(this) {
            LOGIN -> LoginFragment.TAG
            SIGNUP -> SignUpFragment.TAG
            WELCOME -> WelcomeFragment.TAG
            UPDATE_INFO -> UpdateInfoFragment.TAG
        }
    }
}
