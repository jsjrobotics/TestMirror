package com.jsjrobotics.testmirror.connectToMirror

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SelectMirrorAdapter(private val serviceNames: List<String>) : RecyclerView.Adapter<SelectMirrorViewHolder>() {
    private val mirrorSelected : PublishSubject<Int> = PublishSubject.create()
    val onMirrorSelected: Observable<Int> = mirrorSelected
    private val disposables = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMirrorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = SelectMirrorViewHolder(inflater.inflate(R.layout.view_holder_select_mirror, parent, false))
        val onSelectedDisposable = viewHolder.onMirrorSelected.subscribe { mirrorSelected.onNext(it) }
        disposables.add(onSelectedDisposable)
        return viewHolder
    }

    override fun getItemCount(): Int = serviceNames.size

    override fun onBindViewHolder(viewHolder: SelectMirrorViewHolder, index: Int) {
        viewHolder.setMirrorName(serviceNames[index], index)
    }

    override fun onViewDetachedFromWindow(viewHolder: SelectMirrorViewHolder) {
        viewHolder.unbind()
    }
}
