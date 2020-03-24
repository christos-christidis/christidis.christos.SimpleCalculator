package com.christidischristos.simplecalculator.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object MyToast {
    fun showToastCenter(context: Context?, resId: Int, duration: Int) {
        Toast.makeText(context, resId, duration).apply {
            setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }
}



