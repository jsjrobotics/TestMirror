package com.jsjrobotics.testmirror.welcome

import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelcomePresenter @Inject constructor() : DefaultPresenter(){
    private lateinit var view: WelcomeView
    private val disposables = CompositeDisposable()

    private lateinit var showRequest: (AddFragment) -> Unit

    fun init(v: WelcomeView, onShowRequest: (AddFragment) -> Unit) {
        view = v
        val signupDisposable = v.onSignupClick()
                .subscribe{ launchSignupActivity()}

        val loginDisposable = v.onLoginClick()
                .subscribe{ launchLoginActivity()}
        showRequest = onShowRequest

        disposables.addAll(signupDisposable, loginDisposable)
    }

    private fun launchLoginActivity() {
        val loginRequest = AddFragment(FragmentId.LOGIN, true, containerId = R.id.welcome_fragment)
        showRequest(loginRequest)
    }

    private fun launchSignupActivity() {
        val signupRequest = AddFragment(FragmentId.SIGNUP,true, containerId = R.id.welcome_fragment)
        showRequest(signupRequest)
    }
}
