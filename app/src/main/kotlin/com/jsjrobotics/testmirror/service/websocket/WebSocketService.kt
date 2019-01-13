package com.jsjrobotics.testmirror.service.websocket

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.nsd.NsdServiceInfo
import android.os.IBinder
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.BuildConfig
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.network.ProtobufEnvelopeHandler
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.squareup.wire.Message
import javax.inject.Inject

class WebSocketService : Service() {
    @Inject
    protected lateinit var protobufEnvelopeHandler : ProtobufEnvelopeHandler

    @Inject
    protected lateinit var clientStateDispatcher : ClientStateDispatcher

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var profileModel: ProfileModel

    @Inject
    protected lateinit var loginModel: LoginModel

    private lateinit var  binder : WebSocketBinder
    private val socketManagers : MutableList<WebSocketManager> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        Application.inject(this)
        binder = WebSocketBinder( ::buildWebSocketManager,
                ::lookupClient,
                ::getConnectedSocketManagers,
                profileModel,
                loginModel)
        if (BuildConfig.DEBUG) {
            application.registerReceiver(buildDebugReceiver(), buildDebugIntentFilter())
        }
    }

    private fun getConnectedSocketManagers(): List<WebSocketManager> = socketManagers.filter { it.isConnected() }

    private fun buildWebSocketManager(): WebSocketManager {
        val manager = WebSocketManager(protobufEnvelopeHandler, clientStateDispatcher)
        socketManagers.add(manager)
        return manager
    }

    private fun lookupClient(nsdServiceInfo: NsdServiceInfo): WebSocketManager? {
        return socketManagers.firstOrNull { it.serviceInfo == nsdServiceInfo }
    }

    private fun buildDebugIntentFilter(): IntentFilter {
        return IntentFilter(DEBUG_INTENT)
    }

    private fun buildDebugReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.hasExtra(EXTRA_PROTO_MESSAGE)) {
                    val protoMessage = intent.getSerializableExtra(EXTRA_PROTO_MESSAGE) as Message<*, *>
                    socketManagers.forEach{ it.send(protoMessage) }
                }
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    companion object {
        const val DEBUG_INTENT = "com.jsjrobotics.testmirror.service.websocket.WebSocketService.DEBUG"
        const val EXTRA_PROTO_MESSAGE = "com.jsjrobotics.testmirror.service.websocket.WebSocketService.Proto_Message"
    }

}


