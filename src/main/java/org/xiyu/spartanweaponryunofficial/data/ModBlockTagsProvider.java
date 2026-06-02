package org.xiyu.spartanweaponryunofficial.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModBlockTags;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registry,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registry, ModSpartanWeaponry.ID, existingFileHelper);
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
