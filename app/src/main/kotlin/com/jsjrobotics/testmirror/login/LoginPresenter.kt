package com.jsjrobotics.testmirror.login

import android.os.IBinder
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.LoginData
import javax.inject.Inject

class LoginPresenter @Inject constructor(val application: Application) : DefaultPresenter(){
    private lateinit var view: LoginView
    fun init(v: LoginView) {
        view = v
        val loginDisposable = v.onLoginClick()
                .subscribe{ attemptLogin(it)}

        disposables.add(loginDisposable)
    }

    private fun attemptLogin(loginData: LoginData) {
        application.dataPersistenceService?.let { service ->
            service.attempLogin(buildLoginListener(), loginData.userEmail, loginData.password)
            return
        }
        view.showNoServiceConnection()
    }

    private fun buildLoginListener(): IProfileCallback {
        return object : IProfileCallback {
            override fun loginFailure(userEmail: String?, password: String?) {
                view.showFailedToLogin()
            }

            override fun asBinder(): IBinder? {
                return null
            }

            override fun update(account: Account?) {}

            override fun loginSuccess(account: Account?) {
            }

        }
    }

}
