package com.jsjrobotics.testmirror.settings

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jsjrobotics.testmirror.ConnectedMirrorMap
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.runOnUiThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SettingsView @Inject constructor(){
    lateinit var rootXml: View

    private lateinit var connectedMirrors: RecyclerView

    private lateinit var connectAnotherMirror: Button

    private val connectAnother : PublishSubject<Boolean> = PublishSubject.create()
    val onConnectAnother : Observable<Boolean> = connectAnother

    fun init(inflater: LayoutInflater, parent: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_settings, parent, false)
        connectedMirrors = rootXml.findViewById(R.id.connected_mirrors)
        connectedMirrors.layoutManager = LinearLayoutManager(rootXml.context)
        connectAnotherMirror = rootXml.findViewById(R.id.connect_another_mirror)
        connectAnotherMirror.setOnClickListener { connectAnother.onNext(true) }
    }


    fun displayConnectedMirrors(mirrorMap : ConnectedMirrorMap ) {
        runOnUiThread {
            connectedMirrors.adapter = ConnectedMirrorAdapter(mirrorMap)
        }
    }
}
