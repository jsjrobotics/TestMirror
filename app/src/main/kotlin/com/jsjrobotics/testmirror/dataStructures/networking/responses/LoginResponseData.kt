package com.jsjrobotics.testmirror.dataStructures.networking.responses

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.http.HttpConstants

class LoginResponseData(@SerializedName(HttpConstants.API_TOKEN) val apiToken: String,
                        @SerializedName(HttpConstants.USER_TOKEN) val userToken: String,
                        @SerializedName(HttpConstants.ACCESS_TOKEN) val accessToken: String,
                        @SerializedName(HttpConstants.ACCESS_TOKEN_EXPIRES) val accessTokenExpires: Int,
                        @SerializedName(HttpConstants.REFRESH_TOKEN) val refreshToken : String,
                        @SerializedName(HttpConstants.REFRESH_TOKEN_EXPIRES) val refreshTokenExpires: String)