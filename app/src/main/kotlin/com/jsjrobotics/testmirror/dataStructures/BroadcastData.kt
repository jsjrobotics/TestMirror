package com.jsjrobotics.testmirror.dataStructures

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class BroadcastData(@SerializedName("ends_at") val endsAt: Date,
                    @SerializedName("featured") val isFeatured : Boolean,
                    @SerializedName("is_bookmarked") val isBookmarked: Boolean,
                    @SerializedName("is_encore") val isEncore: Boolean,
                    @SerializedName("name") val name: String,
                    @SerializedName("published") val published: Boolean,
                    @SerializedName("starts_at") val startsAt: Date,
                    @SerializedName("status") val status: Int,
                    @SerializedName("uuid") val uuid: String) : Serializable
