package com.jsjrobotics.testmirror

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.v4.app.Fragment

open class DefaultFragment : Fragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    protected fun addLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    protected fun removeLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.removeObserver(observer)
    }
}
