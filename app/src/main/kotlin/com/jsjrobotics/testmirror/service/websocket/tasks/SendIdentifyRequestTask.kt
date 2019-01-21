package com.jsjrobotics.testmirror.service.websocket.tasks

import com.jsjrobotics.testmirror.ERROR
import com.jsjrobotics.testmirror.login.LoginModel
import com.jsjrobotics.testmirror.profile.ProfileModel
import com.jsjrobotics.testmirror.service.websocket.WebSocketManager
import com.mirror.proto.user.Environment
import com.mirror.proto.user.IdentifyRequest

class SendIdentifyRequestTask(private val socketManager: WebSocketManager,
                              private val profileModel: ProfileModel,
                              private val loginModel: LoginModel) : MirrorWebSocketTask() {
    override fun run() {
        val invalidAccount = profileModel.currentAccount == null
        val invalidToken = loginModel.requestToken == null
        if (invalidAccount || invalidToken) {
            ERROR("Not sending IdentifyRequest due to invalid state")
            return
        }
        val request = IdentifyRequest.Builder()
                .name(profileModel.currentAccount?.fullName)
                .email(profileModel.currentAccount?.userEmail)
                .token(loginModel.requestToken)
                .environment(Environment.STAGING)
                .id(profileModel.currentAccount?.uuid)
                .build()
        socketManager.send(request)
        socketManager.identityRequestSent = true
    }
}
