package org.xiyu.spartanweaponryunofficial.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.init.ModOilRecipes;
import org.xiyu.spartanweaponryunofficial.item.crafting.OilBrewingRecipe;
import org.xiyu.spartanweaponryunofficial.item.crafting.OilBrewingRecipe.OilMix;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class OilBrewingRecipeMaker 
{
	public static List<IJeiBrewingRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactoryIn)
	{
		List<IJeiBrewingRecipe> recipes = new ArrayList<>();
		
		if(!Config.INSTANCE.disableOilRecipes.get() && ModOilRecipes.oilRecipes != null)
		{
			List<OilMix> mixes = OilBrewingRecipe.getValidMixes();
			
			for(OilMix mix : mixes)
			{
				ItemStack fromStack = OilHelper.makeOilStack(mix.from);
				ItemStack toStack = OilHelper.makeOilStack(mix.to);
				
				recipes.add(new JeiOilBrewingRecipe(ImmutableList.of(fromStack), ImmutableList.copyOf(mix.brewingIngredient.getItems()), toStack));
			}
		}
		
		if(!Config.INSTANCE.disableOilRecipes.get() && ModOilRecipes.potionToOilRecipes != null)
		{
			for(Potion potion : BuiltInRegistries.POTION)
			{
				if(OilHelper.isValidPotion(potion))
				{
					var potionHolder = BuiltInRegistries.POTION.wrapAsHolder(potion);
					ItemStack potionStack = PotionContents.createItemStack(Items.POTION, potionHolder);
					ItemStack oilStack = OilHelper.makePotionOilStack(potion);
					ResourceLocation potionLocation = BuiltInRegistries.POTION.getKey(potion);
					
					recipes.add(vanillaRecipeFactoryIn.createBrewingRecipe(ImmutableList.of(new ItemStack(ModItems.GREASE_BALL.get())), ImmutableList.of(potionStack), oilStack, 
							ResourceLocation.tryBuild(ModSpartanWeaponry.ID, potionLocation.getNamespace() + "." + potionLocation.getPath() + "_oil_from_brewing")));
				}
			}
		}
		
		return recipes;
	}
}
