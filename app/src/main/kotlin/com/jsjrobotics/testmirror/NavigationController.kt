package com.jsjrobotics.testmirror

import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.jsjrobotics.testmirror.dataStructures.AddFragment
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

    fun showLogin() {
        showRequest(AddFragment(FragmentId.LOGIN, true, containerId = R.id.welcome_fragment))
    }

    fun showRequest(fragmentRequest: FragmentRequest) {
        currentFragment = fragmentRequest.fragmentId
        showRequest.onNext(fragmentRequest)
    }

    fun removeRequest(fragmentRequest: FragmentRequest) {
        showRequest.onNext(fragmentRequest)
    }

    fun showSignUp() {
        showRequest(AddFragment(FragmentId.SIGNUP,true, containerId = R.id.welcome_fragment))
    }

    fun showProfile(clearBackstack : Boolean) {
        showRequest(AddFragment(FragmentId.HOME, false, clearBackStack = clearBackstack, containerId = R.id.current_fragment))
    }

    fun showLive() {
        showRequest(AddFragment(FragmentId.LIVE, false, containerId = R.id.current_fragment))
    }

    fun showOnDemand() {
        showRequest(AddFragment(FragmentId.ON_DEMAND, false, containerId = R.id.current_fragment))
    }

    fun showProgress() {
        showRequest(AddFragment(FragmentId.PROGRESS, false, containerId = R.id.current_fragment))
    }

    fun showSettings() {
        showRequest(AddFragment(FragmentId.SETTINGS, false, containerId = R.id.current_fragment))
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

}
