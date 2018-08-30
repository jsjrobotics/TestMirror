package com.jsjrobotics.testmirror.signup

import com.jsjrobotics.testmirror.DefaultPresenter
import javax.inject.Inject

class SignUpPresenter @Inject constructor() : DefaultPresenter(){
    private lateinit var view: SignUpView
    fun init(v: SignUpView) {
        view = v
    }

}
