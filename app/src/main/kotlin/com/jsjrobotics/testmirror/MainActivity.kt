package com.jsjrobotics.testmirror

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
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
            showFragment(FragmentRequest(fragment, false))
        }

        val navigationDisposable = navController.onShowRequest
                .subscribe {
            showFragment(it)
        }

        disposables.addAll(navigationDisposable)

    }

    private fun showFragment(request: FragmentRequest) {
        runOnUiThread{
            if (request.clearBackStack) {
                val clearTransaction = supportFragmentManager.beginTransaction()
                supportFragmentManager.fragments
                        .forEach { clearTransaction.remove(it)}
                clearTransaction.commit()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            val transaction = supportFragmentManager.beginTransaction()
            val fragment = request.fragmentId
            val tag = fragment.tag()

            supportFragmentManager.fragments
                    .filter{ it.tag != tag  && it.isVisible}
                    .forEach { transaction.hide(it) }

            supportFragmentManager.findFragmentByTag(tag)?.let { instantiatedFragment ->
                transaction.show(instantiatedFragment)
            } ?: transaction.add(android.R.id.content, fragment.instantiate(), fragment.tag() )
            if (request.addToBackstack) {
                transaction.addToBackStack(request.backstackTag)
            }
            transaction.commit()
        }
    }
}