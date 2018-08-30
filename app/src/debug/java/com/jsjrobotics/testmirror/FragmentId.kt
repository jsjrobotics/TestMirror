package com.jsjrobotics.testmirror

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment

enum class FragmentId {
    LOGIN,
    SIGNUP,
    WELCOME;

    fun instantiate(): Fragment {
        return when(this) {
            LOGIN -> LoginFragment()
            SIGNUP -> SignUpFragment()
            WELCOME -> WelcomeFragment()
        }
    }

    fun tag(): String {
        return when(this) {
            LOGIN -> LoginFragment.TAG
            SIGNUP -> SignUpFragment.TAG
            WELCOME -> WelcomeFragment.TAG
        }
    }
}
