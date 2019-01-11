package com.jsjrobotics.testmirror.service.websocket

import com.jsjrobotics.testmirror.service.websocket.tasks.MirrorWebSocketTask
import com.mirror.proto.oobe.PairRequest

class PairRequestTask constructor(private val socketManager: WebSocketManager,
                                  private val code: String) : MirrorWebSocketTask() {
    override fun run() {
        val pairRequest = PairRequest.Builder()
                .code(code)
                .build()
        socketManager.send(pairRequest)
    }
}
