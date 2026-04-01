package org.xiyu.spartanweaponryunofficial.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.ItemLike;

/**
 * Temporary compatibility shim for 26.1 recipe API migration.
 * TODO: restore full tipped projectile recipe generation.
 */
public class TippedProjectileRecipeBuilder {
    private TippedProjectileRecipeBuilder(ItemLike resultIn) {
    }

    public static TippedProjectileRecipeBuilder tipped(ItemLike resultIn) {
        return new TippedProjectileRecipeBuilder(resultIn);
    }

    public TippedProjectileRecipeBuilder input(ItemLike inputIn) {
        return this;
    }

    public void save(RecipeOutput output) {
        // Intentionally no-op until full datagen migration is restored.
    }
}