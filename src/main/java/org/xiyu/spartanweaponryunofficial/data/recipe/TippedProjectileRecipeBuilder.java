package org.xiyu.spartanweaponryunofficial.data.recipe;

import org.xiyu.spartanweaponryunofficial.item.crafting.TippedProjectileBaseRecipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class TippedProjectileRecipeBuilder 
{
	private final Item result;
	private Item input;
	
	private TippedProjectileRecipeBuilder(ItemLike resultIn)
	{
		result = resultIn.asItem();
	}
	
	public static TippedProjectileRecipeBuilder tipped(ItemLike resultIn)
	{
		return new TippedProjectileRecipeBuilder(resultIn);
	}
	
	public TippedProjectileRecipeBuilder input(ItemLike inputIn)
	{
		if(input != null)
			throw new IllegalStateException("Recipe Input already defined as '" + BuiltInRegistries.ITEM.getKey(input) + "', but is attempted being overwritten to '" + BuiltInRegistries.ITEM.getKey(inputIn.asItem()) + "'");
		input = inputIn.asItem();
		return this;
	}
	
	public void save(RecipeOutput output)
	{
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result);
		TippedProjectileBaseRecipe recipe = new TippedProjectileBaseRecipe(input, result);
		output.accept(id, recipe, null);
	}
}
