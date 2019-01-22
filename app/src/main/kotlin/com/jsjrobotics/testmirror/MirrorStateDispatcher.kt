package com.jsjrobotics.testmirror

import com.jsjrobotics.testmirror.service.websocket.WebSocketManager
import com.jsjrobotics.testmirror.service.websocket.tasks.ClientState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MirrorStateDispatcher @Inject constructor() {
    fun onConnectedToMirror() { }

    private val clientStateEvent : PublishSubject<ClientState> = PublishSubject.create()
    val onOpenCloseEvent : Observable<ClientState> = clientStateEvent

    fun handleOpenEvent(webSocketManager: WebSocketManager) {
        clientStateEvent.onNext(ClientState(webSocketManager, true))
    }

    fun handleCloseEvent(webSocketManager: WebSocketManager) {
        clientStateEvent.onNext(ClientState(webSocketManager, false))
    }

}
