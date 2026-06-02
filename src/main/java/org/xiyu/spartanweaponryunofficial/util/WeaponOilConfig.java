package org.xiyu.spartanweaponryunofficial.util;

/** Centralized config gate for the Weapon Oil mechanic. */
public final class WeaponOilConfig {
    private WeaponOilConfig() {}

    public static boolean isEnabled() {
        return Config.INSTANCE.enableWeaponOil.get();
    }

    public static boolean areRecipesEnabled() {
        return isEnabled() && !Config.INSTANCE.disableOilRecipes.get();
    }
}
