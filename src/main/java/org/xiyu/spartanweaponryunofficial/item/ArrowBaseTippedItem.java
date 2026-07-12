package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowBaseEntity;

public class ArrowBaseTippedItem extends ArrowBaseItem {
    protected String baseName;

    public ArrowBaseTippedItem(String baseName, float damageModifier, float rangeModifier) {
        super(damageModifier, rangeModifier);
        this.baseName = baseName;
    }

    @Override
    public @NotNull AbstractArrow createArrow(
            @NotNull Level level,
            ItemStack stack,
            @NotNull LivingEntity shooter,
            ItemStack weapon) {
        ArrowBaseEntity arrow = new ArrowBaseEntity(level, shooter, stack, weapon);
        ItemStack arrowStack = stack.copy();
        arrowStack.setCount(1);
        arrow.initEntity(this.damageModifier, this.rangeModifier, arrowStack);
        arrow.setPotionEffect(stack);
        return arrow;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            Item.@NotNull TooltipContext tooltipContext,
            List<Component> tooltip,
            @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.add(Component.empty());
        stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion()
                .map(Holder::value)
                .ifPresent(
                        potion ->
                                PotionContents.addPotionTooltip(
                                        potion.getEffects(), tooltip::add, 0.125f, 20.0F));
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        Potion potion =
                stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                        .potion()
                        .map(Holder::value)
                        .orElse(null);
        if (potion == null)
            return Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName);
        var potionKey = net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potion);
        if (potionKey == null)
            return Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName);
        String translationKey =
                "item.spartan_weaponry_unofficial.proj_tipped.effect." + potionKey.getPath();
        return Component.translatable(
                translationKey,
                Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName));
    }
}
