package org.xiyu.spartanweaponryunofficial.api.trait;

import com.google.common.collect.ImmutableMultimap;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

public class SweepWeaponTrait extends WeaponTraitWithMagnitude implements IMeleeTraitCallback {
    public static final ResourceLocation SWEEP_DAMAGE_RATIO_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(
                    SpartanWeaponryAPI.MOD_ID, "sweep_damage_ratio");

    public SweepWeaponTrait(String propType, String propModId) {
        super(propType, propModId, TraitQuality.POSITIVE);
        this.isMelee = true;
    }

    @Override
    public Optional<IMeleeTraitCallback> getMeleeCallback() {
        return Optional.of(this);
    }

    /**
     * Applies the sweep bonus through the vanilla {@link Attributes#SWEEPING_DAMAGE_RATIO}
     * attribute, which is how 1.21 computes sweep damage in {@code Player.attack}.
     */
    @Override
    public void onModifyAttributesMelee(
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(
                Attributes.SWEEPING_DAMAGE_RATIO.value(),
                new AttributeModifier(
                        SWEEP_DAMAGE_RATIO_MODIFIER,
                        this.getMagnitude(),
                        AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        if (this.level == 1)
            tooltip.add(
                    tooltipIndent()
                            .append(
                                    Component.translatable(
                                                    String.format(
                                                            "tooltip.%s.trait.%s.fixed.desc",
                                                            SpartanWeaponryAPI.MOD_ID, this.type),
                                                    this.magnitude * 100.0f)
                                            .withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
        else
            tooltip.add(
                    tooltipIndent()
                            .append(
                                    Component.translatable(
                                                    String.format(
                                                            "tooltip.%s.trait.%s.desc",
                                                            this.modId, this.type),
                                                    this.magnitude * 100.0f)
                                            .withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
    }

    @Override
    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        // In 1.21, we need to compare using resource location since Enchantments constants are now
        // ResourceKeys
        // We can't directly compare Enchantment to ResourceKey, so we match by location
        return false; // Sweeping edge compatibility is now handled in
        // SwordBaseItem.canApplyAtEnchantingTable
    }

    @Override
    public boolean canPerformToolAction(ItemStack stack, ItemAbility action) {
        return action == ItemAbilities.SWORD_SWEEP;
    }
}
