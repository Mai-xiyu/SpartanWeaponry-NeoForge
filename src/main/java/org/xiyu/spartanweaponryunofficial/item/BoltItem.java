package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BoltItem extends Item {
    protected float damageModifier;
    protected float rangeModifier;
    protected float armorPiercingFactor;

    public BoltItem(Item.Properties properties, float damageModifier, float rangeModifier, float armorPiercingFactor) {
        super(properties);
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
        this.armorPiercingFactor = armorPiercingFactor;
    }

    public BoltEntity createBolt(Level level, ItemStack stack, LivingEntity shooter, ItemStack weaponStack) {
        ItemStack boltStack = stack.copy();
        boltStack.setCount(1);
        BoltEntity bolt = new BoltEntity(shooter, level, boltStack, weaponStack);
        bolt.initEntity(this.damageModifier, this.rangeModifier, this.armorPiercingFactor, boltStack);
        if (bolt.isValid())
            return bolt;

        return null;
    }

    public boolean isInfinite(ItemStack stack, ItemStack crossbow, Player player) {
        return this.getClass() == BoltItem.class;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay,
                                @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        this.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.forEach(tooltipAdder);
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
//		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.base_damage.value", this.damageModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.range.value", this.rangeModifier).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.armor_piercing_factor", Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.projectile.armor_piercing_factor.value", this.armorPiercingFactor * 100.0f).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    }

    public void updateFromConfig(float damageModifier, float rangeModifier, float armorPiercingFactor) {
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
        this.armorPiercingFactor = armorPiercingFactor;
    }
}


