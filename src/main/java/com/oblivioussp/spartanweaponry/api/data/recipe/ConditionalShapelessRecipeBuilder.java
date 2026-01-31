package com.oblivioussp.spartanweaponry.api.data.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IRecipeOutputExtension;

/**
 * Shapeless recipe builder with optional NeoForge conditions.
 */
public class ConditionalShapelessRecipeBuilder
{
	private final ItemStack result;
	private final int count;
	private final RecipeCategory category;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group;
	private final List<ICondition> conditions = new ArrayList<>();

	private ConditionalShapelessRecipeBuilder(RecipeCategory categoryIn, ItemStack resultIn, int countIn) 
	{
		category = categoryIn;
		result = resultIn;
		count = countIn;
	}
	
	public static ConditionalShapelessRecipeBuilder shapeless(ItemStack stackIn)
	{
		return shapeless(RecipeCategory.COMBAT, stackIn, 1);
	}
	
	public static ConditionalShapelessRecipeBuilder shapeless(ItemStack stackIn, int countIn)
	{
		return shapeless(RecipeCategory.COMBAT, stackIn, countIn);
	}
	
	public static ConditionalShapelessRecipeBuilder shapeless(ItemLike itemIn)
	{
		return shapeless(RecipeCategory.COMBAT, new ItemStack(itemIn.asItem()), 1);
	}
	
	public static ConditionalShapelessRecipeBuilder shapeless(ItemLike itemIn, int countIn)
	{
		return shapeless(RecipeCategory.COMBAT, new ItemStack(itemIn.asItem()), countIn);
	}
	
	public static ConditionalShapelessRecipeBuilder shapeless(RecipeCategory categoryIn, ItemStack stackIn, int countIn)
	{
		return new ConditionalShapelessRecipeBuilder(categoryIn, stackIn, countIn);
	}
	
	public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tagIn)
	{
		return requires(Ingredient.of(tagIn));
	}
	
	public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tagIn, int countIn)
	{
		return requires(Ingredient.of(tagIn), countIn);
	}
	
	public ConditionalShapelessRecipeBuilder requires(ItemLike itemIn)
	{
		return requires(Ingredient.of(itemIn));
	}
	
	public ConditionalShapelessRecipeBuilder requires(ItemLike itemIn, int countIn)
	{
		return requires(Ingredient.of(itemIn), countIn);
	}
	
	public ConditionalShapelessRecipeBuilder requires(Ingredient ingredientIn)
	{
		ingredients.add(ingredientIn);
		return this;
	}
	
	public ConditionalShapelessRecipeBuilder requires(Ingredient ingredientIn, int countIn)
	{
		for(int i = 0; i < countIn; i++)
			ingredients.add(ingredientIn);
		return this;
	}
	
	public ConditionalShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterionIn)
	{
		criteria.put(name, criterionIn);
		return this;
	}
	
	public ConditionalShapelessRecipeBuilder group(String groupIn)
	{
		group = groupIn;
		return this;
	}
	
	public ConditionalShapelessRecipeBuilder condition(ICondition conditionIn)
	{
		conditions.add(conditionIn);
		return this;
	}
	
	public void save(RecipeOutput output)
	{
		save(output, BuiltInRegistries.ITEM.getKey(result.getItem()));
	}
	
	public void save(RecipeOutput output, String save)
	{
		ResourceLocation resultLoc = BuiltInRegistries.ITEM.getKey(result.getItem());
		ResourceLocation saveLoc = ResourceLocation.parse(save);
		if(saveLoc.equals(resultLoc))
			throw new IllegalStateException("Shapeless recipe " + save + " save argument is redundant as it's the same as the item id!");
		else
			save(output, saveLoc);
	}
	
	public void save(RecipeOutput output, ResourceLocation id)
	{
		validate(id);
		RecipeOutput conditionedOutput = output;
		if(!conditions.isEmpty() && output instanceof IRecipeOutputExtension ext)
			conditionedOutput = ext.withConditions(conditions.toArray(ICondition[]::new));

		CraftingBookCategory bookCategory = RecipeBuilder.determineBookCategory(category);
		ItemStack outputStack = result.copy();
		outputStack.setCount(count);
		NonNullList<Ingredient> ingredientList = NonNullList.copyOf(ingredients);
		ShapelessRecipe recipe = new ShapelessRecipe(group == null ? "" : group, bookCategory, outputStack, ingredientList);

		ResourceLocation advancementId = id.withPrefix("recipes/" + category.getFolderName() + "/");
		var advancement = output.advancement();
		criteria.forEach(advancement::addCriterion);
		advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		AdvancementHolder advancementHolder = advancement.build(advancementId);
		conditionedOutput.accept(id, recipe, advancementHolder);
	}
	
	private void validate(ResourceLocation id)
	{
		if(criteria.isEmpty())
			throw new IllegalStateException("Impossible to obtain recipe " + id + "!");
	}
}
