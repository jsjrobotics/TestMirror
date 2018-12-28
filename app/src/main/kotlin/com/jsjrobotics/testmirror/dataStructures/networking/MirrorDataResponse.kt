package com.jsjrobotics.testmirror.dataStructures.networking

import com.google.gson.annotations.SerializedName

data class MirrorDataResponse (@SerializedName("name") val name: String,
                               @SerializedName("serial") val serial : String,
                               @SerializedName("is_in_oobe") val isInOobe: Boolean,
                               @SerializedName("is_in_use") val isInUse: Boolean,
                               @SerializedName("ethernet") val isEthernet: Boolean,
                               @SerializedName("wifi") val isWifi: Boolean,
                               @SerializedName("ssid") val ssid : String,
                               @SerializedName("band") val band : Int,
                               @SerializedName("proto_version") val protoVersion : String,
                               @SerializedName("client_id") val clientId : String)