package com.jsjrobotics.testmirror

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.v4.app.Fragment
import javax.inject.Inject

open class DefaultFragment : Fragment(){

    @Inject
    protected lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            val isNavBarVisible = FragmentId.isNavBarVisibleFromTag(tag)
            isNavBarVisible?.let {
                navigationController.setNavigationBarVisibility(it)
            }
        }
    }

    protected fun addLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    protected fun removeLifecycleObserver(observer: LifecycleObserver) {
        lifecycle.removeObserver(observer)
    }
}
