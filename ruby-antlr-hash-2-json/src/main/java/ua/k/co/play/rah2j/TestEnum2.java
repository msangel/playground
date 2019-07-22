package ua.k.co.play.rah2j;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by vasyl.khrystiuk on 07/22/2019.
 */
public class TestEnum2 {
    public static void main(String[] args) throws Exception {
        int i = 0;
//        Enum2Lexer.java5 = false; // in lexer not parser
//        if (args.length > 0 && args[i].equals("-java5")) {
//            Enum2Lexer.java5 = true;
//            i++;
//        }
        Enum2Lexer.java5 = true;
        String inputFile = null;
        if (args.length > i) inputFile = args[i];
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        Enum2Lexer lexer = new Enum2Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Enum2Parser parser = new Enum2Parser(tokens);
        parser.prog();
    }
}
