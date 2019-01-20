package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jsjrobotics.testmirror.FragmentId

class ConnectMirrorActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, FragmentId.CONNECT_TO_MIRROR.instantiate(), FragmentId.CONNECT_TO_MIRROR.tag())
                    .commit()
        }
    }
}
