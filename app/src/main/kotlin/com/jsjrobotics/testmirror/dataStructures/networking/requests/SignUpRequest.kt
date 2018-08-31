package com.jsjrobotics.testmirror.dataStructures.networking.requests

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.networking.HttpConstants

class SignUpRequest(@SerializedName(HttpConstants.EMAIL) val email: String,
                    @SerializedName(HttpConstants.PASSWORD) val password: String,
                    @SerializedName(HttpConstants.PASSWORD2) val password2: String,
                    @SerializedName(HttpConstants.NAME) val name: String)