package com.christidischristos.simplecalculator.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.christidischristos.simplecalculator.viewmodels.SimpleCalcViewModel

class SimpleCalcViewModelFactory(private val _app: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimpleCalcViewModel::class.java)) {
            return SimpleCalcViewModel(_app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
