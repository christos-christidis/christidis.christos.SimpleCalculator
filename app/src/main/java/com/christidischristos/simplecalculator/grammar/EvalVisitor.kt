package com.christidischristos.simplecalculator.grammar

class EvalVisitor : SimpleCalcBaseVisitor<Double>() {
    override fun visitMain(ctx: SimpleCalcParser.MainContext?): Double {
        return visit(ctx!!.expr())
    }

    override fun visitNumber(ctx: SimpleCalcParser.NumberContext?): Double {
        return ctx!!.NUMBER().text.toDouble()
    }

    override fun visitParens(ctx: SimpleCalcParser.ParensContext?): Double {
        return visit(ctx!!.expr())
    }

    override fun visitUnaryMinusExpr(ctx: SimpleCalcParser.UnaryMinusExprContext?): Double {
        return -1 * visit(ctx!!.expr())
    }

    override fun visitAddSub(ctx: SimpleCalcParser.AddSubContext?): Double {
        val leftValue = visit(ctx!!.expr(0))
        val rightValue = visit(ctx.expr(1))
        return if (ctx.op.type == SimpleCalcParser.ADD)
            leftValue + rightValue else leftValue - rightValue
    }

    override fun visitMulDiv(ctx: SimpleCalcParser.MulDivContext?): Double {
        val leftValue = visit(ctx!!.expr(0))
        val rightValue = visit(ctx.expr(1))

        if (rightValue == 0.0) {
            throw DivideByZeroException()
        }

        return if (ctx.op.type == SimpleCalcParser.MUL)
            leftValue * rightValue else leftValue / rightValue
    }
}

class DivideByZeroException : Exception()