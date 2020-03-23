package com.christidischristos.simplecalculator.grammar;

// Generated from SimpleCalc.g4 by ANTLR 4.8

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SimpleCalcLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, NUMBER = 3, MUL = 4, DIV = 5, ADD = 6, SUB = 7;
    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };

    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    private static String[] makeRuleNames() {
        return new String[]{
                "T__0", "T__1", "NUMBER", "DIGIT", "MUL", "DIV", "ADD", "SUB"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'('", "')'", null, "'x'", "'/'", "'+'", "'-'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, "NUMBER", "MUL", "DIV", "ADD", "SUB"
        };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }


    public SimpleCalcLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "SimpleCalc.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\t:\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\3\3\3" +
                    "\3\4\6\4\31\n\4\r\4\16\4\32\3\4\3\4\7\4\37\n\4\f\4\16\4\"\13\4\3\4\6\4" +
                    "%\n\4\r\4\16\4&\3\4\3\4\6\4+\n\4\r\4\16\4,\5\4/\n\4\3\5\3\5\3\6\3\6\3" +
                    "\7\3\7\3\b\3\b\3\t\3\t\2\2\n\3\3\5\4\7\5\t\2\13\6\r\7\17\b\21\t\3\2\3" +
                    "\3\2\62;\2>\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2" +
                    "\2\2\17\3\2\2\2\2\21\3\2\2\2\3\23\3\2\2\2\5\25\3\2\2\2\7.\3\2\2\2\t\60" +
                    "\3\2\2\2\13\62\3\2\2\2\r\64\3\2\2\2\17\66\3\2\2\2\218\3\2\2\2\23\24\7" +
                    "*\2\2\24\4\3\2\2\2\25\26\7+\2\2\26\6\3\2\2\2\27\31\5\t\5\2\30\27\3\2\2" +
                    "\2\31\32\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2\2\33\34\3\2\2\2\34 \7\60\2" +
                    "\2\35\37\5\t\5\2\36\35\3\2\2\2\37\"\3\2\2\2 \36\3\2\2\2 !\3\2\2\2!/\3" +
                    "\2\2\2\" \3\2\2\2#%\5\t\5\2$#\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'" +
                    "/\3\2\2\2(*\7\60\2\2)+\5\t\5\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2" +
                    "-/\3\2\2\2.\30\3\2\2\2.$\3\2\2\2.(\3\2\2\2/\b\3\2\2\2\60\61\t\2\2\2\61" +
                    "\n\3\2\2\2\62\63\7z\2\2\63\f\3\2\2\2\64\65\7\61\2\2\65\16\3\2\2\2\66\67" +
                    "\7-\2\2\67\20\3\2\2\289\7/\2\29\22\3\2\2\2\b\2\32 &,.\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}