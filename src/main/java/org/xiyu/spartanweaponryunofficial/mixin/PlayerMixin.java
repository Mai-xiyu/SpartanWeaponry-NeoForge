package org.xiyu.spartanweaponryunofficial.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;

/**
 * Swaps the player attack damage source for an armor-piercing one when the held weapon has the
 * armor-piercing trait. Sweep damage bonuses are applied through the vanilla SWEEPING_DAMAGE_RATIO
 * attribute instead (see SweepWeaponTrait).
 */
@Mixin(Player.class)
public class PlayerMixin {
    @ModifyExpressionValue(
            method = "attack",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/damagesource/DamageSources;playerAttack(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;"))
    private DamageSource damagePlayerAttack(DamageSource originalSource, Entity targetIn) {
        Player playerIn = ((Player) originalSource.getEntity());
        ItemStack weaponStack = playerIn.getMainHandItem();
        if (weaponStack.getItem() instanceof IWeaponTraitContainer<?> container) {
            WeaponTrait armorPiercingTrait =
                    container.getFirstWeaponTraitWithType(WeaponTraits.TYPE_ARMOR_PIERCING);
            if (armorPiercingTrait != null) {
                return ModDamageTypes.armorPiercingMelee(playerIn);
            }
        }
        // Otherwise, return the basic player attack damage source
        return originalSource;
    }
}
