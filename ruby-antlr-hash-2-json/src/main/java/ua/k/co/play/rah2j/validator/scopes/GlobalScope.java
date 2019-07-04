package ua.k.co.play.rah2j.validator.scopes;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class GlobalScope extends BaseScope {
    public GlobalScope(Scope enclosingScope) {
        super(enclosingScope);
    }

    public String getScopeName() {
        return "globals";
    }
}
