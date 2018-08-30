package com.jsjrobotics.testmirror.dataStructures

import android.os.Parcel
import android.os.Parcelable

data class SignUpData(val email: String,
                      val password: String,
                      val fullName: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    fun isValid(): Boolean {
        return !(email.isBlank() || password.isBlank() || fullName.isBlank())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(fullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SignUpData> {
        override fun createFromParcel(parcel: Parcel): SignUpData {
            return SignUpData(parcel)
        }

        override fun newArray(size: Int): Array<SignUpData?> {
            return arrayOfNulls(size)
        }
    }

}
