package ua.k.co.play.rah2j.validator;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import ua.k.co.play.rah2j.CymbolBaseListener;
import ua.k.co.play.rah2j.CymbolParser;
import ua.k.co.play.rah2j.validator.scopes.GlobalScope;
import ua.k.co.play.rah2j.validator.scopes.Scope;
import ua.k.co.play.rah2j.validator.symbols.FunctionSymbol;
import ua.k.co.play.rah2j.validator.symbols.Symbol;
import ua.k.co.play.rah2j.validator.symbols.VariableSymbol;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class RefPhase extends CymbolBaseListener {
    ParseTreeProperty<Scope> scopes;
    GlobalScope globals;
    Scope currentScope; // resolve symbols starting in this scope

    public RefPhase(GlobalScope globals, ParseTreeProperty<Scope> scopes) {
        this.scopes = scopes;
        this.globals = globals;
    }

    public void enterFile(CymbolParser.FileContext ctx) {
        currentScope = globals;
    }

    public void enterFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
        currentScope = scopes.get(ctx);
    }

    public void exitFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    public void enterBlock(CymbolParser.BlockContext ctx) {
        currentScope = scopes.get(ctx);
    }

    public void exitBlock(CymbolParser.BlockContext ctx) {
        currentScope = currentScope.getEnclosingScope();
    }

    public void exitVar(CymbolParser.VarContext ctx) {
        String name = ctx.ID().getSymbol().getText();
        Symbol var = currentScope.resolve(name);
        if (var == null) {
            CheckSymbols.error(ctx.ID().getSymbol(), "no such variable: " + name);
        }
        if (var instanceof FunctionSymbol) {
            CheckSymbols.error(ctx.ID().getSymbol(), name + " is not a variable");
        }
    }

    public void exitCall(CymbolParser.CallContext ctx) {
        // can only handle f(...) not expr(...)
        String funcName = ctx.ID().getText();
        Symbol meth = currentScope.resolve(funcName);
        if (meth == null) {
            CheckSymbols.error(ctx.ID().getSymbol(), "no such function: " + funcName);
        }
        if (meth instanceof VariableSymbol) {
            CheckSymbols.error(ctx.ID().getSymbol(), funcName + " is not a function");
        }
    }
}
