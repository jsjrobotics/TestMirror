package com.jsjrobotics.testmirror.network

import com.jsjrobotics.testmirror.ERROR
import com.mirror.framework.MessageAdapter
import com.mirror.proto.Envelope
import com.mirror.proto.navigation.MirrorScreenRequest
import com.mirror.proto.oobe.PairResponse
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtobufDispatcher @Inject constructor(messageBroker : ProtoBufMessageBroker) {

    private class ProtoBufAdapterHandler<T>(val messageClazz: Class<out Message<*,*>>, val adapter: ProtoAdapter<T>, val handler : (T) -> Unit ) {
        val messageType : String = messageClazz.canonicalName!!
    }

    private val identityResponseHandle = ProtoBufAdapterHandler(
            IdentifyResponse::class.java,
            IdentifyResponse.ADAPTER,
            messageBroker::dispatchIdentityResponse
    )

    private val pairResponseHandle = ProtoBufAdapterHandler(
            PairResponse::class.java,
            PairResponse.ADAPTER,
            messageBroker::dispatchPairResponse
    )
    private val mirrorScreenRequestHandle = ProtoBufAdapterHandler(
            MirrorScreenRequest::class.java,
            MirrorScreenRequest.ADAPTER,
            messageBroker::dispatchMirrorScreenRequest
    )
    private val handlers : List<ProtoBufAdapterHandler<*>> = listOf(
            identityResponseHandle,
            pairResponseHandle,
            mirrorScreenRequestHandle
    )


    @Suppress("UNCHECKED_CAST")
    private val protoIdentityMap : MutableMap<Class<out Message<*, *>>, ProtoBufAdapterHandler<*>> = mutableMapOf<Class<out Message<*, *>>, ProtoBufAdapterHandler<*>>().apply{
        handlers.forEach {
            this[it.messageClazz] = it
        }
    }

    private val messageAdapter: MessageAdapter = MessageAdapter.Builder()
            .apply{
                protoIdentityMap.values.forEach {
                    addAdapter(it.messageType, it.adapter)
                }
            }.build()


    @Suppress("UNCHECKED_CAST")
    fun handleMessage(envelope: Envelope) {
        val adapterHandler = protoIdentityMap.values.firstOrNull { it.messageType == envelope.type }
        adapterHandler?.let {
            val message = messageAdapter.unpack(envelope)
            (it.handler as ((Message<*,*>) -> Unit))(message)
        } ?: ERROR("Received unknown message type: $envelope")
    }
}
