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
import com.christidischristos.simplecalculator.network.FixerApi
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTree
import java.text.SimpleDateFormat
import java.util.*

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

    private enum class State {
        ENTERING_INPUT, SHOWING_RESULT, SHOWING_ERROR
    }

    private var _state = State.ENTERING_INPUT

    fun clearScreenForInput() {
        _userStr.value = ""
        _state = State.ENTERING_INPUT
    }

    fun plainButtonClicked(view: View) {
        if (_state != State.ENTERING_INPUT) {
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
        if (_state != State.ENTERING_INPUT) {
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
        if (_state == State.ENTERING_INPUT && userStr.value!!.isNotEmpty()) {
            computeResult()
        }
    }

    private fun computeResult(print: Boolean = true): Double {
        try {
            val tree = parseString()
            val result = EvalVisitor().visit(tree)
            if (print) {
                printResult(result)
            }
            return result
        } catch (e: IllegalStateException) {
            _userStr.value = _app.getString(R.string.syntax_error)
            _state = State.SHOWING_ERROR
            return -1.0
        } catch (e: DivideByZeroException) {
            _userStr.value = _app.getString(R.string.divided_by_zero)
            _state = State.SHOWING_ERROR
            return -2.0
        }
    }

    private fun parseString(): ParseTree {
        val charStream = CharStreams.fromString(userStr.value)
        val lexer = SimpleCalcLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = SimpleCalcParser(tokens).apply {
            addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(
                    recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int,
                    charPositionInLine: Int, msg: String?, e: RecognitionException?
                ) {
                    // normally the parser doesn't throw exception on mismatches!
                    throw IllegalStateException()
                }
            })
        }
        return parser.main()
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

        _userStr.postValue(numberStr) // postValue cause method is also called on background thread
        _state = State.SHOWING_RESULT
    }

    fun convertTo(targetCurrency: Currency) {
        if (inputInvalid()) {
            _toastMessage.value = R.string.input_not_valid
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = FixerApi.convertService.getExchangeRate(
                    getYesterdayDateString(), BuildConfig.FIXER_IO_KEY,
                    baseCurrency.value.toString(), targetCurrency.toString()
                )

                if (response.success) {
                    val amount = if (_state == State.ENTERING_INPUT)
                        computeResult(print = false) else userStr.value!!.toDouble()
                    val rate = getRate(response, targetCurrency)
                    printResult(amount * rate)
                    _baseCurrency.postValue(targetCurrency)
                } else {
                    _userStr.postValue(_app.getString(R.string.api_error, response.error!!.code))
                    _state = State.SHOWING_ERROR
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _userStr.postValue(_app.getString(R.string.network_error))
                _state = State.SHOWING_ERROR
            }
        }
    }

    private fun inputInvalid(): Boolean {
        if (_state == State.SHOWING_ERROR || userStr.value!!.isEmpty()) {
            return true
        }

        if (_state == State.ENTERING_INPUT) {
            computeResult(print = false)
        }
        return _state == State.SHOWING_ERROR
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