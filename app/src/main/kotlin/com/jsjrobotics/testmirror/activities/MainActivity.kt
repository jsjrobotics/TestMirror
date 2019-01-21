package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.AddFragment

class MainActivity : DefaultActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.navigation_bar)
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = FragmentId.HOME.getNavBarItemId()!!
        }

    }

    private fun showProfile(clearBackstack : Boolean) {
        showFragment(AddFragment(FragmentId.HOME, false, clearBackStack = clearBackstack, containerId = R.id.current_fragment))
    }

    private fun showLive() {
        showFragment(AddFragment(FragmentId.LIVE, false, containerId = R.id.current_fragment))
    }

    private fun showOnDemand() {
        showFragment(AddFragment(FragmentId.ON_DEMAND, false, containerId = R.id.current_fragment))
    }

    private fun showProgress() {
        showFragment(AddFragment(FragmentId.PROGRESS, false, containerId = R.id.current_fragment))
    }

    private fun showSettings() {
        showFragment(AddFragment(FragmentId.SETTINGS, false, containerId = R.id.current_fragment))
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectBottonNavigationItemId(item.itemId)
        return true
    }

    private fun selectBottonNavigationItemId(itemId: Int): Boolean {
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


}