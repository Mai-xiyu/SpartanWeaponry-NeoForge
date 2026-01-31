package org.xiyu.spartanweaponryunofficial.item.crafting;

import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class TagSmeltingRecipe extends SmeltingRecipe implements ITagCookingRecipe
{
	protected final Ingredient result;
	protected final TagKey<Item> resultTag;
	
	public TagSmeltingRecipe(String groupIn,
			CookingBookCategory categoryIn, Ingredient inputIngredientIn, TagKey<Item> resultTagIn, float experienceIn,
			int cookTimeIn) 
	{
		super(groupIn, categoryIn, inputIngredientIn, ItemStack.EMPTY, experienceIn, cookTimeIn);
		resultTag = resultTagIn;
		result = Ingredient.of(resultTagIn);
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) 
	{
		return result.getItems()[0];
	}
	
	@Override
	public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider)
	{
		return getResultItem(provider).copy();
	}

	@Override
	public RecipeSerializer<?> getSerializer() 
	{
		return ModRecipeSerializers.TAGGED_SMELTING.get();
	}

	@Override
	public CookingBookCategory getCategory() 
	{
		return category();
	}

	@Override
	public Ingredient getInputIngredient() 
	{
		return ingredient;
	}

	@Override
	public Ingredient getResultIngredient() 
	{
		return result;
	}

	@Override
	public TagKey<Item> getResultTag()
	{
		return resultTag;
	}

	@Override
	public int getCookTime() 
	{
		return getCookingTime();
	}

	@Override
	public float getExperienceDrop()
	{
		return getExperience();
	}
}
