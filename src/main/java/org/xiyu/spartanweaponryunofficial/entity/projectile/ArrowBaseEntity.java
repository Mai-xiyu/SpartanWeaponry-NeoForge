package org.xiyu.spartanweaponryunofficial.entity.projectile;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class ArrowBaseEntity extends AbstractArrow implements IEntityWithComplexSpawn
{
	protected final String NBT_ARROW = "Arrow";
	protected final String NBT_POTION = "Potion";
	protected final String NBT_POTION_COLOUR = "PotionColour";
	
	protected static final EntityDataAccessor<Integer> COLOUR = SynchedEntityData.defineId(ArrowBaseEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<ItemStack> ARROW = SynchedEntityData.defineId(ArrowBaseEntity.class, EntityDataSerializers.ITEM_STACK);
	protected Potion potion = null;
	
	protected float baseDamage = 1.0f;
	protected float rangeMultiplier = 1.0f;
	
	public ArrowBaseEntity(EntityType<? extends ArrowBaseEntity> type, Level level) 
	{
		super(type, level);
	}

	public ArrowBaseEntity(Level level, double x, double y, double z) 
	{
		super(ModEntities.ARROW_SW.get(), x, y, z, level, ItemStack.EMPTY, ItemStack.EMPTY);
	}

	public ArrowBaseEntity(Level level, LivingEntity shooter) 
	{
		super(ModEntities.ARROW_SW.get(), shooter, level, ItemStack.EMPTY, ItemStack.EMPTY);
	}
	
	public void initEntity(float baseDamage, float rangeMultiplier, ItemStack arrowStack)
	{
		this.baseDamage = baseDamage;
		this.rangeMultiplier = rangeMultiplier;
		setBaseDamage(baseDamage);
		setArrowStack(arrowStack);
	}
	
	@Override
	public void shootFromRotation(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) 
	{
		super.shootFromRotation(shooter, pitch, yaw, p_184547_4_, (float)(velocity * rangeMultiplier), inaccuracy);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) 
	{
		super.defineSynchedData(builder);
		builder.define(COLOUR, -1);
		builder.define(ARROW, ItemStack.EMPTY);
	}

	@Override
	public void tick() 
	{
		super.tick();
		Level level = level();
		if(level.isClientSide /*&& potion != null && potion != Potions.EMPTY*/)
		{
			if(inGround)
			{
				if(inGroundTime % 5 == 0)
					spawnPotionParticles(1);
			}
			else
				spawnPotionParticles(2);
		}
		else if(inGround && inGroundTime != 0 && inGroundTime >= 600)
		{
	         level.broadcastEntityEvent(this, (byte)0);
	         	 potion = null;
	         getEntityData().set(COLOUR, -1);
		}
	}
	
	@Override
	protected void doPostHurtEffects(LivingEntity living) 
	{
		if(potion == null)
			return;
		Entity entity = getEffectSource();
		Level level = level();
		
		for(MobEffectInstance effect : potion.getEffects())
		{
			living.addEffect(new MobEffectInstance(effect.getEffect(), Math.max(effect.getDuration() / 8, 1), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()), entity);
		}
		
		Item arrowItem = getPickupItem().getItem();
		
		// Spawn lightning under the right weather conditions (during a thunderstorm)
		if(level.isThundering() && arrowItem == ModItems.COPPER_ARROW.get() || arrowItem == ModItems.TIPPED_COPPER_ARROW.get())
		{
			// Roll a chance to spawn lightning under the right circumstances
			if(random.nextInt(4) < 1)	// ~25%
			{
				LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
				lightning.moveTo(Vec3.atBottomCenterOf(living.blockPosition()));
				lightning.setCause(living instanceof ServerPlayer ? (ServerPlayer)living : null);
				level.addFreshEntity(lightning);
			}
		}
	}
	
	@Override
	public void handleEntityEvent(byte id) 
	{
		if (id == 0) 
		{
			Level level = level();
			int i = getEntityData().get(COLOUR);
			if (i != -1) 
			{
	           double cR = (double)(i >> 16 & 255) / 255.0D;
	           double cG = (double)(i >> 8 & 255) / 255.0D;
	           double cB = (double)(i >> 0 & 255) / 255.0D;
	
	           for(int j = 0; j < 20; ++j)
	           {
				      level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float)cR, (float)cG, (float)cB), getRandomX(0.5d), getRandomY(), getRandomZ(0.5d), 0.0D, 0.0D, 0.0D);
	           }
			}
		} 
		else 
		{
			super.handleEntityEvent(id);
		}
	}
	
	@Override
	protected ItemStack getPickupItem() 
	{
		return getEntityData().get(ARROW);
//		return arrowStack;
	}

	@Override
	protected ItemStack getDefaultPickupItem()
	{
		return getPickupItem();
	}
	
	protected void setArrowStack(ItemStack stack)
	{
		ItemStack copy = stack.copy();
		copy.setCount(1);
		getEntityData().set(ARROW, copy);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) 
	{
//		buffer.writeItemStack(arrowStack);
		ItemStack.STREAM_CODEC.encode(buffer, getPickupItem());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) 
	{
		super.addAdditionalSaveData(compound);
		compound.put(NBT_ARROW, getPickupItem().save(level().registryAccess()));
		
		if(potion != null)
		{
			compound.putString(NBT_POTION, BuiltInRegistries.POTION.getKey(potion).toString());
		}
		
		compound.putInt(NBT_POTION_COLOUR, getEntityData().get(COLOUR));
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) 
	{
		setArrowStack(ItemStack.STREAM_CODEC.decode(additionalData));
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) 
	{
		super.readAdditionalSaveData(compound);
		CompoundTag nbt = compound.getCompound(NBT_ARROW);
		setArrowStack(ItemStack.parseOptional(level().registryAccess(), nbt));
		
		if(compound.contains(NBT_POTION, 8))
			potion = BuiltInRegistries.POTION.get(ResourceLocation.parse(compound.getString(NBT_POTION)));
		
		getEntityData().set(COLOUR, compound.contains(NBT_POTION_COLOUR) ? compound.getInt(NBT_POTION_COLOUR) : -1);
	}
	
	public boolean isValid()
	{
		return !getPickupItem().isEmpty();
	}
	
	public ResourceLocation getTexture()
	{
		String arrowRegName = BuiltInRegistries.ITEM.getKey(getPickupItem().getItem()).getPath();
		
		String prefix = "tipped_";
		int idx = arrowRegName.indexOf(prefix);
		if(idx != -1)
		{
			arrowRegName = arrowRegName.substring(idx + prefix.length());
		}
		return ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/entity/projectiles/" + arrowRegName + ".png");
	}
	
	public void setPotionEffect(ItemStack stack)
	{
		PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		potion = contents.potion().map(holder -> holder.value()).orElse(null);
		getEntityData().set(COLOUR, contents.getColor());
	}
	
	public void spawnPotionParticles(int particleCount)
	{
		int colour = getEntityData().get(COLOUR);
		if(colour != -1 && particleCount > 0)
		{
			Level level = level();
	        double cR = (double)(colour >> 16 & 255) / 255.0D;
	        double cG = (double)(colour >> 8 & 255) / 255.0D;
	        double cB = (double)(colour >> 0 & 255) / 255.0D;
	        
	        	for(int i = 0; i < particleCount; i++)
	        	{
	        		level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float)cR, (float)cG, (float)cB), getRandomX(0.5d), getRandomY(), getRandomZ(0.5d), 0.0D, 0.0D, 0.0D);
	        	}
		}
	}
}
