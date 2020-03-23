package com.christidischristos.simplecalculator.grammar;

// Generated from SimpleCalc.g4 by ANTLR 4.8

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class SimpleCalcBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements SimpleCalcVisitor<T> {
    @Override
    public T visitNumber(SimpleCalcParser.NumberContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitParens(SimpleCalcParser.ParensContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitUnaryMinusExpr(SimpleCalcParser.UnaryMinusExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitAddSub(SimpleCalcParser.AddSubContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitMulDiv(SimpleCalcParser.MulDivContext ctx) {
        return visitChildren(ctx);
    }
}