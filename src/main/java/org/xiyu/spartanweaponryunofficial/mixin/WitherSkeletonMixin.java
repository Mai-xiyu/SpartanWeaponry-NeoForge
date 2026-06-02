package org.xiyu.spartanweaponryunofficial.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.util.Config;

/**
 * Applies configured Spartan melee weapon replacement to wither skeletons that kept their vanilla
 * stone sword.
 */
@Mixin(WitherSkeleton.class)
public class WitherSkeletonMixin extends MobMixin {
    @Inject(
            at = @At("TAIL"),
            method =
                    "populateDefaultEquipmentSlots(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/DifficultyInstance;)V")
    protected void populateDefaultEquipmentSlots(
            RandomSource randomIn, DifficultyInstance difficultyIn, CallbackInfo callback) {
        this.spartanWeaponry$attemptReplacingMainHandItemRandom(
                ModItemTags.WITHER_SKELETON_SPAWN_WEAPONS,
                difficultyIn,
                !this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.STONE_SWORD)
                        || Config.INSTANCE.disableSpawningWitherSkeletonWithWeapon.get(),
                Config.INSTANCE.witherSkeletonWithMeleeSpawnChanceNormal.get().floatValue(),
                Config.INSTANCE.witherSkeletonWithMeleeSpawnChanceHard.get().floatValue());
    }
}
