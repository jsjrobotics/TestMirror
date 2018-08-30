package com.jsjrobotics.testmirror.welcome

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.NavigationController
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelcomePresenter @Inject constructor(private val navigationController: NavigationController) : LifecycleObserver{
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
        navigationController.showLogin()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun clearDisposables() {
        disposables.clear()
    }

    private fun launchSignupActivity() {
        navigationController.showSignUp()
    }

}
