package org.xiyu.spartanweaponryunofficial.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;

public class TagBlastingRecipe extends BlastingRecipe implements ITagCookingRecipe {
    protected final Ingredient result;
    protected final TagKey<Item> resultTag;

    public TagBlastingRecipe(String groupIn,
                             CookingBookCategory categoryIn, Ingredient inputIngredientIn, TagKey<Item> resultTagIn, float experienceIn,
                             int cookTimeIn) {
        super(groupIn, categoryIn, inputIngredientIn, ItemStack.EMPTY, experienceIn, cookTimeIn);
        this.resultTag = resultTagIn;
        this.result = Ingredient.of(resultTagIn);
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.result.getItems()[0];
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput input, HolderLookup.@NotNull Provider provider) {
        return this.getResultItem(provider).copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TAGGED_BLASTING.get();
    }

    @Override
    public CookingBookCategory getCategory() {
        return this.category();
    }

    @Override
    public Ingredient getInputIngredient() {
        return this.ingredient;
    }

    @Override
    public Ingredient getResultIngredient() {
        return this.result;
    }

    @Override
    public TagKey<Item> getResultTag() {
        return this.resultTag;
    }

    @Override
    public int getCookTime() {
        return this.getCookingTime();
    }

    @Override
    public float getExperienceDrop() {
        return this.getExperience();
    }
}
