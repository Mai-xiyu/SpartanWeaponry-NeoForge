package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;

public class BoltItem extends Item {
    protected float damageModifier;
    protected float rangeModifier;
    protected float armorPiercingFactor;

    public BoltItem(float damageModifier, float rangeModifier, float armorPiercingFactor) {
        super(new Item.Properties());
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
        this.armorPiercingFactor = armorPiercingFactor;
    }

    public BoltEntity createBolt(
            Level level, ItemStack stack, LivingEntity shooter, ItemStack weaponStack) {
        ItemStack boltStack = stack.copy();
        boltStack.setCount(1);
        BoltEntity bolt = new BoltEntity(shooter, level, boltStack, weaponStack);
        bolt.initEntity(
                this.damageModifier, this.rangeModifier, this.armorPiercingFactor, boltStack);
        if (bolt.isValid()) return bolt;

        return null;
    }

    public boolean isInfinite(ItemStack stack, ItemStack crossbow, Player player) {
        int enchant =
                EnchantmentHelper.getItemEnchantmentLevel(
                        player.level()
                                .registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT)
                                .getHolderOrThrow(Enchantments.INFINITY),
                        crossbow);
        return enchant > 0 && this.getClass() == BoltItem.class;
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
        tooltip.add(
                Component.translatable(
                                "tooltip."
                                        + ModSpartanWeaponry.ID
                                        + ".modifiers.projectile.armor_piercing_factor",
                                Component.translatable(
                                                "tooltip."
                                                        + ModSpartanWeaponry.ID
                                                        + ".modifiers.projectile.armor_piercing_factor.value",
                                                this.armorPiercingFactor * 100.0f)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
    }

    public void updateFromConfig(
            float damageModifier, float rangeModifier, float armorPiercingFactor) {
        this.damageModifier = damageModifier;
        this.rangeModifier = rangeModifier;
        this.armorPiercingFactor = armorPiercingFactor;
    }
}
