package com.christidischristos.simplecalculator.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.data.Repository
import com.christidischristos.simplecalculator.enums.CalcState
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.grammar.DivideByZeroException
import com.christidischristos.simplecalculator.util.CurrencyUtils
import com.christidischristos.simplecalculator.util.ParsingUtils
import java.util.*

class SimpleCalcViewModel(private val _app: Application) : ViewModel() {

    private val _repository = Repository()

    private val _baseCurrency = MutableLiveData(Currency.EUR)
    val baseCurrency: LiveData<Currency>
        get() = _baseCurrency

    private val _userStr = MutableLiveData("")
    val userStr: LiveData<String>
        get() = _userStr

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    fun toastMessageSeen() {
        _toastMessage.value = null
    }

    private val _fetchingRates = MutableLiveData(false)
    val fetchingRates: LiveData<Boolean>
        get() = _fetchingRates

    private var _state = MutableLiveData(CalcState.ENTERING_INPUT)
    val state: LiveData<CalcState>
        get() = _state

    fun clearScreenForInput() {
        _userStr.value = ""
        _state.value = CalcState.ENTERING_INPUT
    }

    fun changeBaseCurrency(currency: Currency) {
        _baseCurrency.value = currency
    }

    fun plainButtonClicked(view: View) {
        if (state.value != CalcState.ENTERING_INPUT) {
            clearScreenForInput()
        }

        val s = when (view.id) {
            R.id.button_0 -> "0"
            R.id.button_1 -> "1"
            R.id.button_2 -> "2"
            R.id.button_3 -> "3"
            R.id.button_4 -> "4"
            R.id.button_5 -> "5"
            R.id.button_6 -> "6"
            R.id.button_7 -> "7"
            R.id.button_8 -> "8"
            R.id.button_9 -> "9"
            R.id.button_left_paren -> "("
            R.id.button_right_paren -> ")"
            R.id.button_div -> "/"
            R.id.button_mul -> "x"
            R.id.button_sub -> "-"
            R.id.button_add -> "+"
            R.id.button_dot -> "."
            else -> "0"
        }

        _userStr.value = userStr.value + s
    }

    @Suppress("UNUSED_PARAMETER")
    fun backspaceButtonClicked(view: View) {
        if (state.value != CalcState.ENTERING_INPUT) {
            clearScreenForInput()
            return
        }
        val numChars = userStr.value!!.length
        if (numChars != 0) {
            _userStr.value = userStr.value!!.substring(0, numChars - 1)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun equalsButtonClicked(view: View) {
        if (state.value == CalcState.ENTERING_INPUT && userStr.value!!.isNotEmpty()) {
            try {
                val result = ParsingUtils.computeResult(userStr.value!!)
                printResult(result)
            } catch (e: IllegalStateException) {
                printError(_app.getString(R.string.syntax_error))
            } catch (e: DivideByZeroException) {
                printError(_app.getString(R.string.divided_by_zero))
            }
        }
    }

    private fun printResult(result: Double) {
        var numberStr = String.format(Locale.US, "%g", result)

        if (numberStr.contains("[eE]".toRegex())) {
            numberStr = numberStr.replace("0+e".toRegex(), "e")
            numberStr = numberStr.replace("""\.e""".toRegex(), "e")
        } else if (numberStr.contains(".")) {
            numberStr = numberStr.replace("0*$".toRegex(), "")
            numberStr = numberStr.replace("""\.$""".toRegex(), "")
        }

        _userStr.postValue(numberStr)
        _state.postValue(CalcState.SHOWING_RESULT)
    }

    private fun printError(error: String) {
        _userStr.postValue(error)
        _state.postValue(CalcState.SHOWING_ERROR)
    }

    fun convertTo(targetCurrency: Currency) {
        if (inputInvalid()) {
            _toastMessage.value = R.string.input_not_valid
            return
        }

        _fetchingRates.value = true

        // I pass the methods I want to be called by the repository when it has a success, an error
        // or an exception. These lambdas are executed in the background thread by the repository.
        _repository.fetchExchangeRates({
            val amount = ParsingUtils.computeResult(userStr.value!!)
            val rate = CurrencyUtils.computeExchangeRate(baseCurrency.value!!, targetCurrency, it)
            printResult(amount * rate)
            _baseCurrency.postValue(targetCurrency)
            _fetchingRates.postValue(false)
        }, {
            printError(_app.getString(R.string.api_error, it))
            _fetchingRates.postValue(false)
        }, {
            printError(_app.getString(R.string.network_error))
            _fetchingRates.postValue(false)
        })
    }

    private fun inputInvalid(): Boolean {
        if (state.value == CalcState.SHOWING_ERROR || userStr.value!!.isEmpty()) {
            return true
        }

        if (state.value == CalcState.ENTERING_INPUT) {
            try {
                ParsingUtils.computeResult(userStr.value!!)
            } catch (e: IllegalStateException) {
                printError(_app.getString(R.string.syntax_error))
                return true
            } catch (e: DivideByZeroException) {
                printError(_app.getString(R.string.divided_by_zero))
                return true
            }
        }

        return false
    }
}