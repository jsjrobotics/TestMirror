package com.jsjrobotics.testmirror.service.http

import android.net.nsd.NsdServiceInfo

object Paths {
    const val DOMAIN: String = "https://dev.refinemirror.com/api/v1/"
    const val SIGNUP_PATH = "auth/signup"
    const val LOGIN_PATH = "auth/login"
    const val USER_ME_PATH = "user/me"
    const val AUTHORIZATION_HEADER = "Authorization"
    fun buildAuthorizationHeader(apiToken: String) : String {
        return "Bearer $apiToken"
    }

    fun buildWebSocketAddress(ipAddress: String): String {
        return "ws://$ipAddress:7000/socket"
    }

    fun getMirrorStatusAddress(serviceInfo: NsdServiceInfo): String {
        return  "http://${serviceInfo.host.hostAddress}:8080/"
    }
}