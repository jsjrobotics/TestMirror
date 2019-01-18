package com.jsjrobotics.testmirror.login

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.*
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.LoginData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginPresenter @Inject constructor(val application: Application,
                                         val model: LoginModel) : DefaultPresenter(){
    private lateinit var view: LoginView
    private val disposables = CompositeDisposable()

    fun init(v: LoginView) {
        view = v
        val loginDisposable = v.onLoginClick()
                .subscribe{
                    view.disableLogin()
                    attemptLogin(it)
                }

        disposables.add(loginDisposable)
    }

    private fun attemptLogin(loginData: LoginData) {
        if (!loginData.isValid()) {
            view.showEnterAllFields()
            return
        }
        application.backendService?.let { service ->
            service.attemptLogin(buildLoginListener(), loginData)
            return
        }
        view.showNoServiceConnection()
    }

    private fun buildLoginListener(): IProfileCallback {
        return object : DefaultProfileCallback() {
            override fun loginFailure() {
                view.showFailedToLogin()
                view.enableLogin()
            }
            override fun loginSuccess(account: Account?) {
                view.showToast(R.string.login_successful)
                account?.let {
                    model.successfulLogin(it)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
    }
}
