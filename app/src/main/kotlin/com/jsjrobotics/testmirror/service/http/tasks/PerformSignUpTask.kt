package com.jsjrobotics.testmirror.service.http.tasks

import android.content.res.Resources
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.SignUpData
import com.jsjrobotics.testmirror.dataStructures.networking.requests.SignUpRequest
import com.jsjrobotics.testmirror.service.http.RefineMirrorApi

class PerformSignUpTask(private val getPersistentData: (String) -> CachedProfile?,
                        private val writePersistentData: (SignUpData) -> CachedProfile,
                        private val updateDataStore: (String, CachedProfile) -> Unit,
                        private val backend: RefineMirrorApi,
                        private val resources: Resources,
                        private val callback: IProfileCallback,
                        private val data: SignUpData) : Runnable {
    override fun run() {

        if (!data.isValid()) {
            callback.signUpFailure(resources.getString(R.string.invalid_credentials))
            return
        }
        if (getPersistentData(data.email) != null) {
            callback.signUpFailure(resources.getString(R.string.email_already_exists))
            return
        }
        val signUpRequest = SignUpRequest(data)
        val request = backend.signUp(signUpRequest)
        try {
            val result = request.execute()
            if (result.isSuccessful) {
                val profile = writePersistentData(data)
                updateDataStore(data.email,profile)
                callback.signUpSuccess()
                return
            } else {
                val errorString = result.errorBody()?.string() ?: resources.getString(R.string.unknown_error)
                callback.signUpFailure(errorString)
            }
        } catch (e : Exception) {
            ERROR("Failed to signup: $e")
            callback.signUpFailure(e.message ?: resources.getString(R.string.unknown_error))
        }

    }
}