package org.xiyu.spartanweaponryunofficial.item.crafting;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;

public class TagBlastingRecipe extends BlastingRecipe implements ITagCookingRecipe {
    protected final TagKey<Item> resultTag;

    public TagBlastingRecipe(String groupIn, CookingBookCategory categoryIn,
                             Ingredient inputIngredientIn, TagKey<Item> resultTagIn,
                             float experienceIn, int cookTimeIn) {
        super(new Recipe.CommonInfo(true),
              new AbstractCookingRecipe.CookingBookInfo(categoryIn, groupIn),
              inputIngredientIn, new ItemStackTemplate(Items.BARRIER), experienceIn, cookTimeIn);
        this.resultTag = resultTagIn;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput input) {
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.resultTag)) {
            return new ItemStack(holder);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<BlastingRecipe> getSerializer() {
        return (RecipeSerializer<BlastingRecipe>) (RecipeSerializer<?>) ModRecipeSerializers.TAGGED_BLASTING.get();
    }

    @Override
    public TagKey<Item> getResultTag() {
        return this.resultTag;
    }
}