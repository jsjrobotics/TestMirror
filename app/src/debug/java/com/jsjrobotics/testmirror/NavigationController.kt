package com.jsjrobotics.testmirror

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor(){

    private val showLoginFragment: PublishSubject<Unit> = PublishSubject.create()
    val onShowLoginFragment: Observable<Unit> = showLoginFragment

    private val showSignUpFragment: PublishSubject<Unit> = PublishSubject.create()
    val onShowSignUpFragment: Observable<Unit> = showSignUpFragment

    fun showLogin() {
        showLoginFragment.onNext(Unit)
    }

    fun showSignUp() {
        showSignUpFragment.onNext(Unit)
    }

    val currentFragment: FragmentId = FragmentId.WELCOME

}
