package com.jsjrobotics.testmirror.profile

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DEBUG
import com.jsjrobotics.testmirror.DefaultPresenter
import com.mirror.proto.navigation.MirrorScreen
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor(val application: Application) : DefaultPresenter(){
    private lateinit var view: ProfileView
    private val disposables = CompositeDisposable()

    fun init(v: ProfileView) {
        view = v
        disposables.add(view.onRefreshClick
                .observeOn(Schedulers.io())
                .subscribe{
                    sendScreenRequest()
                })
    }

    fun sendScreenRequest() {
        application.webSocketService?.sendScreenRequest(MirrorScreen.DASHBOARD.name)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
    }
}