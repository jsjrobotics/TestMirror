package com.jsjrobotics.testmirror.service.websocket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IWebSocket
import com.mirror.proto.navigation.MirrorScreen
import com.mirror.proto.navigation.MirrorScreenRequest
import com.mirror.proto.oobe.PairRequest
import java.net.URI
import java.util.concurrent.Executors
import javax.inject.Inject

class WebSocketService : Service() {
    private val executor =  Executors.newSingleThreadExecutor()

    @Inject
    protected lateinit var socketManager : WebSocketManager

    override fun onCreate() {
        super.onCreate()
        Application.inject(this)
    }

    private val binder = object : IWebSocket.Stub() {
        override fun sendPairingCode(code: String) {
            executor.execute {
                val pairRequest = PairRequest.Builder()
                        .code(code)
                        .build()
                socketManager.send(pairRequest)
            }
        }

        override fun connectToClient(ipAddress: String) {
            executor.execute {
                val uri = URI(buildWebSocketAddress(ipAddress))
                socketManager.connectToClient(uri)
            }
        }

        override fun sendScreenRequest(screenName: String) {
            executor.execute {
                val screen = MirrorScreen.values().firstOrNull { it.name == screenName }
                if (screen == null) {
                    ERROR("Send Screen request with null screen name")
                    return@execute
                }
                val screenRequest = MirrorScreenRequest.Builder()
                        .screen(screen)
                        .build()
                socketManager.send(screenRequest)
            }
        }
    }

    private fun buildWebSocketAddress(ipAddress: String): String {
        return "ws://$ipAddress:7000/socket"
    }
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}


