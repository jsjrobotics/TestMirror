package com.jsjrobotics.testmirror.signup

import com.jsjrobotics.testmirror.*
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import javax.inject.Inject

class SignUpPresenter @Inject constructor(val model: SignUpModel) : DefaultPresenter(){
    private lateinit var view: SignUpView

    fun init(v: SignUpView) {
        view = v
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


}
