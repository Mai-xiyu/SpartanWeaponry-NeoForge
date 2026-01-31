package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.IGenericTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.IRangedTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.EventHooks;

public class LongbowItem extends BowItem implements IReloadable
{
	protected WeaponMaterial material;
	protected float drawTime = 1.25f;
	protected float maxVelocity = 1.25f;
	protected String modId = null;
	protected String customDisplayName = null;
	protected boolean doCraftCheck = true;
	protected boolean canBeCrafted = true;
	
	protected final WeaponType type = WeaponType.RANGED;
	protected List<WeaponTrait> rangedTraits;
	protected ItemAttributeModifiers modifiers;
	
	public LongbowItem(Item.Properties prop, WeaponMaterial material)
	{
		super(prop.durability((int)(material.getUses() * 2.0f)));
		this.material = material;
		maxVelocity = Defaults.MultiplierLongbow;
		
		if(FMLEnvironment.dist.isClient())
			ClientHelper.registerLongbowPropertyOverrides(this);
		
		ReloadableHandler.addToItemReloadList(this);
	}
	
	public LongbowItem(Item.Properties prop, WeaponMaterial material, String customDisplayName)
	{
		this(prop, material);
		if(material.useCustomDisplayName())
			this.customDisplayName = customDisplayName;
	}
	
	@Override
	public void reload()
	{
		drawTime = 1.25f;
		rangedTraits = material.getBonusTraits(type);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
		for(WeaponTrait trait : rangedTraits)
		{
			Optional<IRangedTraitCallback> opt = trait.getRangedCallback();
			if(opt.isPresent())
			{
				drawTime = opt.get().modifyLongbowDrawTime(material, drawTime);
			}
			Optional<IGenericTraitCallback> generic = trait.getGenericCallback();
			generic.ifPresent((callback) -> callback.onModifyAttributes(mapBuilder));
		}
		var builtModifiers = mapBuilder.build();
		ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
		builtModifiers.forEach((attribute, modifier) ->
			attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
		modifiers = attributeBuilder.build();
	}
	
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		return modifiers != null ? modifiers : super.getDefaultAttributeModifiers(stack);
	}
	
	/**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
	@Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof Player)
        {
        	Player player = (Player)entityLiving;
	        	RegistryAccess registryAccess = level.registryAccess();
	            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.INFINITY), stack) > 0;
            ItemStack itemstack = player.getProjectile(stack);

			int i = this.getUseDuration(stack, entityLiving) - timeLeft;
			i = EventHooks.onArrowLoose(stack, level, (Player)entityLiving, i, itemstack != null || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                    itemstack = new ItemStack(Items.ARROW);

                float f = getArrowSpeed(i);

                if (f >= 0.1D)
                {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem ? ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player) : false);

                    if (!level.isClientSide)
                    {
                    	ArrowItem itemarrow = ((ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW));
						AbstractArrow entityarrow = itemarrow.createArrow(level, itemstack, player, stack);
                        entityarrow.shootFromRotation(player, player.xRotO, player.yRotO, 0.0f, f * 3.0f, 0.5f);
                        
                    	for(WeaponTrait trait : rangedTraits)
                    		trait.getRangedCallback().ifPresent((callback) -> callback.onProjectileSpawn(material, entityarrow));
                        
                        if (f >= maxVelocity)
                            entityarrow.setCritArrow(true);

	                        int j = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.POWER), stack);
                        if (j > 0)
                            entityarrow.setBaseDamage(entityarrow.getBaseDamage() + j * 0.5d + 0.5d);

	                        int k = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PUNCH), stack);
	                        if (k > 0)
	                            entityarrow.setDeltaMovement(entityarrow.getDeltaMovement().add(0.0D, 0.1D * k, 0.0D));

	                        if (EnchantmentHelper.getItemEnchantmentLevel(registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FLAME), stack) > 0)
	                            entityarrow.igniteForSeconds(100.0F);

	                        EquipmentSlot breakSlot = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
	                        stack.hurtAndBreak(1, player, breakSlot);

                        if (flag1 || player.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() ==  Items.TIPPED_ARROW))
                            entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                        level.addFreshEntity(entityarrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.0F / (level.random.nextFloat() * 0.4f + 1.2f) + f * 0.5f);

                    if (!flag1)
                    {
                        itemstack.shrink(1);
                        if(itemstack.isEmpty())
                        	player.getInventory().removeItem(itemstack);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

	
	/**
     * Gets the velocity of the arrow entity from the longbow's charge
     */
    public float getArrowSpeed(int charge)
    {
        float f = charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > maxVelocity)
        {
            f = maxVelocity;
        }

        return f;
    }
	
	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return material.getEnchantmentValue();
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) 
	{
		return material.getRepairIngredient().test(repair);
	}
    
    @Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) 
    {
    	int damage = amount;
    	for(WeaponTrait trait : rangedTraits)
    	{
    		if(trait.getGenericCallback().isPresent())
    			damage = trait.getGenericCallback().get().onDamageItem(stack, entity, damage);
    		if(damage <= 0)
    			break;
    	}
    	return Math.max(0, damage);
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

    	material.addTagErrorTooltip(stack, tooltip);
		
    	if(material.hasAnyBonusTraits(type))
    	{
			if(rangedTraits != null && !rangedTraits.isEmpty())
			{
				if(isShiftPressed)
					tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
				else
					tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.DARK_AQUA.toString() + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
				tooltip.add(Component.translatable(String.format("tooltip.%s.trait.material_bonus", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.AQUA));
				
				rangedTraits.forEach((trait) -> trait.addTooltip(stack, tooltip, isShiftPressed));
	        	tooltip.add(Component.empty());
			}
    	}
		
    	if(isShiftPressed)
    	{
			tooltip.add(Component.translatable(String.format("tooltip.%s.description", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
    		tooltip.add(Component.translatable(String.format("tooltip.%s.longbow.desc", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    		tooltip.add(Component.translatable(String.format("tooltip.%s.longbow.desc_2", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    	}
    	else
			tooltip.add(Component.translatable(String.format("tooltip.%s.description", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA.toString() + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
   	
    	
    	tooltip.add(Component.empty());
    	tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.ammo.type", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.ammo.arrow", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    	tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.longbow.draw_length", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.longbow.draw_length.value", ModSpartanWeaponry.ID), drawTime).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    	tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.longbow.speed_multiplier", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.longbow.speed_multiplier.value", ModSpartanWeaponry.ID), maxVelocity).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
    	tooltip.add(Component.empty());
    }
	
	// ---- ---- ---- ---- ---- ---- ---- ----
	// Internal methods
	// ---- ---- ---- ---- ---- ---- ---- ----
    public float getNockProgress(ItemStack stack, LivingEntity shooter)
    {
    	return (float)(shooter.getTicksUsingItem() / (20.0F * drawTime));
    }
}
