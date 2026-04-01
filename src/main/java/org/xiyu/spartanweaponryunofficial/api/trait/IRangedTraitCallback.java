package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

/**
 * Callback class for Ranged Weapon Traits; Implement this in your weapon trait class to implement custom behavior for your weapon
 *
 * @author ObliviousSpartan
 */
public interface IRangedTraitCallback {
    /**
     * Modifies the draw time for the Longbow. Return the baseDraw value to do nothing with it.
     *
     * @param baseDraw The draw ticks (so far)
     */
    default float modifyLongbowDrawTime(WeaponMaterial material, float baseDraw) {
        return baseDraw;
    }

    /**
     * Modifies the load time for the Heavy Crossbow. Return the baseLoad value to do nothing with it.
     *
     * @param baseLoad The load ticks (so far)
     */
    default int modifyHeavyCrossbowLoadTime(WeaponMaterial material, int baseLoad) {
        return baseLoad;
    }

    /**
     * Modifies the aim time for the Heavy Crossbow. Return the baseAim value to do nothing with it.
     *
     * @param baseAim  The aim ticks (so far)
     */
    default int modifyHeavyCrossbowAimTime(WeaponMaterial material, int baseAim) {
        return baseAim;
    }

    /**
     * Adjusts the projectile entity before it is spawned in the world
     *
     */
    default void onProjectileSpawn(WeaponMaterial material, AbstractArrow projectile) {
    }
}
