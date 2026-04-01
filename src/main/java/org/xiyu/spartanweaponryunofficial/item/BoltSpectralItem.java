package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltSpectralEntity;

import java.util.List;

public class BoltSpectralItem extends BoltItem {
    public BoltSpectralItem(Item.Properties properties, float damageModifier, float rangeModifier, float armorPiercingFactor) {
        super(properties, damageModifier, rangeModifier, armorPiercingFactor);
    }

    @Override
    public BoltEntity createBolt(Level level, ItemStack stack, LivingEntity shooter, ItemStack weaponStack) {
        ItemStack boltStack = stack.copy();
        boltStack.setCount(1);
        BoltEntity bolt = new BoltSpectralEntity(shooter, level, boltStack, weaponStack);
        bolt.initEntity(this.damageModifier, this.rangeModifier, this.armorPiercingFactor, boltStack);
        return bolt;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.impact.glowing").withStyle(ChatFormatting.BLUE));
    }
}
