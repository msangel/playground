// antlr4 Pred.g4
// javac -cp ".:$(realpath ~/soft/antlr4/antlr-4.7.2-complete.jar):$CLASSPATH" Pred*.java
// grun Pred assign
grammar Pred;

assign
    : ID '=' v=INT {$v.int>0}? ';'
      {System.out.println("assign "+$ID.text+" to ");}
    ;

INT :   [0-9]+ ;
ID  :   [a-zA-Z]+ ;
WS  :   [ \t\r\n]+ -> skip ;
