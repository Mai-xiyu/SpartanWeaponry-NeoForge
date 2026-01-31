package com.oblivioussp.spartanweaponry.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.tags.ModEntityTypeTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider 
{

	public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, @Nullable ExistingFileHelper existingFileHelperIn) 
	{
		super(output, registry, ModSpartanWeaponry.ID, existingFileHelperIn);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider registry)
	{
		tag(ModEntityTypeTags.CREEPERS).add(EntityType.CREEPER);
		tag(ModEntityTypeTags.HUMANOIDS).addTag(EntityTypeTags.RAIDERS).add(EntityType.VILLAGER).add(EntityType.PLAYER);
		tag(ModEntityTypeTags.ENDER).add(EntityType.ENDERMAN, EntityType.ENDER_DRAGON, EntityType.ENDERMITE, EntityType.SHULKER);
	}
	
	@Override
	public String getName()
	{
		return ModSpartanWeaponry.NAME + ": Entity Type Tags";
	}

}
