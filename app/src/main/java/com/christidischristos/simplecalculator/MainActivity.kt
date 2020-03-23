package com.christidischristos.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.christidischristos.simplecalculator.grammar.EvalVisitor
import com.christidischristos.simplecalculator.grammar.SimpleCalcLexer
import com.christidischristos.simplecalculator.grammar.SimpleCalcParser
import kotlinx.android.synthetic.main.activity_main.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.tree.ParseTree
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickMe(view: View) {
        val userText = text_input.text.toString()
        if (userText.isNotBlank()) {
            try {
                val tree = parseString(userText)
                val visitor = EvalVisitor()
                result_text.text = visitor.visit(tree).toString()
            } catch (e: IllegalStateException) {
                result_text.text = getString(R.string.syntax_error)
            } catch (e: ArithmeticException) {
                result_text.text = getString(R.string.divided_by_zero)
            }
        }
    }

    private fun parseString(userText: String): ParseTree {
        val charStream = CharStreams.fromString(userText)
        val lexer = SimpleCalcLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = SimpleCalcParser(tokens)
        return parser.expr()
    }
}
