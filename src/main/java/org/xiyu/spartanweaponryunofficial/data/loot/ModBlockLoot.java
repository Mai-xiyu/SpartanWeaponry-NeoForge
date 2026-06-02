package org.xiyu.spartanweaponryunofficial.data.loot;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModBlocks;

public class ModBlockLoot extends BlockLootSubProvider {
    private static final Set<Block> heads =
            ImmutableSet.of(
                    ModBlocks.BLAZE_HEAD.get(),
                    ModBlocks.ENDERMAN_HEAD.get(),
                    ModBlocks.SPIDER_HEAD.get(),
                    ModBlocks.CAVE_SPIDER_HEAD.get(),
                    ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(),
                    ModBlocks.HUSK_HEAD.get(),
                    ModBlocks.STRAY_SKULL.get(),
                    ModBlocks.DROWNED_HEAD.get(),
                    ModBlocks.ILLAGER_HEAD.get(),
                    ModBlocks.WITCH_HEAD.get());

    public ModBlockLoot(HolderLookup.Provider lookupProvider) {
        super(
                heads.stream().map(Block::asItem).collect(Collectors.toUnmodifiableSet()),
                FeatureFlags.REGISTRY.allFlags(),
                lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return heads;
    }

    @Override
    protected void generate() {
        for (Block block : heads) this.dropSelf(block);
    }
}
