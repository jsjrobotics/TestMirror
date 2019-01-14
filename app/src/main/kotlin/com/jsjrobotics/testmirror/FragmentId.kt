package com.jsjrobotics.testmirror

import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.browseLive.BrowseLiveFragment
import com.jsjrobotics.testmirror.browseOnDemand.BrowseOnDemandFragment
import com.jsjrobotics.testmirror.connectToMirror.ConnectToMirrorFragment
import com.jsjrobotics.testmirror.login.LoginFragment
import com.jsjrobotics.testmirror.profile.ProfileFragment
import com.jsjrobotics.testmirror.progress.ProgressFragment
import com.jsjrobotics.testmirror.settings.SettingsFragment
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment

enum class FragmentId {
    LOGIN,
    SIGNUP,
    UPDATE_INFO,
    WELCOME,
    CONNECT_TO_MIRROR,
    HOME,
    LIVE,
    ON_DEMAND,
    PROGRESS,
    SETTINGS;

    companion object {
        fun isNavBarVisibleFromTag(tag: String?): Boolean? {
            return FragmentId.values().firstOrNull{ it.tag() == tag}?.isNavBarVisible()
        }

    }
    fun instantiate(): Fragment {
        return when(this) {
            LOGIN -> LoginFragment()
            SIGNUP -> SignUpFragment()
            WELCOME -> WelcomeFragment()
            UPDATE_INFO -> UpdateInfoFragment()
            HOME -> ProfileFragment()
            CONNECT_TO_MIRROR -> ConnectToMirrorFragment()
            LIVE -> BrowseLiveFragment()
            ON_DEMAND -> BrowseOnDemandFragment()
            PROGRESS -> ProgressFragment()
            SETTINGS -> SettingsFragment()
        }
    }

    fun tag(): String {
        return when(this) {
            LOGIN -> LoginFragment.TAG
            SIGNUP -> SignUpFragment.TAG
            WELCOME -> WelcomeFragment.TAG
            UPDATE_INFO -> UpdateInfoFragment.TAG
            HOME -> ProfileFragment.TAG
            CONNECT_TO_MIRROR -> ConnectToMirrorFragment.TAG
            LIVE -> BrowseLiveFragment.TAG
            ON_DEMAND -> BrowseOnDemandFragment.TAG
            PROGRESS -> ProgressFragment.TAG
            SETTINGS -> SettingsFragment.TAG
        }
    }

    fun isNavBarVisible(): Boolean {
        val fragmentsWithNavBar = listOf(
                HOME,
                LIVE,
                ON_DEMAND,
                PROGRESS,
                SETTINGS
        )
        return fragmentsWithNavBar.contains(this)
    }
}
