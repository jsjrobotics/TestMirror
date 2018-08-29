package com.jsjrobotics.testmirror

import android.os.Bundle
import android.support.v4.app.FragmentActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val initalFragment = LoginFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, initalFragment, initalFragment.tag() )
                    .commit()
        }
    }
}