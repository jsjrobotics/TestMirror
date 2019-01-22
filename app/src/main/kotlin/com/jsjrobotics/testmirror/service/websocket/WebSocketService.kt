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
import com.jsjrobotics.testmirror.MirrorStateDispatcher
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.network.ProtoBufMessageDispatcher
import com.jsjrobotics.testmirror.network.ProtobufEnvelopeHandler
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.squareup.wire.Message
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WebSocketService : Service() {
    @Inject
    protected lateinit var protobufEnvelopeHandler : ProtobufEnvelopeHandler

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var mirrorStateDispatcher: MirrorStateDispatcher

    @Inject
    protected lateinit var messageDispatcher: ProtoBufMessageDispatcher

    @Inject
    protected lateinit var profileModel: ProfileModel

    @Inject
    protected lateinit var loginModel: LoginModel

    private lateinit var  binder : WebSocketBinder
    private val socketManagers : MutableList<WebSocketManager> = mutableListOf()

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        Application.inject(this)
        binder = WebSocketBinder( this,
                profileModel,
                loginModel)
        val identifyResponseDisposable = messageDispatcher.onIdentifyResponse.subscribe { identifyResponse ->
            socketManagers.firstOrNull { it.identityRequestSent }?.let {
                it.receivedMessage(identifyResponse)
                it.identityRequestSent = false
            }
        }
        val onMirrorCloseDisposable = mirrorStateDispatcher.onOpenCloseEvent.subscribe {
            if (!it.isConnected) {
                socketManagers.remove(it.socketManager)
            }
        }
        disposables.addAll(identifyResponseDisposable, onMirrorCloseDisposable)
        if (BuildConfig.DEBUG) {
            application.registerReceiver(buildDebugReceiver(), buildDebugIntentFilter())
        }
    }

    internal fun getConnectedSocketManagers(): List<WebSocketManager> = socketManagers.filter { it.isConnected() }

    internal fun buildWebSocketManager(resolvedMirrorData: ResolvedMirrorData): WebSocketManager {
        val manager = WebSocketManager(protobufEnvelopeHandler, mirrorStateDispatcher)
        manager.setResolvedData(resolvedMirrorData)
        socketManagers.add(manager)
        return manager
    }

    internal fun lookupClient(nsdServiceInfo: NsdServiceInfo): WebSocketManager? {
        return socketManagers.firstOrNull { it.serviceInfo == nsdServiceInfo }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
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


