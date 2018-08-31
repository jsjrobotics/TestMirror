package com.jsjrobotics.testmirror

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : FragmentActivity() {
    @Inject
    lateinit var navController: NavigationController

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
        if (savedInstanceState == null) {
            val fragment = navController.currentFragment
            showFragment(fragment, false)
        }

        val navigationDisposable = navController.onShowRequest
                .subscribe {
            showFragment(it)
        }

        disposables.addAll(navigationDisposable)

    }

    private fun showFragment(fragment: FragmentId, addToBackstack : Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        val tag = fragment.tag()
        supportFragmentManager.fragments
                .filter{ it.tag != tag  && it.isVisible}
                .forEach { transaction.hide(it) }

        supportFragmentManager.findFragmentByTag(tag)?.let { instantiatedFragment ->
            transaction.show(instantiatedFragment)
                    .addToBackStack(null)
            transaction.commit()
            return
        }
        transaction.add(android.R.id.content, fragment.instantiate(), fragment.tag() )
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}