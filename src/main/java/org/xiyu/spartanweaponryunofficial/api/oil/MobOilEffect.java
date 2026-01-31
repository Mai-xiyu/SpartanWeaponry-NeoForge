package org.xiyu.spartanweaponryunofficial.api.oil;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An {@link OilEffect} that also inflicts a Mob Effect when hitting a mob with the weapon this is applied to<br>
 * @author ObliviousSpartan
 * @see {@link OilEffect}
 *
 */
public class MobOilEffect extends OilEffect 
{
	private final Holder<MobEffect> mobEffect;
	private final int effectDuration;
	private final int effectLevel;

	public MobOilEffect(String nameIn, OilEffectType typeIn, int colorIn, int maxUsesIn, float damageModifierIn, IUsePredicate usePredicateIn, Holder<MobEffect> mobEffectIn, int effectDurationIn, int effectLevelIn) 
	{
		super(nameIn, typeIn, colorIn, maxUsesIn, damageModifierIn, usePredicateIn);
		mobEffect = mobEffectIn;
		effectDuration = effectDurationIn;
		effectLevel = effectLevelIn;
	}
	
	public MobOilEffect(String nameIn, OilEffectType typeIn, int colorIn, int maxUsesIn, Holder<MobEffect> mobEffectIn, int effectDurationIn, int effectLevelIn)
	{
		this(nameIn, typeIn, colorIn, maxUsesIn, 0.0f, OilEffect.USE_NOTHING, mobEffectIn, effectDurationIn, effectLevelIn);
	}

	@Override
	public float onUse(float baseDamageIn, Level levelIn, LivingEntity targetIn, LivingEntity userIn, ItemStack oilStackIn)
	{
		targetIn.addEffect(new MobEffectInstance(mobEffect, effectDuration, effectLevel), userIn);
		return super.onUse(baseDamageIn, levelIn, targetIn, userIn, oilStackIn);
	}
	
	@Override
	public void getTooltip(ItemStack stackIn, List<Component> tooltipListIn)
	{
		MutableComponent mobEffectComponent = mobEffect.value().getDisplayName().copy().withStyle(ChatFormatting.YELLOW);
		if(effectLevel > 0)
			mobEffectComponent.append(" ").append(Component.translatable("enchantment.level." + Integer.toString(effectLevel + 1)));
		if(damageModifier == 0.0f)
			tooltipListIn.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.applied." + name, mobEffectComponent, (float)effectDuration / 20.0f).withStyle(ChatFormatting.BLUE));			
		else
			tooltipListIn.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.applied." + name, (getDamageModifier() * 100.0f), mobEffectComponent, (float)effectDuration / 20.0f).withStyle(ChatFormatting.BLUE));
	}
}
