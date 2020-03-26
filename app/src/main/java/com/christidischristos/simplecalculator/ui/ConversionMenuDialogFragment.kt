package com.christidischristos.simplecalculator.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.christidischristos.simplecalculator.R

class ConversionMenuDialogFragment(private val _adapter: ConversionMenuAdapter) : DialogFragment() {

    init {
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recyclerView = RecyclerView(activity!!).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = _adapter
            val padding8 = dpToPixels(activity!!, 8f).toInt()
            val padding16 = dpToPixels(activity!!, 16f).toInt()
            setPadding(padding16, padding8, padding16, padding16)
        }

        return AlertDialog.Builder(activity)
            .setView(recyclerView)
            .setTitle(R.string.convert_to)
            .create()
    }


    @Suppress("SameParameterValue")
    private fun dpToPixels(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
    }
}