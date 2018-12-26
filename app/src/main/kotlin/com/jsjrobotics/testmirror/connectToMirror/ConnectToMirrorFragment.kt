package com.jsjrobotics.testmirror.connectToMirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class ConnectToMirrorFragment : DefaultFragment() {

    companion object {
        val TAG = "ConnectToMirrorFragment"
    }
    @Inject
    lateinit var presenter : ConnectToMirrorPresenter

    @Inject
    lateinit var view : ConnectToMirrorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver(presenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {
            view.init(inflater, it)
            presenter.bind(view)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        removeLifecycleObserver(presenter)
        super.onDestroy()
    }
}