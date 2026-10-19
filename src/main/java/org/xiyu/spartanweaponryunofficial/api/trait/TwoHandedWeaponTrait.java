package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

public class TwoHandedWeaponTrait extends MeleeCallbackWeaponTrait {
    public TwoHandedWeaponTrait(String typeIn, String modIdIn) {
        super(typeIn, modIdIn, TraitQuality.NEGATIVE);
        this.isMelee = true;
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
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
    public void onItemUpdate(
            WeaponMaterial material,
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int itemSlot,
            boolean isSelected) {}

    @Override
    public float modifyDamageDealt(
            WeaponMaterial material,
            float baseDamage,
            DamageSource source,
            LivingEntity attacker,
            LivingEntity victim) {
        if (!attacker.getMainHandItem().isEmpty() && !attacker.getOffhandItem().isEmpty()) {
            return baseDamage * (1.0f - this.magnitude);
        }
        return baseDamage;
    }
}
