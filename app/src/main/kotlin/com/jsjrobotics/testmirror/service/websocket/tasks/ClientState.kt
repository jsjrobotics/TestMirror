package com.jsjrobotics.testmirror.service.websocket.tasks

import com.jsjrobotics.testmirror.service.websocket.WebSocketManager

data class ClientState(val socketManager: WebSocketManager,
                       val isConnected: Boolean)