package com.jsjrobotics.testmirror.login

import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.profile.ProfileModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginModel @Inject constructor(val profileModel: ProfileModel,
                                     val navigationController: NavigationController) {
    fun successfulLogin(account: Account) {
        profileModel.setCurrentAccount(account)
        if (account.needsUpdateInfo()) {
            navigationController.showUpdateInfo()
        } else {
            navigationController.showProfile()
        }

    }

}