package org.xiyu.spartanweaponryunofficial.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

public class ModDatapackRegistriesProvider extends DatapackBuiltinEntriesProvider
{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, context -> 
		{
			context.register(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER, new DamageType("player", 0.1f));
			context.register(ModDamageTypes.KEY_THROWN_WEAPON_MOB, new DamageType("mob", 0.1f));
			context.register(ModDamageTypes.KEY_ARMOR_PIERCING_MELEE, new DamageType("player", 0.1f));
			context.register(ModDamageTypes.KEY_ARMOR_PIERCING_BOLT, new DamageType("arrow", 0.1f));
		});
	
	public ModDatapackRegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry)
	{
		super(output, registry, BUILDER, Set.of(ModSpartanWeaponry.ID));
	}
	
	@Override
	public String getName() 
	{
		return ModSpartanWeaponry.NAME + ": Datapack Registries";
	}
}
