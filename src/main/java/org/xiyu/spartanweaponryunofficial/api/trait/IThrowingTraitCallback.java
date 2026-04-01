package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

/**
 * Callback class for Throwing Weapon Traits; Implement this in your weapon trait class to implement custom behavior for your weapon
 *
 * @author ObliviousSpartan
 */
public interface IThrowingTraitCallback {
    /**
     * Modifies the draw time for the Throwing Weapon. Return the baseCharge value to do nothing with it.
     *
     * @param baseCharge The draw ticks (so far)
     */
    default int modifyThrowingChargeTime(WeaponMaterial material, int baseCharge) {
        return baseCharge;
    }

    /**
     * Adjusts the projectile entity before it is spawned in the world
     *
     */
    default void onThrowingProjectileSpawn(WeaponMaterial material, AbstractArrow projectile) {
    }
}
