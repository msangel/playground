package ua.k.co.play.rah2j.validator.symbols;

import ua.k.co.play.rah2j.validator.scopes.Scope;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class Symbol {
    public enum Type {tINVALID, tVOID, tINT, tFLOAT}

    public String name;      // All symbols at least have a name
    public Type type;
    public Scope scope;      // All symbols know what scope contains them.

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this(name);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        if (type != Type.tINVALID) return '<' + getName() + ":" + type + '>';
        return getName();
    }
}
