package com.jsjrobotics.testmirror.service.websocket

import com.jsjrobotics.testmirror.IWebSocket
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.jsjrobotics.testmirror.service.websocket.tasks.ScreenRequestTask
import com.jsjrobotics.testmirror.service.websocket.tasks.SendIdentifyRequestTask
import java.util.concurrent.Executors

class WebSocketBinder(private val socketManager: WebSocketManager,
                      private val profileModel: ProfileModel,
                      private val loginModel: LoginModel) : IWebSocket.Stub() {
    private val executor =  Executors.newSingleThreadExecutor()

    override fun sendIdentifyRequest() {
        executor.execute {
            SendIdentifyRequestTask(socketManager,
                    profileModel,
                    loginModel).run()
        }
    }

    override fun sendPairingCode(code: String) {
        executor.execute {
            PairRequestTask(socketManager, code)
                    .run()
        }
    }

    override fun connectToClient(ipAddress: String) {
        executor.execute {
            ConnectToClientTask(socketManager, ipAddress)
                    .run()
        }
    }

    override fun sendScreenRequest(screenName: String) {
        executor.execute {
            ScreenRequestTask(socketManager, screenName)
                    .run()
        }
    }
}