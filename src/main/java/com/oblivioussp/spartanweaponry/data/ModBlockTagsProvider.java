package com.oblivioussp.spartanweaponry.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.tags.ModBlockTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider
{
	public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, @Nullable ExistingFileHelper existingFileHelper) 
	{
		super(output, registry, ModSpartanWeaponry.ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider p_256380_)
	{
		tag(ModBlockTags.GRASS).add(Blocks.SHORT_GRASS, Blocks.SEAGRASS, Blocks.FERN);
	}
	
	@Override
	public String getName() 
	{
		return ModSpartanWeaponry.NAME + " Block Tags";
	}
}
