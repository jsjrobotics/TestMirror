package com.jsjrobotics.testmirror

import android.os.IBinder
import com.jsjrobotics.testmirror.dataStructures.Account

abstract class DefaultProfileCallback : IProfileCallback {

    override fun loginFailure() {}
    override fun loginSuccess(account: Account?) {}
    override fun signUpFailure(error: String?) {}
    override fun signUpSuccess() {}
    override fun asBinder(): IBinder? = null
    override fun update(account: Account?) {}
    override fun updateInfoSuccess() {}
    override fun updateInfoFailure() {}
}