package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.util.Config;

public class ArrowExplosiveEntity extends ArrowEntitySW {
    public ArrowExplosiveEntity(EntityType<? extends ArrowEntitySW> type, Level level) {
        super(type, level);
    }

    public ArrowExplosiveEntity(Level level, double x, double y, double z, ItemStack weapon) {
        super(ModEntities.ARROW_EXPLOSIVE.get(), level, x, y, z, weapon);
    }

    public ArrowExplosiveEntity(Level level, LivingEntity shooter, ItemStack weapon) {
        super(ModEntities.ARROW_EXPLOSIVE.get(), level, shooter, weapon);
    }

    @Override
    protected void initStats() {
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        living.hurtTime = 0;
        this.explode();
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();
        if (level.isClientSide && !this.inGround) {
            level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }

        if (this.inGround) {
            this.explode();
        }
    }

    protected void explode() {
        Level level = this.level();
        if (!level.isClientSide) {
            boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            level.explode(this, this.xOld, this.yOld, this.zOld, Config.INSTANCE.arrowExplosiveExplosionStrength.get().floatValue(), !Config.INSTANCE.disableTerrainDamage.get() && mobGriefing ? ExplosionInteraction.TNT : ExplosionInteraction.NONE);
            this.discard();
        }
    }
}
