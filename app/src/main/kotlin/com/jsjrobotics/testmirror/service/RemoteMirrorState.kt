package com.jsjrobotics.testmirror.service

import android.os.Parcel
import android.os.Parcelable
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message

/**
 * Remote Mirror state
 * Null values indicate no received data
 */
data class RemoteMirrorState(val pairingRequired: Boolean? = null,
                             val onWifi: Boolean? = null ) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(pairingRequired)
        parcel.writeValue(onWifi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemoteMirrorState> {
        override fun createFromParcel(parcel: Parcel): RemoteMirrorState {
            return RemoteMirrorState(parcel)
        }

        override fun newArray(size: Int): Array<RemoteMirrorState?> {
            return arrayOfNulls(size)
        }
    }

    fun updateState(message: Message<*, *>) : RemoteMirrorState {
        return when(message) {
            is IdentifyResponse -> updateIdentifyState(message)
            else -> this
        }
    }

    private fun updateIdentifyState(identifyResponse: IdentifyResponse) : RemoteMirrorState {
        return RemoteMirrorState(
                identifyResponse.pairingRequired,
                identifyResponse.onWifi
        )
    }
}
