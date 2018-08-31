package com.jsjrobotics.testmirror.service.tasks

import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.LoginData

class PerformLoginTask(private val getPersistentData: (String) -> CachedProfile?,
                       val callback: IProfileCallback,
                       val data: LoginData) : Runnable {
    override fun run() {
        val userEmail = data.userEmail
        val password = data.password
        val savedData: CachedProfile? = getPersistentData(userEmail)
        if (savedData == null) {
            callback.loginFailure()
            return
        }
        if (savedData.account.userPassword == password) {
            callback.loginSuccess(savedData.account)
        } else {
            callback.loginFailure()
        }
    }
}