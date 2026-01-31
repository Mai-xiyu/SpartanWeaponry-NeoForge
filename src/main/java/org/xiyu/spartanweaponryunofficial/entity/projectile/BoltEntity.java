package org.xiyu.spartanweaponryunofficial.entity.projectile;

import java.util.Arrays;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class BoltEntity extends AbstractArrow implements IEntityWithComplexSpawn
{
	protected final String NBT_BOLT = "Bolt";
	protected final String NBT_POTION = "Potion";
	protected final String NBT_POTION_COLOUR = "PotionColour";

	protected static final EntityDataAccessor<Integer> DATA_COLOUR = SynchedEntityData.defineId(BoltEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<ItemStack> DATA_BOLT = SynchedEntityData.defineId(BoltEntity.class, EntityDataSerializers.ITEM_STACK);
	protected Potion potion = null;
	
	protected float baseDamage = 1.0f;
	protected float rangeMultiplier = 1.0f;
	protected float armorPiercingFactor = 0.0f;
	
	public BoltEntity(EntityType<? extends BoltEntity> type, Level level)
    {
        super(type, level);
    }

    public BoltEntity(EntityType<? extends BoltEntity> type, double x, double y, double z, Level level, ItemStack pickupItemStack, ItemStack weaponStack)
    {
		super(type, x, y, z, level, pickupItemStack, weaponStack);
    }
    
    public BoltEntity(EntityType<? extends BoltEntity> type, LivingEntity shooter, Level level, ItemStack pickupItemStack, ItemStack weaponStack)
    {
		super(type, shooter, level, pickupItemStack, weaponStack);
    }

    public BoltEntity(LivingEntity shooter, Level level, ItemStack boltStack, ItemStack weaponStack)
    {
    	this(ModEntities.BOLT.get(), shooter, level, boltStack, weaponStack);
    }
    
	
	public void initEntity(float baseDamage, float rangeMultiplier, float armorPiercingFactor, ItemStack boltStack)
	{
		this.baseDamage = baseDamage;
		this.rangeMultiplier = rangeMultiplier;
		this.armorPiercingFactor = armorPiercingFactor;
		this.setBaseDamage(baseDamage);
		getEntityData().set(DATA_BOLT, boltStack);
	}
    
/*    @Override
    public void shootFromRotation(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
    	super.shootFromRotation(shooter, pitch, yaw, p_184547_4_, (float)(velocity * rangeMultiplier), inaccuracy);
    }*/
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) 
	{
		super.defineSynchedData(builder);
		builder.define(DATA_COLOUR, -1);
		builder.define(DATA_BOLT, ItemStack.EMPTY);
	}
    
    @Override
    public void tick() 
    {
    	super.tick();
    	
    	Level level = level();
		if(level.isClientSide)
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
	         getEntityData().set(DATA_COLOUR, -1);
		}
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result)
    {
    	Level level = level();
		RegistryAccess registryAccess = level.registryAccess();
    	Entity entity = result.getEntity();
        float velocity = (float)getDeltaMovement().length();
        int damage = Mth.ceil(Mth.clamp((double)velocity * getBaseDamage(), 0.0D, 2.147483647E9D));

        if(isCritArrow()) 
        {
        	long critDamageBonus = (long)random.nextInt(damage / 2 + 2);
        	damage = (int)Math.min(critDamageBonus + (long)damage, 2147483647L);
        }

        Entity shooter = getOwner();
        DamageSource source;
        if(shooter == null)
        	source = ModDamageTypes.armorPiercingProjectile(this, this);
        else 
        {
        	source = ModDamageTypes.armorPiercingProjectile(this, shooter);
        	if(shooter instanceof LivingEntity)
        		((LivingEntity)shooter).setLastHurtMob(entity);
        }

        boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
        int fireTimer = entity.getRemainingFireTicks();
        if(isOnFire() && !isEnderman)
	        entity.igniteForSeconds(5.0F);

	        if(entity.hurt(source, (float)damage)) 
        {
        	if(isEnderman)
        		return;

        	if(entity instanceof LivingEntity) 
        	{
        		LivingEntity livingentity = (LivingEntity)entity;
        		if(!level.isClientSide && getPierceLevel() <= 0)
        			livingentity.setArrowCount(livingentity.getArrowCount() + 1);

	        		int knockback = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.KNOCKBACK), getPickupItem());
	        		if(knockback > 0)
        		{
	        			Vec3 vector3d = getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D);
        			if (vector3d.lengthSqr() > 0.0D)
        				livingentity.push(vector3d.x, 0.1D, vector3d.z);
        		}

        		doPostHurtEffects(livingentity);
        		if(shooter != null && livingentity != shooter && livingentity instanceof Player && shooter instanceof ServerPlayer && !isSilent())
        			((ServerPlayer)shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));


	        		if(!level.isClientSide && shooter instanceof ServerPlayer serverplayerentity && !entity.isAlive() && shotFromCrossbow())
	        			CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
        	}

        	playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
        	if(getPierceLevel() <= 0)
        		discard();
        } 
        else 
        {
        	entity.setRemainingFireTicks(fireTimer);
        	setDeltaMovement(getDeltaMovement().scale(-0.1D));
        	setYRot(getYRot() + 180.0f);
        	yRotO += 180.0F;
        	if(!level.isClientSide && getDeltaMovement().lengthSqr() < 1.0E-7D)
        	{
        		if (pickup == AbstractArrow.Pickup.ALLOWED)
        			spawnAtLocation(getPickupItem(), 0.1F);

        		discard();
        	}
        }
    }
	
	@Override
	protected void doPostHurtEffects(LivingEntity living) 
	{
		super.doPostHurtEffects(living);
    	Level level = level();
		
		if(potion != null)
		{
			for(MobEffectInstance effect : potion.getEffects())
			{
				living.addEffect(new MobEffectInstance(effect.getEffect(), Math.max(effect.getDuration() / 8, 1), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
			}
		}
		
		Item arrowItem = getPickupItem().getItem();
		
		// Spawn lightning under the right weather conditions (during a thunderstorm)
		if(level.isThundering() && arrowItem == ModItems.COPPER_BOLT.get() || arrowItem == ModItems.TIPPED_COPPER_BOLT.get())
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
	protected ItemStack getPickupItem()
	{
		return getEntityData().get(DATA_BOLT);
	}

	@Override
	protected ItemStack getDefaultPickupItem()
	{
		return getPickupItem();
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buffer)
	{
		double x, y, z;
		x = buffer.readDouble();
		y = buffer.readDouble();
		z = buffer.readDouble();
		setDeltaMovement(x, y, z);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains(NBT_POTION, 8))
			potion = BuiltInRegistries.POTION.get(ResourceLocation.parse(compound.getString(NBT_POTION)));
		
		getEntityData().set(DATA_COLOUR, compound.contains(NBT_POTION_COLOUR) ? compound.getInt(NBT_POTION_COLOUR) : -1);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer)
	{
		buffer.writeDouble(getDeltaMovement().x);
		buffer.writeDouble(getDeltaMovement().y);
		buffer.writeDouble(getDeltaMovement().z);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) 
	{
		super.addAdditionalSaveData(compound);
		
		if(potion != null)
		{
			compound.putString(NBT_POTION, BuiltInRegistries.POTION.getKey(potion).toString());
		}
		
		compound.putInt(NBT_POTION_COLOUR, getEntityData().get(DATA_COLOUR));
	}
	
	public float getRangeMultiplier() 
	{
		return rangeMultiplier;
	}
	
	public void setPotionEffect(ItemStack stack)
	{
		PotionContents contents = stack.getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		potion = contents.potion().map(holder -> holder.value()).orElse(null);
		getEntityData().set(DATA_COLOUR, contents.getColor());
	}
	
	private void spawnPotionParticles(int particleCount)
	{
		int colour = getEntityData().get(DATA_COLOUR);
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
	
	public boolean isValid()
	{
		return !getPickupItem().isEmpty();
	}
	
	public ResourceLocation getTexture()
	{
		ItemStack boltStack = getPickupItem();
		if(boltStack.isEmpty())
			return ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "missing_stack");
					
		String boltRegName = BuiltInRegistries.ITEM.getKey(boltStack.getItem()).getPath();
		
		String prefix = "tipped_";
		int idx = boltRegName.indexOf(prefix);
		if(idx != -1)
		{
			boltRegName = boltRegName.substring(idx + prefix.length());
		}
		return ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/entity/projectiles/" + boltRegName + ".png");
	}
}
