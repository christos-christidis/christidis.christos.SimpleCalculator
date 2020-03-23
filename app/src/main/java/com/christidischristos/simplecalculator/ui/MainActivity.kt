package com.christidischristos.simplecalculator.ui

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.databinding.ActivityMainBinding
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.viewmodelfactories.SimpleCalcViewModelFactory
import com.christidischristos.simplecalculator.viewmodels.SimpleCalcViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val _viewModel: SimpleCalcViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
    }

    private fun getViewModel(): SimpleCalcViewModel {
        val viewModelFactory = SimpleCalcViewModelFactory(application)
        return ViewModelProvider(this, viewModelFactory).get(SimpleCalcViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        button_backspace.setOnLongClickListener {
            _viewModel.clear()
            true
        }

        registerForContextMenu(button_convert)
    }

    fun convertButtonClicked(view: View) {
        view.performLongClick()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle(R.string.convert_to)
        for (currency in Currency.values()) {
            if (currency != _viewModel.baseCurrency.value) {
                menu?.add(0, currency.ordinal, currency.ordinal, currency.toTitleRes())
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        _viewModel.convertTo(Currency.values()[item.itemId])
        return true
    }
}

