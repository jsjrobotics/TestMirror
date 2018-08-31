package com.jsjrobotics.testmirror.signup

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SignUpView @Inject constructor() : DefaultView(){
    lateinit var rootXml: ViewGroup ; private set
    lateinit var emailInput: EditText ; private set
    lateinit var passwordInput: EditText ; private set
    lateinit var confirmPasswordInput: EditText ; private set
    lateinit var fullNameInput: EditText ; private set
    lateinit var signUpButton: Button ; private set

    private val onSignUpClick : PublishSubject<SignUpData> = PublishSubject.create()

    override fun getContext() = rootXml.context

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_signup, container, false) as ViewGroup
        emailInput = rootXml.findViewById(R.id.email)
        passwordInput = rootXml.findViewById(R.id.password)
        confirmPasswordInput = rootXml.findViewById(R.id.confirm_password)
        fullNameInput = rootXml.findViewById(R.id.full_name)
        signUpButton = rootXml.findViewById(R.id.signup)
        signUpButton.setOnClickListener {
            val signUpData = SignUpData(emailInput.text.toString(),
                                        passwordInput.text.toString(),
                                        confirmPasswordInput.text.toString(),
                                        fullNameInput.text.toString())
            onSignUpClick.onNext(signUpData)
        }
    }

    fun onSignUpClick(): Observable<SignUpData> = onSignUpClick
    fun clearInputFields() {
        emailInput.setText("")
        passwordInput.setText("")
        fullNameInput.setText("")
    }
}
