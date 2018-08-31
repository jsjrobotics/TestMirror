package com.jsjrobotics.testmirror

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor(){
    var currentFragment: FragmentId = FragmentId.WELCOME

    private val showRequest: PublishSubject<FragmentId> = PublishSubject.create()
    val onShowRequest: Observable<FragmentId> = showRequest

    fun showLogin() {
        showRequest(FragmentId.LOGIN)
    }

    private fun showRequest(id: FragmentId) {
        currentFragment = id
        showRequest.onNext(id)
    }
    fun showSignUp() {
        showRequest(FragmentId.SIGNUP)
    }

    fun showUpdateInfo() {
        showRequest(FragmentId.UPDATE_INFO)
    }

    fun showProfile() {
        showRequest(FragmentId.PROFILE)
    }


}
