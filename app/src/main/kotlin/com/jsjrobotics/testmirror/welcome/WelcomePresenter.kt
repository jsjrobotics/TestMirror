package com.jsjrobotics.testmirror.welcome

import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.NavigationController
import javax.inject.Inject

class WelcomePresenter @Inject constructor(private val navigationController: NavigationController) : DefaultPresenter(){
    private lateinit var view: WelcomeView

    fun init(v: WelcomeView) {
        view = v
        val signupDisposable = v.onSignupClick()
                .subscribe{ launchSignupActivity()}

        val loginDisposable = v.onLoginClick()
                .subscribe{ launchLoginActivity()}

        disposables.addAll(signupDisposable, loginDisposable)
    }

    private fun launchLoginActivity() {
        navigationController.showLogin()
    }

    private fun launchSignupActivity() {
        navigationController.showSignUp()
    }
}
