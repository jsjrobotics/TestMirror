package com.jsjrobotics.testmirror.service.tasks

import android.content.res.Resources
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.CachedProfile
import com.jsjrobotics.testmirror.dataStructures.SignUpData

class PerformSignUpTask(private val getPersistentData: (String) -> CachedProfile?,
                        private val writePersistentData: (SignUpData) -> CachedProfile,
                        private val updateDataStore: (String, CachedProfile) -> Unit,
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
        val profile = writePersistentData(data)
        updateDataStore(data.email,profile)
        callback.signUpSuccess()
    }
}