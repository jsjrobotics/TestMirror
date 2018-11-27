package com.jsjrobotics.testmirror.dataStructures

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class UpdateInfoData(val year: Int = UNKNOWN_VALUE,
                          val month: Int = UNKNOWN_VALUE,
                          val dayOfMonth: Int = UNKNOWN_VALUE,
                          val location: String = UNKNOWN_LOCATION) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(dayOfMonth)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun isValid(): Boolean {
        return year != UNKNOWN_VALUE &&
                month != UNKNOWN_VALUE &&
                dayOfMonth != UNKNOWN_VALUE &&
                location.isNotBlank() && location != UNKNOWN_LOCATION
    }

    fun getTimeMilliseconds(): Long {
        val calendar = GregorianCalendar(year, month, dayOfMonth)
        return calendar.getTimeInMillis()
    }

    fun birthdayString(): String {
        val monthString : String = if (month < 10) {
            "0$month"
        } else {
            "$month"
        }

        val dayString = if (dayOfMonth < 10) {
            "0$dayOfMonth"
        } else {
            "$dayOfMonth"
        }

        return "$year-$monthString-$dayString"
    }

    companion object CREATOR : Parcelable.Creator<UpdateInfoData> {
        val UNKNOWN_VALUE : Int = -1
        val UNKNOWN_LOCATION = ""
        override fun createFromParcel(parcel: Parcel): UpdateInfoData {
            return UpdateInfoData(parcel)
        }

        override fun newArray(size: Int): Array<UpdateInfoData?> {
            return arrayOfNulls(size)
        }

    }

}
