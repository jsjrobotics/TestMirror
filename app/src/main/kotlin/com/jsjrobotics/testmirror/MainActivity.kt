package com.jsjrobotics.testmirror

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
import com.jsjrobotics.testmirror.dataStructures.FragmentRequest
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : FragmentActivity() {
    @Inject
    lateinit var navController: NavigationController

    private val disposables: CompositeDisposable = CompositeDisposable()

    private lateinit var navigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.inject(this)
        setContentView(R.layout.activity_main)
        navigationBar = findViewById(R.id.navigation_bar)
        if (savedInstanceState == null) {
            val fragment = navController.currentFragment
            showFragment(FragmentRequest(fragment, false))
        }
    }

    override fun onStart() {
        super.onStart()
        val navigationDisposable = navController.onShowRequest
                .subscribe {
                    showFragment(it)
                }

        disposables.addAll(navigationDisposable)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
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
            } ?: transaction.add(R.id.current_fragment, fragment.instantiate(), fragment.tag() )
            if (request.addToBackstack) {
                transaction.addToBackStack(request.backstackTag)
            }
            transaction.commit()
            setNavigationBarVisibility(request.fragmentId.isNavBarVisible())
        }
    }

    private fun setNavigationBarVisibility(navBarVisible: Boolean) {
        val visibility = if (navBarVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        navigationBar.visibility = visibility
    }
}