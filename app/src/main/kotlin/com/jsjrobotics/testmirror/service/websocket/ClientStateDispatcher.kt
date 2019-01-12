package com.jsjrobotics.testmirror.service.websocket

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientStateDispatcher @Inject constructor(){
    private val openEvent : PublishSubject<Boolean> = PublishSubject.create()
    val onOpenEvent : Observable<Boolean> = openEvent

    fun handleOpenEvent() {
        openEvent.onNext(true)
    }

    fun handleCloseEvent() {
        openEvent.onNext(false)
    }

}
