package ua.k.co.play.rah2j;


/*
{
    "description" : "An imaginary server config file",
    "logs" : {"level":"verbose", "dir":"/var/log"},
    "host" : "antlr.org",
    "admin": ["parrt", "tombu"]
    "aliases": []
}

to

<description>An imaginary server config file</description>
<logs>
    <level>verbose</level>
    <dir>/var/log</dir>
</logs>
<host>antlr.org</host>
<admin>
    <element>parrt</element> <!-- inexact -->
    <element>tombu</element>
</admin>
<aliases></aliases>
 */

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

public class JSON2XML {
    public static class XMLEmitter extends JSONBaseListener {
        ParseTreeProperty<String> xml = new ParseTreeProperty<String>();
        String getXML(ParseTree ctx) { return xml.get(ctx); }
        void setXML(ParseTree ctx, String s) { xml.put(ctx, s); }

        public void exitJson(JSONParser.JsonContext ctx) {
            setXML(ctx, getXML(ctx.getChild(0)));
        }

        public void exitAnObject(JSONParser.AnObjectContext ctx) {
            StringBuilder buf = new StringBuilder();
            buf.append("\n");
            for (JSONParser.PairContext pctx : ctx.pair()) {
                buf.append(getXML(pctx));
            }
            setXML(ctx, buf.toString());
        }
        public void exitEmptyObject(JSONParser.EmptyObjectContext ctx) {
            setXML(ctx, "");
        }

        public void exitArrayOfValues(JSONParser.ArrayOfValuesContext ctx) {
            StringBuilder buf = new StringBuilder();
            buf.append("\n");
            for (JSONParser.ValueContext vctx : ctx.value()) {
                buf.append("<element>"); // conjure up element for valid XML
                buf.append(getXML(vctx));
                buf.append("</element>");
                buf.append("\n");
            }
            setXML(ctx, buf.toString());
        }

        public void exitEmptyArray(JSONParser.EmptyArrayContext ctx) {
            setXML(ctx, "");
        }

        public void exitPair(JSONParser.PairContext ctx) {
            String tag = stripQuotes(ctx.STRING().getText());
            JSONParser.ValueContext vctx = ctx.value();
            String x = String.format("<%s>%s</%s>\n", tag, getXML(vctx), tag);
            setXML(ctx, x);
        }

        public void exitObjectValue(JSONParser.ObjectValueContext ctx) {
            // analogous to String value() {return object();}
            setXML(ctx, getXML(ctx.object()));
        }

        public void exitArrayValue(JSONParser.ArrayValueContext ctx) {
            setXML(ctx, getXML(ctx.array())); // String value() {return array();}
        }

        public void exitAtom(JSONParser.AtomContext ctx) {
            setXML(ctx, ctx.getText());
        }

        public void exitString(JSONParser.StringContext ctx) {
            setXML(ctx, stripQuotes(ctx.getText()));
        }

        public static String stripQuotes(String s) {
            if ( s==null || s.charAt(0)!='"' ) return s;
            return s.substring(1, s.length() - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        JSONLexer lexer = new JSONLexer(CharStreams.fromString(FileReader.read("t.json")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.json();
        // show tree in text form
//        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        XMLEmitter converter = new XMLEmitter();
        walker.walk(converter, tree);
        System.out.println(converter.getXML(tree));
    }
}
