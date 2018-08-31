package com.jsjrobotics.testmirror.service.tasks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.IBinder
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.*
import com.jsjrobotics.testmirror.service.DataPersistenceService
import java.util.*

class PerformUpdateTask(private val getPersistentData: (String) -> CachedProfile?,
                        private val writePersistentData: (CachedProfile) -> Unit,
                        private val callback: IProfileCallback,
                        private val account: Account,
                        private val data: UpdateInfoData,
                        private val timeSource: () -> Long) : Runnable {
    override fun run() {
        if (!data.isValid()) {
            callback.updateInfoFailure()
            return
        }
        val oldData = getPersistentData(account.userEmail)
        if (oldData == null) {
            callback.updateInfoFailure()
            return
        }
        val date = Date()
        date.year
        val accountUpdate = oldData.account.copy(location = data.location, birthday = data.getTimeMilliseconds())
        val cacheUpdate = CachedProfile(accountUpdate, timeSource.invoke())
        writePersistentData(cacheUpdate)
        callback.updateInfoSuccess()
    }

}
