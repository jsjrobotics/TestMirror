package com.jsjrobotics.testmirror.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.activities.DefaultActivity
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import javax.inject.Inject

class WelcomeFragment : DefaultFragment() {

    @Inject
    lateinit var view: WelcomeView

    @Inject
    lateinit var presenter: WelcomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver(presenter)
    }

    override fun getFragmentId(): FragmentId = FragmentId.WELCOME

    override fun onStart() {
        super.onStart()
        val requestShowFragment : (AddFragment) -> Unit = {(activity as DefaultActivity?)?.showFragment(it)}
        presenter.init(view, requestShowFragment)
    }

    override fun onDestroy() {
        removeLifecycleObserver(presenter)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let { parent ->
            view.init(inflater, parent, savedInstanceState)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val TAG : String = WelcomeFragment::class.qualifiedName!!.toString()
    }
}