package ua.k.co.play.rah2j.validator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ua.k.co.play.rah2j.CymbolLexer;
import ua.k.co.play.rah2j.CymbolParser;
import ua.k.co.play.rah2j.FileReader;
import ua.k.co.play.rah2j.validator.symbols.Symbol;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
@SuppressWarnings("Duplicates")
public class CheckSymbols {
    public static Symbol.Type getType(int tokenType) {
        switch (tokenType) {
            case CymbolParser.K_VOID:
                return Symbol.Type.tVOID;
            case CymbolParser.K_INT:
                return Symbol.Type.tINT;
            case CymbolParser.K_FLOAT:
                return Symbol.Type.tFLOAT;
        }
        return Symbol.Type.tINVALID;
    }

    public static void error(Token t, String msg) {
        System.err.printf("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(),
                msg);
    }

    public void process(String[] args) throws Exception {
        CymbolLexer lexer = new CymbolLexer(CharStreams.fromString(FileReader.read("vars.cymbol")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CymbolParser parser = new CymbolParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file();
        // show tree in text form
//        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        DefPhase def = new DefPhase();
        walker.walk(def, tree);
        // create next phase and feed symbol table info from def to ref phase
        RefPhase ref = new RefPhase(def.globals, def.scopes);
        walker.walk(ref, tree);
    }

    public static void main(String[] args) throws Exception {
        new CheckSymbols().process(args);
    }
}
