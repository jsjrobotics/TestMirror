package com.jsjrobotics.testmirror.settings

import android.net.nsd.NsdServiceInfo
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.ConnectedMirrorMap
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.service.RemoteMirrorState
import javax.inject.Inject

class SettingsView @Inject constructor(){
    lateinit var rootXml: View

    private lateinit var connectedMirrors: RecyclerView

    fun init(inflater: LayoutInflater, parent: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_settings, parent, false)
        connectedMirrors = rootXml.findViewById(R.id.connected_mirrors)
        connectedMirrors.layoutManager = LinearLayoutManager(rootXml.context)
    }


    fun displayConnectedMirrors(mirrorMap : ConnectedMirrorMap ) {
        connectedMirrors.adapter = ConnectedMirrorAdapter(mirrorMap)
    }
}
