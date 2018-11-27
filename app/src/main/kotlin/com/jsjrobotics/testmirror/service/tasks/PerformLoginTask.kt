package com.jsjrobotics.testmirror.service.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.LoginData
import com.jsjrobotics.testmirror.dataStructures.networking.requests.LoginRequest
import com.jsjrobotics.testmirror.dataStructures.networking.responses.LoginResponse
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.service.networking.Paths
import com.jsjrobotics.testmirror.service.networking.RefineMirrorApi
import kotlin.math.log

class PerformLoginTask(private val getPersistentData: (String) -> CachedProfile?,
                       private val backend: RefineMirrorApi,
                       private val callback: IProfileCallback,
                       private val data: LoginData,
                       private val loginModel: LoginModel) : Runnable {
    private val offlineLoginEnabled = false

    override fun run() {
        val userEmail = data.userEmail
        val password = data.password
        if (offlineLoginEnabled) {
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
        } else {
            handleEmptyCache()
        }

    }

    private fun handleEmptyCache() {
        val loginRequest = LoginRequest(data)
        val request = backend.login(loginRequest)
        try {
            val result = request.execute()
            val bodyResponse = result.body()
            if (result.isSuccessful && bodyResponse != null) {
                val requestToken = Paths.buildAuthorizationHeader(bodyResponse.data.apiToken)
                loginModel.loggedInToken = requestToken
                val account = getUserData(requestToken)
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

    private fun getUserData(token: String): Account? {
        val request = backend.getUserData(token)
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