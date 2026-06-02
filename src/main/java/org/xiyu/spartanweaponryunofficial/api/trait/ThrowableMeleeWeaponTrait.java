package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;

public class ThrowableMeleeWeaponTrait extends WeaponTrait implements IActionTraitCallback {

    public ThrowableMeleeWeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
        this.isMelee = true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            ItemStack usingStackIn, Level levelIn, Player playerIn, InteractionHand handIn) {
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.consume(usingStackIn);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public Optional<IActionTraitCallback> getActionCallback() {
        return Optional.of(this);
    }

    @Override
    public void releaseUsing(
            ItemStack stackIn,
            Level levelIn,
            LivingEntity entityLivingIn,
            int timeLeftIn,
            float attackDamage) {
        if (entityLivingIn instanceof Player player) {

            int charge = this.getUseDuration(stackIn, entityLivingIn) - timeLeftIn;

            if (charge >= 5) charge = 5;

            if (!levelIn.isClientSide && charge > 2) {
                ThrowingWeaponEntity thrown =
                        new ThrowingWeaponEntity(
                                ModEntities.THROWING_WEAPON.get(), player, levelIn, stackIn);
                thrown.setWeapon(stackIn);
                thrown.shootFromRotation(
                        player,
                        player.xRotO,
                        player.yRotO,
                        0.0F,
                        1.5f * (charge / 10.0f + 0.5f),
                        0.5f);
                thrown.setBaseDamage(attackDamage + 1.0f);

                // Apply enchantments as necessary - using new 1.21 API
                RegistryAccess registryAccess = levelIn.registryAccess();
                int j =
                        EnchantmentHelper.getItemEnchantmentLevel(
                                registryAccess
                                        .registryOrThrow(Registries.ENCHANTMENT)
                                        .getHolderOrThrow(Enchantments.SHARPNESS),
                                stackIn);
                if (j > 0) {
                    thrown.setBaseDamage(thrown.getBaseDamage() + j * 0.5d + 0.5d);
                }
                // Knockback is handled in ThrowingWeaponEntity.onHitEntity by reading from weapon
                // enchantments
                // Fire aspect - set entity on fire
                int fireAspect =
                        EnchantmentHelper.getItemEnchantmentLevel(
                                registryAccess
                                        .registryOrThrow(Registries.ENCHANTMENT)
                                        .getHolderOrThrow(Enchantments.FIRE_ASPECT),
                                stackIn);
                if (fireAspect > 0) {
                    thrown.setRemainingFireTicks(100 * 20); // 100 seconds in ticks
                }

                if (player.getAbilities().instabuild)
                    thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                else if (thrown.isValidThrowingWeapon()) {
                    stackIn.shrink(1);
                    if (stackIn.getCount() <= 0) player.getInventory().removeItem(stackIn);
                }

                if (thrown.isValidThrowingWeapon()) {
                    levelIn.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            ModSounds.THROWN_WEAPON_THROW.get(),
                            SoundSource.PLAYERS,
                            0.5f,
                            0.4f / (levelIn.random.nextFloat() * 0.4f + 0.8f));
                    levelIn.addFreshEntity(thrown);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(stackIn.getItem()));
        }
    }
}
