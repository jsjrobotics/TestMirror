package com.jsjrobotics.testmirror.service.websocket

import android.net.nsd.NsdServiceInfo
import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.IWebSocket
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.jsjrobotics.testmirror.service.RemoteMirrorState
import com.jsjrobotics.testmirror.service.websocket.tasks.ScreenRequestTask
import com.jsjrobotics.testmirror.service.websocket.tasks.SendIdentifyRequestTask
import java.util.concurrent.Executors

class WebSocketBinder(private val service: WebSocketService,
                      private val profileModel: ProfileModel,
                      private val loginModel: LoginModel) : IWebSocket.Stub() {

    override fun getConnectedMirrors(): MutableMap<NsdServiceInfo, RemoteMirrorState> {
        val map: MutableMap<NsdServiceInfo, RemoteMirrorState> = mutableMapOf()
        service.getConnectedSocketManagers().map { Pair(it.serviceInfo, it.mirrorState.copy()) }
                .filter { it.first != null }
                .forEach { map[it.first!!] = it.second }
        return map
    }

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

    override fun connectToClient(resolvedMirrorData: ResolvedMirrorData) {
        executor.execute {
                ConnectToClientTask(service.buildWebSocketManager(resolvedMirrorData), resolvedMirrorData.serviceInfo)
                        .run()
        }
    }

    private fun lookupClientAndRun(nsdServiceInfo: NsdServiceInfo, toRun : (WebSocketManager) -> Unit) {
        service.lookupClient(nsdServiceInfo)?.apply(toRun) ?: reportError(nsdServiceInfo)
    }

    override fun sendScreenRequest(screenName: String) {
        executor.execute {
            service.getConnectedSocketManagers()
                    .forEach {
                        ScreenRequestTask(it, screenName)
                                .run()
                    }
        }
    }
}