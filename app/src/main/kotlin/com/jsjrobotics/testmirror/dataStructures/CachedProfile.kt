package com.jsjrobotics.testmirror.dataStructures

data class CachedProfile(val account: Account,
                         val cacheTime: Long) {
    fun cacheKey() : String = account.userEmail
}
