package com.jsjrobotics.testmirror.service.http.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.dataStructures.Account
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData
import com.jsjrobotics.testmirror.dataStructures.networking.UpdateUserDataRequest
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.service.http.RefineMirrorApi
import java.io.IOException

class UpdateTask(private val getPersistentData: (String) -> CachedProfile?,
                 private val writePersistentData: (CachedProfile) -> Unit,
                 private val callback: IProfileCallback,
                 private val account: Account,
                 private val updateInfoData: UpdateInfoData,
                 private val timeSource: () -> Long,
                 private val backend: RefineMirrorApi,
                 private val loginModel: LoginModel) : Runnable {
    override fun run() {

        if (!updateInfoData.isValid()) {
            callback.updateInfoFailure("Invalid Data")
            return
        }
        val oldData = getPersistentData(account.userEmail)
        if (oldData == null) {
            callback.updateInfoFailure("Account updateInfoData not found. Was signup used?")
            return
        }
        val accountUpdate = oldData.account.copy(location = updateInfoData.location, birthday = updateInfoData.getTimeMilliseconds())
        val cacheUpdate = CachedProfile(accountUpdate, timeSource.invoke())
        writePersistentData(cacheUpdate)
        loginModel.loggedInToken?.let {
            sendUpdateRequest(it)
            return
        }
        callback.updateInfoFailure("Failed to update backend")
    }

    private fun sendUpdateRequest(loggedInToken: String) {
        val requestData = UpdateUserDataRequest(account.fullName,
                updateInfoData.birthdayString(),
                updateInfoData.location)
        val request = backend.updateUserData(loggedInToken, requestData)
        try {
            val result = request.execute()
            if (result.isSuccessful) {
                callback.updateInfoSuccess()
                return
            }
        } catch (exception: IOException) {
            ERROR("Failed to update account info remotely")
        }
    }

}
