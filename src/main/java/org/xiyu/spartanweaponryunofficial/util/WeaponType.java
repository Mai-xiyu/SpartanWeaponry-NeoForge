package org.xiyu.spartanweaponryunofficial.util;

import java.util.function.Predicate;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

public enum WeaponType {
    MELEE(WeaponTrait::isMeleeTrait),
    RANGED(WeaponTrait::isRangedTrait),
    THROWING(WeaponTrait::isThrowingTrait);

    private final Predicate<WeaponTrait> filter;

    WeaponType(Predicate<WeaponTrait> traitFilter) {
        this.filter = traitFilter;
    }

    public Predicate<WeaponTrait> getTraitFilter() {
        return this.filter;
    }
}
