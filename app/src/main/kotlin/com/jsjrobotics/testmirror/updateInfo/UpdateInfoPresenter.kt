package com.jsjrobotics.testmirror.updateInfo

import com.jsjrobotics.testmirror.DefaultPresenter
import javax.inject.Inject

class UpdateInfoPresenter @Inject constructor() : DefaultPresenter(){
    private lateinit var view: UpdateInfoView

    fun init(v: UpdateInfoView) {
        view = v
        val saveClick = view.onSaveClick()
                .subscribe {
                    performSave()
                }
        disposables.add(saveClick)
    }

    private fun performSave() {
    }
}