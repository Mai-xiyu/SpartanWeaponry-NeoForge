package com.oblivioussp.spartanweaponry.data;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.init.ModBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockModelProvider extends BlockStateProvider
{
	
	public ModBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, ModSpartanWeaponry.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() 
	{
		generateSkullModels(ModBlocks.BLAZE_HEAD.get(), ModBlocks.BLAZE_WALL_HEAD.get());
		generateSkullModels(ModBlocks.ENDERMAN_HEAD.get(), ModBlocks.ENDERMAN_WALL_HEAD.get());
		generateSkullModels(ModBlocks.SPIDER_HEAD.get(), ModBlocks.SPIDER_WALL_HEAD.get());
		generateSkullModels(ModBlocks.CAVE_SPIDER_HEAD.get(), ModBlocks.CAVE_SPIDER_WALL_HEAD.get());
		generateSkullModels(ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(), ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get());
		generateSkullModels(ModBlocks.HUSK_HEAD.get(), ModBlocks.HUSK_WALL_HEAD.get());
		generateSkullModels(ModBlocks.STRAY_SKULL.get(), ModBlocks.STRAY_WALL_SKULL.get());
		generateSkullModels(ModBlocks.DROWNED_HEAD.get(), ModBlocks.DROWNED_WALL_HEAD.get());
		generateSkullModels(ModBlocks.ILLAGER_HEAD.get(), ModBlocks.ILLAGER_WALL_HEAD.get());
		generateSkullModels(ModBlocks.WITCH_HEAD.get(), ModBlocks.WITCH_WALL_HEAD.get());
	}
	
	protected void generateSkullModels(Block head, Block wallHead)
	{
		getVariantBuilder(head).partialState().setModels(ConfiguredModel.builder().modelFile(new ExistingModelFile(mcLoc("block/skull"), this.models().existingFileHelper)).build());
		getVariantBuilder(wallHead).partialState().setModels(ConfiguredModel.builder().modelFile(new ExistingModelFile(mcLoc("block/skull"), this.models().existingFileHelper)).build());
		itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(head).getPath(), mcLoc("item/template_skull"));
	}
}