package com.jsjrobotics.testmirror.service.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.LoginData
import com.jsjrobotics.testmirror.dataStructures.networking.requests.LoginRequest
import com.jsjrobotics.testmirror.dataStructures.networking.responses.LoginResponse
import com.jsjrobotics.testmirror.dataStructures.networking.responses.UserDataResponse
import com.jsjrobotics.testmirror.service.networking.Paths
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
                val account = getUserData(bodyResponse)
                account?.let {
                    callback.loginSuccess(it)
                    return
                }
                callback.loginFailure()
            } else {
                callback.loginFailure()
            }
        } catch (e: Exception) {
            ERROR("Failed to download login data: $e")
            callback.loginFailure()
        }
    }

    private fun getUserData(response: LoginResponse): Account? {
        val requestToken = Paths.buildAuthorizationHeader(response.data.apiToken)
        val request = backend.getUserData(requestToken)
        try {
            val result = request.execute()
            if (result.isSuccessful) {
                result.body()?.let {
                    return Account(it.data)
                }
            }
        } catch (e: Exception) {
            ERROR("Failed to get account data: $e")
        }
        return null
    }
}