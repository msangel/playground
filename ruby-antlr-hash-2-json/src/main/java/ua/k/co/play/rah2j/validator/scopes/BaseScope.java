package ua.k.co.play.rah2j.validator.scopes;

import ua.k.co.play.rah2j.validator.symbols.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public abstract class BaseScope implements Scope {
    Scope enclosingScope; // null if global (outermost) scope
    Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();

    public BaseScope(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    public Symbol resolve(String name) {
        Symbol s = symbols.get(name);
        if (s != null) return s;
        // if not here, check any enclosing scope
        if (enclosingScope != null) return enclosingScope.resolve(name);
        return null; // not found
    }

    public void define(Symbol sym) {
        symbols.put(sym.name, sym);
        sym.scope = this; // track the scope in each symbol
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public String toString() {
        return getScopeName() + ":" + symbols.keySet().toString();
    }
}
