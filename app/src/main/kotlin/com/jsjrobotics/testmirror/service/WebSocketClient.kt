package com.jsjrobotics.testmirror.service

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WebSocketClient(uri: URI) : WebSocketClient(uri) {
    private val openEvent : PublishSubject<Boolean> = PublishSubject.create()
    private val closeEvent : PublishSubject<Boolean> = PublishSubject.create()
    private val messageEvent : PublishSubject<String> = PublishSubject.create()
    private val errorEvent : PublishSubject<Exception> = PublishSubject.create()

    val onOpenEvent : Observable<Boolean> = openEvent
    val onCloseEvent : Observable<Boolean> = closeEvent
    val onMessageEvent : Observable<String> = messageEvent
    val onErrorEvent : Observable<Exception> = errorEvent

    override fun onOpen(handshakedata: ServerHandshake?) {
        openEvent.onNext(true)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        closeEvent.onNext(true)
    }

    override fun onMessage(message: String?) {
        message?.let {
            messageEvent.onNext(it)
        }
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            errorEvent.onNext(it)
        }
    }
}