grammar LExpr;

s : e ;

e returns [int value]
  : e MULT e 		# Mult
  | e ADD e 		# Add
  | INT        		# Int
  ;

MULT: '*' ;
ADD : '+' ;
INT : [0-9]+ ;
WS : [ \t\n]+ -> skip ;
