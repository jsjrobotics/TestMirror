package com.jsjrobotics.testmirror.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import javax.inject.Inject

class SignUpFragment : DefaultFragment() {

    @Inject
    lateinit var view: SignUpView

    @Inject
    lateinit var presenter: SignUpPresenter

    override fun getFragmentId(): FragmentId = FragmentId.SIGNUP

    override fun onStart() {
        super.onStart()
        presenter.init(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {parent ->
            view.init(inflater, parent)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val TAG : String = SignUpFragment::class.qualifiedName!!.toString()
    }
}