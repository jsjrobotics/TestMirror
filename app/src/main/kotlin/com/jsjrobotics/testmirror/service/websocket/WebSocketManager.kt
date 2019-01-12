package com.jsjrobotics.testmirror.service.websocket

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.network.ProtobufEnvelopeHandler
import com.mirror.framework.MessageAdapter
import com.squareup.wire.Message
import io.reactivex.disposables.CompositeDisposable
import java.net.URI
import javax.inject.Inject

/**
 * Enforces connection to only one client at a time
 * Is not a singleton so multiple instances can be created, however state callbacks are passed along
 * to Singleton dispatchers
 */
class WebSocketManager @Inject constructor(private val protobufEnvelopeHandler : ProtobufEnvelopeHandler,
                                           private val clientStateDispatcher : ClientStateDispatcher) {
    private var client : WebSocketClient? = null
    private val clientObserverDisposables = CompositeDisposable()


    fun connectToClient(uri: URI) : Boolean {
        disconnectFromClient()
        val handleOpen : (Boolean) -> Unit = { clientStateDispatcher.handleOpenEvent() }
        val openError : (Throwable) -> Unit = { reportClientError("OnOpenEvent", it)}
        val handleClose : (Boolean) -> Unit = {
            handleCloseEvent()
            clientStateDispatcher.handleCloseEvent()
        }
        val closeError : (Throwable) -> Unit = { reportClientError("OnCloseEvent", it)}
        val messageError : (Throwable) -> Unit = { reportClientError("OnMessageEvent", it)}
        val handleError = this::handleClientError
        val exceptionError : (Throwable) -> Unit = { reportClientError("OnExceptionEvent", it)}

        client = WebSocketClient(uri).apply {
            val openDisposable = onOpenEvent.subscribe( handleOpen , openError)
            val closeDisposable = onCloseEvent.subscribe( handleClose , closeError)
            val messageDisposable = onMessageEvent.subscribe( protobufEnvelopeHandler::handleMessage, messageError)
            val errorDisposable = onErrorEvent.subscribe ( handleError, exceptionError )

            clientObserverDisposables.addAll(openDisposable,
                    closeDisposable,
                    messageDisposable,
                    errorDisposable)
        }
        val connected = client?.connectBlocking() ?: false
        if (!connected) {
            disconnectFromClient()
        }
        return connected
    }

    private fun disconnectFromClient() {
        client?.closeBlocking()
        handleCloseEvent()
    }

    private fun handleClientError(exception: Exception) {
        handleCloseEvent()
    }

    private fun reportClientError(functionName: String, error: Throwable) {
        ERROR("$functionName error -> ${error.message}")
    }

    fun send(message: Message<*, *>) {
        client?.send(MessageAdapter.encode(message)) ?: ERROR("Failed to send $message to null client")
    }

    private fun handleCloseEvent() {
        clientObserverDisposables.clear()
    }

    fun isConnected(): Boolean {
        return client?.isOpen ?: false
    }
}