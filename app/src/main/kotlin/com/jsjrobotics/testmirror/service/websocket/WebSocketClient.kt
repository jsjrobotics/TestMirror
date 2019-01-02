package com.jsjrobotics.testmirror.service.websocket

import com.mirror.framework.MessageAdapter
import com.mirror.proto.Envelope
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer

class WebSocketClient(uri: URI) : WebSocketClient(uri) {
    private val openEvent : PublishSubject<Boolean> = PublishSubject.create()
    private val closeEvent : PublishSubject<Boolean> = PublishSubject.create()
    private val messageEvent : PublishSubject<Envelope> = PublishSubject.create()
    private val errorEvent : PublishSubject<Exception> = PublishSubject.create()

    val onOpenEvent : Observable<Boolean> = openEvent
    val onCloseEvent : Observable<Boolean> = closeEvent
    val onMessageEvent : Observable<Envelope> = messageEvent
    val onErrorEvent : Observable<Exception> = errorEvent

    override fun onOpen(handshakedata: ServerHandshake?) {
        openEvent.onNext(true)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        closeEvent.onNext(true)
    }

    override fun onMessage(message: String?) { /* Unused*/ }

    override fun onMessage(bytes: ByteBuffer) {
        val envelope = MessageAdapter.decode(bytes.array())
        messageEvent.onNext(envelope)
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            errorEvent.onNext(it)
        }
    }
}