package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
// Removed - interface not available in Forge 1.21.1
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import java.util.List;

public class BoltEntity extends AbstractArrow {
    protected final String NBT_BOLT = "Bolt";
    protected final String NBT_POTION = "Potion";
    protected final String NBT_POTION_COLOUR = "PotionColour";

    protected static final EntityDataAccessor<Integer> DATA_COLOUR = SynchedEntityData.defineId(BoltEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<ItemStack> DATA_BOLT = SynchedEntityData.defineId(BoltEntity.class, EntityDataSerializers.ITEM_STACK);
    protected Potion potion = null;

    protected float baseDamage = 1.0f;
    protected float rangeMultiplier = 1.0f;
    protected float armorPiercingFactor = 0.0f;

    public BoltEntity(EntityType<? extends BoltEntity> type, Level level) {
        super(type, level);
    }

    public BoltEntity(EntityType<? extends BoltEntity> type, double x, double y, double z, Level level, ItemStack pickupItemStack, ItemStack weaponStack) {
        super(type, x, y, z, level, pickupItemStack, weaponStack);
    }

    public BoltEntity(EntityType<? extends BoltEntity> type, LivingEntity shooter, Level level, ItemStack pickupItemStack, ItemStack weaponStack) {
        super(type, shooter, level, pickupItemStack, weaponStack);
    }

    public BoltEntity(LivingEntity shooter, Level level, ItemStack boltStack, ItemStack weaponStack) {
        this(ModEntities.BOLT.get(), shooter, level, boltStack, weaponStack);
    }


    public void initEntity(float baseDamage, float rangeMultiplier, float armorPiercingFactor, ItemStack boltStack) {
        this.baseDamage = baseDamage;
        this.rangeMultiplier = rangeMultiplier;
        this.armorPiercingFactor = armorPiercingFactor;
        this.setBaseDamage(baseDamage);
        this.getEntityData().set(DATA_BOLT, boltStack);
    }
    
/*    @Override
    public void shootFromRotation(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
    	super.shootFromRotation(shooter, pitch, yaw, p_184547_4_, (float)(velocity * rangeMultiplier), inaccuracy);
    }*/

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COLOUR, -1);
        builder.define(DATA_BOLT, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();
        if (level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0)
                    this.spawnPotionParticles(1);
            } else
                this.spawnPotionParticles(2);
        } else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            level.broadcastEntityEvent(this, (byte) 0);
            this.potion = null;
            this.getEntityData().set(DATA_COLOUR, -1);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Level level = this.level();
        RegistryAccess registryAccess = level.registryAccess();
        Entity entity = result.getEntity();
        float velocity = (float) this.getDeltaMovement().length();
        int damage = Mth.ceil(Mth.clamp((double) velocity * this.getBaseDamage(), 0.0D, 2.147483647E9D));

        if (this.isCritArrow()) {
            long critDamageBonus = this.random.nextInt(damage / 2 + 2);
            damage = (int) Math.min(critDamageBonus + (long) damage, 2147483647L);
        }

        Entity shooter = this.getOwner();
        DamageSource source;
        if (shooter == null)
            source = ModDamageTypes.armorPiercingProjectile(this, this);
        else {
            source = ModDamageTypes.armorPiercingProjectile(this, shooter);
            if (shooter instanceof LivingEntity)
                ((LivingEntity) shooter).setLastHurtMob(entity);
        }

        boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
        int fireTimer = entity.getRemainingFireTicks();
        if (this.isOnFire() && !isEnderman)
            entity.igniteForSeconds(5.0F);

        if (entity.hurt(source, (float) damage)) {
            if (isEnderman)
                return;

            if (entity instanceof LivingEntity livingentity) {
                if (!level.isClientSide && this.getPierceLevel() <= 0)
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);

                int knockback = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.KNOCKBACK), this.getPickupItem());
                if (knockback > 0) {
                    Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) knockback * 0.6D);
                    if (vector3d.lengthSqr() > 0.0D)
                        livingentity.push(vector3d.x, 0.1D, vector3d.z);
                }

                this.doPostHurtEffects(livingentity);
                if (livingentity != shooter && livingentity instanceof Player && shooter instanceof ServerPlayer && !this.isSilent())
                    ((ServerPlayer) shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));


                if (!level.isClientSide && shooter instanceof ServerPlayer serverplayerentity && !entity.isAlive() && this.shotFromCrossbow())
                    CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, List.of(entity));
            }

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0)
                this.discard();
        } else {
            entity.setRemainingFireTicks(fireTimer);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0f);
            this.yRotO += 180.0F;
            if (!level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED)
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);

                this.discard();
            }
        }
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        Level level = this.level();

        if (this.potion != null) {
            for (MobEffectInstance effect : this.potion.getEffects()) {
                living.addEffect(new MobEffectInstance(effect.getEffect(), Math.max(effect.getDuration() / 8, 1), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }
        }

        Item arrowItem = this.getPickupItem().getItem();

        // Spawn lightning under the right weather conditions (during a thunderstorm)
        if (level.isThundering() && arrowItem == ModItems.COPPER_BOLT.get() || arrowItem == ModItems.TIPPED_COPPER_BOLT.get()) {
            // Roll a chance to spawn lightning under the right circumstances
            if (this.random.nextInt(4) < 1) {// ~25%
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                assert lightning != null;
                lightning.moveTo(Vec3.atBottomCenterOf(living.blockPosition()));
                lightning.setCause(living instanceof ServerPlayer ? (ServerPlayer) living : null);
                level.addFreshEntity(lightning);
            }
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.getEntityData().get(DATA_BOLT);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.getPickupItem();
    }

    // Removed @Override - IEntityAdditionalSpawnData interface not available in Forge 1.21.1
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        double x, y, z;
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        this.setDeltaMovement(x, y, z);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains(this.NBT_POTION, 8))
            this.potion = BuiltInRegistries.POTION.get(ResourceLocation.parse(compound.getString(this.NBT_POTION)));

        this.getEntityData().set(DATA_COLOUR, compound.contains(this.NBT_POTION_COLOUR) ? compound.getInt(this.NBT_POTION_COLOUR) : -1);
    }

    // Removed @Override - IEntityAdditionalSpawnData interface not available in Forge 1.21.1
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeDouble(this.getDeltaMovement().x);
        buffer.writeDouble(this.getDeltaMovement().y);
        buffer.writeDouble(this.getDeltaMovement().z);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        if (this.potion != null) {
            compound.putString(this.NBT_POTION, BuiltInRegistries.POTION.getKey(this.potion).toString());
        }

        compound.putInt(this.NBT_POTION_COLOUR, this.getEntityData().get(DATA_COLOUR));
    }

    public float getRangeMultiplier() {
        return this.rangeMultiplier;
    }

    public void setPotionEffect(ItemStack stack) {
        PotionContents contents = stack.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        this.potion = contents.potion().map(Holder::value).orElse(null);
        this.getEntityData().set(DATA_COLOUR, contents.getColor());
    }

    private void spawnPotionParticles(int particleCount) {
        int colour = this.getEntityData().get(DATA_COLOUR);
        if (colour != -1 && particleCount > 0) {
            Level level = this.level();
            double cR = (double) (colour >> 16 & 255) / 255.0D;
            double cG = (double) (colour >> 8 & 255) / 255.0D;
            double cB = (double) (colour & 255) / 255.0D;

            for (int i = 0; i < particleCount; i++) {
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float) cR, (float) cG, (float) cB), this.getRandomX(0.5d), this.getRandomY(), this.getRandomZ(0.5d), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean isValid() {
        return !this.getPickupItem().isEmpty();
    }

    public ResourceLocation getTexture() {
        ItemStack boltStack = this.getPickupItem();
        if (boltStack.isEmpty())
            return ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "missing_stack");

        String boltRegName = BuiltInRegistries.ITEM.getKey(boltStack.getItem()).getPath();

        String prefix = "tipped_";
        int idx = boltRegName.indexOf(prefix);
        if (idx != -1) {
            boltRegName = boltRegName.substring(idx + prefix.length());
        }
        return ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/entity/projectiles/" + boltRegName + ".png");
    }
}
