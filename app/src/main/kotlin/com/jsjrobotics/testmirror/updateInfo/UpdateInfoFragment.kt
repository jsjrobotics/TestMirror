package com.jsjrobotics.testmirror.updateInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import javax.inject.Inject

class UpdateInfoFragment : DefaultFragment() {
    @Inject
    lateinit var view: UpdateInfoView

    @Inject
    lateinit var presenter: UpdateInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver(presenter)
    }

    override fun onDestroy() {
        removeLifecycleObserver(presenter)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {parent ->
            view.init(inflater, parent)
            presenter.init(view)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val TAG : String = UpdateInfoFragment::class.qualifiedName!!.toString()
    }
}