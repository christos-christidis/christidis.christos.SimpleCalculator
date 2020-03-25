package com.christidischristos.simplecalculator.grammar;

// Generated from SimpleCalc.g4 by ANTLR 4.8

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

interface SimpleCalcVisitor<T> extends ParseTreeVisitor<T> {
    T visitMain(SimpleCalcParser.MainContext ctx);

    T visitNumber(SimpleCalcParser.NumberContext ctx);

    T visitParens(SimpleCalcParser.ParensContext ctx);

    T visitUnaryMinusExpr(SimpleCalcParser.UnaryMinusExprContext ctx);

    T visitAddSub(SimpleCalcParser.AddSubContext ctx);

    T visitMulDiv(SimpleCalcParser.MulDivContext ctx);
}