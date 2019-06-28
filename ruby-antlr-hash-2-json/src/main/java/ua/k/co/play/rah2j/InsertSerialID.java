package ua.k.co.play.rah2j;

/**
 * Created by vasyl.khrystiuk on 06/27/2019.
 */

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.InputStream;

@SuppressWarnings("ALL")
public class InsertSerialID {

    public static class InsertSerialIDListener extends JavaBaseListener {
        TokenStreamRewriter rewriter;

        public InsertSerialIDListener(TokenStream tokens) {
            rewriter = new TokenStreamRewriter(tokens);
        }

        @Override
        public void enterClassBody(JavaParser.ClassBodyContext ctx) {
            String field = "\n\tpublic static final long serialVersionUID = 1L;";
            rewriter.insertAfter(ctx.start, field);
        }
    }

    public static void main(String[] args) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(FileReader.read("Demo.java"));

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit(); // parse

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        InsertSerialIDListener extractor = new InsertSerialIDListener(tokens);
        walker.walk(extractor, tree); // initiate walk of tree with listener

        // print back ALTERED stream
        System.out.println(extractor.rewriter.getText());
    }
}
