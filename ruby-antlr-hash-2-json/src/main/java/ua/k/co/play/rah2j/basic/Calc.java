package ua.k.co.play.rah2j.basic;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ua.k.co.play.rah2j.LabeledExprBaseVisitor;
import ua.k.co.play.rah2j.LabeledExprLexer;
import ua.k.co.play.rah2j.LabeledExprParser;

import java.util.HashMap;
import java.util.Map;

public class Calc {
    public static void main(String[] args) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(FileReader.read("t.expr"));
        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser = new LabeledExprParser(tokens);
        ParseTree tree = parser.prog(); // parse

        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }
    public static class EvalVisitor extends LabeledExprBaseVisitor<Integer> {
        /** "memory" for our calculator; variable/value pairs go here */
        Map<String, Integer> memory = new HashMap<>();

        /** ID '=' expr NEWLINE */
        @Override
        public Integer visitAssign(LabeledExprParser.AssignContext ctx) {
            String id = ctx.ID().getText();  // id is left-hand side of '='
            int value = visit(ctx.expr());   // compute value of expression on right
            memory.put(id, value);           // store it in our memory
            return value;
        }

        /** expr NEWLINE */
        @Override
        public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
            Integer value = visit(ctx.expr()); // evaluate the expr child
            System.out.println(value);         // print the result
            return 0;                          // return dummy value
        }

        /** INT */
        @Override
        public Integer visitInt(LabeledExprParser.IntContext ctx) {
            return Integer.valueOf(ctx.INT().getText());
        }

        /** ID */
        @Override
        public Integer visitId(LabeledExprParser.IdContext ctx) {
            String id = ctx.ID().getText();
            if ( memory.containsKey(id) ) return memory.get(id);
            return 0;
        }

        /** expr op=('*'|'/') expr */
        @Override
        public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
            int left = visit(ctx.expr(0));  // get value of left subexpression
            int right = visit(ctx.expr(1)); // get value of right subexpression
            if ( ctx.op.getType() == LabeledExprParser.MUL ) return left * right;
            return left / right; // must be DIV
        }

        /** expr op=('+'|'-') expr */
        @Override
        public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
            int left = visit(ctx.expr(0));  // get value of left subexpression
            int right = visit(ctx.expr(1)); // get value of right subexpression
            if ( ctx.op.getType() == LabeledExprParser.ADD ) return left + right;
            return left - right; // must be SUB
        }

        /** '(' expr ')' */
        @Override
        public Integer visitParens(LabeledExprParser.ParensContext ctx) {
            return visit(ctx.expr()); // return child expr's value
        }

        @Override
        public Integer visitClear(LabeledExprParser.ClearContext ctx) {
            memory.clear();
            System.out.println("memory cleared");
            return 0;
        }
    }
}
