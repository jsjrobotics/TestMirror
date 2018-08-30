package com.jsjrobotics.testmirror.login

import android.arch.lifecycle.LifecycleObserver
import javax.inject.Inject

class LoginPresenter @Inject constructor() : LifecycleObserver{
    private lateinit var view: LoginView

    fun init(v: LoginView) {
        view = v
        v.onSignupClick()
                .subscribe{ launchSignupActivity()}
    }

    private fun launchSignupActivity() {

    }

}
