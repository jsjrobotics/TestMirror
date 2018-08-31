package com.jsjrobotics.testmirror

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

abstract class DefaultView {

    protected abstract fun getContext() : Context
    fun showEnterAllFields() {
        runOnUiThread {
            Toast.makeText(getContext(),
                           R.string.complete_all_fields,
                           Toast.LENGTH_SHORT)
                    .show()
        }
    }

    fun showNoServiceConnection() {
        runOnUiThread {
            Toast.makeText(getContext(),
                           R.string.no_service_connection,
                           Toast.LENGTH_SHORT)
                    .show()
        }
    }

    fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(getContext(),
                           message,
                           Toast.LENGTH_SHORT)
                    .show()
        }
    }

    fun showToast(@StringRes id: Int) {
        showToast(Application.instance().getString(id))
    }
}
