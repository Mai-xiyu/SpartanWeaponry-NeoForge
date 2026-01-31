package com.oblivioussp.spartanweaponry.item;

import java.util.List;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.entity.projectile.BoltEntity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class BoltTippedItem extends BoltItem
{
	protected String baseName;
	
	public BoltTippedItem(String baseName, float damageModifier, float rangeModifier, float armorPiercingFactor) 
	{
		super(damageModifier, rangeModifier, armorPiercingFactor);
		this.baseName = baseName;
	}
	
	@Override
	public BoltEntity createBolt(Level level, ItemStack stack, LivingEntity shooter) 
	{
		BoltEntity bolt = new BoltEntity(shooter, level);
    	ItemStack boltStack = stack.copy();
    	boltStack.setCount(1);
    	bolt.initEntity(damageModifier, rangeModifier, armorPiercingFactor, boltStack);
		bolt.setPotionEffect(stack);
    	if(bolt.isValid())
    		return bolt;
    	
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
