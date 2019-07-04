package ua.k.co.play.rah2j;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoadCSV {
    public static class Loader extends CSVBaseListener {
        public static final String EMPTY = "";
        /** Load a list of row maps that map field name to value */
        List<Map<String,String>> rows = new ArrayList<>();
        /** List of column names */
        List<String> header;
        /** Build up a list of fields in current row */
        List<String> currentRowFieldValues;

        public void exitHdr(CSVParser.HdrContext ctx) {
            header = new ArrayList<>();
            header.addAll(currentRowFieldValues);
        }

        public void enterRow(CSVParser.RowContext ctx) {
            currentRowFieldValues = new ArrayList<>();
        }

        public void exitRow(CSVParser.RowContext ctx) {
            // If this is the header row, do nothing
            // if ( ctx.parent instanceof CSVParser.HdrContext ) return; OR:
            if ( ctx.getParent().getRuleIndex() == CSVParser.RULE_hdr ) return;
            // It's a data row
            Map<String, String> m = new LinkedHashMap<>();
            int i = 0;
            for (String v : currentRowFieldValues) {
                m.put(header.get(i), v);
                i++;
            }
            rows.add(m);
        }

        public void exitString(CSVParser.StringContext ctx) {
            currentRowFieldValues.add(ctx.STRING().getText());
        }

        public void exitText(CSVParser.TextContext ctx) {
            currentRowFieldValues.add(ctx.TEXT().getText());
        }

        public void exitEmpty(CSVParser.EmptyContext ctx) {
            currentRowFieldValues.add(EMPTY);
        }
    }

    public static void main(String[] args) throws Exception {
        CSVLexer lexer = new CSVLexer(CharStreams.fromString(FileReader.read("t.csv")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSVParser parser = new CSVParser(tokens);
        parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
        ParseTree tree = parser.file();

        ParseTreeWalker walker = new ParseTreeWalker();
        Loader loader = new Loader();
        walker.walk(loader, tree);
        System.out.println(loader.rows);
    }
}
