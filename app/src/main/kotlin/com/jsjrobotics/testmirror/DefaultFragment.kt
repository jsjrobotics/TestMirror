package com.jsjrobotics.testmirror

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class DefaultFragment : Fragment(){

    abstract fun getFragmentId() : FragmentId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    override fun onStart() {
        super.onStart()
        if (getFragmentId().isNavBarVisible()) {
            (activity as MainActivity?)?.setNavigationBarSelected(getFragmentId())
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (activity as MainActivity?)?.setNavigationBarSelected(getFragmentId())
        }
    }

    protected fun addLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    protected fun removeLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.removeObserver(observer)
    }
}
