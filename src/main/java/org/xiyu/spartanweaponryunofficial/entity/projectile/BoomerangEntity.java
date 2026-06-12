package org.xiyu.spartanweaponryunofficial.entity.projectile;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;

public class BoomerangEntity extends ThrowingWeaponEntity {
    private static final EntityDataAccessor<Boolean> DATA_BOOMERANG_RETURNING =
            SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_MAX_DISTANCE =
            SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);

    protected static final String NBT_RETURN_POSITION = "ReturnPosition";
    protected static final String NBT_X = "X";
    protected static final String NBT_Y = "Y";
    protected static final String NBT_Z = "Z";
    protected static final String NBT_RETURNING = "Returning";
    protected static final String NBT_DISTANCE_TO_RETURN = "DistanceToReturn";

    public static final double DISTANCE_TO_RETURN = 5.0d;
    protected final double MAX_VELOCITY = 2.0d;
    protected final double ACCELERATION = 0.1d;

    protected final int TICKS_PER_SOUND = 5;

    protected Vec3 returnPos = null;
    protected double maxDistance = DISTANCE_TO_RETURN;
    protected int ticksUntilSound = 0;

    protected boolean affectedByWaterDrag = true;

    protected int caughtItems = 0;
    protected static final Predicate<Entity> ITEMS_AND_XP =
            EntitySelector.NO_SPECTATORS.and(
                    (entity) ->
                            entity.getType() == EntityType.EXPERIENCE_ORB
                                    || entity instanceof ItemEntity);

    public BoomerangEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level) {
        super(type, level);
        this.initEntity();
    }

    public BoomerangEntity(Level level, double x, double y, double z, ItemStack weapon) {
        super(ModEntities.BOOMERANG.get(), level, x, y, z, weapon);
        this.initEntity();
    }

    public BoomerangEntity(Level level, LivingEntity shooter, ItemStack weapon) {
        super(ModEntities.BOOMERANG.get(), shooter, level, weapon);
        this.initEntity();
    }

    protected void initEntity() {
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BOOMERANG_RETURNING, false);
        builder.define(DATA_MAX_DISTANCE, (float) DISTANCE_TO_RETURN);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_BOOMERANG_RETURNING.equals(key)) {
            this.isReturning = this.getEntityData().get(DATA_BOOMERANG_RETURNING);
        } else if (DATA_MAX_DISTANCE.equals(key)) {
            this.maxDistance = this.getEntityData().get(DATA_MAX_DISTANCE);
        }
    }

    protected void setReturnPosition(Entity shooter) {
        if (shooter != null)
            this.returnPos =
                    new Vec3(
                            shooter.getX(),
                            shooter.getY() + (shooter.getEyeHeight() * 0.9d) - 0.1d,
                            shooter.getZ());
    }

    public void setDistanceToReturn(double distance) {
        this.maxDistance = distance;
        this.getEntityData().set(DATA_MAX_DISTANCE, (float) distance);
    }

    private boolean isBoomerangReturning() {
        return this.getEntityData().get(DATA_BOOMERANG_RETURNING);
    }

    private void setBoomerangReturning(boolean returning) {
        this.isReturning = returning;
        this.getEntityData().set(DATA_BOOMERANG_RETURNING, returning);
    }

    private void clearBoomerangReturnState() {
        this.setBoomerangReturning(false);
        this.returnPos = null;
        this.playedReturnSound = false;
    }

    @Override
    public void tick() {
        Level level = this.level();
        Entity owner = this.getOwner();

        // Check for spectator BEFORE calling super.tick() to prevent parent class from interfering
        if (owner != null && owner.isSpectator()) {
            // Owner is spectating - stop all returning behavior and let it drop
            if (this.isNoGravity()) this.setNoGravity(false);
            this.clearBoomerangReturnState();
            // Still need to call super.tick() for basic physics, but we've cleared the return state
        }

        super.tick();

        // Do nothing more if the Boomerang is in the ground
        if (this.inGround) {
            this.clearBoomerangReturnState();
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoPhysics(false);
            this.setNoGravity(false);
            this.xRotO = this.getXRot();
            this.yRotO = this.getYRot();
            return;
        }

        // Skip return logic if owner is spectator (double check after super.tick)
        if (owner != null && owner.isSpectator()) {
            return;
        }

        // Update the return position, accounting the player's movement
        this.setReturnPosition(owner);

        // Get the distance between this entity and the shooter
        double distance = -1.0d;
        if (this.returnPos != null) distance = this.returnPos.distanceTo(this.position());

        // Check that the Boomerang is still in flight (either going out or coming back)
        if (this.isNoGravity()) {
            // Start dropping when the boomerang is close to the player and when it's returning
            // Or if it's return position is invalid
            if ((distance < 1.0d && this.isBoomerangReturning())
                    || (this.isInWater() && this.waterInertia <= 0.0f)
                    || this.returnPos == null) {
                this.setNoGravity(false);
                this.clearBoomerangReturnState();
            }
            if (distance > this.maxDistance && !this.isBoomerangReturning())
                this.setBoomerangReturning(true);

            // Override motion for Boomerang when returning to the thrower
            if (this.isBoomerangReturning() && this.returnPos != null) {
                Vec3 distanceVec = this.position().subtract(this.returnPos);
                double length = distanceVec.length();

                // Fly towards the player when close enough.
                if (length < 5.0d)
                    this.setDeltaMovement(
                            -distanceVec.x / length,
                            -distanceVec.y / length,
                            -distanceVec.z / length);
                //                if(length < 1.0d && getOwner() instanceof Player)
                //                        attemptCatch((Player)getOwner());
                // Otherwise, just fly in reverse as normal
                else {
                    Vec3 motion =
                            this.getDeltaMovement()
                                    .add(
                                            -this.ACCELERATION * (distanceVec.x / length),
                                            -this.ACCELERATION * (distanceVec.y / length),
                                            -this.ACCELERATION * (distanceVec.z / length));
                    this.setDeltaMovement(motion);
                }
            }

            // Attempt to catch the first item the boomerang finds
            ItemStack weaponItem = this.getWeaponItem();
            int collectorangLevel =
                    ModEnchantments.getLevel(
                            level.registryAccess(), ModEnchantments.COLLECTORANG, weaponItem);
            if (this.caughtItems < collectorangLevel) {
                AABB aabb = this.getBoundingBox().inflate(1.0d, 1.0d, 1.0d);
                List<Entity> catchableEntities = level.getEntities(this, aabb, ITEMS_AND_XP);
                if (!catchableEntities.isEmpty()) {
                    for (Entity entity : catchableEntities) {
                        entity.startRiding(this, true);
                        this.caughtItems++;

                        if (this.caughtItems >= collectorangLevel) break;
                    }
                }
            }

            // Play the sound every 5 ticks
            if (this.ticksUntilSound <= 0 && !this.isNoPhysics()) {
                this.ticksUntilSound = this.TICKS_PER_SOUND;
                if (!level.isClientSide)
                    level.playSound(
                            null,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            this.getFlySound(),
                            SoundSource.NEUTRAL,
                            2.0f,
                            0.5f);
            }

            --this.ticksUntilSound;
        }
        if (!level.isClientSide && this.tickCount > 200) {
            if (this.getEntityData().get(DATA_RETURN) > 0 && !this.isNoPhysics())
                this.setNoPhysics(true);
            else if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                this.dropAsItem();
                this.discard();
            }
        }
        if (level.isClientSide && !this.inGround) {
            level.addParticle(
                    ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        // If this hits any entity, return back to the thrower.
        this.setBoomerangReturning(true);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        // Bounce off the block surface and return when hitting a block, but only when not returning
        // Note: This will mean that the Boomerang will no longer activate buttons or pressure
        // plates... (unless they are not moving in flight in front of it)
        if (this.isNoGravity()) {
            Level level = this.level();
            // Once the Boomerang hits any surface, it should return to the player.

            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);

            // Attempt to make the boomerang bounce off a block face
            // To do this, calculate a reflection vector from the block that was hit.
            // Firstly, the face that was hit for this is needed, and from that, it's normalized
            // direction vector,
            // as well as the current motion vector
            Vec3i faceNormali = hitResult.getDirection().getNormal();
            Vec3 faceNormalVec =
                    new Vec3(faceNormali.getX(), faceNormali.getY(), faceNormali.getZ());
            Vec3 motionVec = this.getDeltaMovement();
            // This should be normalized already, but just to ensure that it is, normalize it
            // anyway.
            faceNormalVec.normalize();

            // Formula -> reflect = normal x (2 x motion . normal) - motion
            // This results in a reflection vector that is going into the surface, so negation is
            // required. That is done below.
            Vec3 reflectVec =
                    faceNormalVec.scale(2 * motionVec.dot(faceNormalVec)).subtract(motionVec);

            // Apply this reflection motion, but not without negating and dampening the vector first
            this.setDeltaMovement(reflectVec.scale(-0.75d));

            this.playSound(
                    this.getBounceSound(), 1.0f, 2.2f / this.random.nextFloat() * 0.2f + 0.9f);

            // Do Block collision logic with projectiles (e.g. Set the projectile on fire, etc.)
            if (!blockState.isAir()) blockState.onProjectileHit(level, blockState, hitResult, this);
        } else super.onHitBlock(hitResult);
    }

    // Used for showing items picked up by the boomerang
    public double getPassengersRidingOffset() {
        return 0.0d;
    }

    @Override
    protected void positionRider(@NotNull Entity entityIn, Entity.@NotNull MoveFunction moveIn) {
        if (this.hasPassenger(entityIn)) {
            List<Entity> passengers = this.getPassengers();
            for (int i = 0; i < passengers.size(); i++) {
                if (entityIn == passengers.get(i)) {
                    double yOff = this.getY() + this.getPassengersRidingOffset() + (i * 0.5d);
                    moveIn.accept(entityIn, this.getX(), yOff, this.getZ());
                    break;
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains(this.NBT_RETURN_POSITION)) {
            double x, y, z;
            CompoundTag returnPosNBT = compound.getCompound(this.NBT_RETURN_POSITION);

            if (!returnPosNBT.isEmpty()) {
                x = returnPosNBT.getDouble(this.NBT_X);
                y = returnPosNBT.getDouble(this.NBT_Y);
                z = returnPosNBT.getDouble(this.NBT_Z);

                this.returnPos = new Vec3(x, y, z);
            } else this.returnPos = null;

            this.maxDistance = compound.getDouble(this.NBT_DISTANCE_TO_RETURN);
        } else this.returnPos = null;

        this.setBoomerangReturning(compound.getBoolean(this.NBT_RETURNING));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        if (this.returnPos != null) {
            CompoundTag returnPosNBT = new CompoundTag();
            returnPosNBT.putDouble(this.NBT_X, this.returnPos.x);
            returnPosNBT.putDouble(this.NBT_Y, this.returnPos.y);
            returnPosNBT.putDouble(this.NBT_Z, this.returnPos.z);
            compound.put(this.NBT_RETURN_POSITION, returnPosNBT);
            compound.putDouble(this.NBT_DISTANCE_TO_RETURN, this.maxDistance);
        } else compound.remove(this.NBT_RETURN_POSITION);

        compound.putBoolean(this.NBT_RETURNING, this.isBoomerangReturning());
    }

    @Override
    protected boolean canBeCaughtInMidair(Entity shooter, Entity entityHit) {
        // Only the shooter can catch the Boomerang
        return shooter.is(entityHit);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.BOOMERANG_HIT_GROUND.get();
    }

    @Override
    protected SoundEvent getMobHitSound() {
        return ModSounds.BOOMERANG_HIT_MOB.get();
    }

    protected SoundEvent getFlySound() {
        return ModSounds.BOOMERANG_FLY.get();
    }

    protected SoundEvent getBounceSound() {
        return ModSounds.BOOMERANG_BOUNCE.get();
    }
}
