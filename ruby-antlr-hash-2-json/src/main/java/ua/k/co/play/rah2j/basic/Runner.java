package ua.k.co.play.rah2j.basic;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ua.k.co.play.rah2j.ArrayInitBaseListener;
import ua.k.co.play.rah2j.ArrayInitLexer;
import ua.k.co.play.rah2j.ArrayInitParser;

import java.io.IOException;

public class Runner {
    public static class ShortToUnicodeString extends ArrayInitBaseListener {
        /**
         * Translate { to "
         */
        @Override
        public void enterInit(ArrayInitParser.InitContext ctx) {
            System.out.print('"');
        }

        /**
         * Translate } to "
         */
        @Override
        public void exitInit(ArrayInitParser.InitContext ctx) {
            System.out.print('"');
        }

        /**
         * Translate integers to 4-digit hexadecimal strings prefixed with \\u
         */
        @Override
        public void enterValue(ArrayInitParser.ValueContext ctx) {
            // Assumes no nested array initializers
            int value = Integer.valueOf(ctx.INT().getText());
            System.out.printf("\\u%04x", value);
        }
    }

    public static void main(String[] args) throws IOException {

        // create a CharStream that reads from standard input
        CharStream input = CharStreams.fromString("{1,2,4, 7}");

        // create a lexer that feeds off of input CharStream
        ArrayInitLexer lexer = new ArrayInitLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        ArrayInitParser parser = new ArrayInitParser(tokens);
        ParseTree tree = parser.init(); // begin parsing at init rule


        // Create a generic parse tree walker that can trigger callbacks
        ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(new ShortToUnicodeString(), tree);

        System.out.println(); // print a \n after translation

    }

    public int add(int a, int b) {
        System.out.println("running add");
        return a + b;
    }
}
