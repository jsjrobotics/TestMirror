package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.AddFragment

class MainActivity : DefaultActivity() {
    private lateinit var navigationBar: BottomNavigationView

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.navigation_bar)
        bottomNavigation.setOnNavigationItemSelectedListener(navController)
        navigationBar = findViewById(R.id.navigation_bar)
        if (savedInstanceState == null) {
            showFragment(AddFragment(FragmentId.HOME, false, containerId = R.id.current_fragment))
        }
    }




}