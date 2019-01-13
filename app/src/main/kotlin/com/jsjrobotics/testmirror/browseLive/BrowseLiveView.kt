package com.jsjrobotics.testmirror.browseLive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R
import javax.inject.Inject

class BrowseLiveView @Inject constructor(){
    lateinit var rootXml: View

    fun init(inflater: LayoutInflater, parent: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_browse_live, parent, false)
    }

}
