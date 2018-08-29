package com.jsjrobotics.testmirror

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var application: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {
            return inflater.inflate(R.layout.fragment_login, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun tag() : String {
        return this::class.qualifiedName!!.toString()
    }
}