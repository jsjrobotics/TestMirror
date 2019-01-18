package com.jsjrobotics.testmirror.settings

import com.jsjrobotics.testmirror.NavigationController
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(private val model: SettingsModel,
                                            private val navigationController: NavigationController){
    private lateinit var view: SettingsView

    private val disposables = CompositeDisposable()

    fun init(v: SettingsView) {
        view = v
        val connectedDisposable = model.onConnectedMirrorsReceived.subscribe (view::displayConnectedMirrors)
        val connectAnotherDisposable = view.onConnectAnother.subscribe {
            navigationController.showConnectToMirror()
        }
        disposables.addAll(connectedDisposable, connectAnotherDisposable)
    }

    fun loadData() {
        model.requestConnectedMirrors()
    }

}
