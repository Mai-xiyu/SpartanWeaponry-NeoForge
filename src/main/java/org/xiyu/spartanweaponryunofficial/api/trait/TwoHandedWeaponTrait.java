package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TwoHandedWeaponTrait extends MeleeCallbackWeaponTrait
{
	public TwoHandedWeaponTrait(String typeIn, String modIdIn) 
	{
		super(typeIn, modIdIn, TraitQuality.NEGATIVE);
		isMelee = true;
	}
	
	@Override
	protected void addTooltipDescription(ItemStack stack, List<Component> tooltip)
	{
		tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", modId, this.type), magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
	}

	@Override
	public void onItemUpdate(WeaponMaterial material, ItemStack stack, Level level, LivingEntity entity, int itemSlot, boolean isSelected)
	{
		// 双手武器不再自动移除副手物品
		// 只通过伤害减益来惩罚双手持物的行为
		// 玩家可以在工具提示中看到这个惩罚，需要自己决定是否清空副手
	}
	
	@Override
	public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) 
	{
		float resultDamage = baseDamage;
		ItemStack mainHand = attacker.getMainHandItem();
		ItemStack offHand = attacker.getOffhandItem();
		
		// 如果双手都有物品，则减少伤害
		if(!mainHand.isEmpty() && !offHand.isEmpty())
		{
			resultDamage *= (1.0f - magnitude);
		}
		return resultDamage;
	}
}
