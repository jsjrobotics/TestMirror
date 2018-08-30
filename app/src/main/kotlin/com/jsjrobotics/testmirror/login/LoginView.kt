package com.jsjrobotics.testmirror.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LoginView @Inject constructor(){
    lateinit var rootXml: ViewGroup ; private set
    lateinit var emailInput: EditText ; private set
    lateinit var passwordInput: EditText ; private set
    lateinit var loginButton: Button ; private set

    private val onLoginClick : PublishSubject<Unit> = PublishSubject.create()

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_login, container, false) as ViewGroup
        emailInput = rootXml.findViewById(R.id.email)
        passwordInput = rootXml.findViewById(R.id.password)
        loginButton = rootXml.findViewById(R.id.login)
        loginButton.setOnClickListener { onLoginClick.onNext(Unit) }
    }

    fun onLoginClick(): Observable<Unit> = onLoginClick


}
