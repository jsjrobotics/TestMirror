package com.jsjrobotics.testmirror.login

import android.os.IBinder
import com.jsjrobotics.testmirror.*
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.LoginData
import javax.inject.Inject

class LoginPresenter @Inject constructor(val application: Application,
                                         val model: LoginModel) : DefaultPresenter(){
    private lateinit var view: LoginView
    fun init(v: LoginView) {
        view = v
        val loginDisposable = v.onLoginClick()
                .subscribe{ attemptLogin(it)}

        disposables.add(loginDisposable)
    }

    private fun attemptLogin(loginData: LoginData) {
        if (!loginData.isValid()) {
            view.showEnterAllFields()
            return
        }
        application.dataPersistenceService?.let { service ->
            service.attemptLogin(buildLoginListener(), loginData)
            return
        }
        view.showNoServiceConnection()
    }

    private fun buildLoginListener(): IProfileCallback {
        return object : DefaultProfileCallback() {
            override fun loginFailure() {
                view.showFailedToLogin()
            }
            override fun loginSuccess(account: Account?) {
                view.showToast(R.string.login_successful)
                account?.let {
                    model.successfulLogin(it)
                }
            }
        }
    }

}
