package ua.k.co.play.rah2j.validator.symbols;
/**
 * Represents a variable definition (name,type) in symbol table
 */
public class VariableSymbol extends Symbol {
    public VariableSymbol(String name, Type type) {
        super(name, type);
    }
}
