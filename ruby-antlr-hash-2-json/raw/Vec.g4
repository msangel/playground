// antlr4 Vec.g4
// javac -cp ".:$(realpath ~/soft/antlr4/antlr-4.7.2-complete.jar):$CLASSPATH" Vec*.java
// grun Vec vec4

grammar Vec;

vec4:   '[' ints[4] ']' ;
ints[int max]
locals [int i=1]
    :   INT ( ',' {$i++;} {$i<=$max}? INT )*
    ;

INT :   [0-9]+ ;
WS  :   [ \t\r\n]+ -> skip ;
