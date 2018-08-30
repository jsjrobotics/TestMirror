package com.jsjrobotics.testmirror

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FragmentIdTest {

    @Test
    fun verifyUniqueFragmentIds() {
        val ids = FragmentId.values()
        ids.forEach { testSubject ->
            ids.filter { it != testSubject }
                    .forEach { anotherId ->
                        assertFalse(testSubject.tag() === anotherId.tag(),
                                    "Each fragment id must have unique tag. ${testSubject.name} x ${anotherId.name} both have ${testSubject.tag()}")
                    }
        }
    }
}