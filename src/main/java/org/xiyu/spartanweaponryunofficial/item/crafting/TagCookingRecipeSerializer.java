package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TagCookingRecipeSerializer<T extends ITagCookingRecipe> implements RecipeSerializer<T> 
{
	private final RecipeFactory<T> factory;
	private final int defaultCookingTime;
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
	
	public TagCookingRecipeSerializer(RecipeFactory<T> factoryIn, int defaultCookingTimeIn)
	{
		factory = factoryIn;
		defaultCookingTime = defaultCookingTimeIn;
		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.getGroup()),
				CookingBookCategory.CODEC.optionalFieldOf("category", CookingBookCategory.MISC).forGetter(ITagCookingRecipe::getCategory),
				Ingredient.CODEC.fieldOf("ingredient").forGetter(ITagCookingRecipe::getInputIngredient),
				TagKey.codec(Registries.ITEM).fieldOf("result_tag").forGetter(ITagCookingRecipe::getResultTag),
				Codec.FLOAT.optionalFieldOf("experience", 0.0f).forGetter(ITagCookingRecipe::getExperienceDrop),
				Codec.INT.optionalFieldOf("cookingtime", defaultCookingTime).forGetter(ITagCookingRecipe::getCookTime)
		).apply(instance, factory::create));
		streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());
	}

	@Override
	public MapCodec<T> codec()
	{
		return codec;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec()
	{
		return streamCodec;
	}

	@FunctionalInterface
	public interface RecipeFactory<T extends ITagCookingRecipe>
	{
		T create(String groupIn, CookingBookCategory categoryIn, Ingredient inputIngredientIn, TagKey<Item> resultTagIn, float experienceIn, int cookTimeIn);
	}
}
