package com.jsjrobotics.testmirror.service

import com.jsjrobotics.testmirror.ERROR
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoBufSettings @Inject constructor(){
    val protoIdentityMap : Map<Class<out Message<*,*>>, ProtoAdapter<*>> = mapOf(
            Pair(IdentifyResponse::class.java, IdentifyResponse.ADAPTER)
    )

    val knownProtobufMessages : Set<Class<out Message<*,*>>> = protoIdentityMap.keys
    val knownEnvelopeTypes : Set<String> = knownProtobufMessages.map { it.javaClass.canonicalName }.toSet()
    private val listeners : MutableMap<String, MutableList<(Message<*,*>) -> Unit>> = mutableMapOf()

    fun addProtoBufListener(clazz: Class<out Message<*,*>>, consumer: ((Message<*,*>) -> Unit)) {
        listeners[clazz.javaClass.canonicalName]?.add(consumer)
    }
    fun dispatchMessageToProtoBufListeners( message : Message<*, *>) {
        val messageType = knownProtobufMessages.firstOrNull { messageClazz -> messageClazz.isAssignableFrom(message.javaClass) }
        val childMessage = messageType?.cast(message)
        childMessage?.let { dispatch(it) } ?: ERROR("Unknown message type. Unable to dispatch. $message")
    }

    private fun dispatch(message: Message<*, *>) {
        listeners[message::class.java.canonicalName]?.forEach { it.invoke(message) }
    }
}
