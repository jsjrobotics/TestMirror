package com.jsjrobotics.testmirror

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationControllerTest {
    lateinit var testSubject: NavigationController

    @Before
    fun setup() {
        testSubject = NavigationController()
    }

    @Test
    fun testLogin() {
        var testPassed = false
        testSubject.onShowLoginFragment
                .subscribe { testPassed = true }
        testSubject.showSignUp()
        assertFalse(testPassed, "Show Login should not respond to show Sign up")
        testSubject.showLogin()
        assertTrue(testPassed, "Did not receive event on login click")
    }

    @Test
    fun testSignUp() {
        var testPassed = false
        testSubject.onShowSignUpFragment
                .subscribe { testPassed = true }
        testSubject.showLogin()
        assertFalse(testPassed, "Show Sign UP should not respond to show Login")
        testSubject.showSignUp()
        assertTrue(testPassed, "Did not receive event on signup click")
    }
}