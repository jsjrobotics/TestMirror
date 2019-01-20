package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.AddFragment

class WelcomeActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if (savedInstanceState == null) {
            showFragment(AddFragment(FragmentId.WELCOME, true, containerId = R.id.welcome_fragment))
        }
    }
}