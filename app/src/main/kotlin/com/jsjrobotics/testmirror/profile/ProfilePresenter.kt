package com.jsjrobotics.testmirror.profile

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DEBUG
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.IntentConstants
import com.jsjrobotics.testmirror.dataStructures.networking.responses.ListingResponseData
import com.mirror.proto.navigation.MirrorScreen
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor(val application: Application,
                                           val localBroadcastManager: LocalBroadcastManager) : DefaultPresenter(){
    private lateinit var view: ProfileView
    private val disposables = CompositeDisposable()

    fun init(v: ProfileView) {
        view = v
        registerReceiver()
        disposables.add(view.onRefreshClick
                .observeOn(Schedulers.io())
                .subscribe{
                    sendScreenRequest()
                    application.backendService?.getOnDemandWorkout()
                })
    }

    fun sendScreenRequest() {
        application.webSocketService?.sendScreenRequest(MirrorScreen.DASHBOARD.name)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
        unregisterReceiver()
    }

    private val workoutReceiver: BroadcastReceiver = buildWorkoutReceiver()

    private fun registerReceiver() {
        localBroadcastManager.registerReceiver(workoutReceiver,
                IntentFilter(IntentConstants.ON_DEMAND_WORKOUTS_ACTION))
    }

    private fun unregisterReceiver() {
        localBroadcastManager.unregisterReceiver(workoutReceiver)
    }

    private fun buildWorkoutReceiver() : BroadcastReceiver{
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val workoutData = intent?.getBundleExtra(IntentConstants.BUNDLE_EXTRA)
                        ?.getSerializable(IntentConstants.EXTRA_WORKOUTS) as List<ListingResponseData>?
                workoutData?.let {
                    DEBUG("Received data")
                }
            }

        }
    }
}