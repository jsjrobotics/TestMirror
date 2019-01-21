package com.jsjrobotics.testmirror.profile

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultProfileCallback
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.SharedPreferencesManager
import com.jsjrobotics.testmirror.dataStructures.Account
import javax.inject.Inject

class ProfileModel @Inject constructor(private val application: Application,
                                       private val preferencesManager: SharedPreferencesManager)  {
    val updateCallback = buildUpdateCallback()

    var currentAccount: Account? = preferencesManager.getAccount() ; set (value) {
        field = value
        preferencesManager.setAccount(value)
    }

    fun registerCallback() {
        application.backendService?.registerCallback(updateCallback)
    }

    fun unregister() {
        application.backendService?.unregisterCallback(updateCallback)
    }

    private fun buildUpdateCallback(): IProfileCallback {
        return object : DefaultProfileCallback() {
            override fun update(account: Account?) {
                account?.let { accountUpdate ->
                    currentAccount?.let { current ->
                        if (current.userEmail == accountUpdate.userEmail) {
                            currentAccount = accountUpdate
                        }
                    }
                }
            }
        }
    }

    fun setAccount(account: Account) {
        currentAccount = account
        preferencesManager.setAccount(account)
    }
}