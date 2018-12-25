package com.jsjrobotics.testmirror.connectToMirror

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import javax.inject.Inject

class ConnectToMirrorView @Inject constructor() : DefaultView(){
    override fun getContext(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var rootXml: ViewGroup

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_connect_to_mirror, container, false) as ViewGroup
    }
}
