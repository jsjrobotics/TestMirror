package com.jsjrobotics.testmirror.service

import com.jsjrobotics.testmirror.DEBUG
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.mirror.framework.MessageAdapter
import com.mirror.proto.Envelope
import com.mirror.proto.user.Environment
import com.mirror.proto.user.IdentifyRequest
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

class WebSocketManager @Inject constructor(private val profileModel: ProfileModel,
                                           private val loginModel: LoginModel) {
    private var client : WebSocketClient? = null
    private val clientObserverDisposables = CompositeDisposable()

    private val protoIdentityMap : Map<String, ProtoAdapter<*>> = mapOf(
            Pair(IdentifyResponse::class.java.canonicalName, IdentifyResponse.ADAPTER)
    )

    private val messageAdapter: MessageAdapter = MessageAdapter.Builder()
            .apply{ protoIdentityMap.keys.forEach { messageType ->
                addAdapter(messageType, protoIdentityMap[messageType])
            }}.build()

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

    private fun handleMessage(envelope: Envelope) {
        if (protoIdentityMap.keys.contains(envelope.type)) {
            val message = messageAdapter.unpack(envelope)
            when(message) {
                is IdentifyResponse -> DEBUG("Received Identify Response message")
            }
        } else {
            ERROR("Received unknown message type: $envelope")
        }
    }

    private fun reportClientError(functionName: String, error: Throwable) {
        ERROR("$functionName error -> ${error.message}")
    }

    private fun handleOpenEvent() {
        sendIdentity()
    }

    private fun sendIdentity() {
        val request = IdentifyRequest.Builder()
                .name(profileModel.currentAccount?.fullName)
                .email(profileModel.currentAccount?.userEmail)
                .token(loginModel.loggedInToken)
                .environment(Environment.ENVIRONMENT_UNKNOWN)
                .id(profileModel.currentAccount?.uuid)
                .build()
        send(request)
    }

    private fun send(message: Message<*, *>) {
        client?.send(MessageAdapter.encode(message)) ?: ERROR("Failed to send $message to null client")
    }

    private fun handleCloseEvent() {

    }
}