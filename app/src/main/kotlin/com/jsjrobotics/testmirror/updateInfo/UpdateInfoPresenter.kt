package com.jsjrobotics.testmirror.updateInfo

import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData
import javax.inject.Inject

class UpdateInfoPresenter @Inject constructor(val application: Application,
                                              val model: UpdateInfoModel) : DefaultPresenter(){
    private lateinit var view: UpdateInfoView

    fun init(v: UpdateInfoView) {
        view = v
        val saveClick = view.onSaveClick()
                .subscribe {
                    performSave(it)
                }
        disposables.add(saveClick)
    }

    private fun performSave(data: UpdateInfoData) {
        if (data.isValid()) {
            model.saveUpdateInfo(data)
        } else {
            view.showEnterAllFields()
        }
    }
}