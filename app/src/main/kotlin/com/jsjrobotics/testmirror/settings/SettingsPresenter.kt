package com.jsjrobotics.testmirror.settings

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(val model: SettingsModel){
    private lateinit var view: SettingsView

    private val disposables = CompositeDisposable()

    fun init(v: SettingsView) {
        view = v
        val connectedDisposable = model.onConnectedMirrorsReceived.subscribe (view::displayConnectedMirrors)
        disposables.add(connectedDisposable)
    }

    fun loadData() {
        model.requestConnectedMirrors()
    }

}
