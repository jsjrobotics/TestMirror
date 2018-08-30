package com.jsjrobotics.testmirror.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class SignUpFragment : DefaultFragment() {

    @Inject
    lateinit var view: SignUpView

    @Inject
    lateinit var presenter: SignUpPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {parent ->
            view.init(inflater, parent)
            presenter.init(view)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val TAG : String = SignUpFragment::class.qualifiedName!!.toString()
    }
}