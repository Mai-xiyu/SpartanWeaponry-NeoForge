package com.oblivioussp.spartanweaponry.init;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.advancement.criterion.BrewOilTrigger;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModCriteriaTriggers 
{
	public static final BrewOilTrigger BREW_OIL = Registry.register(BuiltInRegistries.TRIGGER_TYPES, ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "brew_oil"), new BrewOilTrigger());
	
	public static void register()
	{
		// Registration is handled by static initialization.
	}
}
