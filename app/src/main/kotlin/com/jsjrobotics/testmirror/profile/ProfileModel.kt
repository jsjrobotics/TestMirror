package com.jsjrobotics.testmirror.profile

import com.jsjrobotics.testmirror.dataStructures.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileModel @Inject constructor()  {
    private var currentAccount: Account? = null

    fun setCurrentAccount(account: Account) {
        currentAccount = account
    }
}