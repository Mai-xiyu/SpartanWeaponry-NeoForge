package org.xiyu.spartanweaponryunofficial.entity.projectile;

import java.util.Collection;
import java.util.Optional;

import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.api.trait.IMeleeTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModParticles;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.RegistryAccess;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class ThrowingWeaponEntity extends AbstractArrow implements IEntityWithComplexSpawn
{
	public static final String NBT_WEAPON = "Weapon";
	protected static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(ThrowingWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
	protected static final EntityDataAccessor<Byte> DATA_RETURN = SynchedEntityData.defineId(ThrowingWeaponEntity.class, EntityDataSerializers.BYTE);
	protected int ticksInAir = 0;
	protected float waterInertia = 0.0f;
	protected boolean isReturning = false;
	protected boolean playedReturnSound = false;
	protected int despawnTicks = 0;
	
	public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level) 
	{
		super(type, level);
	}

	public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, Level level, double x, double y, double z, ItemStack weapon) 
	{
		super(type, x, y, z, level, weapon, weapon);
	}

	public ThrowingWeaponEntity(EntityType<? extends ThrowingWeaponEntity> type, LivingEntity shooter, Level level, ItemStack weapon) 
	{
		super(type, shooter, level, weapon, weapon);
	}
	
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) 
	{
		super.defineSynchedData(builder);
		builder.define(DATA_WEAPON, ItemStack.EMPTY);
		builder.define(DATA_RETURN, (byte)0);
	}

	@Override
	protected ItemStack getPickupItem()
	{
		return getEntityData().get(DATA_WEAPON);
	}

	@Override
	protected ItemStack getDefaultPickupItem()
	{
		return getWeaponItem();
	}
	
	public boolean isReturning()
	{
		return isReturning;
	}
	
	@Override
	public void tick() 
	{
		Level level = level();
		RegistryAccess registryAccess = level.registryAccess();
		if(waterInertia == 0.0f)
			waterInertia = ModEnchantments.getLevel(level.registryAccess(), ModEnchantments.HYDRODYNAMIC, getWeaponItem()) == 1 ? 0.98f : -1.0f;
		
		Entity thrower = getOwner();
		int returnLevel = getEntityData().get(DATA_RETURN);
		
		// Only process return logic if the weapon has Loyalty enchantment
		if(returnLevel > 0 && thrower != null)
		{
			// Check if we should start returning (after being in ground for a short time, or already returning)
			if(inGroundTime > 4 || isReturning)
			{
				if(thrower.isAlive() && (!(thrower instanceof ServerPlayer) || !thrower.isSpectator()))
				{
					// Return to thrower - ensure we're in the returning state
					if(!isReturning)
					{
						setNoPhysics(true);
						inGround = false;
						isReturning = true;
						setNoGravity(true);
					}
					
					// Keep forcing these states while returning to prevent position conflicts
					if(isReturning)
					{
						if(inGround)
							inGround = false;
						if(!isNoPhysics())
							setNoPhysics(true);
					}
					
					Vec3 distance = thrower.getEyePosition().subtract(position());
					setPosRaw(getX(), getY() + distance.y * 0.015 * (double)returnLevel, getZ());
					if(level.isClientSide)
					{
						yOld = getY();
					}
					
					double velocity = 0.10d * (double)returnLevel;
					setDeltaMovement(getDeltaMovement().scale(0.95d).add(distance.normalize().scale(velocity)));
					
					if(!playedReturnSound)
					{
						playSound(ModSounds.THROWING_WEAPON_LOYALTY_RETURN.get(), 10.0F, 1.0F);
						playedReturnSound = true;
					}
				}
				else if(!thrower.isAlive() || thrower.isSpectator())
				{
					// Thrower is dead or spectating - stop returning and drop naturally
					if(isReturning)
					{
						setNoPhysics(false);
						isReturning = false;
						setNoGravity(false);
						playedReturnSound = false;
					}
				}
			}
		}
		
		super.tick();
		
		// Post-tick: ensure returning state is maintained (fix for position snap-back issues)
		if(isReturning && inGround)
			inGround = false;
		
		if(!inGround)
			++ticksInAir;
		else if(ticksInAir != 0)
			ticksInAir = 0;
	}
	
	protected void tickDespawn() 
	{
		++despawnTicks;
		if (despawnTicks >= 1200) 
		{
			dropAsItem();
			discard();
		}
	}
	
	public int getPortalWaitTime()
	{
		// Set this time higher to prevent this entity from being duplicated when hitting a portal...
		return 100;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onHitEntity(EntityHitResult hitResult) 
	{
		Entity entity = hitResult.getEntity();
		Level level = level();
		RegistryAccess registryAccess = level.registryAccess();
		Entity shooter = getOwner();    //func_234616_v_();
		
		if(entity != null)
		{
			ItemStack weapon = getWeaponItem();
			float damage = Mth.ceil(getBaseDamage());
			DamageSource src;

            if (isCritArrow())
            {
            	damage += random.nextInt((int)damage / 2 + 2);
            }
            
			// Try and catch the throwing weapon if possible.
			if(shooter != null && (canBeCaughtInMidair(shooter, entity) || isReturning) && entity instanceof Player player)
			{
				if(attemptCatch(player))
					return;
			}
			if(shooter == null)
//				src = new IndirectEntityDamageSource("mob", this, this).setProjectile();
				src = ModDamageTypes.thrownWeaponMob(this, this);
			else if(shooter instanceof Player)
//				src = new IndirectEntityDamageSource("player", this, shooter).setProjectile();
				src = ModDamageTypes.thrownWeaponPlayer(this, shooter);
			else
//				src = new IndirectEntityDamageSource("mob", this, shooter).setProjectile();
				src = ModDamageTypes.thrownWeaponMob(this, shooter);
			
			if(entity instanceof LivingEntity && shooter instanceof LivingEntity)
			{
				if(weapon.getItem() instanceof IWeaponTraitContainer &&  !entity.is(shooter))
				{
					IWeaponTraitContainer<Item> container = (IWeaponTraitContainer<Item>)weapon.getItem();
	            	WeaponMaterial material = container.getMaterial();
	            	Collection<WeaponTrait> traits = container.getAllWeaponTraits();
	            	
	            	for(WeaponTrait trait : traits)
	            	{
	            		Optional<IMeleeTraitCallback> opt = trait.getMeleeCallback();
	            		if(opt.isPresent())
	            		{
	            			IMeleeTraitCallback callback = opt.get();
	            			damage = callback.modifyDamageDealt(material, damage, src, (LivingEntity)shooter, (LivingEntity)entity);
	            			callback.onHitEntity(container.getMaterial(), weapon, (LivingEntity)entity, (LivingEntity)shooter, this);
	            		}
	            	}
				}
				if(weapon.is(ModItemTags.OILABLE_WEAPONS))
				{
					IOilHandler oilHandler = weapon.getCapability(ModCapabilities.OIL_CAPABILITY);
					if(oilHandler != null && oilHandler.isOiled())
					{
						float dmgUnmodified = damage;
						damage = oilHandler.useEffect(damage, level, (LivingEntity)entity, (LivingEntity)shooter, weapon);
						if(damage != dmgUnmodified && !level.isClientSide())
							((ServerLevel)level).sendParticles(ModParticles.OIL_DAMAGE_BOOSTED.get(), entity.getX(), entity.getY() + (entity.getBbHeight() / 2.0f), entity.getZ(), 8, 0.2d, 0.2d, 0.2d, 0.5d);
					}
				}
			}
			
			boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
			if (isOnFire() && !isEnderman)
			{
				entity.igniteForSeconds(5.0F);
			}
            
			if(entity.hurt(src, damage))
            {
				if(weapon.isDamageableItem())
				{
					int newDamage = weapon.getDamageValue() + 1;
					if(newDamage >= weapon.getMaxDamage())
					{
						playSound(SoundEvents.ITEM_BREAK, 0.8f, 0.8f + random.nextFloat() * 0.4f);
						level.broadcastEntityEvent(this, (byte)3);
						discard();
					}
					else
					{
						weapon.setDamageValue(newDamage);
					}
				}
            	
				if (entity instanceof LivingEntity)
                {
            		LivingEntity entitylivingbase = (LivingEntity)entity;

					int knockback = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.KNOCKBACK), weapon);
					if (knockback > 0)
                    {
						Vec3 knockVec = getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D);

                        if (knockVec.lengthSqr() > 0.0F)
                            entitylivingbase.push(knockVec.x, 0.1d, knockVec.z);
                    }

                    doPostHurtEffects(entitylivingbase);

                    if (shooter != null && entitylivingbase != shooter && entitylivingbase instanceof Player && shooter instanceof ServerPlayer)
                    {
                        ((ServerPlayer)shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                    }
                }

                playSound(getMobHitSound(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));

                if (!isEnderman)
                {
                	setDeltaMovement(getDeltaMovement().scale(-0.1d));
                    setYRot(getYRot() + 180.0f);
                    yRotO += 180.0F;
                }
            }
            else
            {
            	setDeltaMovement(getDeltaMovement().scale(-0.1d));
                setYRot(getYRot() + 180.0f);
                yRotO += 180.0F;
                ticksInAir = 0;

                if (!level.isClientSide && getDeltaMovement().lengthSqr() < 1.0e-7d)
                {
                	if(getEntityData().get(DATA_RETURN) > 0 && !isNoPhysics())
                		setNoPhysics(true);
                	else if (pickup == AbstractArrow.Pickup.ALLOWED)
                    {
                		dropAsItem();
                        discard();
                    }
                }
            }
        }
        else
        {
    		super.onHitEntity(hitResult);
        }
	}
	
	@Override
	protected void onHitBlock(BlockHitResult hitResult) 
	{
		Level level = level();
    	if(hitResult.getType() == Type.BLOCK)
    	{
    		if(!level.isClientSide)
    		{
    			BlockState state = level.getBlockState(hitResult.getBlockPos());
    			((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(hitResult.getBlockPos()), getX(), getY(), getZ(), 5, 0.1d, 0.1d, 0.1d, 0.05d);
    		}
    		
    		ItemStack stack = getWeaponItem();
    		removeEnchantments(stack);
    	}
		super.onHitBlock(hitResult);
	}
	
	/**
	 * Delete any enchantments from the provided item stack to prevent duping of said enchantments
	 * @param stack The item stack to remove enchantments from
	 */
	protected void removeEnchantments(ItemStack stack)
	{
		Level level = level();
		if(stack.isEnchanted() && ItemStackDataHelper.getTag(stack).contains(ThrowingWeaponItem.NBT_AMMO_USED))
		{
			EnchantmentHelper.setEnchantments(stack, ItemEnchantments.EMPTY);

    		// Spawn magic dispersal particles
    		if(!level.isClientSide)
    			((ServerLevel)level).sendParticles(ParticleTypes.WITCH, getX(), getY(), getZ(), 10, 0.1d, 0.1d, 0.1d, 0.2d);
		}
	}
	
	protected void dropAsItem()
	{
		ItemStack stack = getWeaponItem();
		removeEnchantments(stack);
		spawnAtLocation(stack, 0.1F);
	}
	
	@Override
	public void playerTouch(Player entityIn)
	{
		if(inGround || isReturning)
			attemptCatch(entityIn);
	}
	
	@Override
	protected float getWaterInertia()
	{
		return waterInertia > 0.0f ? waterInertia : super.getWaterInertia();
	}
	
	protected SoundEvent getDefaultHitGroundSoundEvent()
	{
		return ModSounds.THROWN_WEAPON_HIT_GROUND.get();
	}
	
	protected SoundEvent getMobHitSound()
	{
		return ModSounds.THROWN_WEAPON_HIT_MOB.get();
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer)
	{
		ItemStack weapon = getWeaponItem();
		// Use OPTIONAL_STREAM_CODEC to handle empty ItemStack
		ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, weapon);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) 
	{
		ItemStack weapon = ItemStack.OPTIONAL_STREAM_CODEC.decode(additionalData);
		if(!weapon.isEmpty())
		{
			setWeapon(weapon);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
	}
	
	// New Methods
	public ItemStack getWeaponItem()
	{
		return getPickupItem();
	}
	
	public boolean isValidThrowingWeapon()
	{
		return !getWeaponItem().isEmpty();
	}
	
	public void setWeapon(ItemStack weaponStack)
	{
		ItemStack stack = weaponStack.copy();
		// Store original weapon data for ammo system
		if(!ItemStackDataHelper.getTag(stack).contains(ThrowingWeaponItem.NBT_ORIGINAL))
		{
			// Save the original weapon state before modifying ammo counter
			CompoundTag original = new CompoundTag();
			stack.save(level().registryAccess(), original);
			ItemStackDataHelper.updateTag(stack, tag -> tag.put(ThrowingWeaponItem.NBT_ORIGINAL, original));
		}
		getEntityData().set(DATA_WEAPON, stack);
		int loyalty = EnchantmentHelper.getItemEnchantmentLevel(level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOYALTY), stack);
		getEntityData().set(DATA_RETURN, (byte)loyalty);
	}
	
	protected boolean canBeCaughtInMidair(Entity shooter, Entity entityHit)
	{
		return shooter == entityHit && isNoPhysics();
	}
	
	public int getTicksInAir()
	{
		return ticksInAir;
	}
	
	protected boolean attemptCatch(Player player)
	{
		Level level = level();
		if(!level.isClientSide && shakeTime <= 0)
		{
			boolean canBePickedUp = pickup == AbstractArrow.Pickup.ALLOWED || pickup == AbstractArrow.Pickup.CREATIVE_ONLY && player.getAbilities().instabuild;
			
			if(pickup == AbstractArrow.Pickup.ALLOWED)
			{
				// Restore ammo: decrease the ammo used counter when picking up the weapon
				ItemStack pickUpStack = getWeaponItem().copy();
				removeEnchantments(pickUpStack);
				
				// Try to merge with existing weapon in inventory
				boolean merged = false;
				for(int i = 0; i < player.getInventory().getContainerSize(); i++)
				{
					ItemStack invStack = player.getInventory().getItem(i);
					if(!invStack.isEmpty() && ItemStack.isSameItem(invStack, pickUpStack))
					{
						// Found matching weapon, decrease its ammo counter
						int currentAmmo = ItemStackDataHelper.getTag(invStack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
						if(currentAmmo > 0)
						{
							ItemStackDataHelper.updateTag(invStack, tag -> tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, Math.max(0, tag.getInt(ThrowingWeaponItem.NBT_AMMO_USED) - 1)));
							merged = true;
							break;
						}
					}
				}
				
				// If couldn't merge, try adding as new item with reset ammo
				if(!merged)
				{
					ItemStackDataHelper.updateTag(pickUpStack, tag -> tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, 0));
					canBePickedUp = player.getInventory().add(pickUpStack);
				}
			}
			
			if(canBePickedUp)
			{
				player.take(this, 1);
				discard();
			}
			
			return canBePickedUp;
		}
		return false;
	}
}
