package com.jsjrobotics.testmirror.login

import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.profile.ProfileModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginModel @Inject constructor(private val profileModel: ProfileModel,
                                     private val navigationController: NavigationController) {
    var loggedInToken : String? = null
    private val loginSuccess : PublishSubject<Boolean> = PublishSubject.create()
    val onLoginSuccess : Observable<Boolean> = loginSuccess

    fun successfulLogin(account: Account) {
        profileModel.setAccount(account)
        loginSuccess.onNext(true)
        /*if (account.needsUpdateInfo()) {
            navigationController.showUpdateInfo()
        } else {
            navigationController.showProfile()
        }*/

    }

}