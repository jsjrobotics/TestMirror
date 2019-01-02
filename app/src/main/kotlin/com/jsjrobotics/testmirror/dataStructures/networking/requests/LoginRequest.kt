package com.jsjrobotics.testmirror.dataStructures.networking.requests

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.dataStructures.LoginData
import com.jsjrobotics.testmirror.service.http.HttpConstants

class LoginRequest (@SerializedName(HttpConstants.EMAIL) val email: String,
                    @SerializedName(HttpConstants.PASSWORD) val password: String ) {
    constructor(data: LoginData) : this(data.userEmail, data.password)
}