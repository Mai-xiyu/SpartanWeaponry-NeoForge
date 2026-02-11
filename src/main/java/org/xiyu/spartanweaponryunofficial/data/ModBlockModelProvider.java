package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModBlocks;

public class ModBlockModelProvider extends BlockStateProvider {

    public ModBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModSpartanWeaponry.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.generateSkullModels(ModBlocks.BLAZE_HEAD.get(), ModBlocks.BLAZE_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.ENDERMAN_HEAD.get(), ModBlocks.ENDERMAN_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.SPIDER_HEAD.get(), ModBlocks.SPIDER_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.CAVE_SPIDER_HEAD.get(), ModBlocks.CAVE_SPIDER_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(), ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.HUSK_HEAD.get(), ModBlocks.HUSK_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.STRAY_SKULL.get(), ModBlocks.STRAY_WALL_SKULL.get());
        this.generateSkullModels(ModBlocks.DROWNED_HEAD.get(), ModBlocks.DROWNED_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.ILLAGER_HEAD.get(), ModBlocks.ILLAGER_WALL_HEAD.get());
        this.generateSkullModels(ModBlocks.WITCH_HEAD.get(), ModBlocks.WITCH_WALL_HEAD.get());
    }

    protected void generateSkullModels(Block head, Block wallHead) {
        this.getVariantBuilder(head).partialState().setModels(ConfiguredModel.builder().modelFile(new ExistingModelFile(this.mcLoc("block/skull"), this.models().existingFileHelper)).build());
        this.getVariantBuilder(wallHead).partialState().setModels(ConfiguredModel.builder().modelFile(new ExistingModelFile(this.mcLoc("block/skull"), this.models().existingFileHelper)).build());
        this.itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(head).getPath(), this.mcLoc("item/template_skull"));
    }
}