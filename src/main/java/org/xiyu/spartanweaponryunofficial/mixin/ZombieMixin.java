package org.xiyu.spartanweaponryunofficial.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.util.Config;

/**
 * Applies configured Spartan melee weapon replacement after vanilla zombie equipment is populated.
 */
@Mixin(Zombie.class)
public class ZombieMixin extends MobMixin {
    @Inject(
            at = @At("TAIL"),
            method =
                    "populateDefaultEquipmentSlots(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/DifficultyInstance;)V")
    protected void populateDefaultEquipmentSlots(
            RandomSource randomIn, DifficultyInstance difficultyIn, CallbackInfo callback) {
        this.spartanWeaponry$attemptReplacingMainHandItemRandom(
                ModItemTags.ZOMBIE_SPAWN_WEAPONS,
                randomIn,
                difficultyIn,
                Config.INSTANCE.disableSpawningZombieWithWeapon.get(),
                Config.INSTANCE.zombieWithMeleeSpawnChanceNormal.get().floatValue(),
                Config.INSTANCE.zombieWithMeleeSpawnChanceHard.get().floatValue());
    }
}
