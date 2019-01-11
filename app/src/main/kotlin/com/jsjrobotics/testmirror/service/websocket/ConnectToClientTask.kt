package com.jsjrobotics.testmirror.service.websocket

import com.jsjrobotics.testmirror.service.http.Paths
import com.jsjrobotics.testmirror.service.websocket.tasks.MirrorWebSocketTask
import java.net.URI

class ConnectToClientTask(private val socketManager: WebSocketManager,
                          private val ipAddress: String) : MirrorWebSocketTask() {

    override fun run() {
        val uri = URI(Paths.buildWebSocketAddress(ipAddress))
        socketManager.connectToClient(uri)
    }

}
