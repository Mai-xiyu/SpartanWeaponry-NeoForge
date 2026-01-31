package com.oblivioussp.spartanweaponry.compat.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class TippedProjectileRecipeMaker
{
	public static List<RecipeHolder<CraftingRecipe>> getRecipes(Item projectile, Item tippedProjectile)
	{
		List<RecipeHolder<CraftingRecipe>> list = new ArrayList<>();
		String recipeGroup = "jei.spartanweaponry.tipped_projectile";
    
		for (Potion potionType : BuiltInRegistries.POTION)
		{
			if (potionType != Potions.WATER.value() && potionType != Potions.MUNDANE.value() && potionType != Potions.THICK.value() && 
						potionType != Potions.AWKWARD.value()) 
			{
				ItemStack projStack = new ItemStack(projectile);
				var potionHolder = BuiltInRegistries.POTION.wrapAsHolder(potionType);
				ItemStack potionStack = PotionContents.createItemStack(Items.LINGERING_POTION, potionHolder);
				ItemStack outputStack = PotionContents.createItemStack(tippedProjectile, potionHolder);
				outputStack.setCount(8);
        
				Ingredient projIngredient = Ingredient.of(new ItemStack[] { projStack });
				Ingredient potionIngredient = Ingredient.of(new ItemStack[] { potionStack });
				
				Map<Character, Ingredient> ingredientMap = new HashMap<>();
				ingredientMap.put('P', projIngredient);
				ingredientMap.put('L', potionIngredient);
				
				List<String> pattern = List.of("PPP", "PLP", "PPP");
				
	        	String potionId = BuiltInRegistries.POTION.getKey(potionType).getPath();
	        	ResourceLocation recipeResLoc = ResourceLocation.fromNamespaceAndPath("spartanweaponry", "tipped_projectile." + BuiltInRegistries.ITEM.getKey(outputStack.getItem()).getPath() + ".effect." + potionId);
	        	ShapedRecipePattern recipePattern = ShapedRecipePattern.of(ingredientMap, pattern);
	        	ShapedRecipe recipe = new ShapedRecipe(recipeGroup, CraftingBookCategory.MISC, recipePattern, outputStack);
	        	list.add(new RecipeHolder<>(recipeResLoc, recipe));
      		} 
    	} 
    	return list;
	}
}