grammar Hello; // Define a grammar called Hello
//@header {
//package ua.k.co.play.rah2j ;
//}
// from progect root:
// antlr4 -package ua.k.co.play.rah2j src/main/antlr/ua/k/co/play/rah2j/Hello.g4

r : 'hello' ID ; // match keyword hello followed by an identifier
ID : [a-z]+ ; // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines, \r (Windows)

