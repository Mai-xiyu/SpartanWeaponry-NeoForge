package org.xiyu.spartanweaponryunofficial.item.crafting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public interface ITagCookingRecipe extends Recipe<SingleRecipeInput>
{
	public CookingBookCategory getCategory();
	public Ingredient getInputIngredient();
	public Ingredient getResultIngredient();
	public TagKey<Item> getResultTag();
	public float getExperienceDrop();
	public int getCookTime();
}