package com.jsjrobotics.testmirror.settings

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.service.RemoteMirrorState

class ConnectedMirrorViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val name: TextView = itemView.findViewById(R.id.name_value)

    fun bind(state: RemoteMirrorState) {
        name.text = state.getMirrorName()
    }

}
