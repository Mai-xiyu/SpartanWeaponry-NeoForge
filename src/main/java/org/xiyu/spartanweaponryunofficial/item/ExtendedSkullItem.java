package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExtendedSkullItem extends StandingAndWallBlockItem {

    public ExtendedSkullItem(Block floorBlockIn, Block wallBlockIn, Properties builder, Direction directionIn) {
        super(floorBlockIn, wallBlockIn, directionIn, builder);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay,
                                @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        this.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.forEach(tooltipAdder);
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + BuiltInRegistries.ITEM.getKey(this).getPath() + ".desc").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
