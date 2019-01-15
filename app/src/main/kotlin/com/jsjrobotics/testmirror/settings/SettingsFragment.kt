package com.jsjrobotics.testmirror.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import javax.inject.Inject

class SettingsFragment : DefaultFragment() {
    @Inject
    lateinit var view: SettingsView

    @Inject
    lateinit var presenter: SettingsPresenter

    override fun getFragmentId(): FragmentId = FragmentId.SETTINGS

    override fun onStart() {
        super.onStart()
        presenter.init(view)
        presenter.loadData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.loadData()
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
        val TAG : String = SettingsFragment::class.qualifiedName!!.toString()
    }
}