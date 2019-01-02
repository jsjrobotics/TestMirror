package com.jsjrobotics.testmirror.profile

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultPresenter
import com.mirror.proto.navigation.MirrorScreen
import javax.inject.Inject

class ProfilePresenter @Inject constructor(val application: Application) : DefaultPresenter(){
    private lateinit var view: ProfileView

    fun init(v: ProfileView) {
        view = v
        disposables.add(view.onRefreshClick.subscribe{ sendScreenRequest()})
    }

    fun sendScreenRequest() {
        application.webSocketService?.sendScreenRequest(MirrorScreen.DASHBOARD.name)
    }
}