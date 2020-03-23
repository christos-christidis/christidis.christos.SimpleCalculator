package com.christidischristos.simplecalculator.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.grammar.DivideByZeroException
import com.christidischristos.simplecalculator.grammar.EvalVisitor
import com.christidischristos.simplecalculator.grammar.SimpleCalcLexer
import com.christidischristos.simplecalculator.grammar.SimpleCalcParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.truncate

class SimpleCalcViewModel(app: Application) : AndroidViewModel(app) {

    private val _baseCurrency = MutableLiveData(Currency.EUR)
    val baseCurrency: LiveData<Currency>
        get() = _baseCurrency

    private val _userStr = MutableLiveData("")
    val userStr: LiveData<String>
        get() = _userStr

    private var _showingError = false

    fun clear() {
        _userStr.value = ""
        _showingError = false
    }

    fun plainButtonClicked(view: View) {
        if (_showingError) {
            clear()
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
        if (_showingError) {
            clear()
            return
        }
        val numChars = userStr.value!!.length
        if (numChars != 0) {
            _userStr.value = userStr.value!!.substring(0, numChars - 1)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun equalsButtonClicked(view: View) {
        computeResult()
    }

    private fun computeResult() {
        if (_showingError || userStr.value!!.isBlank()) {
            return
        }

        try {
            val tree = parseString(userStr.value!!)
            val result = EvalVisitor().visit(tree)
            printResult(result)
        } catch (e: IllegalStateException) {
            _userStr.value = getApplication<Application>().getString(R.string.syntax_error)
            _showingError = true
        } catch (e: DivideByZeroException) {
            _userStr.value = getApplication<Application>().getString(R.string.divided_by_zero)
            _showingError = true
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
        if (userStr.value!!.isEmpty()) {
            return
        }

        computeResult()
        if (_showingError) {
            return
        }

        val currencyUnits = userStr.value!!.toDouble()

        Log.i(
            "WTF",
            "Converting $currencyUnits units from ${baseCurrency.value} to $targetCurrency"
        )

        _userStr.value = "RESULT!"
        _baseCurrency.value = targetCurrency
    }
}