package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowExplosiveEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ArrowExplosiveItem extends ArrowItemSW 
{
	public ArrowExplosiveItem(float rangeModifier) 
	{
		super();
		this.rangeModifier = rangeModifier;
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, ItemStack weapon)
	{
		AbstractArrow arrow = new ArrowExplosiveEntity(level, shooter);
		return arrow;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
		tooltip.add(Component.empty());
		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.impact.explosion").withStyle(ChatFormatting.BLUE));
	}
}