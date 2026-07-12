package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowBaseEntity;

public class ArrowBaseItem extends ArrowItem {
    protected float damageModifier;
    protected float rangeModifier;

    public ArrowBaseItem(float damageModifier, float rangeModifier) {
        super(new Item.Properties());
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            Item.@NotNull TooltipContext tooltipContext,
            List<Component> tooltip,
            @NotNull TooltipFlag flagIn) {
        //        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID +
        // ".modifiers").withStyle(ChatFormatting.GOLD));
        tooltip.add(
                Component.translatable(
                                "tooltip."
                                        + ModSpartanWeaponry.ID
                                        + ".modifiers.projectile.base_damage",
                                Component.translatable(
                                                "tooltip."
                                                        + ModSpartanWeaponry.ID
                                                        + ".modifiers.projectile.base_damage.value",
                                                this.damageModifier)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(
                Component.translatable(
                                "tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range",
                                Component.translatable(
                                                "tooltip."
                                                        + ModSpartanWeaponry.ID
                                                        + ".modifiers.projectile.range.value",
                                                this.rangeModifier)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
    }

    @Override
    public @NotNull AbstractArrow createArrow(
            @NotNull Level levelIn,
            ItemStack stack,
            @NotNull LivingEntity shooter,
            ItemStack weapon) {
        ArrowBaseEntity arrow = new ArrowBaseEntity(levelIn, shooter, stack, weapon);
        ItemStack arrowStack = stack.copy();
        arrowStack.setCount(1);
        arrow.initEntity(this.damageModifier, this.rangeModifier, arrowStack);
        return arrow;
    }

    public void updateFromConfig(float damageModifier, float rangeModifier) {
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
    }
}
