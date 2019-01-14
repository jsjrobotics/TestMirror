package com.jsjrobotics.testmirror

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationController @Inject constructor() : BottomNavigationView.OnNavigationItemSelectedListener{
    var currentFragment: FragmentId = FragmentId.WELCOME

    private val showRequest: PublishSubject<FragmentRequest> = PublishSubject.create()
    val onShowRequest: Observable<FragmentRequest> = showRequest

    private val showNavBarRequest: PublishSubject<Boolean> = PublishSubject.create()
    val onShowNavBarRequest: Observable<Boolean> = showNavBarRequest

    fun showLogin() {
        showRequest(FragmentRequest(FragmentId.LOGIN, true))
    }

    private fun showRequest(fragmentRequest: FragmentRequest) {
        currentFragment = fragmentRequest.fragmentId
        showRequest.onNext(fragmentRequest)
    }

    fun showSignUp() {
        showRequest(FragmentRequest(FragmentId.SIGNUP,true))
    }

    fun showUpdateInfo() {
        showRequest(FragmentRequest(FragmentId.UPDATE_INFO, false))
    }

    fun showProfile(clearBackstack : Boolean) {
        showRequest(FragmentRequest(FragmentId.HOME, false, clearBackStack = clearBackstack))
    }

    fun showConnectToMirror(addToBackstack: Boolean, clearBackstack: Boolean) {
        showRequest(FragmentRequest(FragmentId.CONNECT_TO_MIRROR, addToBackstack = addToBackstack, clearBackStack = clearBackstack))
    }

    fun showLive() {
        showRequest(FragmentRequest(FragmentId.LIVE, false))
    }

    fun showOnDemand() {
        showRequest(FragmentRequest(FragmentId.ON_DEMAND, false))
    }

    fun showProgress() {
        showRequest(FragmentRequest(FragmentId.PROGRESS, false))
    }

    fun showSettings() {
        showRequest(FragmentRequest(FragmentId.SETTINGS, false))
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

    fun setNavigationBarVisibility(isVisible: Boolean) {
        showNavBarRequest.onNext(isVisible)
    }
}
