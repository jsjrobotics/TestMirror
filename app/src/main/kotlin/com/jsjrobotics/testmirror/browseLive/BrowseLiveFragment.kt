package com.jsjrobotics.testmirror.browseLive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class BrowseLiveFragment : DefaultFragment() {
    @Inject
    lateinit var view: BrowseLiveView

    @Inject
    lateinit var presenter: BrowseLivePresenter

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
        val TAG : String = BrowseLiveFragment::class.qualifiedName!!.toString()
    }
}