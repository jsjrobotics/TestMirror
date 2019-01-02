package com.jsjrobotics.testmirror.dataStructures.networking.responses

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.http.HttpConstants

class UserDataResponse(@SerializedName(HttpConstants.MESSAGE) val message: String,
                       @SerializedName(HttpConstants.DATA) val data: UserDataResponseData)
