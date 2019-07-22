grammar Enum2;
@lexer::members {public static boolean java5 = false;}

prog:   (   stat
        )+
    ;

stat:   ID '=' expr ';' {System.out.println($ID.text+"="+$expr.text);} ;

expr:   ID
    |   INT
    ;


ENUM:   'enum' {java5}? ; // must be before ID
ID  :   [a-zA-Z]+ ;


INT :   [0-9]+ ;
WS  :   [ \t\r\n]+ -> skip ;
