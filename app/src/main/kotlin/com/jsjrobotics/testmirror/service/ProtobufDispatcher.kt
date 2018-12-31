package com.jsjrobotics.testmirror.service

import com.jsjrobotics.testmirror.BuildConfig
import com.jsjrobotics.testmirror.ERROR
import com.mirror.framework.MessageAdapter
import com.mirror.proto.Envelope
import com.mirror.proto.navigation.MirrorScreenRequest
import com.mirror.proto.oobe.PairResponse
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtobufDispatcher @Inject constructor(messageHandlers : ProtoBufMessageBroker) {

    private class ProtoBufAdapterHandler<T>(val messageType: String, val adapter: ProtoAdapter<T>, val handler : (T) -> Unit )
    private val identityResponseHandle = ProtoBufAdapterHandler(
            IdentifyResponse::class.java.canonicalName!!,
            IdentifyResponse.ADAPTER,
            messageHandlers::dispatchIdentityResponse
    )

    private val pairResponseHandle = ProtoBufAdapterHandler(
            PairResponse::class.java.canonicalName!!,
            PairResponse.ADAPTER,
            messageHandlers::dispatchPairResponse
    )
    private val mirrorScreenRequestHandle = ProtoBufAdapterHandler(
            MirrorScreenRequest::class.java.canonicalName!!,
            MirrorScreenRequest.ADAPTER,
            messageHandlers::dispatchMirrorScreenRequest
    )

    private val protoIdentityMap : Map<Class<out Message<*, *>>, ProtoBufAdapterHandler<*>> = mapOf(
            Pair(IdentifyResponse::class.java, identityResponseHandle),
            Pair(MirrorScreenRequest::class.java, mirrorScreenRequestHandle)
    )

    private val messageAdapter: MessageAdapter = MessageAdapter.Builder()
            .apply{
                protoIdentityMap.values.forEach {
                    addAdapter(it.messageType, it.adapter)
                }
            }.build()

    init {
        // Runtime check to verify all messages covered
        if (BuildConfig.DEBUG) {
            protoIdentityMap.keys.forEach {
                val keyNameEqualsMessageType = protoIdentityMap[it]!!.messageType == it.canonicalName
                if (!keyNameEqualsMessageType) {
                    throw IllegalStateException("$it has an invalid adapterHandle")
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun handleMessage(envelope: Envelope) {
        val adapterHandler = protoIdentityMap.values.firstOrNull { it.messageType == envelope.type }
        adapterHandler?.let {
            val message = messageAdapter.unpack(envelope)
            (it.handler as ((Message<*,*>) -> Unit))(message)
        } ?: ERROR("Received unknown message type: $envelope")
    }
}
