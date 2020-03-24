package com.christidischristos.simplecalculator.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.christidischristos.simplecalculator.BuildConfig
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.grammar.DivideByZeroException
import com.christidischristos.simplecalculator.grammar.EvalVisitor
import com.christidischristos.simplecalculator.grammar.SimpleCalcLexer
import com.christidischristos.simplecalculator.grammar.SimpleCalcParser
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse
import com.christidischristos.simplecalculator.network.FixerApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.truncate

class SimpleCalcViewModel(private val _app: Application) : ViewModel() {

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

    private var _errorOnScreen = false

    fun clearScreen() {
        _userStr.value = ""
        _errorOnScreen = false
    }

    fun plainButtonClicked(view: View) {
        if (_errorOnScreen) {
            clearScreen()
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
        if (_errorOnScreen) {
            clearScreen()
            return
        }
        val numChars = userStr.value!!.length
        if (numChars != 0) {
            _userStr.value = userStr.value!!.substring(0, numChars - 1)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun equalsButtonClicked(view: View) {
        if (!_errorOnScreen && userStr.value!!.isNotEmpty()) {
            computeResult()
        }
    }

    private fun computeResult() {
        try {
            val tree = parseString(userStr.value!!)
            val result = EvalVisitor().visit(tree)
            printResult(result)
        } catch (e: IllegalStateException) {
            _userStr.value = _app.getString(R.string.syntax_error)
            _errorOnScreen = true
        } catch (e: DivideByZeroException) {
            _userStr.value = _app.getString(R.string.divided_by_zero)
            _errorOnScreen = true
        }
    }

    private fun parseString(userText: String): ParseTree {
        val charStream = CharStreams.fromString(userText)
        val lexer = SimpleCalcLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = SimpleCalcParser(tokens)
        return parser.expr()
    }

    private fun printResult(result: Double) {
        _userStr.value = if (result == truncate(result))
            result.toLong().toString()
        else
            String.format(Locale.US, "%g", result)
    }

    fun convertTo(targetCurrency: Currency) {
        if (inputInvalid()) {
            _toastMessage.value = R.string.input_not_valid
            return
        }

        val amount = userStr.value!!.toDouble()

        GlobalScope.launch {
            try {
                val response = FixerApi.convertService.getExchangeRate(
                    getYesterdayDateString(), BuildConfig.FIXER_IO_KEY,
                    baseCurrency.value.toString(), targetCurrency.toString()
                )

                if (response.success) {
                    val rate = getRate(response, targetCurrency)
                    _userStr.postValue("${amount * rate}")
                    _baseCurrency.postValue(targetCurrency)
                } else {
                    _userStr.postValue(_app.getString(R.string.api_error, response.error!!.code))
                    _errorOnScreen = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _userStr.postValue(_app.getString(R.string.network_error))
                _errorOnScreen = true
            }
        }
    }

    private fun inputInvalid(): Boolean {
        if (_errorOnScreen || userStr.value!!.isEmpty()) {
            return true
        }

        computeResult()
        return _errorOnScreen
    }

    private fun getYesterdayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }

    private fun getRate(response: HistoricalRatesResponse, targetCurrency: Currency): Double {
        return when (targetCurrency) {
            Currency.USD -> response.rates!!.USD!!
            Currency.EUR -> response.rates!!.EUR!!
            Currency.JPY -> response.rates!!.JPY!!
            Currency.GBP -> response.rates!!.GBP!!
            Currency.AUD -> response.rates!!.AUD!!
            Currency.CAD -> response.rates!!.CAD!!
            Currency.CHF -> response.rates!!.CHF!!
        }
    }
}