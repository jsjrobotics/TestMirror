package com.jsjrobotics.testmirror.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.IWebSocket
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
    }

    private fun buildWebSocketAddress(ipAddress: String): String {
        return "ws://$ipAddress:7000/socket"
    }
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}


