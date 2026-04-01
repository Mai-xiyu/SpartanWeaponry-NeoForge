package org.xiyu.spartanweaponryunofficial.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;

@Mixin(Player.class)
public class PlayerMixin {
    // 26.1: Sweep damage ratio is now an attribute (Attributes.SWEEPING_DAMAGE_RATIO)
    // read inside Player.doSweepAttack(). We modify the attribute value read there
    // to inject our custom sweep trait magnitude.
    @ModifyExpressionValue(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double modifySweepDamageRatio(double original) {
        Player player = (Player) (Object) this;
        ItemStack weaponStack = player.getMainHandItem();
        if (weaponStack.getItem() instanceof IWeaponTraitContainer<?> container) {
            WeaponTrait sweepTrait = container.getFirstWeaponTraitWithType(WeaponTraits.TYPE_SWEEP_DAMAGE);
            if (sweepTrait != null) {
                float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (damage > 0) {
                    return sweepTrait.getMagnitude() - (1.0f / damage);
                }
            }
        }
        return original;
    }

    // 26.1: Player.attack() no longer calls DamageSources.playerAttack() directly.
    // It now calls createAttackSource(ItemStack) which internally delegates to playerAttack.
    // We modify the result of createAttackSource to replace with armor-piercing damage type.
    @ModifyExpressionValue(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;createAttackSource(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/damagesource/DamageSource;"))
    private DamageSource modifyAttackSource(DamageSource originalSource, Entity targetIn) {
        Player playerIn = (Player) (Object) this;
        ItemStack weaponStack = playerIn.getMainHandItem();
        if (weaponStack.getItem() instanceof IWeaponTraitContainer<?> container) {
            WeaponTrait armorPiercingTrait = container.getFirstWeaponTraitWithType(WeaponTraits.TYPE_ARMOR_PIERCING);
            if (armorPiercingTrait != null) {
                return ModDamageTypes.armorPiercingMelee(playerIn);
            }
        }
        return originalSource;
    }
}
