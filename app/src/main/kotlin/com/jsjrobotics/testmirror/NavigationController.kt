package com.jsjrobotics.testmirror

import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor(){
    var currentFragment: FragmentId = FragmentId.WELCOME

    private val showRequest: PublishSubject<FragmentRequest> = PublishSubject.create()
    val onShowRequest: Observable<FragmentRequest> = showRequest

    fun showLogin() {
        showRequest(FragmentRequest(FragmentId.LOGIN, true))
    }

    private fun showRequest(fragmentRequest: FragmentRequest) {
        currentFragment = fragmentRequest.fragmentId
        showRequest.onNext(fragmentRequest)
    }

    fun showSignUp() {
        showRequest(FragmentRequest(FragmentId.SIGNUP,true))
    }

    fun showUpdateInfo() {
        showRequest(FragmentRequest(FragmentId.UPDATE_INFO, false))
    }

    fun showProfile() {
        showRequest(FragmentRequest(FragmentId.PROFILE, false, clearBackStack = true))
    }

    fun showConnectToMirror() {
        showRequest(FragmentRequest(FragmentId.CONNECT_TO_MIRROR, false, clearBackStack = true))
    }


}
