package org.xiyu.spartanweaponryunofficial.data.recipe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * Temporary compatibility shim for 26.1 recipe API migration.
 * TODO: restore full custom tagged cooking recipe output.
 */
public class TagCookingRecipeBuilder {
    private TagCookingRecipeBuilder() {
    }

    public static TagCookingRecipeBuilder smelting(ImmutableMap<String, Item> ingredientMapIn, RecipeCategory recipeCategoryIn, TagKey<Item> resultTagIn, float experienceIn, int cookingTimeIn) {
        return new TagCookingRecipeBuilder();
    }

    public static TagCookingRecipeBuilder blasting(ImmutableMap<String, Item> ingredientMapIn, RecipeCategory recipeCategoryIn, TagKey<Item> resultTagIn, float experienceIn, int cookingTimeIn) {
        return new TagCookingRecipeBuilder();
    }

    public @NotNull TagCookingRecipeBuilder unlockedBy(@NotNull String nameIn, @NotNull Criterion<?> criterionIn) {
        return this;
    }

    public @NotNull TagCookingRecipeBuilder group(String groupIn) {
        return this;
    }

    public TagCookingRecipeBuilder addDisabledTypes(String... disableTypes) {
        return this;
    }

    public @NotNull Item getResult() {
        return Items.BARRIER;
    }

    public void save(RecipeOutput output, Identifier idIn) {
        // Intentionally no-op until full datagen migration is restored.
    }
}