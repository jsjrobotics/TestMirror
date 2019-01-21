package com.jsjrobotics.testmirror

import android.content.SharedPreferences
import com.google.gson.Gson
import com.jsjrobotics.testmirror.dataStructures.Account
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences){
    private val REQUEST_TOKEN: String = "loggedin.request.token"
    private val ACCOUNT: String = "loggedin.account"

    fun setRequestToken(value: String?) {
        sharedPreferences.edit().putString(REQUEST_TOKEN, value).apply()
    }

    fun getRequestToken(): String = sharedPreferences.getString(REQUEST_TOKEN, "")!!

    fun setAccount(account: Account?) {
        sharedPreferences.edit().putString(ACCOUNT, Gson().toJson(account)).apply()
    }

    fun getAccount() : Account? {
        val savedAccount = sharedPreferences.getString(ACCOUNT, null)
        return savedAccount?.let {
            return Gson().fromJson(savedAccount, Account::class.java)
        }
    }
}
