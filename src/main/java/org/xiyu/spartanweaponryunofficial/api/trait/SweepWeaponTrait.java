package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;

public class SweepWeaponTrait extends WeaponTraitWithMagnitude 
{

	public SweepWeaponTrait(String propType, String propModId) 
	{
		super(propType, propModId, TraitQuality.POSITIVE);
		isMelee = true;
	}

	@Override
	protected void addTooltipDescription(ItemStack stack, List<Component> tooltip)
	{
		if(level == 1)
			tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.fixed.desc", SpartanWeaponryAPI.MOD_ID, this.type), magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
		else
			tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", modId, this.type), magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
	}
	
	@Override
	public boolean isEnchantmentCompatible(Enchantment enchantIn) 
	{
		// In 1.21, we need to compare using resource location since Enchantments constants are now ResourceKeys
		// We can't directly compare Enchantment to ResourceKey, so we match by location
		return false; // Sweeping edge compatibility is now handled in SwordBaseItem.canApplyAtEnchantingTable
	}
	
	@Override
	public boolean canPerformToolAction(ItemStack stack, ItemAbility action) 
	{
		return action == ItemAbilities.SWORD_SWEEP;
	}
}
