package com.jsjrobotics.testmirror.dataStructures.networking.responses

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.service.http.HttpConstants

class UserDataResponseData (@SerializedName(HttpConstants.UUID) val uuid: String,
                            @SerializedName(HttpConstants.NAME) val name: String,
                            @SerializedName(HttpConstants.PUBLIC_NAME) val publicName: String,
                            @SerializedName(HttpConstants.EMAIL) val email: String,
                            @SerializedName(HttpConstants.IS_CHILD) val isChild: Boolean,
                            @SerializedName(HttpConstants.IS_PARENT) val isParent: Boolean,
                            @SerializedName(HttpConstants.IS_PENDING) val isPending: Boolean)

