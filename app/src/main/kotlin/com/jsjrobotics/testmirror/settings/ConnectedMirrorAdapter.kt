package com.jsjrobotics.testmirror.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.ConnectedMirrorMap
import com.jsjrobotics.testmirror.R

class ConnectedMirrorAdapter(val mirrorMap: ConnectedMirrorMap) : RecyclerView.Adapter<ConnectedMirrorViewHolder>() {
    val keyIndex = mirrorMap.keys.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedMirrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConnectedMirrorViewHolder(inflater.inflate(R.layout.view_holder_connected_mirror, parent, false))
    }

    override fun getItemCount(): Int = mirrorMap.size

    override fun onBindViewHolder(viewHolder: ConnectedMirrorViewHolder, position: Int) {
        val mirror = mirrorMap[keyIndex[position]]!!
        viewHolder.bind(mirror)
    }
}