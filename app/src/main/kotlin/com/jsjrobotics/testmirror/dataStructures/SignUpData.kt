package com.jsjrobotics.testmirror.dataStructures

import android.os.Parcel
import android.os.Parcelable

data class SignUpData(val email: String,
                      val password: String,
                      val confirmPassword: String,
                      val fullName: String) : Parcelable  {

    fun isValid(): Boolean {
        val fieldsComplete = !(email.isBlank() || password.isBlank() || fullName.isBlank())
        val passwordsMatch = password == confirmPassword
        return fieldsComplete && passwordsMatch
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(confirmPassword)
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
