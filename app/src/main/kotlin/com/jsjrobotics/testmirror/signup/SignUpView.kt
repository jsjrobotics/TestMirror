package com.jsjrobotics.testmirror.signup

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SignUpView @Inject constructor(){
    lateinit var rootXml: ViewGroup ; private set
    lateinit var emailInput: EditText ; private set
    lateinit var passwordInput: EditText ; private set
    lateinit var fullNameInput: EditText ; private set
    lateinit var signUpButton: Button ; private set

    private val onSignUpClick : PublishSubject<Unit> = PublishSubject.create()

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_signup, container, false) as ViewGroup
        emailInput = rootXml.findViewById(R.id.email)
        passwordInput = rootXml.findViewById(R.id.password)
        fullNameInput = rootXml.findViewById(R.id.full_name)
        signUpButton = rootXml.findViewById(R.id.signup)
        signUpButton.setOnClickListener { onSignUpClick.onNext(Unit) }
    }

    fun onSignUpClick(): Observable<Unit> = onSignUpClick


}
