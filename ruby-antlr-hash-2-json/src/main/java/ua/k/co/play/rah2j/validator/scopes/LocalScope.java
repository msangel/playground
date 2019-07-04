package ua.k.co.play.rah2j.validator.scopes;

/**
 * Created by vasyl.khrystiuk on 07/04/2019.
 */
public class LocalScope extends BaseScope {
    public LocalScope(Scope parent) {
        super(parent);
    }

    public String getScopeName() {
        return "locals";
    }
}
