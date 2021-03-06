package com.jsjrobotics.testmirror.service.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.LoginData
import com.jsjrobotics.testmirror.dataStructures.networking.requests.LoginRequest
import com.jsjrobotics.testmirror.dataStructures.networking.responses.LoginResponse
import com.jsjrobotics.testmirror.service.networking.RefineMirrorApi

class PerformLoginTask(private val getPersistentData: (String) -> CachedProfile?,
                       private val backend: RefineMirrorApi,
                       private val callback: IProfileCallback,
                       private val data: LoginData) : Runnable {
    override fun run() {
        val userEmail = data.userEmail
        val password = data.password
        val savedData: CachedProfile? = getPersistentData(userEmail)
        if (savedData == null) {
            handleEmptyCache()
            return
        }
        if (savedData.account.userPassword == password) {
            callback.loginSuccess(savedData.account)
        } else {
            callback.loginFailure()
        }
    }

    private fun handleEmptyCache() {
        val loginRequest = LoginRequest(data)
        val request = backend.login(loginRequest)
        try {
            val result = request.execute()
            val bodyResponse = result.body()
            if (result.isSuccessful && bodyResponse != null) {
                val account = getAccountData(bodyResponse)
                if (account != null) {
                    callback.loginSuccess(account)
                } else {
                    callback.loginFailure()
                }
            } else {
                callback.loginFailure()
            }
        } catch (e: Exception) {
            ERROR("Failed to download login data: $e")
            callback.loginFailure()
        }
    }

    private fun getAccountData(response: LoginResponse): Account? {
        val request = backend.getUserData(response.data.apiToken)
        try {
            val result = request.execute()
            if (result.isSuccessful) {

                return null
            }
        } catch (e: Exception) {
            ERROR("Failed to get account data: $e")
        }
        return null
    }
}