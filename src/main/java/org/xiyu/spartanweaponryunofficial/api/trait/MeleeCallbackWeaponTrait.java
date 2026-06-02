package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.Optional;

/**
 * Default Weapon Property class with melee callback methods. Extend this if you want a melee weapon
 * trait with custom behaviour.
 *
 * @author ObliviousSpartan
 */
public class MeleeCallbackWeaponTrait extends WeaponTrait implements IMeleeTraitCallback {
    public MeleeCallbackWeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
        this.isMelee = true;
    }

    @Override
    public Optional<IGenericTraitCallback> getGenericCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IMeleeTraitCallback> getMeleeCallback() {
        return Optional.of(this);
    }
}
