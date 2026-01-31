package org.xiyu.spartanweaponryunofficial.item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.trait.IGenericTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.IThrowingTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.client.gui.HudCrosshairThrowingWeapon;
import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class ThrowingWeaponItem extends Item implements IWeaponTraitContainer<ThrowingWeaponItem>, IReloadable, IHudCrosshair
{
	public static final String NBT_AMMO_USED = "AmmoUsed";
	public static final String NBT_UUID = "UUID";
	public static final String NBT_ORIGINAL = "Original";
	
	protected float attackDamage = 1.0f;
	protected double attackSpeed = 0.0D;
	protected float throwVelocity = 2.0f;
	protected float throwDamageMultiplier = 2.0f;
	protected WeaponMaterial material;
	protected String customDisplayName = null;
	protected boolean doCraftCheck = true;
	protected boolean canBeCrafted = true;
	protected int maxAmmo = 1;
	protected int maxChargeTicks = 5;

	protected ItemAttributeModifiers modifiers;
	protected final WeaponArchetype archetype;
	
	protected List<WeaponTrait> traits;
	
	public ThrowingWeaponItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, float weaponSpeed, int maxAmmoCapacity, int chargeTicks)
	{
		super(prop.durability(materialIn.getUses() / 4));
		material = materialIn;
		setAttackDamage(weaponBaseDamage, weaponDamageMultiplier);
		setAttackSpeed(weaponSpeed);
		maxAmmo = maxAmmoCapacity;
		setChargeTicks(chargeTicks);

		if(FMLEnvironment.dist.isClient())
			ClientHelper.registerThrowingWeaponPropertyOverrides(this);
		
		archetype = archetypeIn;
		ReloadableHandler.addToItemReloadList(this);
	}
	
	public ThrowingWeaponItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, float weaponSpeed, int maxAmmoCapacity, int chargeTicks, String customDisplayNameIn)
	{
		this(prop, material, archetypeIn, weaponBaseDamage, weaponDamageMultiplier, weaponSpeed, maxAmmoCapacity, chargeTicks);
		if(material.useCustomDisplayName())
			customDisplayName = customDisplayNameIn;
	}

	@Override
	public void reload() 
	{
		ImmutableList.Builder<WeaponTrait> builder = ImmutableList.builder();
		builder.addAll(archetype.getTraits());
		builder.addAll(material.getBonusTraits(archetype.getType()));
		traits = builder.build();

		// Initialize the weapon's attribute modifier map
		ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
		mapBuilder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (double)getDirectAttackDamage(), AttributeModifier.Operation.ADD_VALUE));
		mapBuilder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (double)attackSpeed - 4.0d, AttributeModifier.Operation.ADD_VALUE));
		
		// Add melee attributes from Weapon Traits
