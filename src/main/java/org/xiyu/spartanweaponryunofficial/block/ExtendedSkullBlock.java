package org.xiyu.spartanweaponryunofficial.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.block.entity.ExtendedSkullBlockEntity;

public class ExtendedSkullBlock extends SkullBlock {
    protected static final VoxelShape PIGLIN_SHAPE =
            Block.box(2.0d, 0.0d, 2.0d, 14.0d, 8.0d, 14.0d);
    protected static final VoxelShape ILLAGER_SHAPE =
            Block.box(4.0d, 0.0d, 4.0d, 12.0d, 10.0d, 12.0d);
    protected static final VoxelShape WITCH_SHAPE =
            Block.box(4.0d, 0.0d, 4.0d, 12.0d, 16.0d, 12.0d);

    public ExtendedSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState p_56336_,
            @NotNull BlockGetter p_56337_,
            @NotNull BlockPos p_56338_,
            @NotNull CollisionContext p_60482_) {
        return switch (this.getType()) {
            case Types.ZOMBIE_PIGLIN -> PIGLIN_SHAPE;
            case Types.ILLAGER -> ILLAGER_SHAPE;
            case Types.WITCH -> WITCH_SHAPE;
            default -> super.getShape(p_56336_, p_56337_, p_56338_, p_60482_);
        };
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(
            @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ExtendedSkullBlockEntity(blockPos, blockState);
    }

    public enum Types implements SkullBlock.Type, StringRepresentable {
        BLAZE("blaze"),
        ENDERMAN("enderman"),
        SPIDER("spider"),
        CAVE_SPIDER("cave_spider"),
        //        PIGLIN("piglin"),
        ZOMBIE_PIGLIN("zombie_piglin"),
        HUSK("husk"),
        STRAY("stray"),
        DROWNED("drowned"),
        ILLAGER("illager"),
        WITCH("witch");

        private final String name;

        Types(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
