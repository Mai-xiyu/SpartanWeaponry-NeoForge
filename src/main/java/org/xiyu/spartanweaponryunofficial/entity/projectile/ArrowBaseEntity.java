package org.xiyu.spartanweaponryunofficial.entity.projectile;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public class ArrowBaseEntity extends AbstractArrow implements IEntityWithComplexSpawn {
    protected final String NBT_ARROW = "Arrow";
    protected final String NBT_POTION = "Potion";
    protected final String NBT_POTION_COLOUR = "PotionColour";

    protected static final EntityDataAccessor<Integer> COLOUR =
            SynchedEntityData.defineId(ArrowBaseEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<ItemStack> ARROW =
            SynchedEntityData.defineId(ArrowBaseEntity.class, EntityDataSerializers.ITEM_STACK);
    protected Potion potion = null;

    protected float baseDamage = 1.0f;
    protected float rangeMultiplier = 1.0f;

    public ArrowBaseEntity(EntityType<? extends ArrowBaseEntity> type, Level level) {
        super(type, level);
    }

    public ArrowBaseEntity(Level level, double x, double y, double z, ItemStack weapon) {
        super(ModEntities.ARROW_SW.get(), x, y, z, level, Items.ARROW.getDefaultInstance(), weapon);
    }

    public ArrowBaseEntity(Level level, LivingEntity shooter, ItemStack weapon) {
        super(ModEntities.ARROW_SW.get(), shooter, level, Items.ARROW.getDefaultInstance(), weapon);
    }

    public void initEntity(float baseDamage, float rangeMultiplier, ItemStack arrowStack) {
        this.baseDamage = baseDamage;
        this.rangeMultiplier = rangeMultiplier;
        this.setBaseDamage(baseDamage);
        this.setArrowStack(arrowStack);
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOUR, -1);
        builder.define(ARROW, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (level.isClientSide /*&& potion != null && potion != Potions.EMPTY*/) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) this.spawnPotionParticles(1);
            } else this.spawnPotionParticles(2);
        } else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            level.broadcastEntityEvent(this, (byte) 0);
            this.potion = null;
            this.getEntityData().set(COLOUR, -1);
        }
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        if (this.potion == null) return;
        Entity entity = this.getEffectSource();
        Level level = this.level();

        for (MobEffectInstance effect : this.potion.getEffects()) {
            living.addEffect(
                    new MobEffectInstance(
                            effect.getEffect(),
                            Math.max(effect.getDuration() / 8, 1),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible()),
                    entity);
        }

        Item arrowItem = this.getPickupItem().getItem();

        // Spawn lightning under the right weather conditions (during a thunderstorm)
        if (level.isThundering() && arrowItem == ModItems.COPPER_ARROW.get()
                || arrowItem == ModItems.TIPPED_COPPER_ARROW.get()) {
            // Roll a chance to spawn lightning under the right circumstances
            if (this.random.nextInt(4) < 1) // ~25%
            {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                assert lightning != null;
                lightning.moveTo(Vec3.atBottomCenterOf(living.blockPosition()));
                lightning.setCause(living instanceof ServerPlayer ? (ServerPlayer) living : null);
                level.addFreshEntity(lightning);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 0) {
            Level level = this.level();
            int i = this.getEntityData().get(COLOUR);
            if (i != -1) {
                double cR = (double) (i >> 16 & 255) / 255.0D;
                double cG = (double) (i >> 8 & 255) / 255.0D;
                double cB = (double) (i & 255) / 255.0D;

                for (int j = 0; j < 20; ++j) {
                    level.addParticle(
                            ColorParticleOption.create(
                                    ParticleTypes.ENTITY_EFFECT,
                                    (float) cR,
                                    (float) cG,
                                    (float) cB),
                            this.getRandomX(0.5d),
                            this.getRandomY(),
                            this.getRandomZ(0.5d),
                            0.0D,
                            0.0D,
                            0.0D);
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.getEntityData().get(ARROW);
        //        return arrowStack;
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return this.getPickupItem();
    }

    protected void setArrowStack(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        this.getEntityData().set(ARROW, copy);
    }

    @Override
    public void writeSpawnData(@NotNull RegistryFriendlyByteBuf buffer) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, this.getPickupItem());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        // Guard: AbstractArrow.addAdditionalSaveData calls getPickupItem().save() which crashes on
        // empty ItemStack.
        // Temporarily set a fallback if our ARROW data is empty, then restore after super call.
        ItemStack arrowData = this.getEntityData().get(ARROW);
        boolean wasEmpty = arrowData.isEmpty();
        if (wasEmpty) {
            this.getEntityData().set(ARROW, Items.ARROW.getDefaultInstance());
        }
        super.addAdditionalSaveData(compound);
        if (wasEmpty) {
            this.getEntityData().set(ARROW, ItemStack.EMPTY);
        }

        ItemStack arrowStack = this.getPickupItem();
        if (!arrowStack.isEmpty()) {
            compound.put(this.NBT_ARROW, arrowStack.save(this.level().registryAccess()));
        }

        if (this.potion != null) {
            compound.putString(
                    this.NBT_POTION, BuiltInRegistries.POTION.getKey(this.potion).toString());
        }

        compound.putInt(this.NBT_POTION_COLOUR, this.getEntityData().get(COLOUR));
    }

    @Override
    public void readSpawnData(@NotNull RegistryFriendlyByteBuf additionalData) {
        ItemStack decoded = ItemStack.OPTIONAL_STREAM_CODEC.decode(additionalData);
        if (!decoded.isEmpty()) {
            this.setArrowStack(decoded);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag nbt = compound.getCompound(this.NBT_ARROW);
        this.setArrowStack(ItemStack.parseOptional(this.level().registryAccess(), nbt));

        if (compound.contains(this.NBT_POTION, 8))
            this.potion =
                    BuiltInRegistries.POTION.get(
                            ResourceLocation.parse(compound.getString(this.NBT_POTION)));

        this.getEntityData()
                .set(
                        COLOUR,
                        compound.contains(this.NBT_POTION_COLOUR)
                                ? compound.getInt(this.NBT_POTION_COLOUR)
                                : -1);
    }

    public boolean isValid() {
        return !this.getPickupItem().isEmpty();
    }

    public ResourceLocation getTexture() {
        String arrowRegName =
                BuiltInRegistries.ITEM.getKey(this.getPickupItem().getItem()).getPath();

        String prefix = "tipped_";
        int idx = arrowRegName.indexOf(prefix);
        if (idx != -1) {
            arrowRegName = arrowRegName.substring(idx + prefix.length());
        }
        return ResourceLocation.tryBuild(
                ModSpartanWeaponry.ID, "textures/entity/projectiles/" + arrowRegName + ".png");
    }

    public void setPotionEffect(ItemStack stack) {
        PotionContents contents =
                stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        this.potion = contents.potion().map(Holder::value).orElse(null);
        this.getEntityData().set(COLOUR, contents.getColor());
    }

    public void spawnPotionParticles(int particleCount) {
        int colour = this.getEntityData().get(COLOUR);
        if (colour != -1 && particleCount > 0) {
            Level level = this.level();
            double cR = (double) (colour >> 16 & 255) / 255.0D;
            double cG = (double) (colour >> 8 & 255) / 255.0D;
            double cB = (double) (colour & 255) / 255.0D;

            for (int i = 0; i < particleCount; i++) {
                level.addParticle(
                        ColorParticleOption.create(
                                ParticleTypes.ENTITY_EFFECT, (float) cR, (float) cG, (float) cB),
                        this.getRandomX(0.5d),
                        this.getRandomY(),
                        this.getRandomZ(0.5d),
                        0.0D,
                        0.0D,
                        0.0D);
            }
        }
    }
}
