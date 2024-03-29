package ua.k.co.play.rah2j;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.ST;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class CallGraph {
    /**
     * A graph model of the output. Tracks call from one function to
     * another by mapping function to list of called functions. E.g.,
     * f -> [g, h]
     * Can dump DOT two ways (StringBuilder and ST). Sample output:
     * digraph G {
     * ... setup ...
     * f -> g;
     * g -> f;
     * g -> h;
     * h -> h;
     * }
     */
    static class Graph {
        // I'm using org.antlr.v4.runtime.misc: OrderedHashSet, MultiMap
        Set<String> nodes = new OrderedHashSet<String>(); // list of functions
        MultiMap<String, String> edges =                  // caller->callee
                new MultiMap<String, String>();

        public void edge(String source, String target) {
            edges.map(source, target);
        }

        public String toString() {
            return "edges: " + edges.toString() + ", functions: " + nodes;
        }

        public String toDOT() {
            StringBuilder buf = new StringBuilder();
            buf.append("digraph G {\n");
            buf.append("  ranksep=.25;\n");
            buf.append("  edge [arrowsize=.5]\n");
            buf.append("  node [shape=circle, fontname=\"ArialNarrow\",\n");
            buf.append("        fontsize=12, fixedsize=true, height=.45];\n");
            buf.append("  ");
            for (String node : nodes) { // print all nodes first
                buf.append(node);
                buf.append("; ");
            }
            buf.append("\n");
            for (String src : edges.keySet()) {
                for (String trg : edges.get(src)) {
                    buf.append("  ");
                    buf.append(src);
                    buf.append(" -> ");
                    buf.append(trg);
                    buf.append(";\n");
                }
            }
            buf.append("}\n");
            return buf.toString();
        }

        /**
         * Fill StringTemplate:
         * digraph G {
         * rankdir=LR;
         * <edgePairs:{edge| <edge.a> -> <edge.b>;}; separator="\n">
         * <childless:{f | <f>;}; separator="\n">
         * }
         * <p>
         * Just as an example. Much cleaner than buf.append method
         */
        public ST toST() {
            ST st = new ST(
                    "digraph G {\n" +
                            "  ranksep=.25; \n" +
                            "  edge [arrowsize=.5]\n" +
                            "  node [shape=circle, fontname=\"ArialNarrow\",\n" +
                            "        fontsize=12, fixedsize=true, height=.45];\n" +
                            "  <funcs:{f | <f>; }>\n" +
                            "  <edgePairs:{edge| <edge.a> -> <edge.b>;}; separator=\"\\n\">\n" +
                            "}\n"
            );
            st.add("edgePairs", edges.getPairs());
            st.add("funcs", nodes);
            return st;
        }
    }

    static class FunctionListener extends CymbolBaseListener {
        Graph graph = new Graph();
        String currentFunctionName = null;

        public void enterFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
            currentFunctionName = ctx.ID().getText();
            graph.nodes.add(currentFunctionName);
        }

        public void exitCall(CymbolParser.CallContext ctx) {
            String funcName = ctx.ID().getText();
            // map current function to the callee
            graph.edge(currentFunctionName, funcName);
        }
    }

    public static void main(String[] args) throws Exception {
        CymbolLexer lexer = new CymbolLexer(CharStreams.fromString(FileReader.read("t.cymbol")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CymbolParser parser = new CymbolParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file();
        // show tree in text form
//        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionListener collector = new FunctionListener();
        walker.walk(collector, tree);
        System.out.println(collector.graph.toString());
        System.out.println(collector.graph.toDOT());

        // Here's another example that uses StringTemplate to generate output
//        System.out.println(collector.graph.toST().render());
    }
}
