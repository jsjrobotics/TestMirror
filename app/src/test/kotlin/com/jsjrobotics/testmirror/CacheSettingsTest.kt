package com.jsjrobotics.testmirror

import com.jsjrobotics.testmirror.dataStructures.CacheSettings
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CacheSettingsTest {

    lateinit var testSubject: CacheSettings

    @Before
    fun setup() {
        testSubject = CacheSettings(5, 10)
    }

    @Test
    fun testSoftTTL() {
        val beforeTTL: (() -> Long) = { 2 }
        val afterTTL: (() -> Long) = { 7 }
        val cachedTime = 0L
        assertFalse(testSubject.softTtlExpired(beforeTTL, cachedTime), "SoftTTL not yet expired")
        assertTrue(testSubject.softTtlExpired(afterTTL, cachedTime), "SoftTTL should be expired")
    }

    @Test
    fun testHardTTL() {
        val beforeTTL: (() -> Long) = { 7 }
        val afterTTL: (() -> Long) = { 11 }
        val cachedTime = 0L
        assertFalse(testSubject.hardTtlExpired(beforeTTL, cachedTime), "HardTTL not yet expired")
        assertTrue(testSubject.hardTtlExpired(afterTTL, cachedTime), "HardTTL should be expired")
    }
}