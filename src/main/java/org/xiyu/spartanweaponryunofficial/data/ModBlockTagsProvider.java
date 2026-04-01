package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModBlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry) {
        super(output, registry, ModSpartanWeaponry.ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        this.tag(ModBlockTags.GRASS).add(Blocks.SHORT_GRASS, Blocks.SEAGRASS, Blocks.FERN);
    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Block Tags";
    }
}
