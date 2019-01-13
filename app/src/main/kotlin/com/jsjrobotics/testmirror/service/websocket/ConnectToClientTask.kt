package com.jsjrobotics.testmirror.service.websocket

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.service.websocket.tasks.MirrorWebSocketTask

class ConnectToClientTask(private val socketManager: WebSocketManager,
                          private val nsdServiceInfo: NsdServiceInfo) : MirrorWebSocketTask() {

    override fun run() {
        socketManager.connectToClient(nsdServiceInfo)
    }

}
