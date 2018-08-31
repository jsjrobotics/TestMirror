package com.jsjrobotics.testmirror.profile

import com.jsjrobotics.testmirror.DefaultPresenter
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : DefaultPresenter(){
    private lateinit var view: ProfileView

    fun init(v: ProfileView) {
        view = v
    }
}