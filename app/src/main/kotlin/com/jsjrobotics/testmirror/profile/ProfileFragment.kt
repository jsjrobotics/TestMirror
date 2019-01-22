package com.jsjrobotics.testmirror.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import javax.inject.Inject

class ProfileFragment : DefaultFragment() {
    @Inject
    lateinit var view: ProfileView

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun getFragmentId(): FragmentId = FragmentId.HOME

    override fun onStart() {
        super.onStart()
        presenter.init(view)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.sendDashboardScreenRequest()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {parent ->
            view.init(inflater, parent)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        val TAG : String = ProfileFragment::class.qualifiedName!!.toString()
    }

}