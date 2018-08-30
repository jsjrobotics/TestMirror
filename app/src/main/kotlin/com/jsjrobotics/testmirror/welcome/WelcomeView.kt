package com.jsjrobotics.testmirror.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class WelcomeView @Inject constructor(){
    lateinit var rootXml: ViewGroup
    lateinit var loginButton: Button
    lateinit var signUpButton: Button

    private val onSignupClick : PublishSubject<Unit> = PublishSubject.create()
    private val onLoginClick : PublishSubject<Unit> = PublishSubject.create()

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_welcome, container, false) as ViewGroup
        loginButton = rootXml.findViewById(R.id.login)
        signUpButton = rootXml.findViewById(R.id.signup)

        loginButton.setOnClickListener { onLoginClick.onNext(Unit) }
        signUpButton.setOnClickListener{ onSignupClick.onNext(Unit) }
    }

    fun onSignupClick(): Observable<Unit> = onSignupClick
    fun onLoginClick(): Observable<Unit> = onLoginClick


}
