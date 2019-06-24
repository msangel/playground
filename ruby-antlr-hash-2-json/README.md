# ruby-antlr-hash-2-json

This is playground where I will try to fix existing ANTLR configuration of [Liqp library](https://github.com/msangel/Liqp) for allowing native ruby hash literals as a value of expression. Currently this:
```
{% assign my_variable = {"a" => 1 } %}
```
is not allowed and the idea is to add such support. 

As far as this is Java library, the value will be translated into jackson's JsonObject.

## Whats here
Sample code with tests

## How to run
`./gradlew`

The default task will be executed.

## Docs
 * liquid docs: https://shopify.github.io/liquid/
 * existed grammar: https://github.com/msangel/Liqp/blob/master/src/main/antlr4/liquid/parser/v4/LiquidLexer.g4
 * existed parser: https://github.com/msangel/Liqp/blob/master/src/main/antlr4/liquid/parser/v4/LiquidParser.g4
 * sample ruby grammar(but without hash literal): https://github.com/antlr/grammars-v4/blob/master/ruby/Corundum.g4
 * ruby hash literals docs: https://docs.ruby-lang.org/en/2.0.0/Hash.html
 * ruby :symbol vs "string": https://www.engineyard.com/blog/tips-ruby-for-rails-hash (tldr: "A symbol is immutable while a string is mutable. You can't change a symbol once it's created. :locked on different lines in your code is the same object. The "locked" on different lines on the other hand are different objects.")