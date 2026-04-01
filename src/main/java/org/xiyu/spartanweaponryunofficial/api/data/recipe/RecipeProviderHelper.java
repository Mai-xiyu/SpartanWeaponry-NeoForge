package org.xiyu.spartanweaponryunofficial.api.data.recipe;

import java.util.Collections;

import org.xiyu.spartanweaponryunofficial.api.crafting.condition.TypeDisabledCondition;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Helper functions for making recipes for addon weapons using the data generator<br>
 * Find more info about using the data generator here: <a href=https://mcforge.readthedocs.io/en/1.18.x/datagen/>https://mcforge.readthedocs.io/en/1.18.x/datagen/</a>
 * @author ObliviousSpartan
 *
 */
public class RecipeProviderHelper
{
	/**
	 * Constructs a Smithing Table recipe
	 * @param consumer The function used to generate the recipe file
	 * @param base The base item to upgrade
	 * @param additionTag The item Tag which contains specific items to apply to the base item to upgrade it
	 * @param result The resulting upgraded item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the addition tag is in the player's inventory
	 */
	public static void smithingRecipe(RecipeOutput consumer, ItemLike base, TagKey<Item> additionTag, ItemLike result, String hasItemCriterionName)
	{
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(base), tagIngredient(additionTag), RecipeCategory.MISC, result.asItem()).unlocks(hasItemCriterionName, hasItem(additionTag)).
			save(consumer, BuiltInRegistries.ITEM.getKey(result.asItem()) + "_smithing");
	}
	
	/**
	 * Constructs a Shaped Crafting recipe using the Dagger pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Dagger item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeDagger(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeDagger(consumer, handle, material, result, hasItemCriterionName, "");
	}
	
	/**
	 * Constructs a Shaped Crafting recipe using the Dagger pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Dagger item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeDagger(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("#").pattern("|").
			group("spartan_weaponry_unofficial:dagger").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.DAGGER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}
	
	/**
	 * Constructs a Shaped Crafting recipe using the Parrying Dagger pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Parrying Dagger item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeParryingDagger(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeParryingDagger(consumer, handle, material, result, hasItemCriterionName, "");
	}
	
	/**
	 * Constructs a Shaped Crafting recipe using the Parrying Dagger pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Parrying Dagger item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeParryingDagger(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern(" #").pattern("#|").
			group("spartan_weaponry_unofficial:parrying_dagger").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.PARRYING_DAGGER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Longsword pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Longsword item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeLongsword(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeLongsword(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Longsword pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Longsword item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeLongsword(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern(" # ").pattern(" # ").pattern("#|#").
			group("spartan_weaponry_unofficial:longsword").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.LONGSWORD))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Katana pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Katana item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeKatana(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeKatana(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Katana pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Katana item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeKatana(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("  #").pattern(" # ").pattern("|  ").
			group("spartan_weaponry_unofficial:katana").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.KATANA))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Saber pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Saber item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeSaber(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeSaber(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Saber pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Saber item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeSaber(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern(" #").pattern(" #").pattern("#|").
			group("spartan_weaponry_unofficial:saber").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.SABER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Rapier pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Rapier item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeRapier(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeRapier(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Rapier pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Rapier item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeRapier(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("  #").pattern("## ").pattern("|# ").
			group("spartan_weaponry_unofficial:rapier").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.RAPIER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Greatsword pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Greatsword item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeGreatsword(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeGreatsword(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Greatsword pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Greatsword item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeGreatsword(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern(" # ").pattern("###").pattern("#|#").
			group("spartan_weaponry_unofficial:greatsword").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.GREATSWORD))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Battle Hammer pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Battle Hammer item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeBattleHammer(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeBattleHammer(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Battle Hammer pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Battle Hammer item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeBattleHammer(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("###").pattern("###").pattern(" | ").
			group("spartan_weaponry_unofficial:battle_hammer").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.BATTLE_HAMMER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Warhammer pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Warhammer item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeWarhammer(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeWarhammer(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Warhammer pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Warhammer item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeWarhammer(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern(" #").pattern("##").pattern(" |").
			group("spartan_weaponry_unofficial:warhammer").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.WARHAMMER))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Spear pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Spear item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeSpear(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeSpear(consumer, pole, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Spear pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Spear item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeSpear(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("#").pattern("/").
			group("spartan_weaponry_unofficial:spear").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.SPEAR))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Halberd pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Halberd item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeHalberd(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeHalberd(consumer, pole, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Halberd pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Halberd item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeHalberd(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern(" #").pattern("##").pattern("#/").
			group("spartan_weaponry_unofficial:halberd").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.HALBERD))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Pike pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Pike item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipePike(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipePike(consumer, pole, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Pike pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Pike item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipePike(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("#").pattern("/").pattern("/").
			group("spartan_weaponry_unofficial:pike").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.PIKE))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Lance pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Lance item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeLance(RecipeOutput consumer, ItemLike handle, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeLance(consumer, handle, pole, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Lance pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Lance item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeLance(RecipeOutput consumer, ItemLike handle, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).define('/', pole).pattern("  #").pattern("#/ ").pattern("|# ").
			group("spartan_weaponry_unofficial:lance").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.LANCE))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Longbow pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param string The item used for the string
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Longbow item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeLongbow(RecipeOutput consumer, TagKey<Item> stick, TagKey<Item> string, ItemLike handle, TagKey<Item> material, ItemLike result, 
			String hasItemCriterionName)
	{
		recipeLongbow(consumer, tagIngredient(stick), tagIngredient(string), handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Longbow pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param string The item used for the string
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Longbow item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeLongbow(RecipeOutput consumer, Ingredient stick, Ingredient string, ItemLike handle, TagKey<Item> material, ItemLike result, 
			String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).define('/', stick).define('~', string).
			pattern("|/#").pattern("/ ~").pattern("#~~").group("spartan_weaponry_unofficial:longbow").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.LONGBOW))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Heavy Crossbow pattern
	 * @param consumer The function used to generate the recipe file
	 * @param planks The item used for the planks
	 * @param bow The item used for the bow
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Heavy Crossbow item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeHeavyCrossbow(RecipeOutput consumer, TagKey<Item> planks, ItemLike bow, ItemLike handle, TagKey<Item> material, ItemLike result, 
			String hasItemCriterionName)
	{
		recipeHeavyCrossbow(consumer, tagIngredient(planks), bow, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Heavy Crossbow pattern
	 * @param consumer The function used to generate the recipe file
	 * @param planks The item used for the planks
	 * @param bow The item used for the bow
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Heavy Crossbow item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeHeavyCrossbow(RecipeOutput consumer, Ingredient planks, ItemLike bow, ItemLike handle, TagKey<Item> material, ItemLike result, 
			String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).define('P', planks).define('D', bow).define('H', Items.TRIPWIRE_HOOK).
			pattern("#D#").pattern("PHP").pattern(" | ").group("spartan_weaponry_unofficial:heavy_crossbow").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.HEAVY_CROSSBOW))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Throwing Knife pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Throwing Knife item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeThrowingKnife(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeThrowingKnife(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Throwing Knife pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Throwing Knife item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeThrowingKnife(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("|#").group("spartan_weaponry_unofficial:throwing_knife").
			condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.THROWING_KNIFE))).unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Tomahawk pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Tomahawk item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeTomahawk(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeTomahawk(consumer, handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Tomahawk pattern
	 * @param consumer The function used to generate the recipe file
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Tomahawk item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeTomahawk(RecipeOutput consumer, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).pattern("|#").pattern(" #").
			group("spartan_weaponry_unofficial:tomahawk").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.TOMAHAWK))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Javelin pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Javelin item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeJavelin(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeJavelin(consumer, pole, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Javelin pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Javelin item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeJavelin(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("/#").group("spartan_weaponry_unofficial:javelin").
			condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.JAVELIN))).unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Boomerang pattern
	 * @param consumer The function used to generate the recipe file
	 * @param planks The item used for the planks
	 * @param material The item tag used for the material
	 * @param result The resulting Boomerang item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeBoomerang(RecipeOutput consumer, TagKey<Item> planks, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeBoomerang(consumer, tagIngredient(planks), material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Boomerang pattern
	 * @param consumer The function used to generate the recipe file
	 * @param planks The item used for the planks
	 * @param material The item tag used for the material
	 * @param result The resulting Boomerang item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeBoomerang(RecipeOutput consumer, Ingredient planks, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('P', planks).pattern("#PP").pattern("P  ").pattern("P  ").
			group("spartan_weaponry_unofficial:boomerang").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.BOOMERANG))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Battleaxe pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Battleaxe item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeBattleaxe(RecipeOutput consumer, TagKey<Item> stick, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeBattleaxe(consumer, tagIngredient(stick), handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Battleaxe pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Battleaxe item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeBattleaxe(RecipeOutput consumer, Ingredient stick, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, 
			String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).define('/', stick).
			pattern("###").pattern("#/#").pattern(" | ").group("spartan_weaponry_unofficial:battleaxe").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.BATTLEAXE))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Flanged Mace pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Flanged Mace item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeFlangedMace(RecipeOutput consumer, TagKey<Item> stick, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeFlangedMace(consumer, tagIngredient(stick), handle, material, result, hasItemCriterionName, "");
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Flanged Mace pattern
	 * @param consumer The function used to generate the recipe file
	 * @param stick The item used for the stick
	 * @param handle The item used for the handle
	 * @param material The item tag used for the material
	 * @param result The resulting Flanged Mace item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeFlangedMace(RecipeOutput consumer, Ingredient stick, ItemLike handle, TagKey<Item> material, ItemLike result, String hasItemCriterionName, 
			String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('|', handle).define('/', stick).
			pattern(" ##").pattern(" /#").pattern("|  ").group("spartan_weaponry_unofficial:flanged_mace").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.FLANGED_MACE))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Glaive pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Glaive item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeGlaive(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeGlaive(consumer, pole, material, result, hasItemCriterionName);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Glaive pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Glaive item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeGlaive(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern(" #").pattern(" #").pattern(" /").
			group("spartan_weaponry_unofficial:glaive").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.GLAIVE))).unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Quarterstaff pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Quarterstaff item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeQuarterstaff(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		recipeQuarterstaff(consumer, pole, material, result, hasItemCriterionName);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Quarterstaff pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Quarterstaff item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeQuarterstaff(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("  #").pattern(" / ").pattern("#  ").
			group("spartan_weaponry_unofficial:quarterstaff").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUARTERSTAFF))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Scythe pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Scythe item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 */
	public static void recipeScythe(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName)
	{
		ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("## ").pattern("  #").pattern(" / ").group("spartan_weaponry_unofficial:scythe").
			condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.SCYTHE))).unlockedBy(hasItemCriterionName, hasItem(material)).save(consumer);
	}

	/**
	 * Constructs a Shaped Crafting recipe using the Scythe pattern
	 * @param consumer The function used to generate the recipe file
	 * @param pole The item used for the pole
	 * @param material The item tag used for the material
	 * @param result The resulting Scythe item
	 * @param hasItemCriterionName The name of the unlock criteria for this recipe. The recipe will be "unlocked" when any item in the material tag is in the player's inventory
	 * @param requiredModId The name of a required mod ID to be present for the recipe to load. Leave as "" to ignore this condition
	 */
	public static void recipeScythe(RecipeOutput consumer, ItemLike pole, TagKey<Item> material, ItemLike result, String hasItemCriterionName, String requiredModId)
	{
		ConditionalShapedRecipeBuilder recipe = ConditionalShapedRecipeBuilder.shaped(result).define('#', material).define('/', pole).pattern("## ").pattern("  #").pattern(" / ").
			group("spartan_weaponry_unofficial:scythe").condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.SCYTHE))).
			unlockedBy(hasItemCriterionName, hasItem(material));
		if(!requiredModId.isEmpty())
			recipe.condition(new ModLoadedCondition(requiredModId));
		recipe.save(consumer);
	}
	
	/**
	 * Constructs a unlock criterion for detecting items in the player's inventory
	 * @param item The item to detect
	 * @return The unlock criterion
	 */
	protected static Criterion<?> hasItem(ItemLike item)
	{
		return InventoryChangeTrigger.TriggerInstance.hasItems(item);
	}

	/**
	 * Constructs a unlock criterion for detecting items in the player's inventory
	 * @param tag The item tag to check
	 * @return The unlock criterion
	 */
	protected static Criterion<?> hasItem(TagKey<Item> tag)
	{
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, tag));
	}

	private static Ingredient tagIngredient(TagKey<Item> tag)
	{
		return Ingredient.of(HolderSet.emptyNamed(BuiltInRegistries.ITEM, tag));
	}
}
