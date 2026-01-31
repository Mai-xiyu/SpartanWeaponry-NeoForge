package com.oblivioussp.spartanweaponry.item;

import java.util.List;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.entity.projectile.ArrowBaseEntity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class ArrowBaseTippedItem extends ArrowBaseItem
{
	protected String baseName;
	
	public ArrowBaseTippedItem(String baseName, float damageModifier, float rangeModifier) 
	{
		super(damageModifier, rangeModifier);
		this.baseName = baseName;
	}
	
	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, ItemStack weapon) 
	{
		ArrowBaseEntity arrow = new ArrowBaseEntity(level, shooter);
		ItemStack arrowStack = stack.copy();
		arrowStack.setCount(1);
		arrow.initEntity(damageModifier, rangeModifier, arrowStack);
		arrow.setPotionEffect(stack);
		
		if(arrow.isValid())
			return arrow;
		return null;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) 
	{
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
		tooltip.add(Component.empty());
		Potion potion = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
				.potion().map(Holder::value).orElse(null);
		if(potion != null)
			PotionContents.addPotionTooltip(potion.getEffects(), tooltip::add, 0.125f, 20.0F);
	}

	@Override
	public Component getName(ItemStack stack)
	{
		Potion potion = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
				.potion().map(Holder::value).orElse(null);
		if(potion == null)
			return Component.translatable("item." + ModSpartanWeaponry.ID + "." + baseName);
		var potionKey = net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potion);
		if(potionKey == null)
			return Component.translatable("item." + ModSpartanWeaponry.ID + "." + baseName);
		String translationKey = "item.spartanweaponry.proj_tipped.effect." + potionKey.getPath();
		return Component.translatable(translationKey, Component.translatable("item." + ModSpartanWeaponry.ID + "." + baseName));
	}
}
