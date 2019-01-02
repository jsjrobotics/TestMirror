package com.jsjrobotics.testmirror.dataStructures.networking.requests

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.http.HttpConstants

class UpdateInfoRequest (@SerializedName(HttpConstants.NAME) val email: String,
                         @SerializedName(HttpConstants.BIRTHDATE) val password: String,
                         @SerializedName(HttpConstants.LOCATION) val location: String)