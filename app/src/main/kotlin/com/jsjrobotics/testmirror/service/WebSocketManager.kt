package com.jsjrobotics.testmirror.service

import com.jsjrobotics.testmirror.ERROR
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

class WebSocketManager @Inject constructor() {
    private var client : WebSocketClient? = null
    private val clientObserverDisposables = CompositeDisposable()

    fun connectToClient(uri: URI) : Boolean {
        disconnectFromClient()
        val handleOpen : (Boolean) -> Unit = { handleOpenEvent() }
        val openError : (Throwable) -> Unit = { reportClientError("OnOpenEvent", it)}
        val handleClose : (Boolean) -> Unit = { handleCloseEvent() }
        val closeError : (Throwable) -> Unit = { reportClientError("OnCloseEvent", it)}
        val handleMessage = this::handleMessage
        val messageError : (Throwable) -> Unit = { reportClientError("OnMessageEvent", it)}
        val handleError = this::handleClientError
        val exceptionError : (Throwable) -> Unit = { reportClientError("OnExceptionEvent", it)}

        client = WebSocketClient(uri)
        client?.apply {
            val openDisposable = onOpenEvent.subscribe( handleOpen , openError)
            val closeDisposable = onCloseEvent.subscribe( handleClose , closeError)
            val messageDisposable = onMessageEvent.subscribe( handleMessage, messageError)
            val errorDisposable = onErrorEvent.subscribe ( handleError, exceptionError )
            clientObserverDisposables.addAll(openDisposable, closeDisposable, messageDisposable, errorDisposable)
        } ?: ERROR("Failed to create websocket client")

        return client?.connectBlocking() ?: false
    }

    private fun disconnectFromClient() {
        client?.closeBlocking()
        clientObserverDisposables.clear()
    }

    private fun handleClientError(exception: Exception) {

    }

    private fun handleMessage(message: String) {
    }

    private fun reportClientError(functionName: String, error: Throwable) {
        ERROR("$functionName error -> ${error.message}")
    }

    private fun handleOpenEvent() {

    }

    private fun handleCloseEvent() {

    }
}