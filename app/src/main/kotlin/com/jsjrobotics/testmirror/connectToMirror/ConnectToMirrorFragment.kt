package com.jsjrobotics.testmirror.connectToMirror

import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class ConnectToMirrorFragment : DefaultFragment() {

    @Inject
    lateinit var presenter : ConnectToMirrorPresenter

    @Inject
    lateinit var view : ConnectToMirrorView
}