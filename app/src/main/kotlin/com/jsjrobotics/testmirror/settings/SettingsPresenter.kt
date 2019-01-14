package com.jsjrobotics.testmirror.settings

import javax.inject.Inject

class SettingsPresenter @Inject constructor(val model: SettingsModel){
    private lateinit var view: SettingsView

    fun init(v: SettingsView) {
        view = v
    }

    fun loadData() {
        model.requestConnectedMirrors()
    }

}
