package com.jsjrobotics.testmirror.dataStructures.networking.responses

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.http.HttpConstants

class LoginResponseData(@SerializedName(HttpConstants.API_TOKEN) val apiToken: String,
                        @SerializedName(HttpConstants.USER_TOKEN) val userToken: String)