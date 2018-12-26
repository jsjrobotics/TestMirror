package com.jsjrobotics.testmirror.connectToMirror

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jsjrobotics.testmirror.R

class SelectMirrorViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private var mirrorName: TextView = itemView.findViewById(R.id.mirror_name)

    fun setMirrorName(name: String) {
        mirrorName.text = name
    }

}
