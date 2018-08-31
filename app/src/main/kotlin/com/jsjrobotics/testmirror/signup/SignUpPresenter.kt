package com.jsjrobotics.testmirror.signup

import android.os.IBinder
import com.jsjrobotics.testmirror.*
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import javax.inject.Inject

class SignUpPresenter @Inject constructor(val application: Application,
                                          val navigationController: NavigationController) : DefaultPresenter(){
    private lateinit var view: SignUpView

    fun init(v: SignUpView) {
        view = v
        val signUpDisposable = view.onSignUpClick()
                .subscribe { performSignup(it) }
    }

    private fun performSignup(data: SignUpData) {
        if (!data.isValid()) {
            view.showEnterAllFields()
            return
        }
        application.dataPersistenceService?.let { service ->
            service.attemptSignup(buildSignUpCallback(), data)
            return
        }
        view.showNoServiceConnection()
    }

    private fun buildSignUpCallback(): IProfileCallback {
        return object : DefaultProfileCallback() {

            override fun signUpFailure(error: String?) {
                error?.let {
                    view.showToast(it)
                    return
                }
                view.showToast(R.string.unknown_error)
            }

            override fun signUpSuccess() {
                view.showToast(R.string.signup_success)
                view.clearInputFields()
                navigationController.showLogin()
            }


        }
    }

}
