package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.util.Config;

public class DynamiteEntity extends ThrowableItemProjectile {
    protected int timer;
    //    protected boolean stickToSurface = false;
    protected int fuseTicks;

    public DynamiteEntity(EntityType<? extends DynamiteEntity> type, Level level) {
        super(type, level);
        this.fuseTicks = Config.INSTANCE.fuseTicksDynamite.get();
    }

    public DynamiteEntity(double x, double y, double z, Level level) {
        super(ModEntities.DYNAMITE.get(), x, y, z, level);
        this.fuseTicks = Config.INSTANCE.fuseTicksDynamite.get();
    }

    public DynamiteEntity(LivingEntity thrower, Level level) {
        super(ModEntities.DYNAMITE.get(), thrower, level);
        this.fuseTicks = Config.INSTANCE.fuseTicksDynamite.get();
    }

    @Override
    public void tick() {
        Level level = this.level();
        this.baseTick();
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0d, 0.04d, 0.0d));
        }

        double drag = 0.98d;
        if (this.isInWater()) {
            Vec3 delta = this.getDeltaMovement();

            double nextX = this.getX() + delta.x;
            double nextY = this.getY() + delta.y;
            double nextZ = this.getZ() + delta.z;

            for (int i = 0; i < 4; i++) {
                level.addParticle(
                        ParticleTypes.BUBBLE,
                        nextX - delta.x * 0.25d,
                        nextY - delta.y * 0.25d,
                        nextZ - delta.z * 0.25d,
                        delta.x,
                        delta.y,
                        delta.z);
            }
            drag = 0.8d;
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(drag));

        if (this.onGround())
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7d, -0.5d, 0.7d));

        // TODO: Allow Dynamite to stick to surfaces and mobs for Sticky Dynamite
        //        if(stickToSurface)
        //            setDeltaMovement(0.0d, 0.0d, 0.0d);

        this.timer++;
        if (this.timer >= this.fuseTicks) this.explode();
        else
            level.addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 0.25D,
                    this.getZ(),
                    0.0d,
                    0.1d,
                    0.0d);
    }

    @Override
    protected void onHit(@NotNull HitResult result) {}

    protected void explode() {
        Level level = this.level();
        if (!level.isClientSide) {
            boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            level.explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    Config.INSTANCE.explosionStrengthDynamite.get().floatValue(),
                    mobGriefing && !Config.INSTANCE.disableTerrainDamage.get()
                            ? ExplosionInteraction.TNT
                            : ExplosionInteraction
                                    .NONE); /*ConfigHandler.enableTerrainDamage &&*/ // mobGriefing);
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.DYNAMITE.get();
    }
}
