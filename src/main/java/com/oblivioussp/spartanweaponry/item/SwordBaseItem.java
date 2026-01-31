
package com.oblivioussp.spartanweaponry.item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.IReloadable;
import com.oblivioussp.spartanweaponry.api.IWeaponTraitContainer;
import com.oblivioussp.spartanweaponry.api.ReloadableHandler;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.WeaponTraits;
import com.oblivioussp.spartanweaponry.api.trait.IGenericTraitCallback;
import com.oblivioussp.spartanweaponry.api.trait.VersatileWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.client.ClientHelper;
import com.oblivioussp.spartanweaponry.util.ClientConfig;
import com.oblivioussp.spartanweaponry.util.WeaponArchetype;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ItemAbility;

public class SwordBaseItem extends SwordItem implements IWeaponTraitContainer<SwordBaseItem>, IReloadable
{
	protected float attackDamage = 1.0f;
	protected double attackSpeed = 0.0D;
	protected WeaponMaterial material;
	protected String customDisplayName = null;
	
	protected boolean doCraftCheck = true;
	protected boolean canBeCrafted = true;
	
	protected ItemAttributeModifiers modifiers;
	protected final WeaponArchetype archetype;
	
	/**
	 * A list of *ALL* Weapon Traits, including material bonus traits. Refreshed when the world is loaded (after tags are updated)
	 */
	protected List<WeaponTrait> traits = ImmutableList.of();
	
	public SwordBaseItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, double weaponSpeed) 
	{
		super(materialIn, prop.durability(materialIn.getUses()));
		material = materialIn;
		archetype = archetypeIn;
		setAttackDamageAndSpeed(weaponBaseDamage, weaponDamageMultiplier, weaponSpeed);
		
		ReloadableHandler.addToItemReloadList(this);
		
		if(FMLEnvironment.dist.isClient())
			ClientHelper.registerMeleeWeaponPropertyOverrides(this);
	}
	
	public SwordBaseItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, double weaponSpeed, String customDisplayNameIn)
	{
		this(prop, materialIn, archetypeIn, weaponBaseDamage, weaponDamageMultiplier, weaponSpeed);
		if(materialIn.useCustomDisplayName())
			customDisplayName = customDisplayNameIn;
	}
	
	@Override
	public void reload() 
	{
		setAttackDamageAndSpeed(archetype.getBaseDamage(), archetype.getDamageMultiplier(), archetype.getAttackSpeed());
		
		ImmutableList.Builder<WeaponTrait> builder = ImmutableList.builder();

//		Log.info("'" + ForgeRegistries.ITEMS.getKey(this).toString() +  "' -> Material: " + (material != null ? material : "NULL!"));
		builder.addAll(archetype.getTraits());
		builder.addAll(material.getBonusTraits(archetype.getType()));
		traits = builder.build();
		
		// Initialize the weapon's attribute modifier map
		ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
		mapBuilder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, getDirectAttackDamage(), AttributeModifier.Operation.ADD_VALUE));
		mapBuilder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed - 4.0D, AttributeModifier.Operation.ADD_VALUE));
		
		// Add attributes from Weapon Traits
//		if(traits != null)
			traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onModifyAttributes(mapBuilder)));
		
		var builtModifiers = mapBuilder.build();
		ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
		builtModifiers.forEach((attribute, modifier) ->
			attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
		modifiers = attributeBuilder.build();
	}
	
