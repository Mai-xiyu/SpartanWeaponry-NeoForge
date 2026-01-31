package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class BasicItem extends Item 
{

	public BasicItem(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(Component.translatable(String.format("tooltip.%s.%s.desc", ModSpartanWeaponry.ID, BuiltInRegistries.ITEM.getKey(this).getPath())).withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}
}
