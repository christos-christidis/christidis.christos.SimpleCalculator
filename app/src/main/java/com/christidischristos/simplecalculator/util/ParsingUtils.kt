package com.christidischristos.simplecalculator.util

import com.christidischristos.simplecalculator.grammar.EvalVisitor
import com.christidischristos.simplecalculator.grammar.SimpleCalcLexer
import com.christidischristos.simplecalculator.grammar.SimpleCalcParser
import org.antlr.v4.runtime.*

object ParsingUtils {
    fun computeResult(userInput: String): Double {
        val charStream = CharStreams.fromString(userInput)
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

        val tree = parser.main()
        return EvalVisitor().visit(tree)
    }
}