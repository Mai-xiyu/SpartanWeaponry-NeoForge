package org.xiyu.spartanweaponryunofficial.entity.projectile;

import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.ItemStack;

public class ThrowingKnifeEntity extends ThrowingWeaponEntity 
{
	public ThrowingKnifeEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level) 
	{
		super(type, level);
	}

	public ThrowingKnifeEntity(Level level, LivingEntity shooter, ItemStack weapon) 
	{
		super(ModEntities.THROWING_KNIFE.get(), shooter, level, weapon);
	}
	
	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() 
	{
		return ModSounds.THROWING_KNIFE_HIT_GROUND.get();
	}
	
	@Override
	protected SoundEvent getMobHitSound() 
	{
		return ModSounds.THROWING_KNIFE_HIT_MOB.get();
	}
}
