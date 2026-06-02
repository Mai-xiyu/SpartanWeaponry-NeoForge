package org.xiyu.spartanweaponryunofficial.data.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.xiyu.spartanweaponryunofficial.item.crafting.TippedProjectileBaseRecipe;

public class TippedProjectileRecipeBuilder {
    private final Item result;
    private Item input;

    private TippedProjectileRecipeBuilder(ItemLike resultIn) {
        this.result = resultIn.asItem();
    }

    public static TippedProjectileRecipeBuilder tipped(ItemLike resultIn) {
        return new TippedProjectileRecipeBuilder(resultIn);
    }

    public TippedProjectileRecipeBuilder input(ItemLike inputIn) {
        if (this.input != null)
            throw new IllegalStateException(
                    "Recipe Input already defined as '"
                            + BuiltInRegistries.ITEM.getKey(this.input)
                            + "', but is attempted being overwritten to '"
                            + BuiltInRegistries.ITEM.getKey(inputIn.asItem())
                            + "'");
        this.input = inputIn.asItem();
        return this;
    }

    public void save(RecipeOutput output) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);
        TippedProjectileBaseRecipe recipe = new TippedProjectileBaseRecipe(this.input, this.result);
        output.accept(id, recipe, null);
    }
}
