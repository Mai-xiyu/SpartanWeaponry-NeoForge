package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ArrowItemSW extends ArrowItem {
    protected float damageModifier = 1.0f;
    protected float rangeModifier = 1.0f;

    public ArrowItemSW(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay,
                                @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        this.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.forEach(tooltipAdder);
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        //	tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage.value", this.damageModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range.value", this.rangeModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    }
}