/*	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) 
	{
//		return super.initCapabilities(stack, nbt);
		SwordBaseItem item = this;
		return new ICapabilityProvider()
			{
				@Override
				public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
				{
					return ModCapabilities.WEAPON_TRAITS.orEmpty(cap, LazyOptional.of(() -> item));
				}
			};
	}*/
	
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		return modifiers != null ? modifiers : super.getDefaultAttributeModifiers(stack);
	}
	
	/**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
	@Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected)
    {
		// Check for two-handed traits, and other such effects
		if(entity instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity)entity;
			
			if(traits != null)
				traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onItemUpdate(material, stack, level, living, itemSlot, isSelected)));
		}
    }

	/**
     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
     */
	public float getDamage()
    {
        return material.getAttackDamageBonus();
    }
	
	@Override
	public int getMaxDamage(ItemStack stack) 
	{
		return material.getUses();
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
    {
		for(WeaponTrait trait : getAllWeaponTraitsWithType(WeaponTraits.TYPE_VERSATILE))
		{
			VersatileWeaponTrait versatileTrait = (VersatileWeaponTrait)trait;
			if(state.is(versatileTrait.getEffectiveBlocks()))
				return material.getSpeed();
		}
		if(archetype.isBladed() && state.is(Blocks.COBWEB))
			return 15.0f;
        return super.getDestroySpeed(stack, state);
    }
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
	{
		return hasWeaponTrait(WeaponTraits.SHIELD_BREACH.get());
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
    	
		if(traits != null && !traits.isEmpty())
		{
			if(isShiftPressed)
				tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
			else
				tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA.toString() + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));

			archetype.addTraitsToTooltip(stack, tooltip, isShiftPressed);
//			tooltip.add(Component.empty());
		}
    	material.addTraitsToTooltip(stack, archetype.getType(), tooltip, isShiftPressed);
		
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}
	
	public float getDirectAttackDamage()
	{
		return attackDamage;
	}
	
	@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
		traits.forEach((trait) -> 
			trait.getMeleeCallback().ifPresent((callback) -> callback.onHitEntity(material, stack, target, attacker, null)));
		
        return super.hurtEnemy(stack, target, attacker);
    }
	
	@Override
	public InteractionResult useOn(UseOnContext contextIn) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		if(actionTrait.isPresent())
		{
			WeaponTrait trait = actionTrait.get();
			if(trait.getActionCallback().isPresent())
				return trait.getActionCallback().get().useOn(contextIn);
		}
		return super.useOn(contextIn);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		if(actionTrait.isPresent())
		{
			WeaponTrait trait = actionTrait.get();
			if(trait.getActionCallback().isPresent())
				return trait.getActionCallback().get().use(stack, levelIn, player, hand);
		}
		return super.use(levelIn, player, hand);
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		actionTrait.ifPresent((trait) -> 
			trait.getActionCallback().ifPresent((callback) -> 
				callback.releaseUsing(stack, level, entityLiving, timeLeft, getDirectAttackDamage())));
		super.releaseUsing(stack, level, entityLiving, timeLeft);
	}
	
	@Override
	public void onUseTick(Level levelIn, LivingEntity player, ItemStack stack, int count) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		actionTrait.ifPresent((trait) -> 
			trait.getActionCallback().ifPresent((callback) -> 
				callback.onUsingTick(stack, player, count, getDirectAttackDamage())));
		super.onUseTick(levelIn, player, stack, count);
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		if(actionTrait.isPresent())
		{
			WeaponTrait trait = actionTrait.get();
			if(trait.getActionCallback().isPresent())
				return trait.getActionCallback().get().getUseDuration(stack, entity);
		}
		
		return super.getUseDuration(stack, entity);
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		if(actionTrait.isPresent())
		{
			WeaponTrait trait = actionTrait.get();
			if(trait.getActionCallback().isPresent())
				return trait.getActionCallback().get().getUseAnimation(stack);
		}
		return super.getUseAnimation(stack);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) 
	{
		Optional<WeaponTrait> actionTrait = archetype.getActionTrait();
		if(actionTrait.isPresent())
		{
			WeaponTrait trait = actionTrait.get();
			if(trait.getActionCallback().isPresent())
				return trait.getActionCallback().get().doesSneakBypassUse(stack, level, pos, player);
		}
		return super.doesSneakBypassUse(stack, level, pos, player);
	}
	
	@Override
	public void onCraftedBy(ItemStack stack, Level levelIn, Player playerIn) 
	{
		traits.forEach((trait) -> getGenericCallback(trait).ifPresent((callback) -> callback.onCreateItem(material, stack)));
		super.onCraftedBy(stack, levelIn, playerIn);
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility toolAction)
	{
		for(WeaponTrait trait : traits)
		{
			// Pass the action to another trait if false
			if(trait.canPerformToolAction(stack, toolAction))
				return true;
		}
		return archetype.canPerformToolAction(toolAction);
	}
    
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		for(WeaponTrait trait : traits)
		{
			if(trait.isEnchantmentIncompatible(enchantment))
				return false;
			else if(trait.isEnchantmentCompatible(enchantment))
				return true;
		}
		RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		ResourceLocation enchantmentKey = registryAccess.registry(Registries.ENCHANTMENT)
				.map(registry -> registry.getKey(enchantment))
				.orElse(null);
		if(enchantmentKey != null && enchantmentKey.equals(net.minecraft.world.item.enchantment.Enchantments.SWEEPING_EDGE.location()))
			return false;
		return true;
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
    public SwordBaseItem getAsItem() 
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
		return traits == null ? false : traits.stream().anyMatch((trait) -> trait.getType() == type);
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
		// Traits are immutable anyway so it should be safe to reference them directly
		return traits;
	}

	@Override
	public WeaponMaterial getMaterial()
	{
		return material;
	}
	
	private Optional<IGenericTraitCallback> getGenericCallback(WeaponTrait trait)
	{
		return trait.getMeleeCallback().isPresent() ? Optional.of(trait.getMeleeCallback().get()) : trait.getGenericCallback().isPresent() ? trait.getGenericCallback() : Optional.empty();
	}
	
	public void setAttackDamageAndSpeed(float baseDamage, float damageMultiplier, double speed)
	{
		attackDamage = (material.getAttackDamageBonus() * damageMultiplier) + baseDamage - 1.0f;
		attackSpeed = speed;
	}
}
