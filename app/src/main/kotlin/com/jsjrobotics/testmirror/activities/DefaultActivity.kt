package com.jsjrobotics.testmirror.activities

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.jsjrobotics.testmirror.Application
import com.jsjrobotics.testmirror.dataStructures.AddFragment
import com.jsjrobotics.testmirror.dataStructures.RemoveFragment
import io.reactivex.disposables.CompositeDisposable

abstract class DefaultActivity : FragmentActivity() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    fun showFragment(request: AddFragment) {
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