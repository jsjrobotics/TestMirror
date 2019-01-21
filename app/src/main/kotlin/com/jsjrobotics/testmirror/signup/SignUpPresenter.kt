package com.jsjrobotics.testmirror.signup

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SignUpPresenter @Inject constructor(private val model: SignUpModel) : DefaultPresenter(){
    private lateinit var view: SignUpView
    private val disposables = CompositeDisposable()
    private lateinit var showRequest: (AddFragment) -> Unit

    fun init(v: SignUpView, onShowRequest: (AddFragment) -> Unit) {
        view = v
        showRequest = onShowRequest
        val signUpDisposable = view.onSignUpClick()
                .subscribe (::performSignup)
        val failureDisposable = model.onSignUpFailure
                .subscribe (::onFailure)
        val successDisposable = model.onSignUpSuccess
                .subscribe { onSuccess()}

        disposables.addAll(signUpDisposable,
                           failureDisposable,
                           successDisposable)
    }

    private fun onFailure(error: String) {
        view.showToast(error)
    }

    private fun onSuccess() {
        view.showToast(R.string.signup_success)
        view.clearInputFields()
        showRequest(AddFragment(FragmentId.LOGIN, true, containerId = R.id.welcome_fragment))
    }
    private fun performSignup(data: SignUpData) {
        if (!data.isValid()) {
            view.showEnterAllFields()
            return
        }
        if(!model.beginSignUp(data)) {
            view.showNoServiceConnection()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
    }

}
