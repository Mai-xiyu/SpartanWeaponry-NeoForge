package com.oblivioussp.spartanweaponry.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.datafixers.util.Pair;
import com.oblivioussp.spartanweaponry.api.OilEffects;
import com.oblivioussp.spartanweaponry.api.oil.OilEffect;
import com.oblivioussp.spartanweaponry.capability.OilHandler;
import com.oblivioussp.spartanweaponry.init.ModItems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;

public class OilHelper
{
	
	private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
	
	public static OilEffect getOilFromStack(ItemStack stackIn)
	{
		CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL);
		ResourceLocation oil = ResourceLocation.parse(tag.getString(OilHandler.NBT_OIL_EFFECT));
		Registry<OilEffect> registry = getOilRegistry();
		if(registry != null && registry.containsKey(oil))
			return registry.get(oil);
		return OilEffects.NONE.get();
	}
	
	public static ItemStack makeOilStack(OilEffect oilIn)
	{
		ItemStack stack = new ItemStack(ModItems.WEAPON_OIL.get());
		CompoundTag tag = new CompoundTag();
		Registry<OilEffect> registry = getOilRegistry();
		if(registry != null)
			tag.putString(OilHandler.NBT_OIL_EFFECT, registry.getKey(oilIn).toString());
		ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
		return stack;
	}
	
	public static Potion getPotionFromStack(ItemStack stackIn)
	{
		CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL);
		String potionId = tag.getString(OilHandler.NBT_POTION);
		if(potionId.isEmpty())
			return null;
		return BuiltInRegistries.POTION.get(ResourceLocation.parse(potionId));
	}
	
	public static ItemStack makePotionOilStack(Potion potionIn)
	{
		ItemStack stack = makeOilStack(OilEffects.POTION.get());
		CompoundTag tag = ItemStackDataHelper.getTag(stack).getCompound(OilHandler.NBT_OIL);
		tag.putString(OilHandler.NBT_POTION, BuiltInRegistries.POTION.getKey(potionIn).toString());
		ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
		return stack;
	}
	
	public static boolean isValidPotion(Potion potionIn)
	{
		boolean isValidPotion = true;
		if(potionIn.getEffects().isEmpty() || Config.INSTANCE.potionOilBlacklist.get().contains(BuiltInRegistries.POTION.getKey(potionIn).toString()))
			return false;
		
		if(Config.INSTANCE.potionOilWhitelist.get().contains(BuiltInRegistries.POTION.getKey(potionIn).toString()))
			return true;
			
		for(MobEffectInstance effect : potionIn.getEffects())
		{
			// Block non-harmful effects
			if(effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL)
			{
				isValidPotion = false;
				break;
			}
		}
		return isValidPotion;
	}
	
	public static void addPotionTooltip(ItemStack stackIn, List<Component> tooltipListIn, float durationModifierIn)
	{
		addPotionTooltip(ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL), tooltipListIn, durationModifierIn);
	}
	
	public static void addPotionTooltip(CompoundTag tagIn, List<Component> tooltipListIn, float durationModifierIn)
	{
		Potion potion = getPotionFromTag(tagIn);
		if(potion == null)
		{
			tooltipListIn.add(NO_EFFECT);
			return;
		}
		PotionContents.addPotionTooltip(potion.getEffects(), tooltipListIn::add, durationModifierIn, 20.0F);
	}

	private static Potion getPotionFromTag(CompoundTag tag)
	{
		String potionId = tag.getString(OilHandler.NBT_POTION);
		if(potionId.isEmpty())
			return null;
		return BuiltInRegistries.POTION.get(ResourceLocation.parse(potionId));
	}

	@SuppressWarnings("unchecked")
	private static Registry<OilEffect> getOilRegistry()
	{
		return (Registry<OilEffect>)BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
	}
}
