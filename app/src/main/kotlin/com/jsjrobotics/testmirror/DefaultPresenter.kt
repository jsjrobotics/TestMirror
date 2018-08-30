package com.jsjrobotics.testmirror

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

open class DefaultPresenter : LifecycleObserver{

    protected val disposables = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun clearDisposables() {
        disposables.clear()
    }
}
