package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

public class ExtendedSkullItem extends StandingAndWallBlockItem {

    public ExtendedSkullItem(
            Block floorBlockIn, Block wallBlockIn, Properties builder, Direction directionIn) {
        super(floorBlockIn, wallBlockIn, builder, directionIn);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            Item.@NotNull TooltipContext tooltipContext,
            List<Component> tooltip,
            @NotNull TooltipFlag flagIn) {
        tooltip.add(
                Component.translatable(
                                "tooltip."
                                        + ModSpartanWeaponry.ID
                                        + "."
                                        + BuiltInRegistries.ITEM.getKey(this).getPath()
                                        + ".desc")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }
}
