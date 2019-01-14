package com.jsjrobotics.testmirror.service

import android.os.Parcel
import android.os.Parcelable
import com.jsjrobotics.testmirror.dataStructures.Constants.UNKNOWN_MIRROR_NAME
import com.jsjrobotics.testmirror.dataStructures.ResolvedMirrorData
import com.mirror.proto.user.IdentifyResponse
import com.squareup.wire.Message

/**
 * Remote Mirror state
 * Null values indicate no received data
 * Note that the copy method of kotlin is always a shallow copy. Hence, must pass resolvedData.copy()
 * whenever copy is called
 */
data class RemoteMirrorState(val pairingRequired: Boolean? = null,
                             val onWifi: Boolean? = null ,
                             val resolvedData: ResolvedMirrorData? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readParcelable(ResolvedMirrorData::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(pairingRequired)
        parcel.writeValue(onWifi)
        parcel.writeParcelable(resolvedData, flags)
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
        return RemoteMirrorState().copy(
                pairingRequired = identifyResponse.pairingRequired,
                onWifi = identifyResponse.onWifi,
                resolvedData = resolvedData?.copy()
        )
    }


    fun updateState(resolvedData: ResolvedMirrorData): RemoteMirrorState {
        return RemoteMirrorState().copy(resolvedData = resolvedData)
    }

    fun getMirrorName(): String {
        return resolvedData?.getMirrorName() ?: UNKNOWN_MIRROR_NAME
    }
}
