package com.jsjrobotics.testmirror.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsjrobotics.testmirror.DefaultFragment
import com.jsjrobotics.testmirror.FragmentId
import com.jsjrobotics.testmirror.activities.MainActivity
import javax.inject.Inject

class LoginFragment : DefaultFragment() {

    @Inject
    lateinit var view: LoginView

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleObserver(presenter)
    }

    override fun getFragmentId(): FragmentId = FragmentId.LOGIN

    override fun onStart() {
        super.onStart()
        presenter.init(view, this::onLoginSuccess)
    }

    private fun onLoginSuccess() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroy() {
        removeLifecycleObserver(presenter)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {parent ->
            view.init(inflater, parent)
            return view.rootXml
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val TAG : String = LoginFragment::class.qualifiedName!!.toString()
    }
}