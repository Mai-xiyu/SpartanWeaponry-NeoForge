package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.block.entity.ExtendedSkullBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModSpartanWeaponry.ID);

    public static final DeferredHolder<
                    BlockEntityType<?>, BlockEntityType<ExtendedSkullBlockEntity>>
            EXTENDED_SKULL_TYPE =
                    REGISTRY.register(
                            "skull_extended",
                            () ->
                                    BlockEntityType.Builder.of(
                                                    ExtendedSkullBlockEntity::new,
                                                    ModBlocks.BLAZE_HEAD.get(),
                                                    ModBlocks.BLAZE_WALL_HEAD.get(),
                                                    ModBlocks.ENDERMAN_HEAD.get(),
                                                    ModBlocks.ENDERMAN_WALL_HEAD.get(),
                                                    ModBlocks.SPIDER_HEAD.get(),
                                                    ModBlocks.SPIDER_WALL_HEAD.get(),
                                                    ModBlocks.CAVE_SPIDER_HEAD.get(),
                                                    ModBlocks.CAVE_SPIDER_WALL_HEAD.get(),
                                                    ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(),
                                                    ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get(),
                                                    ModBlocks.HUSK_HEAD.get(),
                                                    ModBlocks.HUSK_WALL_HEAD.get(),
                                                    ModBlocks.STRAY_SKULL.get(),
                                                    ModBlocks.STRAY_WALL_SKULL.get(),
                                                    ModBlocks.DROWNED_HEAD.get(),
                                                    ModBlocks.DROWNED_WALL_HEAD.get(),
                                                    ModBlocks.ILLAGER_HEAD.get(),
                                                    ModBlocks.ILLAGER_WALL_HEAD.get(),
                                                    ModBlocks.WITCH_HEAD.get(),
                                                    ModBlocks.WITCH_WALL_HEAD.get())
                                            .build(null));
}
