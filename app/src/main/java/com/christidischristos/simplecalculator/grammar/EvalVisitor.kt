package com.christidischristos.simplecalculator.grammar

class EvalVisitor : SimpleCalcBaseVisitor<Int>() {
    override fun visitNumber(ctx: SimpleCalcParser.NumberContext?): Int {
        return ctx!!.NUMBER().text.toInt()
    }

    override fun visitParens(ctx: SimpleCalcParser.ParensContext?): Int {
        return visit(ctx!!.expr())
    }

    override fun visitAddSub(ctx: SimpleCalcParser.AddSubContext?): Int {
        val leftValue = visit(ctx!!.expr(0))
        val rightValue = visit(ctx.expr(1))
        return if (ctx.op.type == SimpleCalcParser.ADD)
            leftValue + rightValue else leftValue - rightValue
    }

    override fun visitMulDiv(ctx: SimpleCalcParser.MulDivContext?): Int {
        val leftValue = visit(ctx!!.expr(0))
        val rightValue = visit(ctx.expr(1))
        return if (ctx.op.type == SimpleCalcParser.MUL)
            leftValue * rightValue else leftValue / rightValue
    }
}