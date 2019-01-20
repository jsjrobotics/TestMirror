package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.NavigationController
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import com.jsjrobotics.testmirror.dataStructures.RemoveFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class DefaultActivity : FragmentActivity() {
    private val disposables: CompositeDisposable = CompositeDisposable()
    @Inject
    protected lateinit var navController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    override fun onStart() {
        super.onStart()
        val navigationDisposable = navController.onShowRequest
                .subscribe {
                    when (it) {
                        is AddFragment -> showFragment(it)
                        is RemoveFragment -> removeFragment(it)
                    }
                }

        disposables.addAll(navigationDisposable)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    protected fun showFragment(request: AddFragment) {
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
            } ?: transaction.add(request.containerId, fragment.instantiate(), fragment.tag() )
            if (request.addToBackstack) {
                transaction.addToBackStack(request.backstackTag)
            }
            transaction.commit()
        }
    }

    protected fun removeFragment(request: RemoveFragment) {
        runOnUiThread{
            supportFragmentManager.popBackStack(request.fragmentId.tag(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}