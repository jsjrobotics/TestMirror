package com.jsjrobotics.testmirror.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R
import javax.inject.Inject

class SettingsView @Inject constructor(){
    lateinit var rootXml: View

    private lateinit var connectedMirrors: RecyclerView

    fun init(inflater: LayoutInflater, parent: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_settings, parent, false)
        connectedMirrors = rootXml.findViewById(R.id.connected_mirrors)
    }

}
