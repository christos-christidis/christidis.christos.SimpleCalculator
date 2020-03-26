package com.christidischristos.simplecalculator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.databinding.ActivityMainBinding
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.util.MyToast
import com.christidischristos.simplecalculator.util.NetUtils
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
            _viewModel.clearScreenForInput()
            true
        }

        registerForContextMenu(button_convert)

        base_currencies_recycler_view.adapter = CurrencyAdapter {
            base_currencies_recycler_view.visibility = View.INVISIBLE
            _viewModel.changeBaseCurrency(it)
        }

        _viewModel.toastMessage.observe(this, Observer {
            it?.let {
                _viewModel.toastMessageSeen()
                MyToast.showToastCenter(this, it, Toast.LENGTH_LONG)
            }
        })
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
        if (NetUtils.isInternetAvailable(this)) {
            _viewModel.convertTo(Currency.values()[item.itemId])
        } else {
            MyToast.showToastCenter(this, R.string.internet_not_available, Toast.LENGTH_LONG)
        }
        return true
    }

    private var _animationRunning = false

    fun baseCurrencyClicked(view: View) {
        if (_animationRunning) {
            return
        }

        _animationRunning = true

        if (base_currencies_recycler_view.visibility == View.INVISIBLE) {
            showRecyclerView()
        } else {
            hideRecyclerView()
        }
    }

    private fun showRecyclerView() {
        base_currencies_recycler_view.alpha = 0f
        base_currencies_recycler_view.visibility = View.VISIBLE
        base_currencies_recycler_view.animate().alpha(1f).apply {
            duration = 300
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    _animationRunning = false
                }
            })
        }
    }

    private fun hideRecyclerView() {
        base_currencies_recycler_view.animate().alpha(0f).apply {
            duration = 300
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    base_currencies_recycler_view.visibility = View.INVISIBLE
                    _animationRunning = false
                }
            })
        }
    }
}

