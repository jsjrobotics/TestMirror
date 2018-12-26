package com.jsjrobotics.testmirror.connectToMirror

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R

class SelectMirrorAdapter(private val serviceNames: List<String>) : RecyclerView.Adapter<SelectMirrorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMirrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SelectMirrorViewHolder(inflater.inflate(R.layout.view_holder_select_mirror, parent, false))
    }

    override fun getItemCount(): Int = serviceNames.size

    override fun onBindViewHolder(viewHolder: SelectMirrorViewHolder, index: Int) {
        viewHolder.setMirrorName(serviceNames[index])
    }

}
