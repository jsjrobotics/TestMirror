package com.jsjrobotics.testmirror.login

import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.profile.ProfileModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginModel @Inject constructor(private val profileModel: ProfileModel,
                                     private val navigationController: NavigationController) {
    var loggedInToken : String? = null

    fun successfulLogin(account: Account) {
        profileModel.setAccount(account)
        navigationController.showConnectToMirror()
        /*if (account.needsUpdateInfo()) {
            navigationController.showUpdateInfo()
        } else {
            navigationController.showProfile()
        }*/

    }

}