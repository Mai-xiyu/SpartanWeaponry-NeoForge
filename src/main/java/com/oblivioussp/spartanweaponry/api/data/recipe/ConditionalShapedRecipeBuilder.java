package com.oblivioussp.spartanweaponry.api.data.recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IRecipeOutputExtension;

/**
 * Copy of vanilla's {@linkplain ShapedRecipeBuilder} with additions to allow NeoForge's condition system to be serialized too.
 */
public class ConditionalShapedRecipeBuilder
{
	private final Item result;
	private final int count;
	private final RecipeCategory category;
	private final List<String> pattern = Lists.newArrayList();
	private final Map<Character, Ingredient> keys = Maps.newLinkedHashMap();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group;
	private final List<ICondition> conditions = new ArrayList<>();

	private ConditionalShapedRecipeBuilder(RecipeCategory categoryIn, ItemLike resultIn, int countIn) 
	{
		category = categoryIn;
		result = resultIn.asItem();
		count = countIn;
	}
	
	public static ConditionalShapedRecipeBuilder shaped(ItemLike itemIn)
	{
		return shaped(RecipeCategory.COMBAT, itemIn, 1);
	}
	
	public static ConditionalShapedRecipeBuilder shaped(ItemLike itemIn, int countIn)
	{
		return shaped(RecipeCategory.COMBAT, itemIn, countIn);
	}
	
	public static ConditionalShapedRecipeBuilder shaped(RecipeCategory categoryIn, ItemLike itemIn)
	{
		return shaped(categoryIn, itemIn, 1);
	}
	
	public static ConditionalShapedRecipeBuilder shaped(RecipeCategory categoryIn, ItemLike itemIn, int countIn)
	{
		return new ConditionalShapedRecipeBuilder(categoryIn, itemIn, countIn);
	}
	
	public ConditionalShapedRecipeBuilder define(Character character, TagKey<Item> tagIn)
	{
		return define(character, Ingredient.of(tagIn));
	}
	
	public ConditionalShapedRecipeBuilder define(Character character, ItemLike itemIn)
	{
		return define(character, Ingredient.of(itemIn));
	}
	
	public ConditionalShapedRecipeBuilder define(Character character, Ingredient ingredientIn)
	{
		if(keys.containsKey(character))
			throw new IllegalArgumentException("Key character '" + character + "' is already defined!");
		else if(character == ' ')
			throw new IllegalArgumentException("Key character ' ' (whitespace) cannot be defined as it is reserved!");
		else
			keys.put(character, ingredientIn);
		return this;
	}
	
	public ConditionalShapedRecipeBuilder pattern(String patternIn)
	{
		if(!pattern.isEmpty() && patternIn.length() != pattern.get(0).length())
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		else
			pattern.add(patternIn);
		return this;
	}
	
	public ConditionalShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterionIn)
	{
		criteria.put(name, criterionIn);
		return this;
	}
	
	public ConditionalShapedRecipeBuilder group(String groupIn)
	{
		group = groupIn;
		return this;
	}
	
	public ConditionalShapedRecipeBuilder condition(ICondition conditionIn)
	{
		conditions.add(conditionIn);
		return this;
	}
	
	public void save(RecipeOutput output)
	{
		save(output, BuiltInRegistries.ITEM.getKey(result));
	}
	
	public void save(RecipeOutput output, String save)
	{
		ResourceLocation resultLoc = BuiltInRegistries.ITEM.getKey(result);
		ResourceLocation saveLoc = ResourceLocation.tryBuild(save);
		if(saveLoc.equals(resultLoc))
			throw new IllegalStateException("Shaped recipe " + save + " save argument is redundant as it's the same as the item id!");
		else
			save(output, saveLoc);
	}
	
	public void save(RecipeOutput output, ResourceLocation id)
	{
		validate(id);
		RecipeOutput conditionedOutput = output;
		if(!conditions.isEmpty() && output instanceof IRecipeOutputExtension ext)
			conditionedOutput = ext.withConditions(conditions.toArray(ICondition[]::new));

		ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, result, count);
		for(Entry<Character, Ingredient> entry : keys.entrySet())
			builder.define(entry.getKey(), entry.getValue());
		for(String line : pattern)
			builder.pattern(line);
		if(group != null)
			builder.group(group);
		criteria.forEach(builder::unlockedBy);
		builder.save(conditionedOutput, id);
	}
	
	private void validate(ResourceLocation id)
	{
		if(pattern.isEmpty())
			throw new IllegalStateException("No pattern was defined for recipe " + id + "!");
		else
		{
			Set<Character> characters = new HashSet<>(keys.keySet());
			characters.remove(' ');
			
			for(int iS = 0; iS < pattern.size() ; iS++)
			{
				String s = pattern.get(iS);
				if(s.length() != pattern.get(0).length())
					throw new IllegalStateException("Pattern rows in recipe " + id + " must be the same length! Expected row size " + pattern.get(0).length() + "; got " + s.length() + " on row " + iS);
				for(int i = 0; i < s.length(); i++)
				{
					char c = s.charAt(i);
					if(!keys.containsKey(c) && c != ' ')
						throw new IllegalStateException("Pattern in recipe " + id + " uses an undefined key '" + c + "'" + " in location " + iS + ", " + i);
					characters.remove(c);
				}
			}
			
			if(!characters.isEmpty())
				throw new IllegalStateException("Defined ingredients are not used in recipe " + id + "!");
			else if(pattern.size() == 1 && pattern.get(0).length() == 1)
				throw new IllegalStateException("Single item only defined in shaped recipe " + id + "! Use a shapeless recipe instead!");
			else if(criteria.isEmpty())
				throw new IllegalStateException("Impossible to obtain recipe " + id + "!");
		}
	}
}
