package org.xiyu.spartanweaponryunofficial.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.block.ExtendedSkullBlock.Types;
import org.xiyu.spartanweaponryunofficial.block.entity.ExtendedSkullBlockEntity;

import java.util.Map;

public class ExtendedWallSkullBlock extends WallSkullBlock {
    protected static final VoxelShape PIGLIN_SHAPE = Block.box(2.0d, 0.0d, 2.0d, 14.0d, 8.0d, 14.0d);
    protected static final Map<Direction, VoxelShape> PIGLIN_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(2.0d, 4.0d, 8.0d, 14.0d, 12.0d, 16.0d),
            Direction.SOUTH, Block.box(2.0d, 4.0d, 0.0d, 14.0d, 12.0d, 8.0d),
            Direction.EAST, Block.box(0.0d, 4.0d, 2.0d, 8.0d, 12.0d, 14.0d),
            Direction.WEST, Block.box(8.0d, 4.0d, 2.0d, 16.0d, 12.0d, 14.0d)));
    protected static final Map<Direction, VoxelShape> ILLAGER_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0d, 4.0d, 8.0d, 12.0d, 14.0d, 16.0d),
            Direction.SOUTH, Block.box(4.0d, 4.0d, 0.0d, 12.0d, 14.0d, 8.0d),
            Direction.EAST, Block.box(0.0d, 4.0d, 4.0d, 8.0d, 14.0d, 12.0d),
            Direction.WEST, Block.box(8.0d, 4.0d, 4.0d, 16.0d, 14.0d, 12.0d)));
    protected static final Map<Direction, VoxelShape> WITCH_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0d, 4.0d, 8.0d, 12.0d, 16.0d, 16.0d),
            Direction.SOUTH, Block.box(4.0d, 4.0d, 0.0d, 12.0d, 16.0d, 8.0d),
            Direction.EAST, Block.box(0.0d, 4.0d, 4.0d, 8.0d, 16.0d, 12.0d),
            Direction.WEST, Block.box(8.0d, 4.0d, 4.0d, 16.0d, 16.0d, 12.0d)));

    public ExtendedWallSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter getter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return switch (this.getType()) {
            case Types.ZOMBIE_PIGLIN -> PIGLIN_SHAPES.get(blockState.getValue(FACING));
            case Types.ILLAGER -> ILLAGER_SHAPES.get(blockState.getValue(FACING));
            case Types.WITCH -> WITCH_SHAPES.get(blockState.getValue(FACING));
            default -> super.getShape(blockState, getter, blockPos, context);
        };
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ExtendedSkullBlockEntity(blockPos, blockState);
    }
}
