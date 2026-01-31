package org.xiyu.spartanweaponryunofficial.util;

import java.util.function.Predicate;

import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

public enum WeaponType
{
	MELEE((trait) -> trait.isMeleeTrait()),
	RANGED((trait) -> trait.isRangedTrait()),
	THROWING((trait) -> trait.isThrowingTrait());

	private Predicate<WeaponTrait> filter;
	
	private WeaponType(Predicate<WeaponTrait> traitFilter)
	{
		filter = traitFilter;
	}
	
	public Predicate<WeaponTrait> getTraitFilter() 
	{
		return filter;
	}
}
