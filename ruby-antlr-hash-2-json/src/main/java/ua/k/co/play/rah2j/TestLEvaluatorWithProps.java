package ua.k.co.play.rah2j;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by vasyl.khrystiuk on 07/03/2019.
 */
@SuppressWarnings("Duplicates")
public class TestLEvaluatorWithProps {
    /**
     * Sample "calculator" using property of nodes
     */
    public static class EvaluatorWithProps extends LExprBaseListener {
        /**
         * maps nodes to integers with Map<ParseTree,Integer>
         */
        ParseTreeProperty<Integer> values = new ParseTreeProperty<>();

        /**
         * Need to pass e's value out of rule s : e ;
         */
        public void exitS(LExprParser.SContext ctx) {
            setValue(ctx, getValue(ctx.e())); // like: int s() { return e(); }
        }

        public void exitMult(LExprParser.MultContext ctx) {
            int left = getValue(ctx.e(0));  // e '*' e   # Mult
            int right = getValue(ctx.e(1));
            setValue(ctx, left * right);
        }

        public void exitAdd(LExprParser.AddContext ctx) {
            int left = getValue(ctx.e(0)); // e '+' e   # Add
            int right = getValue(ctx.e(1));
            setValue(ctx, left + right);
        }

        public void exitInt(LExprParser.IntContext ctx) {
            String intText = ctx.INT().getText(); // INT   # Int
            setValue(ctx, Integer.valueOf(intText));
        }

        public void setValue(ParseTree node, int value) {
            values.put(node, value);
        }

        public int getValue(ParseTree node) {
            return values.get(node);
        }
    }

    public static void main(String[] args) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(FileReader.read("simple.expression"));
        LExprLexer lexer = new LExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LExprParser parser = new LExprParser(tokens);
        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
        ParseTree tree = parser.s(); // parse
        // show tree in text form
        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        EvaluatorWithProps evalProp = new EvaluatorWithProps();
        walker.walk(evalProp, tree);
        System.out.println("properties result = " + evalProp.getValue(tree));
    }
}
