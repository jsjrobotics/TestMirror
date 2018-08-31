package com.jsjrobotics.testmirror.signup

import com.jsjrobotics.testmirror.*
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpModel @Inject constructor(val application: Application,
                                      val navigationController: NavigationController ) {

    private val signUpFailure : PublishSubject<String> = PublishSubject.create()
    val onSignUpFailure : Observable<String> = signUpFailure

    private val signUpSuccess : PublishSubject<Unit> = PublishSubject.create()
    val onSignUpSuccess : Observable<Unit> = signUpSuccess

    fun beginSignUp(data: SignUpData): Boolean {
        application.dataPersistenceService?.let { service ->
            service.attemptSignup(buildSignUpCallback(), data)
            return true
        }
        return false
    }

    private fun buildSignUpCallback(): IProfileCallback {
        return object : DefaultProfileCallback() {

            override fun signUpFailure(error: String?) {
                signUpFailure.onNext(error ?: application.getString(R.string.unknown_error))
            }

            override fun signUpSuccess() {
                signUpSuccess.onNext(Unit)
                navigationController.showLogin()
            }
        }
    }
}