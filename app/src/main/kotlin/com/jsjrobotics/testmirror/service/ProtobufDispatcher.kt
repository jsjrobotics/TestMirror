package com.jsjrobotics.testmirror.service

import com.jsjrobotics.testmirror.BuildConfig
import com.jsjrobotics.testmirror.DEBUG
import com.jsjrobotics.testmirror.ERROR
import com.mirror.framework.MessageAdapter
import com.mirror.proto.Envelope
import com.mirror.proto.navigation.MirrorScreenRequest
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtobufDispatcher @Inject constructor() {
    
    private val protoIdentityMap : Map<Class<out Message<*, *>>, ProtoAdapter<*>> = mapOf(
            Pair(IdentifyResponse::class.java, IdentifyResponse.ADAPTER),
            Pair(MirrorScreenRequest::class.java, MirrorScreenRequest.ADAPTER)
    )

    private val handleProtoMessage : Map<String, (Message<*, *>) -> Unit> = mapOf(
            Pair(IdentifyResponse::class.java.canonicalName, { k -> handleIdentityResponse(k as IdentifyResponse) }),
            Pair(MirrorScreenRequest::class.java.canonicalName, { _ -> })
    )

    init {
        // Runtime verification of proto map constants
        if (BuildConfig.DEBUG) {
            protoIdentityMap.keys.map { it.canonicalName!! }
                    .forEach { protobufType ->
                        if (handleProtoMessage[protobufType] == null) {
                            throw IllegalStateException("$protobufType is not in handleProtoMessage")
                        }
                    }
        }
    }

    private fun handleIdentityResponse(identifyResponse: IdentifyResponse) {
        DEBUG("Received Identify Response message: $identifyResponse")
    }

    private val messageAdapter: MessageAdapter = MessageAdapter.Builder()
            .apply{ protoIdentityMap.keys.forEach { messageType ->
                addAdapter(messageType.canonicalName, protoIdentityMap[messageType])
            }}.build()

    fun handleMessage(envelope: Envelope) {
        handleProtoMessage[envelope.type]?.let { messageHandler ->
            val message = messageAdapter.unpack(envelope)
            messageHandler.invoke(message)
        } ?: ERROR("Received unknown message type: $envelope")
    }
}
