package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

import java.util.List;

public class SweepWeaponTrait extends WeaponTraitWithMagnitude {

    public SweepWeaponTrait(String propType, String propModId) {
        super(propType, propModId, TraitQuality.POSITIVE);
        this.isMelee = true;
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        if (this.level == 1)
            tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.fixed.desc", SpartanWeaponryAPI.MOD_ID, this.type), this.magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
        else
            tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", this.modId, this.type), this.magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
    }

    @Override
    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        // In 1.21, we need to compare using resource location since Enchantments constants are now ResourceKeys
        // We can't directly compare Enchantment to ResourceKey, so we match by location
        return false; // Sweeping edge compatibility is now handled in SwordBaseItem.canApplyAtEnchantingTable
    }

    @Override
    public boolean canPerformToolAction(ItemStack stack, ItemAbility action) {
        return action == ItemAbilities.SWORD_SWEEP;
    }
}
