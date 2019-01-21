package com.jsjrobotics.testmirror.signup

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultProfileCallback
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SignUpModel @Inject constructor(val application: Application) {

    private val signUpFailure : PublishSubject<String> = PublishSubject.create()
    val onSignUpFailure : Observable<String> = signUpFailure

    private val signUpSuccess : PublishSubject<Unit> = PublishSubject.create()
    val onSignUpSuccess : Observable<Unit> = signUpSuccess

    fun beginSignUp(data: SignUpData): Boolean {
        application.backendService?.let { service ->
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
            }
        }
    }
}