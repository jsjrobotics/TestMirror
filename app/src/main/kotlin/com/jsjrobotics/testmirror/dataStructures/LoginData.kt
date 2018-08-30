package com.jsjrobotics.testmirror.dataStructures

import android.os.Parcel
import android.os.Parcelable

data class LoginData(val userEmail: String,
                     val password: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    fun isValid() : Boolean {
        return !(userEmail.isBlank() || password.isBlank())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userEmail)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginData> {
        override fun createFromParcel(parcel: Parcel): LoginData {
            return LoginData(parcel)
        }

        override fun newArray(size: Int): Array<LoginData?> {
            return arrayOfNulls(size)
        }
    }
}
