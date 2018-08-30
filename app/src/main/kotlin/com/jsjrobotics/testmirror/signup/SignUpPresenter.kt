package com.jsjrobotics.testmirror.signup

import android.os.IBinder
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import javax.inject.Inject

class SignUpPresenter @Inject constructor(val application: Application) : DefaultPresenter(){
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
        return object : IProfileCallback {
            override fun loginFailure() {}

            override fun signUpFailure(error: String?) {
                error?.let {
                    view.showToast(it)
                    return
                }
                view.showToast(application.getString(R.string.unknown_error))
            }

            override fun asBinder(): IBinder? {
                return null
            }

            override fun update(account: Account?) {}

            override fun loginSuccess(account: Account?) {}

        }
    }

}
