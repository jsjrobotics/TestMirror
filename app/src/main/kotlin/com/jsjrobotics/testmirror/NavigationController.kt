package com.jsjrobotics.testmirror

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
import com.jsjrobotics.testmirror.dataStructures.RemoveFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor() : BottomNavigationView.OnNavigationItemSelectedListener{
    var currentFragment: FragmentId = FragmentId.WELCOME

    private val showRequest: PublishSubject<FragmentRequest> = PublishSubject.create()
    val onShowRequest: Observable<FragmentRequest> = showRequest

    fun showLogin() {
        showRequest(AddFragment(FragmentId.LOGIN, true))
    }

    fun showRequest(fragmentRequest: FragmentRequest) {
        currentFragment = fragmentRequest.fragmentId
        showRequest.onNext(fragmentRequest)
    }

    fun removeRequest(fragmentRequest: FragmentRequest) {
        showRequest.onNext(fragmentRequest)
    }

    fun showSignUp() {
        showRequest(AddFragment(FragmentId.SIGNUP,true))
    }

    fun showProfile(clearBackstack : Boolean) {
        showRequest(AddFragment(FragmentId.HOME, false, clearBackStack = clearBackstack))
    }

    fun showConnectToMirror() {
        showRequest(AddFragment(FragmentId.CONNECT_TO_MIRROR, true))
    }

    fun showLive() {
        showRequest(AddFragment(FragmentId.LIVE, false))
    }

    fun showOnDemand() {
        showRequest(AddFragment(FragmentId.ON_DEMAND, false))
    }

    fun showProgress() {
        showRequest(AddFragment(FragmentId.PROGRESS, false))
    }

    fun showSettings() {
        showRequest(AddFragment(FragmentId.SETTINGS, false))
    }

    fun selectBottonNavigationItemId(itemId: Int): Boolean {
        val navigationOptions = listOf<Pair<Int, Runnable>>(
                Pair(R.id.tab_home, Runnable { showProfile(false) }),
                Pair(R.id.tab_live, Runnable { showLive() }),
                Pair(R.id.tab_on_demand, Runnable { showOnDemand() }),
                Pair(R.id.tab_progress, Runnable { showProgress() }),
                Pair(R.id.tab_settings, Runnable { showSettings() })

        )

        val runnable = navigationOptions.filter { it.first == itemId }
                .map { it.second }
                .firstOrNull()

        runnable?.let {
            it.run()
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectBottonNavigationItemId(item.itemId)
        return true
    }

    fun removeConnectToMirrorFragment() {
        removeRequest(RemoveFragment(FragmentId.CONNECT_TO_MIRROR))
    }
}
