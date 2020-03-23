package com.christidischristos.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.christidischristos.simplecalculator.grammar.EvalVisitor
import com.christidischristos.simplecalculator.grammar.SimpleCalcLexer
import com.christidischristos.simplecalculator.grammar.SimpleCalcParser
import kotlinx.android.synthetic.main.activity_main.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        button_backspace.setOnLongClickListener {
            Log.i("WTF", "long click")
            screen.text = ""
            true
        }
    }

    fun clickMe(view: View) {
        when (view.id) {
            R.id.button_0 -> screen.append("0")
            R.id.button_1 -> screen.append("1")
            R.id.button_2 -> screen.append("2")
            R.id.button_3 -> screen.append("3")
            R.id.button_4 -> screen.append("4")
            R.id.button_5 -> screen.append("5")
            R.id.button_6 -> screen.append("6")
            R.id.button_7 -> screen.append("7")
            R.id.button_8 -> screen.append("8")
            R.id.button_9 -> screen.append("9")
            R.id.button_convert -> {

            }
            R.id.button_left_paren -> screen.append("(")
            R.id.button_right_paren -> screen.append(")")
            R.id.button_backspace -> {
                if (screen.text.isNotBlank()) {
                    screen.text = screen.text.subSequence(0, screen.text.length - 1)
                }
            }
            R.id.button_div -> screen.append("/")
            R.id.button_mul -> screen.append("x")
            R.id.button_sub -> screen.append("-")
            R.id.button_add -> screen.append("+")
            R.id.button_dot -> {

            }
            R.id.button_equals -> {
                if (screen.text.isNotBlank()) {
                    try {
                        val tree = parseString(screen.text.toString())
                        val visitor = EvalVisitor()
                        screen.text = visitor.visit(tree).toString()
                    } catch (e: IllegalStateException) {
                        screen.text = getString(R.string.syntax_error)
                    } catch (e: ArithmeticException) {
                        screen.text = getString(R.string.divided_by_zero)
                    }
                }
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
