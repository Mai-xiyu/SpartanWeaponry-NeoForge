package org.xiyu.spartanweaponryunofficial.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.util.Config;

/**
 * Applies configured Spartan melee weapon replacement after vanilla piglin brute equipment is
 * populated.
 */
@Mixin(PiglinBrute.class)
public class PiglinBruteMixin extends MobMixin {
    @Inject(
            at = @At("TAIL"),
            method =
                    "populateDefaultEquipmentSlots(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/DifficultyInstance;)V")
    protected void populateDefaultEquipmentSlots(
            RandomSource randomIn, DifficultyInstance difficultyIn, CallbackInfo callback) {
        this.spartanWeaponry$attemptReplacingMainHandItemRandom(
                ModItemTags.PIGLIN_BRUTE_SPAWN_WEAPONS,
                randomIn,
                difficultyIn,
                Config.INSTANCE.disableSpawningPiglinWithWeapon.get(),
                Config.INSTANCE.piglinWithMeleeSpawnChanceNormal.get().floatValue(),
                Config.INSTANCE.piglinWithMeleeSpawnChanceHard.get().floatValue());
    }
}
