package com.app.func.utils

import android.content.Context
import android.widget.Toast

object MyToast {

    private var mToast: Toast? = null

    fun showToast(context: Context?, value: String, duration: Int = Toast.LENGTH_SHORT) {
        if (mToast != null) {
            mToast?.cancel()
        }
        mToast = Toast.makeText(context, value, duration)
        mToast?.show()
    }
}