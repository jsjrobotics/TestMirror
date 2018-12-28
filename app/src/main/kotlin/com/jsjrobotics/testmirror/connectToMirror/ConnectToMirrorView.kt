package com.jsjrobotics.testmirror.connectToMirror

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ViewFlipper
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.runOnUiThread
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConnectToMirrorView @Inject constructor() : DefaultView(){
    override fun getContext(): Context {
        return rootXml.context
    }

    lateinit var rootXml: ViewGroup ; private set

    private lateinit var mirrorList: RecyclerView

    private lateinit var loadingRoot: ViewGroup
    private lateinit var selectMirrorRoot: ViewGroup
    private lateinit var connectedRoot: ViewGroup
    private lateinit var connectButton: Button
    private var mirrorSelectedDisposable: Disposable? = null
    private val mirrorSelected : PublishSubject<Int> = PublishSubject.create()
    val onMirrorSelected: Observable<Int> = mirrorSelected

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_connect_to_mirror, container, false) as ViewGroup
        loadingRoot = rootXml.findViewById(R.id.loading)
        selectMirrorRoot = rootXml.findViewById(R.id.select_mirror)
        connectedRoot = rootXml.findViewById(R.id.connected)
        mirrorList = rootXml.findViewById(R.id.mirror_list)
        connectButton = rootXml.findViewById(R.id.connect)
        connectButton.setOnClickListener {  }
        connectButton.setText(R.string.connect)
        disableConnectButton()
        display(loadingRoot)
    }

    private fun display(toDisplay: ViewGroup) {
        arrayListOf(
                loadingRoot,
                selectMirrorRoot,
                connectedRoot
        ).forEach {
            if (it == toDisplay) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
    }

    fun displayMirrors(serviceNames: List<String>) {
        val adapter = SelectMirrorAdapter(serviceNames)
        mirrorSelectedDisposable = adapter.onMirrorSelected.subscribe{ mirrorSelected.onNext(it) }
        runOnUiThread {
            mirrorList.layoutManager = LinearLayoutManager(rootXml.context, LinearLayoutManager.HORIZONTAL, false)
            mirrorList.adapter = adapter
            display(selectMirrorRoot)
        }
    }

    fun onDestroy() {
        mirrorSelectedDisposable?.dispose()
    }

    fun setMirrorSelected(selectedMirror: Int) {
        0.until(mirrorList.childCount)
                .map { index  -> mirrorList.findViewHolderForAdapterPosition(index) as SelectMirrorViewHolder }
                .forEachIndexed { viewHolderIndex, viewHolder ->
                    if (viewHolderIndex == selectedMirror) {
                        viewHolder.setSelected()
                    } else {
                        viewHolder.setUnselected()
                    }
                }

    }

    fun enableConnectButton() {
        connectButton.visibility = View.VISIBLE
    }

    fun disableConnectButton() {
        connectButton.visibility = View.GONE
    }
}
