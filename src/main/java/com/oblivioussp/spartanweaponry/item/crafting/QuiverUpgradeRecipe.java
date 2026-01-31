package com.oblivioussp.spartanweaponry.item.crafting;

import com.oblivioussp.spartanweaponry.capability.IQuiverItemHandler;
import com.oblivioussp.spartanweaponry.init.ModCapabilities;
import com.oblivioussp.spartanweaponry.init.ModRecipeSerializers;
import com.oblivioussp.spartanweaponry.item.QuiverBaseItem;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;

import net.minecraft.core.HolderLookup;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

public class QuiverUpgradeRecipe extends SmithingTransformRecipe
{
	private final Ingredient template;
	private final Ingredient base;
	private final Ingredient addition;
	private final ItemStack result;

	public QuiverUpgradeRecipe(Ingredient templateIn, Ingredient baseIn, Ingredient additionIn, ItemStack resultIn)
	{
		super(templateIn, baseIn, additionIn, resultIn);
		template = templateIn;
		base = baseIn;
		addition = additionIn;
		result = resultIn;
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput inv, HolderLookup.Provider registryAccessIn) 
	{
		ItemStack origOutputStack = getResultItem(registryAccessIn);
		ItemStack outputStack = super.assemble(inv, registryAccessIn);
		// Resize the output tag
		// NOTE: More consistent, but inefficient
		IQuiverItemHandler itemHandler = outputStack.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
		if(itemHandler != null)
			itemHandler.resize(ItemStackDataHelper.getTag(origOutputStack).getCompound(QuiverBaseItem.NBT_AMMO).getInt("Size"));
//		outputStack.getOrCreateTagElement(QuiverBaseItem.NBT_AMMO).putInt("Size", origOutputStack.getOrCreateTagElement(QuiverBaseItem.NBT_AMMO).getInt("Size"));
		
		return outputStack;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ModRecipeSerializers.QUIVER_UPGRADE_SMITHING.get();
	}

	@Override
	public RecipeType<?> getType() 
	{
		return RecipeType.SMITHING;
	}

	public Ingredient getTemplate()
	{
		return template;
	}

	public Ingredient getBase()
	{
		return base;
	}

	public Ingredient getAddition()
	{
		return addition;
	}

	public ItemStack getResultStack()
	{
		return result;
	}

	public static class Serializer implements RecipeSerializer<QuiverUpgradeRecipe>
	{
		public Serializer() {}

		private static final MapCodec<QuiverUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Ingredient.CODEC.fieldOf("template").forGetter(QuiverUpgradeRecipe::getTemplate),
				Ingredient.CODEC.fieldOf("base").forGetter(QuiverUpgradeRecipe::getBase),
				Ingredient.CODEC.fieldOf("addition").forGetter(QuiverUpgradeRecipe::getAddition),
				ItemStack.CODEC.fieldOf("result").forGetter(QuiverUpgradeRecipe::getResultStack)
		).apply(instance, QuiverUpgradeRecipe::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<QuiverUpgradeRecipe> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}
