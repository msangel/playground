package ua.k.co.play.rah2j.validator;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import ua.k.co.play.rah2j.CymbolBaseListener;
import ua.k.co.play.rah2j.CymbolParser;
import ua.k.co.play.rah2j.validator.scopes.GlobalScope;
import ua.k.co.play.rah2j.validator.scopes.LocalScope;
import ua.k.co.play.rah2j.validator.scopes.Scope;
import ua.k.co.play.rah2j.validator.symbols.FunctionSymbol;
import ua.k.co.play.rah2j.validator.symbols.Symbol;
import ua.k.co.play.rah2j.validator.symbols.VariableSymbol;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class DefPhase extends CymbolBaseListener {
    public ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
    public GlobalScope globals;
    public Scope currentScope; // define symbols in this scope

    public void enterFile(CymbolParser.FileContext ctx) {
        globals = new GlobalScope(null);
        currentScope = globals;
    }

    public void exitFile(CymbolParser.FileContext ctx) {
        System.out.println(globals);
    }

    public void enterFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
        String name = ctx.ID().getText();
        int typeTokenType = ctx.type().start.getType();
        Symbol.Type type = CheckSymbols.getType(typeTokenType);

        // push new scope by making new one that points to enclosing scope
        FunctionSymbol function = new FunctionSymbol(name, type, currentScope);
        currentScope.define(function); // Define function in current scope
        saveScope(ctx, function);      // Push: set function's parent to current
        currentScope = function;       // Current scope is now function scope
    }

    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }

    public void exitFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
        System.out.println(currentScope);
        currentScope = currentScope.getEnclosingScope(); // pop scope
    }

    public void enterBlock(CymbolParser.BlockContext ctx) {
        // push new local scope
        currentScope = new LocalScope(currentScope);
        saveScope(ctx, currentScope);
    }

    public void exitBlock(CymbolParser.BlockContext ctx) {
        System.out.println(currentScope);
        currentScope = currentScope.getEnclosingScope(); // pop scope
    }

    public void exitFormalParameter(CymbolParser.FormalParameterContext ctx) {
        defineVar(ctx.type(), ctx.ID().getSymbol());
    }

    public void exitVarDecl(CymbolParser.VarDeclContext ctx) {
        defineVar(ctx.type(), ctx.ID().getSymbol());
    }

    void defineVar(CymbolParser.TypeContext typeCtx, Token nameToken) {
        int typeTokenType = typeCtx.start.getType();
        Symbol.Type type = CheckSymbols.getType(typeTokenType);
        VariableSymbol var = new VariableSymbol(nameToken.getText(), type);
        currentScope.define(var); // Define symbol in current scope
    }
}
