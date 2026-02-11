package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;

public class TomahawkEntity extends ThrowingWeaponEntity {
    public TomahawkEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level) {
        super(type, level);
    }

    public TomahawkEntity(Level level, LivingEntity shooter, ItemStack weapon) {
        super(ModEntities.TOMAHAWK.get(), shooter, level, weapon);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.TOMAHAWK_HIT_GROUND.get();
    }

    @Override
    protected SoundEvent getMobHitSound() {
        return ModSounds.TOMAHAWK_HIT_MOB.get();
    }
}
