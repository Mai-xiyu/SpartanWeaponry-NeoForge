package org.xiyu.spartanweaponryunofficial.entity.projectile;

import com.mojang.serialization.DataResult;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.api.trait.IMeleeTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.*;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import java.util.Collection;
import java.util.Optional;

public class ThrowingWeaponEntity extends AbstractArrow implements IEntityWithComplexSpawn {
    public static final String NBT_WEAPON = "Weapon";
    protected static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(ThrowingWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Byte> DATA_RETURN = SynchedEntityData.defineId(ThrowingWeaponEntity.class, EntityDataSerializers.BYTE);
    protected int ticksInAir = 0;
    protected float waterInertia = 0.0f;
    protected boolean isReturning = false;
    protected boolean playedReturnSound = false;
    protected int despawnTicks = 0;
    protected double cachedBaseDamage = 2.0;

    public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level) {
        super(type, level);
    }

    public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level, double x, double y, double z, ItemStack weapon) {
        super(type, x, y, z, level, weapon, weapon);
    }

    public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, LivingEntity shooter, Level level, ItemStack weapon) {
        super(type, shooter, level, weapon, weapon);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WEAPON, ItemStack.EMPTY);
        builder.define(DATA_RETURN, (byte) 0);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.getEntityData().get(DATA_WEAPON);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.getWeaponItem();
    }

    public boolean isReturning() {
        return this.isReturning;
    }

    @Override
    public void tick() {
        Level level = this.level();
        RegistryAccess registryAccess = level.registryAccess();
        if (this.waterInertia == 0.0f)
            this.waterInertia = ModEnchantments.getLevel(level.registryAccess(), ModEnchantments.HYDRODYNAMIC, this.getWeaponItem()) == 1 ? 0.98f : -1.0f;

        Entity thrower = this.getOwner();
        int returnLevel = this.getEntityData().get(DATA_RETURN);

        // Only process return logic if the weapon has Loyalty enchantment
        if (returnLevel > 0 && thrower != null) {
            // Check if we should start returning (after being in ground for a short time, or already returning)
            if (this.inGroundTime > 4 || this.isReturning) {
                if (thrower.isAlive() && (!(thrower instanceof ServerPlayer) || !thrower.isSpectator())) {
                    // Return to thrower - ensure we're in the returning state
                    if (!this.isReturning) {
                        this.setNoPhysics(true);
                        this.setInGround(false);
                        this.isReturning = true;
                        this.setNoGravity(true);
                    }

                    // Keep forcing these states while returning to prevent position conflicts
                    if (this.isReturning) {
                        if (this.isInGround())
                            this.setInGround(false);
                        if (!this.isNoPhysics())
                            this.setNoPhysics(true);
                    }

                    Vec3 distance = thrower.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + distance.y * 0.015 * (double) returnLevel, this.getZ());
                    if (level.isClientSide()) {
                        this.yOld = this.getY();
                    }

                    double velocity = 0.10d * (double) returnLevel;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95d).add(distance.normalize().scale(velocity)));

                    if (!this.playedReturnSound) {
                        this.playSound(ModSounds.THROWING_WEAPON_LOYALTY_RETURN.get(), 10.0F, 1.0F);
                        this.playedReturnSound = true;
                    }
                } else if (!thrower.isAlive() || thrower.isSpectator()) {
                    // Thrower is dead or spectating - stop returning and drop naturally
                    if (this.isReturning) {
                        this.setNoPhysics(false);
                        this.isReturning = false;
                        this.setNoGravity(false);
                        this.playedReturnSound = false;
                    }
                }
            }
        }

        super.tick();

        // Post-tick: ensure returning state is maintained (fix for position snap-back issues)
        if (this.isReturning && this.isInGround())
            this.setInGround(false);

        if (!this.isInGround())
            ++this.ticksInAir;
        else if (this.ticksInAir != 0)
            this.ticksInAir = 0;
    }

    protected void tickDespawn() {
        ++this.despawnTicks;
        if (this.despawnTicks >= 1200) {
            this.dropAsItem();
            this.discard();
        }
    }

    public int getPortalWaitTime() {
        // Set this time higher to prevent this entity from being duplicated when hitting a portal...
        return 100;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Level level = this.level();
        RegistryAccess registryAccess = level.registryAccess();
        Entity shooter = this.getOwner();    //func_234616_v_();

        ItemStack weapon = this.getWeaponItem();
        float damage = Mth.ceil(this.cachedBaseDamage);
        DamageSource src;

        if (this.isCritArrow()) {
            damage += this.random.nextInt((int) damage / 2 + 2);
        }

        // Try and catch the throwing weapon if possible.
        if (shooter != null && (this.canBeCaughtInMidair(shooter, entity) || this.isReturning) && entity instanceof Player player) {
            if (this.attemptCatch(player))
                return;
        }
        if (shooter == null)
//				src = new IndirectEntityDamageSource("mob", this, this).setProjectile();
            src = ModDamageTypes.thrownWeaponMob(this, this);
        else if (shooter instanceof Player)
//				src = new IndirectEntityDamageSource("player", this, shooter).setProjectile();
            src = ModDamageTypes.thrownWeaponPlayer(this, shooter);
        else
//				src = new IndirectEntityDamageSource("mob", this, shooter).setProjectile();
            src = ModDamageTypes.thrownWeaponMob(this, shooter);

        if (entity instanceof LivingEntity && shooter instanceof LivingEntity) {
            if (weapon.getItem() instanceof IWeaponTraitContainer && !entity.is(shooter)) {
                IWeaponTraitContainer<Item> container = (IWeaponTraitContainer<Item>) weapon.getItem();
                WeaponMaterial material = container.getMaterial();
                Collection<WeaponTrait> traits = container.getAllWeaponTraits();

                for (WeaponTrait trait : traits) {
                    Optional<IMeleeTraitCallback> opt = trait.getMeleeCallback();
                    if (opt.isPresent()) {
                        IMeleeTraitCallback callback = opt.get();
                        damage = callback.modifyDamageDealt(material, damage, src, (LivingEntity) shooter, (LivingEntity) entity);
                        callback.onHitEntity(container.getMaterial(), weapon, (LivingEntity) entity, (LivingEntity) shooter, this);
                    }
                }
            }
            if (weapon.is(ModItemTags.OILABLE_WEAPONS)) {
                IOilHandler oilHandler = weapon.getCapability(ModCapabilities.OIL_CAPABILITY);
                if (oilHandler != null && oilHandler.isOiled()) {
                    float dmgUnmodified = damage;
                    damage = oilHandler.useEffect(damage, level, (LivingEntity) entity, (LivingEntity) shooter, weapon);
                    if (damage != dmgUnmodified && !level.isClientSide())
                        ((ServerLevel) level).sendParticles(ModParticles.OIL_DAMAGE_BOOSTED.get(), entity.getX(), entity.getY() + (entity.getBbHeight() / 2.0f), entity.getZ(), 8, 0.2d, 0.2d, 0.2d, 0.5d);
                }
            }
        }

        boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
        if (this.isOnFire() && !isEnderman) {
            entity.igniteForSeconds(5.0F);
        }

        if (entity.hurtOrSimulate(src, damage)) {
            if (level instanceof ServerLevel serverLevel && shooter instanceof LivingEntity)
                weapon.hurtAndBreak(1, serverLevel, (LivingEntity) shooter, item -> {
                });

            if (entity instanceof LivingEntity entitylivingbase) {

                int knockback = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), weapon);
                if (knockback > 0) {
                    Vec3 knockVec = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) knockback * 0.6D);

                    if (knockVec.lengthSqr() > 0.0F)
                        entitylivingbase.push(knockVec.x, 0.1d, knockVec.z);
                }

                this.doPostHurtEffects(entitylivingbase);

                if (entitylivingbase != shooter && entitylivingbase instanceof Player && shooter instanceof ServerPlayer) {
                    ((ServerPlayer) shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, 0.0F));
                }
            }

            this.playSound(this.getMobHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

            if (!isEnderman) {
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1d));
                this.setYRot(this.getYRot() + 180.0f);
                this.yRotO += 180.0F;
            }
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1d));
            this.setYRot(this.getYRot() + 180.0f);
            this.yRotO += 180.0F;
            this.ticksInAir = 0;

            if (!level.isClientSide() && this.getDeltaMovement().lengthSqr() < 1.0e-7d) {
                if (this.getEntityData().get(DATA_RETURN) > 0 && !this.isNoPhysics())
                    this.setNoPhysics(true);
                else if (this.pickup == Pickup.ALLOWED) {
                    this.dropAsItem();
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        Level level = this.level();
        if (hitResult.getType() == Type.BLOCK) {
            if (!level.isClientSide()) {
                BlockState state = level.getBlockState(hitResult.getBlockPos());
                ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), this.getX(), this.getY(), this.getZ(), 5, 0.1d, 0.1d, 0.1d, 0.05d);
            }

            // Weapon retains enchantments - duplication is prevented by the AmmoUsed merge system
        }
        super.onHitBlock(hitResult);
    }


    protected void dropAsItem() {
        ItemStack stack = this.getWeaponItem().copy();
        // Clean up internal tracking tags but preserve enchantments
        if (ItemStackDataHelper.hasTag(stack)) {
            CompoundTag tag = ItemStackDataHelper.getTag(stack);
            if (tag.contains(ThrowingWeaponItem.NBT_AMMO_USED)) {
                ItemStackDataHelper.updateTag(stack, t -> t.putInt(ThrowingWeaponItem.NBT_AMMO_USED, 0));
            }
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            this.spawnAtLocation(serverLevel, stack, 0.1F);
        }
    }

    @Override
    public void playerTouch(@NotNull Player entityIn) {
        if (this.isInGround() || this.isReturning)
            this.attemptCatch(entityIn);
    }

    @Override
    protected float getWaterInertia() {
        return this.waterInertia > 0.0f ? this.waterInertia : super.getWaterInertia();
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.THROWN_WEAPON_HIT_GROUND.get();
    }

    protected SoundEvent getMobHitSound() {
        return ModSounds.THROWN_WEAPON_HIT_MOB.get();
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read(NBT_WEAPON, ItemStack.CODEC).ifPresent(weapon -> {
            if (!weapon.isEmpty()) {
                this.getEntityData().set(DATA_WEAPON, weapon);
            }
        });
        this.getEntityData().set(DATA_RETURN, input.getByteOr("ReturnLevel", (byte)0));
    }

    @Override
    public void writeSpawnData(@NotNull RegistryFriendlyByteBuf buffer) {
        ItemStack weapon = this.getWeaponItem();
        // Use OPTIONAL_STREAM_CODEC to handle empty ItemStack
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, weapon);
    }

    @Override
    public void readSpawnData(@NotNull RegistryFriendlyByteBuf additionalData) {
        ItemStack weapon = ItemStack.OPTIONAL_STREAM_CODEC.decode(additionalData);
        if (!weapon.isEmpty()) {
            this.setWeapon(weapon);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        // Guard: AbstractArrow.addAdditionalSaveData calls getPickupItem().save() which crashes on empty ItemStack.
        ItemStack weaponData = this.getEntityData().get(DATA_WEAPON);
        boolean wasEmpty = weaponData.isEmpty();
        if (wasEmpty) {
            this.getEntityData().set(DATA_WEAPON, Items.ARROW.getDefaultInstance());
        }
        super.addAdditionalSaveData(output);
        if (wasEmpty) {
            this.getEntityData().set(DATA_WEAPON, ItemStack.EMPTY);
        }

        ItemStack weapon = this.getWeaponItem();
        if (!weapon.isEmpty()) {
            output.store(NBT_WEAPON, ItemStack.CODEC, weapon);
        }
        output.putByte("ReturnLevel", this.getEntityData().get(DATA_RETURN));
    }

    // New Methods
    public @NotNull ItemStack getWeaponItem() {
        return this.getPickupItem();
    }

    public boolean isValidThrowingWeapon() {
        return !this.getWeaponItem().isEmpty();
    }

    public void setWeapon(ItemStack weaponStack) {
        ItemStack stack = weaponStack.copy();
        // Store original weapon data for ammo system
        if (!ItemStackDataHelper.getTag(stack).contains(ThrowingWeaponItem.NBT_ORIGINAL)) {
            // Save the original weapon state before modifying ammo counter
            RegistryOps<Tag> ops = this.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            Tag originalTag = ItemStack.CODEC.encodeStart(ops, stack).getOrThrow();
            ItemStackDataHelper.updateTag(stack, tag -> tag.put(ThrowingWeaponItem.NBT_ORIGINAL, originalTag));
        }
        this.getEntityData().set(DATA_WEAPON, stack);
        int loyalty = EnchantmentHelper.getItemEnchantmentLevel(this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOYALTY), stack);
        this.getEntityData().set(DATA_RETURN, (byte) loyalty);
    }

    protected boolean canBeCaughtInMidair(Entity shooter, Entity entityHit) {
        return shooter == entityHit && this.isNoPhysics();
    }

    public int getTicksInAir() {
        return this.ticksInAir;
    }

    @Override
    public void setBaseDamage(double damage) {
        super.setBaseDamage(damage);
        this.cachedBaseDamage = damage;
    }

    public double getBaseDamage() {
        return this.cachedBaseDamage;
    }

    protected boolean attemptCatch(Player player) {
        Level level = this.level();
        if (!level.isClientSide() && this.shakeTime <= 0) {
            boolean canBePickedUp = this.pickup == AbstractArrow.Pickup.ALLOWED || this.pickup == AbstractArrow.Pickup.CREATIVE_ONLY && player.getAbilities().instabuild;

            if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                // Restore ammo: decrease the ammo used counter when picking up the weapon
                ItemStack pickUpStack = this.getWeaponItem().copy();

                // Try to merge with existing weapon in inventory
                boolean merged = false;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack invStack = player.getInventory().getItem(i);
                    if (!invStack.isEmpty() && ItemStack.isSameItem(invStack, pickUpStack)) {
                        // Found matching weapon, decrease its ammo counter
                        int currentAmmo = ItemStackDataHelper.getTag(invStack).getIntOr(ThrowingWeaponItem.NBT_AMMO_USED, 0);
                        if (currentAmmo > 0) {
                            invStack.setDamageValue(invStack.getDamageValue() + pickUpStack.getDamageValue());
                            ItemStackDataHelper.updateTag(invStack, tag -> tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, Math.max(0, tag.getIntOr(ThrowingWeaponItem.NBT_AMMO_USED, 0) - 1)));
                            merged = true;
                            break;
                        }
                    }
                }

                // If couldn't merge, try adding as new item with reset ammo
                if (!merged) {
                    // Only reset AmmoUsed if already present (don't add it to non-ThrowingWeaponItem weapons)
                    if (ItemStackDataHelper.hasTag(pickUpStack) && ItemStackDataHelper.getTag(pickUpStack).contains(ThrowingWeaponItem.NBT_AMMO_USED)) {
                        ItemStackDataHelper.updateTag(pickUpStack, tag -> tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, 0));
                    }
                    canBePickedUp = player.getInventory().add(pickUpStack);
                }
            }

            if (canBePickedUp) {
                player.take(this, 1);
                this.discard();
            }

            return canBePickedUp;
        }
        return false;
    }
}
