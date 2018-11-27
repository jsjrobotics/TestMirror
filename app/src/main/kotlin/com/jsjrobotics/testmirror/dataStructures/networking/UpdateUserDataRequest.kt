package com.jsjrobotics.testmirror.dataStructures.networking

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.networking.HttpConstants

class UpdateUserDataRequest(@SerializedName(HttpConstants.NAME) val name: String,
                            @SerializedName(HttpConstants.BIRTHDATE) val birthDate: String,
                            @SerializedName(HttpConstants.LOCATION) val location: String)
