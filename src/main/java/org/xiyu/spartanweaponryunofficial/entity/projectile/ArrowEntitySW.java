package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public abstract class ArrowEntitySW extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> PICKUP_ITEM =
            SynchedEntityData.defineId(ArrowEntitySW.class, EntityDataSerializers.ITEM_STACK);
    private static final String NBT_PICKUP_ITEM = "PickupItem";

    protected float baseDamage = 1.0f;
    protected float rangeMultiplier = 1.0f;

    public ArrowEntitySW(EntityType<? extends ArrowEntitySW> type, Level level) {
        super(type, level);
        this.initEntity();
    }

    public ArrowEntitySW(
            EntityType<? extends ArrowEntitySW> type,
            Level level,
            double x,
            double y,
            double z,
            ItemStack weapon) {
        this(type, level, x, y, z, Items.ARROW.getDefaultInstance(), weapon);
    }

    public ArrowEntitySW(
            EntityType<? extends ArrowEntitySW> type,
            Level level,
            double x,
            double y,
            double z,
            ItemStack pickupItem,
            ItemStack weapon) {
        super(type, x, y, z, level, pickupItem, weapon);
        this.setPickupItem(pickupItem);
        this.initEntity();
    }

    public ArrowEntitySW(
            EntityType<? extends ArrowEntitySW> type,
            Level level,
            LivingEntity shooter,
            ItemStack weapon) {
        this(type, level, shooter, Items.ARROW.getDefaultInstance(), weapon);
    }

    public ArrowEntitySW(
            EntityType<? extends ArrowEntitySW> type,
            Level level,
            LivingEntity shooter,
            ItemStack pickupItem,
            ItemStack weapon) {
        super(type, shooter, level, pickupItem, weapon);
        this.setPickupItem(pickupItem);
        this.initEntity();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PICKUP_ITEM, ModItems.WOODEN_ARROW.get().getDefaultInstance());
    }

    protected void initEntity() {
        this.initStats();
        this.setBaseDamage(this.baseDamage);
    }

    protected abstract void initStats();

    private void setPickupItem(ItemStack stack) {
        this.getEntityData().set(PICKUP_ITEM, stack.copyWithCount(1));
    }

    public float getRangeMultiplier() {
        return this.rangeMultiplier;
    }

    @Override
    public void shootFromRotation(
            @NotNull Entity shooter,
            float pitch,
            float yaw,
            float p_184547_4_,
            float velocity,
            float inaccuracy) {
        super.shootFromRotation(
                shooter, pitch, yaw, p_184547_4_, velocity * this.rangeMultiplier, inaccuracy);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.getEntityData().get(PICKUP_ITEM);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.getPickupItem();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        ItemStack pickupItem = this.getPickupItem();
        boolean wasEmpty = pickupItem.isEmpty();
        if (wasEmpty) this.setPickupItem(Items.ARROW.getDefaultInstance());

        super.addAdditionalSaveData(compound);

        if (wasEmpty) this.getEntityData().set(PICKUP_ITEM, ItemStack.EMPTY);

        ItemStack savedPickupItem = this.getPickupItem();
        if (!savedPickupItem.isEmpty()) {
            compound.put(
                    NBT_PICKUP_ITEM, savedPickupItem.save(this.level().registryAccess()));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains(NBT_PICKUP_ITEM, Tag.TAG_COMPOUND)) {
            this.setPickupItem(
                    ItemStack.parseOptional(
                            this.level().registryAccess(), compound.getCompound(NBT_PICKUP_ITEM)));
        }
    }
}
