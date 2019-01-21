package com.jsjrobotics.testmirror.login

import com.jsjrobotics.testmirror.SharedPreferencesManager
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.profile.ProfileModel
import javax.inject.Inject

class LoginModel @Inject constructor(private val profileModel: ProfileModel,
                                     private val preferencesManager: SharedPreferencesManager) {
    var requestToken : String = preferencesManager.getRequestToken() ; set (value) {
        field = value
        preferencesManager.setRequestToken(value)
    }

    fun registerUpdateCallback() = profileModel.registerCallback()
    fun unregister() = profileModel.unregister()

    fun successfulLogin(account: Account) {
        profileModel.setAccount(account)
    }

}