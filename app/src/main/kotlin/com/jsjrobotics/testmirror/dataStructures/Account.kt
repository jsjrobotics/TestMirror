package com.jsjrobotics.testmirror.dataStructures

import android.os.Parcel
import android.os.Parcelable

data class Account(val userEmail: String,
                   val userPassword: String,
                   val fullName: String,
                   val birthday: Long,
                   val location: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userEmail)
        parcel.writeString(userPassword)
        parcel.writeString(fullName)
        parcel.writeLong(birthday)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        val ATTRIBUTES_IN_ACCOUNT: Int = 5

        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}