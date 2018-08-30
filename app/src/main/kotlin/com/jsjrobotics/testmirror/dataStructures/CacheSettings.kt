package com.jsjrobotics.testmirror.dataStructures

data class CacheSettings(val softTimeToLive: Long,
                         val hardTimeToLive: Long) {

    init {
        if (hardTimeToLive <= softTimeToLive) {
            throw IllegalArgumentException("Must satisfy hardTTL > softTTL")
        }
    }

    fun softTtlExpired(timeSource: () -> Long, comparisonTime: Long) : Boolean {
        return timeSource.invoke() - comparisonTime >= softTimeToLive
    }

    fun hardTtlExpired(timeSource: () -> Long, comparisonTime: Long) : Boolean {
        return timeSource.invoke() - comparisonTime >= hardTimeToLive
    }
}