package com.jsjrobotics.testmirror.service.networking

object Paths {
    const val DOMAIN: String = "https://dev.refinemirror.com/api/v1/"
    const val SIGNUP_PATH = "auth/signup"
    const val LOGIN_PATH = "auth/login"
    const val USER_DATA_PATH = "user/me"
    const val AUTHORIZATION_HEADER = "Authorization"
    fun buildAuthorizationHeader(apiToken: String) : String {
        return "Bearer $apiToken"
    }

}