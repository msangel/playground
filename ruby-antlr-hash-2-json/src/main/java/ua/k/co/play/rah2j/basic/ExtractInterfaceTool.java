package ua.k.co.play.rah2j.basic;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ua.k.co.play.rah2j.JavaBaseListener;
import ua.k.co.play.rah2j.JavaLexer;
import ua.k.co.play.rah2j.JavaParser;

@SuppressWarnings("Duplicates")
public class ExtractInterfaceTool {
    public static void main(String[] args) throws Exception {

        ANTLRInputStream input = new ANTLRInputStream(FileReader.read("Demo.java"));

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit(); // parse

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        ExtractInterfaceListener extractor = new ExtractInterfaceListener(parser);
        walker.walk(extractor, tree); // initiate walk of tree with listener
    }

    public static class ExtractInterfaceListener extends JavaBaseListener {
        JavaParser parser;
        public ExtractInterfaceListener(JavaParser parser) {this.parser = parser;}
        /** Listen to matches of classDeclaration */
        @Override
        public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx){
            System.out.println("interface I"+ctx.Identifier()+" {");
        }
        @Override
        public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
            System.out.println("}");
        }

        /** Listen to matches of methodDeclaration */
        @Override
        public void enterMethodDeclaration(
                JavaParser.MethodDeclarationContext ctx
        )
        {
            // need parser to get tokens
            TokenStream tokens = parser.getTokenStream();
            String type = "void";
            if ( ctx.type()!=null ) {
                type = tokens.getText(ctx.type());
            }
            String args = tokens.getText(ctx.formalParameters());
            System.out.println("\t"+type+" "+ctx.Identifier()+args+";");
        }

        @Override
        public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
            System.out.println(parser.getTokenStream().getText(ctx));
        }
    }

}
