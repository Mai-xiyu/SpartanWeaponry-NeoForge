package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

import java.util.List;

public class DamageAbsorbWeaponTrait extends MeleeCallbackWeaponTrait {

    public DamageAbsorbWeaponTrait(String type, String modId) {
        super(type, modId, TraitQuality.POSITIVE);
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", this.modId, this.type), this.magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
    }

    @Override
    public float modifyDamageTaken(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker,
                                   LivingEntity victim) {
        ItemStack heldItemVictim = victim.getMainHandItem();
        if (!heldItemVictim.isEmpty()) {
            if (victim.level() instanceof ServerLevel serverLevel) {
                heldItemVictim.hurtAndBreak(Mth.floor(baseDamage * this.magnitude), serverLevel, victim, (item) -> {
                    victim.onEquippedItemBroken(item, EquipmentSlot.MAINHAND);
                    if (victim instanceof Player player)
                        net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, heldItemVictim, InteractionHand.MAIN_HAND);
                });
            }
            return baseDamage * (1.0f - this.magnitude);
        }

        return baseDamage;
    }
}
