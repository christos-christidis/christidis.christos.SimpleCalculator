package com.christidischristos.simplecalculator.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

interface SimpleCalcVisitor<T> extends ParseTreeVisitor<T> {
    T visitNumber(SimpleCalcParser.NumberContext ctx);

    T visitParens(SimpleCalcParser.ParensContext ctx);

    T visitAddSub(SimpleCalcParser.AddSubContext ctx);

    T visitMulDiv(SimpleCalcParser.MulDivContext ctx);
}