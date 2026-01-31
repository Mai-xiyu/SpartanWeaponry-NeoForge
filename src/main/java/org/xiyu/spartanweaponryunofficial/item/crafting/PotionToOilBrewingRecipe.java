package org.xiyu.spartanweaponryunofficial.item.crafting;

import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class PotionToOilBrewingRecipe implements IBrewingRecipe 
{
	@Override
	public boolean isInput(ItemStack input) 
	{
		Potion inputPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
				.potion().map(Holder::value).orElse(null);
		return inputPotion != null && !Config.INSTANCE.disableOilRecipes.get() && input.is(Items.POTION) && OilHelper.isValidPotion(inputPotion);
	}
	
	@Override
	public boolean isIngredient(ItemStack ingredient) 
	{
		return !Config.INSTANCE.disableOilRecipes.get() && ingredient.is(ModItems.GREASE_BALL.get());
	}
	
	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		if(isInput(input) && isIngredient(ingredient))
		{
			Potion inputPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
					.potion().map(Holder::value).orElse(null);
			if(inputPotion != null)
				return OilHelper.makePotionOilStack(inputPotion);
		}
		
		return ItemStack.EMPTY;
	}
}
