package com.jsjrobotics.testmirror.connectToMirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.NavigationController
import javax.inject.Inject

class ConnectToMirrorFragment : DefaultFragment() {

    companion object {
        val TAG = "ConnectToMirrorFragment"
    }
    @Inject
    lateinit var presenter : ConnectToMirrorPresenter

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var view : ConnectToMirrorView

    override fun getFragmentId(): FragmentId = FragmentId.CONNECT_TO_MIRROR

    override fun onStart() {
        super.onStart()
        presenter.bind(view, this::onConnectSuccess)
    }

    private fun onConnectSuccess() {
        activity?.finish()
    }

    override fun onStop() {
        super.onStop()
        view.unsubscribe()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver(presenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {
            view.init(inflater, it)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        removeLifecycleObserver(presenter)
        super.onDestroy()
    }
}