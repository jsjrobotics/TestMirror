package com.jsjrobotics.testmirror.updateInfo

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultProfileCallback
import com.jsjrobotics.testmirror.IProfileCallback
import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData
import com.jsjrobotics.testmirror.profile.ProfileModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateInfoModel @Inject constructor(val application: Application,
                                          val profileModel: ProfileModel,
                                          val navigationController: NavigationController){

    private val updateFailure : PublishSubject<Unit> = PublishSubject.create()
    val onUpdateFailure : Observable<Unit> = updateFailure

    fun saveUpdateInfo(data: UpdateInfoData) {
        application.dataPersistenceService?.attemptUpdateInfo(buildUpdateReceiver(),
                                                              profileModel.currentAccount,
                                                              data)
    }

    private fun buildUpdateReceiver(): IProfileCallback {
        return object: DefaultProfileCallback() {
            override fun updateInfoSuccess() {
                navigationController.showProfile()
            }
            override fun updateInfoFailure() {
                updateFailure.onNext(Unit)
            }
        }
    }
}