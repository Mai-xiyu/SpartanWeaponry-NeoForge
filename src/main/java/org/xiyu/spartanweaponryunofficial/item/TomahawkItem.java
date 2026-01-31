package org.xiyu.spartanweaponryunofficial.item;

import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.TomahawkEntity;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TomahawkItem extends ThrowingWeaponItem 
{

	public TomahawkItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn) 
	{
		super(prop, material, archetypeIn, Defaults.DamageBaseTomahawk, Defaults.DamageMultiplierTomahawk, Defaults.MeleeSpeedTomahawk, 8, Defaults.ChargeTicksTomahawk);
		this.throwVelocity = 1.75f;
	}
	
	public TomahawkItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, String customDisplayName)
	{
		this(prop, material, archetypeIn);
		if(material.useCustomDisplayName())
			this.customDisplayName = customDisplayName;
	}

	@Override
	public ThrowingWeaponEntity createThrowingWeaponEntity(Level level, Player player, ItemStack stack, int charge) 
	{
		return new TomahawkEntity(level, player, stack);
	}
	
	@Override
	protected SoundEvent getThrowingSound() 
	{
		return ModSounds.TOMAHAWK_THROW.get();
	}
}
