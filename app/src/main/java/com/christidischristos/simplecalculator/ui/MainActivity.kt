package com.christidischristos.simplecalculator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
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

    companion object {
        const val STATE_BASE_CURRENCIES_VISIBLE = "state_base_currencies_visible"
    }

    private val _handler = Handler()

    private val _viewModel: SimpleCalcViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        setUpViews()
        observeViewModel()
    }

    private fun getViewModel(): SimpleCalcViewModel {
        val viewModelFactory = SimpleCalcViewModelFactory(application)
        return ViewModelProvider(this, viewModelFactory).get(SimpleCalcViewModel::class.java)
    }

    private fun setUpViews() {
        button_backspace.setOnLongClickListener {
            _viewModel.clearScreenForInput()
            true
        }

        val currencies = Currency.getAllExcept(_viewModel.baseCurrency.value!!)

        base_currencies_recycler_view.adapter = BaseCurrenciesAdapter(currencies) {
            base_currencies_recycler_view.visibility = View.INVISIBLE
            _viewModel.changeBaseCurrency(it)
        }
    }

    private fun observeViewModel() {
        _viewModel.toastMessage.observe(this, Observer {
            it?.let {
                _viewModel.toastMessageSeen()
                MyToast.showToastCenter(this, it, Toast.LENGTH_LONG)
            }
        })
        _viewModel.fetchingRates.observe(this, Observer {
            if (it) {
                disableTouch()
                _handler.postDelayed({
                    loading_card_view.visibility = View.VISIBLE
                }, 2000)
            } else {
                _handler.removeCallbacksAndMessages(null)
                loading_card_view.visibility = View.INVISIBLE
                enableTouch()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(
            STATE_BASE_CURRENCIES_VISIBLE, base_currencies_recycler_view.visibility == View.VISIBLE
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        base_currencies_recycler_view.visibility =
            if (savedInstanceState.getBoolean(STATE_BASE_CURRENCIES_VISIBLE))
                View.VISIBLE else View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var _dialogFragment: ConversionMenuDialogFragment

    fun convertButtonClicked(view: View) {
        val currencies = Currency.getAllExcept(_viewModel.baseCurrency.value!!)

        val adapter = ConversionMenuAdapter(currencies) {
            _dialogFragment.dismiss()

            if (NetUtils.isInternetAvailable(this)) {
                _viewModel.convertTo(it)
            } else {
                MyToast.showToastCenter(this, R.string.internet_not_available, Toast.LENGTH_LONG)
            }
        }

        _dialogFragment = ConversionMenuDialogFragment(adapter)
        _dialogFragment.show(supportFragmentManager, "CONVERSION_MENU_DIALOG_TAG")
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
                    base_currencies_recycler_view.alpha = 1f
                    _animationRunning = false
                }
            })
        }
    }

    private fun disableTouch() {
        for (child in main_layout.children) {
            child.setOnTouchListener { _, _ -> true }
        }
    }

    private fun enableTouch() {
        for (child in main_layout.children) {
            child.setOnTouchListener(null)
        }
    }
}

