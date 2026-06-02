package org.xiyu.spartanweaponryunofficial.command;

import java.util.function.Predicate;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;

public class OilInput implements Predicate<OilEffect> {
    private final OilEffect oilEffect;

    public OilInput(OilEffect oilEffectIn) {
        this.oilEffect = oilEffectIn;
    }

    public OilEffect getEffect() {
        return this.oilEffect;
    }

    @Override
    public boolean test(OilEffect t) {
        return this.oilEffect == t;
    }
}
