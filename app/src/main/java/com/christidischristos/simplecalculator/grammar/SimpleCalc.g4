grammar SimpleCalc;

main: expr EOF ;

expr : '-' expr                # unaryMinusExpr
     | expr op=('x'|'/') expr  # mulDiv
     | expr op=('+'|'-') expr  # addSub
     | NUMBER                  # number
     | '(' expr ')'            # parens
     ;

NUMBER : DIGIT+ '.' DIGIT*
       | DIGIT+
       | '.' DIGIT+
       ;

fragment
DIGIT : [0-9] ;

MUL : 'x' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;

WS : [ \t\r\n]+ -> skip ;
