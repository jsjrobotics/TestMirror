package com.jsjrobotics.testmirror.welcome

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.NavigationController
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelcomePresenter @Inject constructor(private val navigationController: NavigationController) : DefaultPresenter(){
    private lateinit var view: WelcomeView
    private val disposables = CompositeDisposable()

    fun init(v: WelcomeView) {
        view = v
        val signupDisposable = v.onSignupClick()
                .subscribe{ launchSignupActivity()}

        val loginDisposable = v.onLoginClick()
                .subscribe{ launchLoginActivity()}

        disposables.addAll(signupDisposable, loginDisposable)
    }

    private fun launchLoginActivity() {
        ERROR("Launching login activity")
        navigationController.showLogin()
    }

    private fun launchSignupActivity() {
        navigationController.showSignUp()
    }
}
