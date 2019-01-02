package com.jsjrobotics.testmirror.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.LoginData
import com.jsjrobotics.testmirror.runOnUiThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import android.app.Activity
import android.os.IBinder
import android.view.inputmethod.InputMethodManager


class LoginView @Inject constructor() : DefaultView() {
    lateinit var rootXml: ViewGroup ; private set
    lateinit var emailInput: EditText ; private set
    lateinit var passwordInput: EditText ; private set
    lateinit var loginButton: Button ; private set

    private val onLoginClick : PublishSubject<LoginData> = PublishSubject.create()
    override fun getContext() = rootXml.context

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_login, container, false) as ViewGroup
        emailInput = rootXml.findViewById(R.id.email)
        passwordInput = rootXml.findViewById(R.id.password)
        loginButton = rootXml.findViewById(R.id.login)
        loginButton.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                closeKeyboard(v.windowToken)
            }
        }
        loginButton.setOnClickListener {
                val loginData = LoginData(emailInput.text.toString(),
                        passwordInput.text.toString())
                onLoginClick.onNext(loginData)
            }
        }

    private fun closeKeyboard(windowToken: IBinder) {
        val imm = rootXml.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun onLoginClick(): Observable<LoginData> = onLoginClick
    fun showFailedToLogin() {
        runOnUiThread {
            Toast.makeText(rootXml.context,
                           R.string.invalid_credentials,
                           Toast.LENGTH_SHORT)
                    .show()
        }
    }

    fun disableLogin() {
        loginButton.isEnabled = false
    }

    fun enableLogin() {
        loginButton.isEnabled = true
    }
}
