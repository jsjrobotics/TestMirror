package com.jsjrobotics.testmirror.dataStructures.networking.responses

import com.google.gson.annotations.SerializedName
import com.jsjrobotics.testmirror.dataStructures.BroadcastData
import com.jsjrobotics.testmirror.service.http.HttpConstants
import java.io.Serializable
import java.util.*

class ListingResponseData(@SerializedName(HttpConstants.BROADCAST) val broadcast: BroadcastData,
                          @SerializedName(HttpConstants.IS_BOOKMARKED) val isBookmarked: Boolean,
                          @SerializedName(HttpConstants.SORT_DATE) val sortDate: Date,
                          @SerializedName(HttpConstants.TYPE) val type: String,
                          @SerializedName(HttpConstants.UUID) val uuid: String) : Serializable
