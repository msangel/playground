// antlr4 Call.g4
// javac -cp ".:$(realpath ~/soft/antlr4/antlr-4.7.2-complete.jar):$CLASSPATH" Call*.java
// grun Call stat
grammar Call;

stat:   fcall ';' ;
fcall
    :   ID '(' expr ')'
    |   ID '(' expr ')' ')' {notifyErrorListeners("Too many parentheses");}
    |   ID '(' expr         {notifyErrorListeners("Missing closing ')'");}
    ;

expr:   '(' expr ')'
    |   INT
    ;

INT :   [0-9]+ ;
ID  :   [a-zA-Z]+ ;
WS  :   [ \t\r\n]+ -> skip ;
