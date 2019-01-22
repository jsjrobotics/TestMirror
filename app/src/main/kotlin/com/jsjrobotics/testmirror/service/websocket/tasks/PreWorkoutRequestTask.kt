package com.jsjrobotics.testmirror.service.websocket.tasks

import com.jsjrobotics.testmirror.service.websocket.WebSocketManager
import com.mirror.proto.Envelope
import com.mirror.proto.workout.PreWorkoutRequest
import okio.ByteString
import java.nio.ByteBuffer

class PreWorkoutRequestTask(private val socketManager: WebSocketManager,
                            private val uuid: String?,
                            private val variantId: String?,
                            private val workoutData: String?) : MirrorWebSocketTask() {
    override fun run() {
        val message = PreWorkoutRequest.Builder()
                .uuid(uuid)
                .variant_id(variantId)
                .workout_data(workoutData)
                .build()
        val data = message.encode()
        val buffer = ByteBuffer.allocate(data.size)
        buffer.put(data)
        val envelope = Envelope.Builder()
                .type(PreWorkoutRequest::class.java.canonicalName)
                .message(ByteString.of(buffer))
                .build()
        socketManager.send(message)
    }

}
