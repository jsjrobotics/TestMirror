package com.jsjrobotics.testmirror.service.http.tasks

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IntentConstants
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.service.http.HttpConstants
import com.jsjrobotics.testmirror.service.http.RefineMirrorApi
import java.io.Serializable

class OnDemandWorkoutsTask (private val backend: RefineMirrorApi,
                            private val loginModel: LoginModel,
                            private val localBroadcastManager: LocalBroadcastManager) : Runnable {
    override fun run() {
        val token = loginModel.requestToken
        if (token.isEmpty()) {
            ERROR("NO login token")
            return
        }
        val request = backend.getListing(token, HttpConstants.WORKOUT_TYPE_ON_DEMAND)
        try {
            val result = request.execute()
            val intent = Intent(IntentConstants.ON_DEMAND_WORKOUTS_ACTION)
            if (result.isSuccessful) {
                val data = result.body()?.data
                val resultBundle = Bundle()
                data?.let {
                    resultBundle.putSerializable(IntentConstants.EXTRA_WORKOUTS, it as Serializable)
                }
                intent.putExtra(IntentConstants.BUNDLE_EXTRA, resultBundle)
            }
            localBroadcastManager.sendBroadcast(intent)
            return
        } catch (e : Exception) {
            ERROR("Failed to get data: $e")
            val intent = Intent(IntentConstants.ON_DEMAND_WORKOUTS_ACTION)
            intent.putExtra(IntentConstants.EXTRA_ERROR, e)
            localBroadcastManager.sendBroadcast(intent)
        }

    }
}