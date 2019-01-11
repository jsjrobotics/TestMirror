package com.jsjrobotics.testmirror.service.websocket.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.service.websocket.WebSocketManager
import com.mirror.proto.navigation.MirrorScreen
import com.mirror.proto.navigation.MirrorScreenRequest

class ScreenRequestTask constructor(private val socketManager: WebSocketManager,
                                    private val screenName: String) : MirrorWebSocketTask() {

    override fun run() {
        val screen = MirrorScreen.values().firstOrNull { it.name == screenName }
        if (screen == null) {
            ERROR("Send Screen request with null screen name")
            return
        }
        val screenRequest = MirrorScreenRequest.Builder()
                .screen(screen)
                .build()
        socketManager.send(screenRequest)
    }

}
