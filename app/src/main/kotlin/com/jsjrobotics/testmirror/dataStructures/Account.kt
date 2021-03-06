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

    fun needsUpdateInfo(): Boolean {
        return birthday == UNKNOWN_BIRTHDAY || location == UNKNOWN_LOCATION
    }


    companion object CREATOR : Parcelable.Creator<Account> {
        val ATTRIBUTES_IN_ACCOUNT: Int = 5
        val UNKNOWN_BIRTHDAY = -1L
        val UNKNOWN_LOCATION: String = "unknown"
        val REQUIRED_ENCODING_LENGTH = 1
        val EMAIL_INDEX = 0
        val PASSWORD_INDEX = 1
        val FULL_NAME_INDEX = 2
        val BIRTHDAY_INDEX = 3
        val LOCATION_INDEX = 4

        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }

        fun fromSharedPreferences(data: Set<String>): Account {
            val listData = data.toList()
            val userEmail = decode(EMAIL_INDEX, listData)
            val userPassword = decode(PASSWORD_INDEX, listData)
            val fullName = decode(FULL_NAME_INDEX, listData)
            val birthday = decode(BIRTHDAY_INDEX, listData)
            val location = decode(LOCATION_INDEX, listData)
            return Account(userEmail,
                           userPassword,
                           fullName,
                           birthday.toLong(),
                           location)
        }

        fun toSharedPreferences(account: Account): Set<String> {
            val userEmail = encode(EMAIL_INDEX, account.userEmail)
            val userPassword = encode(PASSWORD_INDEX, account.userPassword)
            val fullName = encode(FULL_NAME_INDEX, account.fullName)
            val birthday = encode(BIRTHDAY_INDEX, account.birthday.toString())
            val location = encode(LOCATION_INDEX, account.location)
            return setOf(
                    userEmail,
                    userPassword,
                    fullName,
                    birthday,
                    location
            )
        }

        // All indexes must have same string length for encode to work
        private fun encode(index: Int, data: String): String {
            if (index.toString().length != REQUIRED_ENCODING_LENGTH) {
                throw IllegalStateException("All encodings must have equal length for decoding to work")
            }
            return "$index$data"
        }

        private fun decode(index: Int, data: List<String>) : String {
            data.forEach {
                if (it.startsWith(index.toString())) {
                    return it.substring(REQUIRED_ENCODING_LENGTH)
                }
            }
            if (index == BIRTHDAY_INDEX) {
                return UNKNOWN_BIRTHDAY.toString()
            } else if (index == LOCATION_INDEX) {
                return UNKNOWN_LOCATION
            } else {
                return ""
            }
        }
    }
}