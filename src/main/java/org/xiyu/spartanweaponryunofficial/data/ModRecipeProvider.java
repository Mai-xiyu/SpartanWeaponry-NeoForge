package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Temporary 26.1-compatible recipe provider runner.
 * TODO: restore full custom recipe generation after data API migration is complete.
 */
public class ModRecipeProvider extends RecipeProvider.Runner {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            protected void buildRecipes() {
                // Intentionally left blank until full 26.1 datagen migration.
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "Spartan Weaponry Recipes";
    }
}