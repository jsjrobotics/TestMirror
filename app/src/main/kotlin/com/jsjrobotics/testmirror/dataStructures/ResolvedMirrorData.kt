package com.jsjrobotics.testmirror.dataStructures

import android.net.nsd.NsdServiceInfo
import android.os.Parcel
import android.os.Parcelable
import com.jsjrobotics.testmirror.dataStructures.networking.MirrorDataResponse

data class ResolvedMirrorData(val serviceInfo: NsdServiceInfo, val mirrorData: MirrorDataResponse? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(NsdServiceInfo::class.java.classLoader),
            parcel.readParcelable(MirrorDataResponse::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(serviceInfo, flags)
        parcel.writeParcelable(mirrorData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getMirrorName(): String {
        val mirrorName = mirrorData?.name ?: ""
        if (mirrorName.isNotEmpty()) {
            return mirrorName
        }
        return serviceInfo.serviceName
    }

    companion object CREATOR : Parcelable.Creator<ResolvedMirrorData> {
        override fun createFromParcel(parcel: Parcel): ResolvedMirrorData {
            return ResolvedMirrorData(parcel)
        }

        override fun newArray(size: Int): Array<ResolvedMirrorData?> {
            return arrayOfNulls(size)
        }
    }
}
