package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public class BoltSpectralEntity extends BoltEntity {
    private int duration = 200;
    private final String NBT_DURATION = "Duration";

    public BoltSpectralEntity(EntityType<? extends BoltEntity> type, Level level) {
        super(type, level);
    }

    public BoltSpectralEntity(EntityType<? extends BoltEntity> type, double x, double y, double z, Level level, ItemStack pickupItemStack, ItemStack weaponStack) {
        super(type, x, y, z, level, pickupItemStack, weaponStack);
    }

    public BoltSpectralEntity(LivingEntity shooter, Level level, ItemStack boltStack, ItemStack weaponStack) {
        super(ModEntities.BOLT_SPECTRAL.get(), shooter, level, boltStack, weaponStack);
    }

    @Override
    public void tick() {
        Level level = this.level();
        super.tick();
        if (level.isClientSide() && !this.isInGround()) {
            level.addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0.0d, 0.0d, 0.0d);
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ModItems.SPECTRAL_BOLT.get());
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        living.addEffect(effect);
    }

    @Override
    public Identifier getTexture() {
        return Identifier.tryBuild(ModSpartanWeaponry.ID, "textures/entity/projectiles/spectral_bolt.png");
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.duration = input.getIntOr(this.NBT_DURATION, this.duration);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(this.NBT_DURATION, this.duration);
    }
}