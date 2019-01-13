package com.jsjrobotics.testmirror.service.websocket

import com.jsjrobotics.testmirror.service.websocket.tasks.ClientState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientStateDispatcher @Inject constructor(){
    private val clientStateEvent : PublishSubject<ClientState> = PublishSubject.create()
    val onOpenEvent : Observable<ClientState> = clientStateEvent

    fun handleOpenEvent(webSocketManager: WebSocketManager) {
        clientStateEvent.onNext(ClientState(webSocketManager, true))
    }

    fun handleCloseEvent(webSocketManager: WebSocketManager) {
        clientStateEvent.onNext(ClientState(webSocketManager, false))
    }

}
