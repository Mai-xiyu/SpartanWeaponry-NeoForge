package com.oblivioussp.spartanweaponry.item;

import java.util.List;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public abstract class ArrowItemSW extends ArrowItem 
{
	protected float damageModifier = 1.0f;
	protected float rangeModifier = 1.0f;
	
	public ArrowItemSW() 
	{
		super(new Item.Properties());
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn)
	{
	//	tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers").withStyle(ChatFormatting.GOLD));
		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage.value", damageModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range",  Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range.value", rangeModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
	}
}
