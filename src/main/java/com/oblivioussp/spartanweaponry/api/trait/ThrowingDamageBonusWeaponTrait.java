package com.oblivioussp.spartanweaponry.api.trait;

import java.util.Optional;

public class ThrowingDamageBonusWeaponTrait extends DamageBonusWeaponTrait 
{

	public ThrowingDamageBonusWeaponTrait(String typeIn, String modIdIn, DamageCalculationFunc func) 
	{
		super(typeIn, modIdIn, func);
	}
	
	@Override
	public Optional<IMeleeTraitCallback> getMeleeCallback() 
	{
		return Optional.empty();
	}

}
