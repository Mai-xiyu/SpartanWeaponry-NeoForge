package com.oblivioussp.spartanweaponry.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.tags.ModDamageTypeTags;
import com.oblivioussp.spartanweaponry.init.ModDamageTypes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider
{
	public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<Provider> registry, @Nullable ExistingFileHelper existingFileHelper) 
	{
		super(output, registry, ModSpartanWeaponry.ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider registry) 
	{
		tag(DamageTypeTags.IS_PROJECTILE).add(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER, ModDamageTypes.KEY_THROWN_WEAPON_MOB, ModDamageTypes.KEY_ARMOR_PIERCING_BOLT);
		tag(ModDamageTypeTags.IS_ARMOR_PIERCING).add(ModDamageTypes.KEY_ARMOR_PIERCING_MELEE, ModDamageTypes.KEY_ARMOR_PIERCING_BOLT);
	}
}
