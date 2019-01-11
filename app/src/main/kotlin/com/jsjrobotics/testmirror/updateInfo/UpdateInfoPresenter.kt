package com.jsjrobotics.testmirror.updateInfo

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.DefaultPresenter
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UpdateInfoPresenter @Inject constructor(val application: Application,
                                              val model: UpdateInfoModel) : DefaultPresenter(){
    private lateinit var view: UpdateInfoView
    private val disposables = CompositeDisposable()

    fun init(v: UpdateInfoView) {
        view = v
        val saveClick = view.onSaveClick()
                .subscribe {
                    performSave(it)
                }
        val updateFailure = model.onUpdateFailure.subscribe { error ->
            view.showToast(error)
        }
        disposables.addAll(saveClick, updateFailure)
    }

    private fun performSave(data: UpdateInfoData) {
        if (data.isValid()) {
            model.saveUpdateInfo(data)
        } else {
            view.showEnterAllFields()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun clearDisposables() {
        disposables.clear()
    }
}