//		if(traits != null)
			traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onModifyAttributes(mapBuilder)));
		
		var builtModifiers = mapBuilder.build();
		ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
		builtModifiers.forEach((attribute, modifier) ->
			attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
		modifiers = attributeBuilder.build();

		WeaponTrait extraDamageTrait = getFirstWeaponTraitWithType(WeaponTraits.TYPE_DAMAGE_BONUS_THROWN);
		throwDamageMultiplier = extraDamageTrait != null ? extraDamageTrait.getMagnitude() : 1.0f;
	}
	
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) 
	{
		return modifiers != null ? modifiers : super.getDefaultAttributeModifiers(stack);
	}
	
	@Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected)
    {
		initNBT(stack, true);
		
		if(entity instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity)entity;
			
			if(traits != null)
			{
				traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onItemUpdate(material, stack, level, living, itemSlot, isSelected)));
			}
		}
    }

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
    {
		return archetype.isBladed() && state.is(Blocks.COBWEB) ? 15.0f : 1.0f;
    }
	
	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos,
			LivingEntity entityLiving)
	{
		// Make the throwing weapon take damage when digging
		if(!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0f)
		{
			damageThrowingWeapon(stack, 2, entityLiving);
		}
		return false;
	}
	
	public void damageThrowingWeapon(ItemStack stack, int damage, LivingEntity entity)
	{
		//stack.damageItem(damage, entity);
		if(stack.isDamageableItem() && ItemStackDataHelper.getTag(stack).getInt(NBT_AMMO_USED) < getMaxAmmo(stack, entity.level()) && 
				(!(entity instanceof Player) || !((Player)entity).getAbilities().instabuild))
		{
			int currentDamage = stack.getDamageValue();
			int maxDamage = stack.getMaxDamage();
			int newDamage = currentDamage + damage;
			if(newDamage >= maxDamage)
			{
				int ammo = ItemStackDataHelper.getTag(stack).getInt(NBT_AMMO_USED);
				int updatedAmmo = ammo + 1;
				ItemStackDataHelper.updateTag(stack, tag -> tag.putInt(NBT_AMMO_USED, updatedAmmo));
				
				if(entity instanceof Player)
				{
					((Player)entity).awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
				}
				
				stack.setDamageValue(0);
			}
			else
			{
				stack.setDamageValue(newDamage);
			}
		}
	}
	
	@Override
	public Component getName(ItemStack stack) 
	{
		if(customDisplayName == null)
			return super.getName(stack);
		return Component.translatable(customDisplayName, material.translateName());
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) 
	{
		boolean isShiftPressed = Screen.hasShiftDown();

		if(doCraftCheck && tooltipContext != null && tooltipContext.level() != null)
    	{
    		if(!ClientConfig.INSTANCE.forceDisableUncraftableTooltips.get() && material.getModId() == ModSpartanWeaponry.ID)
    		{
	    		var tag = BuiltInRegistries.ITEM.getTag(material.getRepairTag());
	        	if(tag.isEmpty() || tag.get().size() == 0)
	    			canBeCrafted = false;
    		}
	    	doCraftCheck = false;
    	}

    	if(!canBeCrafted)
    		tooltip.add(Component.translatable(String.format("tooltip.%s.uncraftable_missing_material", ModSpartanWeaponry.ID), material.getRepairTagName()).withStyle(ChatFormatting.RED));
    	
    	archetype.addTagErrorTooltip(stack, tooltip);
    	material.addTagErrorTooltip(stack, tooltip);
    	
		var stackTag = ItemStackDataHelper.getTag(stack);
		if(stackTag.contains(NBT_ORIGINAL) && !stackTag.getBoolean(NBT_ORIGINAL))
    		tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.not_original", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.DARK_RED));
		if(stackTag.hasUUID(NBT_UUID) && flagIn.isAdvanced())
			tooltip.add(Component.literal("UUID: " + ChatFormatting.GRAY.toString() + stackTag.getUUID(NBT_UUID).toString()).withStyle(ChatFormatting.DARK_PURPLE));
		int mxAmmo = getMaxAmmo(stack, tooltipContext.level());
		tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.ammo", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.throwable.ammo.value", ModSpartanWeaponry.ID), mxAmmo - stackTag.getInt(NBT_AMMO_USED), mxAmmo).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.charge_time", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.throwable.charge_time.value", ModSpartanWeaponry.ID), getMaxChargeTicks(stack, tooltipContext.level()) / 20.0f).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    	
		if(traits != null && !traits.isEmpty())
		{
			if(isShiftPressed)
				tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
			else
				tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA.toString() + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));

			archetype.addTraitsToTooltip(stack, tooltip, isShiftPressed);
			
	    	material.addTraitsToTooltip(stack, archetype.getType(), tooltip, isShiftPressed);
			tooltip.add(Component.empty());
		}

		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
		traits.forEach((trait) -> trait.getMeleeCallback().ifPresent((callback) -> callback.onHitEntity(material, stack, target, attacker, null)));
    	
		// Deal double durability damage when used as a melee weapon
		if(ItemStackDataHelper.getTag(stack).getInt(NBT_AMMO_USED) < getMaxAmmo(stack, attacker.level()))
    		damageThrowingWeapon(stack, 2, attacker);
    	
        return true;
    }
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(ItemStackDataHelper.getTag(stack).getInt(NBT_AMMO_USED) == getMaxAmmo(stack, levelIn))
			return InteractionResultHolder.fail(stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level levelIn, LivingEntity entityLiving, int timeLeft) 
	{
		if(entityLiving instanceof Player)
		{
			int maxAmmo = getMaxAmmo(stack, levelIn);
			int ammoCount = maxAmmo - ItemStackDataHelper.getTag(stack).getInt(NBT_AMMO_USED);
			
			if(ammoCount > 0)
			{
				Player player = (Player)entityLiving;
	
				int maxCharge = getMaxChargeTicks(stack, levelIn);
	            int charge = Math.min(getUseDuration(stack, entityLiving) - timeLeft, maxCharge);
				
				if (!levelIn.isClientSide && charge > 2)
		        {
		            ThrowingWeaponEntity thrown = createThrowingWeaponEntity(levelIn, player, stack, charge);
		            float chargePerc = (charge / (float)maxCharge);
		            
		            if(thrown == null)	return;
		            
		            thrown.setWeapon(stack);
		            int velocityBonus = ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.PROPEL, stack);
		            thrown.shootFromRotation(player, player.xRotO, player.yRotO, 0.0F, throwVelocity * ((velocityBonus * 0.2f) + 1) * (chargePerc * 0.9f + 0.1f), 0.5f);
		            
		            traits.forEach((trait) -> trait.getThrowingCallback().ifPresent((callback) -> callback.onThrowingProjectileSpawn(material, thrown)));
		            
		            double damageMultiplier = (throwDamageMultiplier - 1.0f) * chargePerc + 1.0f;
		            thrown.setBaseDamage((getDirectAttackDamage() + 1.0d) * damageMultiplier);
		            
		            // Apply enchantments as necessary
		            int j = ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.RAZORS_EDGE, stack);
		            if (j > 0)
		            {
		            	thrown.setBaseDamage(thrown.getBaseDamage() + j * 0.5D + 0.5D);
		            }
		            if (ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.INCENDIARY, stack) > 0)
		            {
		            	thrown.igniteForSeconds(100.0F);
		            }
		            /*int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
		            if (k > 0)
		            {
		            	thrown.setKnockbackStrength(k);
		            }*/
		            
		            if(player.getAbilities().instabuild)
		            	thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		            else if(thrown.isValidThrowingWeapon())
		            {
		            	ammoCount--;
		            	final int finalAmmoCount = ammoCount;
		            	ItemStackDataHelper.updateTag(stack, tag -> tag.putInt(NBT_AMMO_USED, maxAmmo - finalAmmoCount));
		            	
		            	// If there is no ammo left and the stack isn't original (picked up from the ground to create a new stack), then delete the stack
		            	if(ammoCount == 0 && !ItemStackDataHelper.getTag(stack).getBoolean(NBT_ORIGINAL))
		            	{
			                stack.shrink(1);
			                if(stack.getCount() <= 0)
			                	player.getInventory().removeItem(stack);
		            	}
		            }
		            
		            if(thrown.isValidThrowingWeapon())
		            {
		            	stack.setDamageValue(0);
		            	levelIn.playSound((Player)null, player.getX(), player.getY(), player.getZ(), getThrowingSound(), SoundSource.PLAYERS, 0.5F, 0.4F / (levelIn.random.nextFloat() * 0.4F + 0.8F));
		            	levelIn.addFreshEntity(thrown);
		            }
		            
			        player.awardStat(Stats.ITEM_USED.get(this));
		        }
			}
		}
		super.releaseUsing(stack, levelIn, entityLiving, timeLeft);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) 
	{
		return 72000;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) 
	{
		return UseAnim.SPEAR;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level levelIn, Player playerIn) 
	{
		traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onCreateItem(material, stack)));
		
		initNBT(stack, true);
	}
	
	public ItemStack makeTabStack()
	{
		ItemStack stack = new ItemStack(this);
		
		initNBT(stack, false);
		return stack;
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) 
	{
		return material.getEnchantmentValue();
	}
	
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) 
	{
		// Allow Loyalty enchantments to work on Throwing Weapons
		RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		ResourceLocation enchantmentKey = registryAccess.registry(Registries.ENCHANTMENT)
				.map(registry -> registry.getKey(enchantment))
				.orElse(null);
		return enchantmentKey != null && enchantmentKey.equals(Enchantments.LOYALTY.location());
	}
    
    @Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) 
    {
    	int damage = amount;
    	for(WeaponTrait trait : traits)
    	{
    		if(trait.getGenericCallback().isPresent())
    			damage = trait.getGenericCallback().get().onDamageItem(stack, entity, damage);
    		if(damage <= 0)
    			break;
    	}
    	return Math.max(0, damage);
    }
	
	// IWeaponTraitContainer

	@Override
	public ThrowingWeaponItem getAsItem() 
	{
		return this;
	}
    
	@Override
	public boolean hasWeaponTrait(WeaponTrait prop)
	{
		return traits.contains(prop);
	}

	@Override
	public boolean hasWeaponTraitWithType(String type) 
	{
		return traits.stream().anyMatch((trait) -> trait.getType() == type);
	}

	@Override
	public WeaponTrait getFirstWeaponTraitWithType(String type)
	{
		for(WeaponTrait trait : traits)
		{
			if(trait.getType() == type)
				return trait;
		}
		return null;
	}

	@Override
	public List<WeaponTrait> getAllWeaponTraitsWithType(String type) 
	{
		if(traits.isEmpty())
			return ImmutableList.of();
		
		return traits.stream().filter((trait) -> trait.getType() == type).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public Collection<WeaponTrait> getAllWeaponTraits() 
	{
		// Traits are immutable after reloading anyway so it should be safe to reference them directly
		return traits;
	}

	@Override
	public WeaponMaterial getMaterial()
	{
		return material;
	}
	
	// New methods
	public float getDirectAttackDamage() 
	{
		return attackDamage;
	}

	public void setAttackDamage(float baseDamage, float damageMultiplier)
	{
		attackDamage = (material.getAttackDamageBonus() * damageMultiplier) + baseDamage - 1.0f;
	}
	
	public void setAttackSpeed(double speed)
	{
		attackSpeed = speed;
	}
	
	public void setChargeTicks(int chargeTicks)
	{
		maxChargeTicks = chargeTicks;
	}
	
	private Optional<IGenericTraitCallback> getGenericCallback(WeaponTrait trait)
	{
		return trait.getMeleeCallback().isPresent() ? Optional.of(trait.getMeleeCallback().get()) : trait.getGenericCallback().isPresent() ? trait.getGenericCallback() : Optional.empty();
	}
	
	public void updateFromConfig(float baseDamage, float damageMultiplier, double speed, int chargeTicks)
	{
		setAttackDamage(baseDamage, damageMultiplier);
		setAttackSpeed(speed);
		setChargeTicks(chargeTicks);
	}
	
	/**
	 * Creates a new Throwing Weapon Entity that is used as a projectile.
	 * @param levelIn The World instance
	 * @param player The Player throwing the weapon
	 * @param stack The Throwing Weapon Item
	 * @param charge The total time (in ticks) that the weapon is held for before throwing it
	 * @return
	 */
	public ThrowingWeaponEntity createThrowingWeaponEntity(Level levelIn, Player player, ItemStack stack, int charge)
	{
		return new ThrowingWeaponEntity(ModEntities.THROWING_WEAPON.get(), player, levelIn, stack);
	}
	
	protected SoundEvent getThrowingSound()
	{
		return ModSounds.THROWN_WEAPON_THROW.get();
	}
	
	protected void initNBT(ItemStack stack, boolean initUUID) 
	{
		ItemStackDataHelper.updateTag(stack, tag -> {
			if(!tag.contains(NBT_AMMO_USED))
			{
				// And, because I don't think it would be a good idea to transfer the ammo value from the old version to the new one
				// Just fill 'er up
				tag.putInt(NBT_AMMO_USED, 0);
			}
			// Initialise UUID tag if necessary, and flag as original stack
			if(initUUID && !tag.hasUUID(NBT_UUID))
			{
				tag.putUUID(NBT_UUID, UUID.randomUUID());
				tag.putBoolean(NBT_ORIGINAL, true);
			}
		});
	}
	
	public int getMaxAmmo(ItemStack stack, RegistryAccess access)
	{
		if(access == null)
			return maxAmmo;
		int level = ModEnchantments.getLevel(access, ModEnchantments.EXPANSE, stack);
		// Find the value to increase by per level (if ammo increase is too small e.g. Boomerang; then use ammo + 1 per level instead)
		int increasePerLevel = Math.max((int)(maxAmmo * 0.25f), 1);
		return maxAmmo + (increasePerLevel * level);
	}

	public int getMaxAmmo(ItemStack stack, Level level)
	{
		return level != null ? getMaxAmmo(stack, level.registryAccess()) : maxAmmo;
	}
	
	public int getMaxAmmoBase()
	{
		return maxAmmo;
	}
	
	public int getMaxChargeTicks(ItemStack stack, RegistryAccess access)
	{
		if(access == null)
			return maxChargeTicks;
		int chargeTicks = (int)(maxChargeTicks * (1 - ModEnchantments.getLevel(access, ModEnchantments.SUPERCHARGE, stack) * 0.2f));
		if(traits != null)
			for(WeaponTrait trait : traits)
			{
				Optional<IThrowingTraitCallback> opt = trait.getThrowingCallback();
				if(opt.isPresent())
					chargeTicks = opt.get().modifyThrowingChargeTime(material, chargeTicks);
			}
		return chargeTicks;
	}

	public int getMaxChargeTicks(ItemStack stack, Level level)
	{
		return level != null ? getMaxChargeTicks(stack, level.registryAccess()) : maxChargeTicks;
	}
    
    @Override
    public ICrosshairOverlay getCrosshairHudElement() 
    {
    	return HudCrosshairThrowingWeapon::render;
    }
}
