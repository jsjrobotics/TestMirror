package com.jsjrobotics.testmirror.service.websocket

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IWebSocket
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.jsjrobotics.testmirror.service.websocket.tasks.ScreenRequestTask
import com.jsjrobotics.testmirror.service.websocket.tasks.SendIdentifyRequestTask
import java.util.concurrent.Executors

class WebSocketBinder(private val newWebsocketManager: () -> WebSocketManager,
                      private val clientLookUp: (NsdServiceInfo) -> WebSocketManager?,
                      private val allClients : () -> List<WebSocketManager>,
                      private val profileModel: ProfileModel,
                      private val loginModel: LoginModel) : IWebSocket.Stub() {
    private val executor =  Executors.newSingleThreadExecutor()

    override fun sendIdentifyRequest(nsdServiceInfo: NsdServiceInfo) {
        executor.execute {
            lookupClientAndRun(nsdServiceInfo) {
                SendIdentifyRequestTask(it,
                        profileModel,
                        loginModel).run()
            }
        }
    }

    private fun reportError(nsdServiceInfo: NsdServiceInfo) {
        ERROR("No client found for $nsdServiceInfo")
    }

    override fun sendPairingCode(nsdServiceInfo: NsdServiceInfo, code: String) {
        executor.execute {
            lookupClientAndRun(nsdServiceInfo) {
                PairRequestTask(it, code)
                        .run()
            }
        }
    }

    override fun connectToClient(nsdServiceInfo: NsdServiceInfo) {
        executor.execute {
                ConnectToClientTask(newWebsocketManager(), nsdServiceInfo)
                        .run()
        }
    }

    private fun lookupClientAndRun(nsdServiceInfo: NsdServiceInfo, toRun : (WebSocketManager) -> Unit) {
        clientLookUp(nsdServiceInfo)?.apply(toRun) ?: reportError(nsdServiceInfo)
    }

    override fun sendScreenRequest(screenName: String) {
        executor.execute {
            allClients.invoke().forEach {
                ScreenRequestTask(it, screenName)
                        .run()
            }
        }
    }
}