package com.jsjrobotics.testmirror.connectToMirror

import android.support.annotation.ColorRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SelectMirrorViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private var mirrorName: TextView = itemView.findViewById(R.id.mirror_name)
    private val mirrorSelected : PublishSubject<Int> = PublishSubject.create()
    val onMirrorSelected : Observable<Int> = mirrorSelected

    private val UNKNOWN_INDEX: Int = -1
    private var mirrorIndex: Int = UNKNOWN_INDEX

    fun setMirrorName(name: String, index: Int) {
        mirrorName.text = name
        mirrorIndex = index
        mirrorName.setOnClickListener { mirrorSelected.onNext(mirrorIndex) }
    }


    fun setSelected() {
        mirrorName.setBackgroundColor(getColor(R.color.selected_background))
    }

    fun setUnselected() {
        mirrorName.setBackgroundColor(getColor(R.color.unselected_background))
    }



    private fun getColor(@ColorRes resourceId: Int): Int {
        return mirrorName.resources.getColor(resourceId, null)
    }

    fun unbind() {
        mirrorName.text = ""
        mirrorIndex = UNKNOWN_INDEX
        mirrorName.setOnClickListener(null)
    }

}
