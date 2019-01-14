package com.jsjrobotics.testmirror.dataStructures.networking

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MirrorDataResponse (@SerializedName("name") val name: String,
                               @SerializedName("serial") val serial : String,
                               @SerializedName("is_in_oobe") val isInOobe: Boolean,
                               @SerializedName("is_in_use") val isInUse: Boolean,
                               @SerializedName("ethernet") val isEthernet: Boolean,
                               @SerializedName("wifi") val isWifi: Boolean,
                               @SerializedName("ssid") val ssid : String,
                               @SerializedName("band") val band : Float,
                               @SerializedName("proto_version") val protoVersion : String,
                               @SerializedName("client_id") val clientId : String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(serial)
        parcel.writeByte(if (isInOobe) 1 else 0)
        parcel.writeByte(if (isInUse) 1 else 0)
        parcel.writeByte(if (isEthernet) 1 else 0)
        parcel.writeByte(if (isWifi) 1 else 0)
        parcel.writeString(ssid)
        parcel.writeFloat(band)
        parcel.writeString(protoVersion)
        parcel.writeString(clientId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MirrorDataResponse> {
        override fun createFromParcel(parcel: Parcel): MirrorDataResponse {
            return MirrorDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<MirrorDataResponse?> {
            return arrayOfNulls(size)
        }
    }
}