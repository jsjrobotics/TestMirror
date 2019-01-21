package com.jsjrobotics.testmirror

import com.jsjrobotics.testmirror.dataStructures.AddFragment
import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor() {

    private val showRequest: PublishSubject<FragmentRequest> = PublishSubject.create()
    val onShowRequest: Observable<FragmentRequest> = showRequest

    fun showLogin() {
        showRequest(AddFragment(FragmentId.LOGIN, true, containerId = R.id.welcome_fragment))
    }

    fun showRequest(fragmentRequest: FragmentRequest) {
        showRequest.onNext(fragmentRequest)
    }

    fun removeRequest(fragmentRequest: FragmentRequest) {
        showRequest.onNext(fragmentRequest)
    }

    fun showSignUp() {
        showRequest(AddFragment(FragmentId.SIGNUP,true, containerId = R.id.welcome_fragment))
    }

}
