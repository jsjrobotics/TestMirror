package com.jsjrobotics.testmirror.updateInfo

import android.arch.lifecycle.LifecycleObserver
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UpdateInfoPresenter @Inject constructor() : LifecycleObserver{
    private lateinit var view: UpdateInfoView

    private val disposables = CompositeDisposable()

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