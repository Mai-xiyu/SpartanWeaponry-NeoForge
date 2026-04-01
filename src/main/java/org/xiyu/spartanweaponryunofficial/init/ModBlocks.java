package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.block.ExtendedSkullBlock;
import org.xiyu.spartanweaponryunofficial.block.ExtendedWallSkullBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(ModSpartanWeaponry.ID);

    private static Block.Properties skullProps() {
        return Block.Properties.of().strength(1.0f).pushReaction(PushReaction.DESTROY);
    }

    public static final DeferredHolder<Block, Block> BLAZE_HEAD = REGISTRY.registerBlock("blaze_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.BLAZE, props), skullProps());
    public static final DeferredHolder<Block, Block> BLAZE_WALL_HEAD = REGISTRY.registerBlock("blaze_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.BLAZE, props), skullProps());
    public static final DeferredHolder<Block, Block> ENDERMAN_HEAD = REGISTRY.registerBlock("enderman_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.ENDERMAN, props), skullProps());
    public static final DeferredHolder<Block, Block> ENDERMAN_WALL_HEAD = REGISTRY.registerBlock("enderman_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.ENDERMAN, props), skullProps());
    public static final DeferredHolder<Block, Block> SPIDER_HEAD = REGISTRY.registerBlock("spider_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.SPIDER, props), skullProps());
    public static final DeferredHolder<Block, Block> SPIDER_WALL_HEAD = REGISTRY.registerBlock("spider_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.SPIDER, props), skullProps());
    public static final DeferredHolder<Block, Block> CAVE_SPIDER_HEAD = REGISTRY.registerBlock("cave_spider_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.CAVE_SPIDER, props), skullProps());
    public static final DeferredHolder<Block, Block> CAVE_SPIDER_WALL_HEAD = REGISTRY.registerBlock("cave_spider_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.CAVE_SPIDER, props), skullProps());
    public static final DeferredHolder<Block, Block> ZOMBIFIED_PIGLIN_HEAD = REGISTRY.registerBlock("zombified_piglin_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.ZOMBIE_PIGLIN, props), skullProps());
    public static final DeferredHolder<Block, Block> ZOMBIFIED_PIGLIN_WALL_HEAD = REGISTRY.registerBlock("zombified_piglin_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.ZOMBIE_PIGLIN, props), skullProps());
    public static final DeferredHolder<Block, Block> HUSK_HEAD = REGISTRY.registerBlock("husk_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.HUSK, props), skullProps());
    public static final DeferredHolder<Block, Block> HUSK_WALL_HEAD = REGISTRY.registerBlock("husk_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.HUSK, props), skullProps());
    public static final DeferredHolder<Block, Block> STRAY_SKULL = REGISTRY.registerBlock("stray_skull", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.STRAY, props), skullProps());
    public static final DeferredHolder<Block, Block> STRAY_WALL_SKULL = REGISTRY.registerBlock("stray_wall_skull", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.STRAY, props), skullProps());
    public static final DeferredHolder<Block, Block> DROWNED_HEAD = REGISTRY.registerBlock("drowned_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.DROWNED, props), skullProps());
    public static final DeferredHolder<Block, Block> DROWNED_WALL_HEAD = REGISTRY.registerBlock("drowned_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.DROWNED, props), skullProps());
    public static final DeferredHolder<Block, Block> ILLAGER_HEAD = REGISTRY.registerBlock("illager_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.ILLAGER, props), skullProps());
    public static final DeferredHolder<Block, Block> ILLAGER_WALL_HEAD = REGISTRY.registerBlock("illager_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.ILLAGER, props), skullProps());
    public static final DeferredHolder<Block, Block> WITCH_HEAD = REGISTRY.registerBlock("witch_head", props -> new ExtendedSkullBlock(ExtendedSkullBlock.Types.WITCH, props), skullProps());
    public static final DeferredHolder<Block, Block> WITCH_WALL_HEAD = REGISTRY.registerBlock("witch_wall_head", props -> new ExtendedWallSkullBlock(ExtendedSkullBlock.Types.WITCH, props), skullProps());
}
