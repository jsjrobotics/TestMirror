package com.jsjrobotics.testmirror.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class ProgressFragment : DefaultFragment() {
    @Inject
    lateinit var view: ProgressView

    @Inject
    lateinit var presenter: ProgressPresenter

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
        val TAG : String = ProgressFragment::class.qualifiedName!!.toString()
    }
}