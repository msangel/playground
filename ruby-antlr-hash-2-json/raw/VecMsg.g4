// antlr4 VecMsg.g4
// javac -cp ".:$(realpath ~/soft/antlr4/antlr-4.7.2-complete.jar):$CLASSPATH" VecMsg*.java
// grun VecMsg vec4
grammar VecMsg;

vec4:   '[' ints[4] ']' ;

ints[int max]
locals [int i=1]
    :   INT ( ',' {$i++;} {$i<=$max}?<fail={"exceeded max "+$max}> INT )*
    ;

INT :   [0-9]+ ;
WS  :   [ \t\r\n]+ -> skip ;
