package com.jsjrobotics.testmirror.signup

import javax.inject.Inject

class SignUpPresenter @Inject constructor(){
    private lateinit var view: SignUpView
    fun init(v: SignUpView) {
        view = v
    }

}
