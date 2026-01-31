package org.xiyu.spartanweaponryunofficial.api.oil;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.capability.OilHandler;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class PotionOilEffect extends OilEffect 
{
	public PotionOilEffect()
	{
		super("potion", OilEffectType.EFFECT_ONLY, 0x0, Defaults.OIL_USES_NORMAL, 0.0f, OilEffect.USE_NOTHING, true);
	}
	
	@Override
	public int getColor(ItemStack stackIn) 
	{
		Potion potion = null;
		CompoundTag oilTag = ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL);
		if(!oilTag.isEmpty())
		{
			potion = OilHelper.getPotionFromStack(stackIn);
			if(potion != null)
			{
				return PotionContents.EMPTY.withPotion(BuiltInRegistries.POTION.wrapAsHolder(potion)).getColor();
			}
		}
		return super.getColor(stackIn);
	}

	@Override
	public float onUse(float baseDamageIn, Level levelIn, LivingEntity targetEntityIn, LivingEntity userEntityIn, ItemStack oilStackIn) 
	{
		Potion potion = null;
		CompoundTag oilTag = ItemStackDataHelper.getTag(oilStackIn).getCompound(OilHandler.NBT_OIL);
		if(!oilTag.isEmpty())
		{
			potion = OilHelper.getPotionFromStack(oilStackIn);
			if(potion == null)
				return super.onUse(baseDamageIn, levelIn, targetEntityIn, userEntityIn, oilStackIn);
			potion.getEffects().forEach((effect) -> {
				if(effect.getEffect().value().isInstantenous())
				{
					// Temporarily bypass hurt time
					int targetHurtTime = targetEntityIn.hurtTime;
					targetEntityIn.hurtTime = 0;
					effect.getEffect().value().applyInstantenousEffect(userEntityIn, userEntityIn, targetEntityIn, effect.getAmplifier(), 1.0d);
					// Restore hurt time
					targetEntityIn.hurtTime = targetHurtTime;
				}
				else
					targetEntityIn.addEffect(new MobEffectInstance(effect.getEffect(), Mth.floor(effect.getDuration() * Config.INSTANCE.potionOilDurationModifier.get()), effect.getAmplifier()), userEntityIn);
			});
		}
		return super.onUse(baseDamageIn, levelIn, targetEntityIn, userEntityIn, oilStackIn);
	}
	
	@Override
	public void getTooltip(ItemStack stackIn, List<Component> tooltipListIn)
	{
		OilHelper.addPotionTooltip(stackIn, tooltipListIn, Config.INSTANCE.potionOilDurationModifier.get().floatValue());
	}
}
