package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public abstract class ArrowEntitySW extends AbstractArrow {
    protected float baseDamage = 1.0f;
    protected float rangeMultiplier = 1.0f;

    public ArrowEntitySW(EntityType<? extends ArrowEntitySW> type, Level level) {
        super(type, level);
        this.initEntity();
    }

    public ArrowEntitySW(EntityType<? extends ArrowEntitySW> type, Level level, double x, double y, double z, ItemStack weapon) {
        super(type, x, y, z, level, Items.ARROW.getDefaultInstance(), weapon);
        this.initEntity();
    }

    public ArrowEntitySW(EntityType<? extends ArrowEntitySW> type, Level level, LivingEntity shooter, ItemStack weapon) {
        super(type, shooter, level, Items.ARROW.getDefaultInstance(), weapon);
        this.initEntity();
    }

    protected void initEntity() {
        this.initStats();
        this.setBaseDamage(this.baseDamage);
    }

    abstract protected void initStats();

    @Override
    public void shootFromRotation(@NotNull Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
        super.shootFromRotation(shooter, pitch, yaw, p_184547_4_, velocity * this.rangeMultiplier, inaccuracy);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ModItems.WOODEN_ARROW.get(), 1);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.getPickupItem();
    }

}
