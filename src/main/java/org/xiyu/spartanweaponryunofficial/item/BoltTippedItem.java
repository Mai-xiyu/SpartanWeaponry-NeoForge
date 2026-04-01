package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;

import java.util.List;

public class BoltTippedItem extends BoltItem {
    protected String baseName;

    public BoltTippedItem(Item.Properties properties, String baseName, float damageModifier, float rangeModifier, float armorPiercingFactor) {
        super(properties, damageModifier, rangeModifier, armorPiercingFactor);
        this.baseName = baseName;
    }

    @Override
    public BoltEntity createBolt(Level level, ItemStack stack, LivingEntity shooter, ItemStack weaponStack) {
        ItemStack boltStack = stack.copy();
        boltStack.setCount(1);
        BoltEntity bolt = new BoltEntity(shooter, level, boltStack, weaponStack);
        bolt.initEntity(this.damageModifier, this.rangeModifier, this.armorPiercingFactor, boltStack);
        bolt.setPotionEffect(stack);
        if (bolt.isValid())
            return bolt;

        return null;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);

        tooltip.add(Component.empty());
        stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion().map(Holder::value).ifPresent(potion -> PotionContents.addPotionTooltip(potion.getEffects(), tooltip::add, 0.125f, 20.0F));
    }

    public @NotNull Component getName(ItemStack stack) {
        Potion potion = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion().map(Holder::value).orElse(null);
        if (potion == null)
            return Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName);
        var potionKey = net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potion);
        if (potionKey == null)
            return Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName);
        String translationKey = "item.spartan_weaponry_unofficial.proj_tipped.effect." + potionKey.getPath();
        return Component.translatable(translationKey, Component.translatable("item." + ModSpartanWeaponry.ID + "." + this.baseName));
    }
}
