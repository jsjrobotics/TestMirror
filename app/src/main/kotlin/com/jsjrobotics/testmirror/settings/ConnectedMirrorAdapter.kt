package com.jsjrobotics.testmirror.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.service.websocket.WebSocketManager

class ConnectedMirrorAdapter(val connectedMirrors: List<WebSocketManager>) : RecyclerView.Adapter<ConnectedMirrorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedMirrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ConnectedMirrorViewHolder(inflater.inflate(R.layout.view_holder_connected_mirror, parent, false))
    }

    override fun getItemCount(): Int = connectedMirrors.size

    override fun onBindViewHolder(viewHolder: ConnectedMirrorViewHolder, position: Int) {
        viewHolder.bind(connectedMirrors[position])
    }
}