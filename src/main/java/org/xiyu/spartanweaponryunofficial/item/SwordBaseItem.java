package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.*;
import org.xiyu.spartanweaponryunofficial.api.trait.IGenericTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.VersatileWeaponTrait;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SwordBaseItem extends SwordItem implements IWeaponTraitContainer<SwordBaseItem>, IReloadable {
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

    public SwordBaseItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, double weaponSpeed) {
        super(materialIn, prop.durability(materialIn.getUses()));
        this.material = materialIn;
        this.archetype = archetypeIn;
        this.setAttackDamageAndSpeed(weaponBaseDamage, weaponDamageMultiplier, weaponSpeed);

        ReloadableHandler.addToItemReloadList(this);

        if (FMLEnvironment.dist.isClient())
            ClientHelper.registerMeleeWeaponPropertyOverrides(this);
    }

    public SwordBaseItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, double weaponSpeed, String customDisplayNameIn) {
        this(prop, materialIn, archetypeIn, weaponBaseDamage, weaponDamageMultiplier, weaponSpeed);
        if (materialIn.useCustomDisplayName())
            this.customDisplayName = customDisplayNameIn;
    }

    @Override
    public void reload() {
        this.setAttackDamageAndSpeed(this.archetype.getBaseDamage(), this.archetype.getDamageMultiplier(), this.archetype.getAttackSpeed());

        ImmutableList.Builder<WeaponTrait> builder = ImmutableList.builder();

//		Log.info("'" + ForgeRegistries.ITEMS.getKey(this).toString() +  "' -> Material: " + (material != null ? material : "NULL!"));
        builder.addAll(this.archetype.getTraits());
        builder.addAll(this.material.getBonusTraits(this.archetype.getType()));
        this.traits = builder.build();

        // Initialize the weapon's attribute modifier map
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.getDirectAttackDamage(), AttributeModifier.Operation.ADD_VALUE));
        mapBuilder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, this.attackSpeed - 4.0D, AttributeModifier.Operation.ADD_VALUE));

        // Add attributes from Weapon Traits
//		if(traits != null)
        this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onModifyAttributes(mapBuilder)));

        var builtModifiers = mapBuilder.build();
        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
        builtModifiers.forEach((attribute, modifier) ->
                attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
        this.modifiers = attributeBuilder.build();
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
    public ItemAttributeModifiers getDefaultAttributeModifiers() { // [修改] 删除参数 stack
        // [修改] 删除 super 调用中的参数
        return this.modifiers != null ? this.modifiers : super.getDefaultAttributeModifiers();
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        // Check for two-handed traits, and other such effects
        if (entity instanceof LivingEntity living) {

            if (this.traits != null)
                this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onItemUpdate(this.material, stack, level, living, itemSlot, isSelected)));
        }
    }

    /**
     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
     */
    public float getDamage() {
        return this.material.getAttackDamageBonus();
    }

    // Removed @Override - signature changed in Forge 1.21.1
    public int getMaxDamage(@NotNull ItemStack stack) {
        return this.material.getUses();
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        for (WeaponTrait trait : this.getAllWeaponTraitsWithType(WeaponTraits.TYPE_VERSATILE)) {
            VersatileWeaponTrait versatileTrait = (VersatileWeaponTrait) trait;
            if (state.is(versatileTrait.getEffectiveBlocks()))
                return this.material.getSpeed();
        }
        if (this.archetype.isBladed() && state.is(Blocks.COBWEB))
            return 15.0f;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canDisableShield(@NotNull ItemStack stack, @NotNull ItemStack shield, @NotNull LivingEntity entity, @NotNull LivingEntity attacker) {
        return this.hasWeaponTrait(WeaponTraits.SHIELD_BREACH.get());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (this.customDisplayName == null)
            return super.getName(stack);
        return Component.translatable(this.customDisplayName, this.material.translateName());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        boolean isShiftPressed = Screen.hasShiftDown();

        if (this.doCraftCheck && Minecraft.getInstance().level != null) {
            if (!ClientConfig.INSTANCE.forceDisableUncraftableTooltips.get() && this.material.getModId().equals(ModSpartanWeaponry.ID)) {
                var tag = BuiltInRegistries.ITEM.getTag(this.material.getRepairTag());
                if (tag.isEmpty() || tag.get().size() == 0)
                    this.canBeCrafted = false;
            }
            this.doCraftCheck = false;
        }

        if (!this.canBeCrafted)
            tooltip.add(Component.translatable(String.format("tooltip.%s.uncraftable_missing_material", ModSpartanWeaponry.ID), this.material.getRepairTagName()).withStyle(ChatFormatting.RED));

        this.archetype.addTagErrorTooltip(stack, tooltip);
        this.material.addTagErrorTooltip(stack, tooltip);

        if (this.traits != null && !this.traits.isEmpty()) {
            if (isShiftPressed)
                tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
            else
                tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));

            this.archetype.addTraitsToTooltip(stack, tooltip, isShiftPressed);
//			tooltip.add(Component.empty());
        }
        this.material.addTraitsToTooltip(stack, this.archetype.getType(), tooltip, isShiftPressed);

        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }

    public float getDirectAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        this.traits.forEach((trait) ->
                trait.getMeleeCallback().ifPresent((callback) -> callback.onHitEntity(this.material, stack, target, attacker, null)));

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext contextIn) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        if (actionTrait.isPresent()) {
            WeaponTrait trait = actionTrait.get();
            if (trait.getActionCallback().isPresent())
                return trait.getActionCallback().get().useOn(contextIn);
        }
        return super.useOn(contextIn);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level levelIn, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        if (actionTrait.isPresent()) {
            WeaponTrait trait = actionTrait.get();
            if (trait.getActionCallback().isPresent())
                return trait.getActionCallback().get().use(stack, levelIn, player, hand);
        }
        return super.use(levelIn, player, hand);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        actionTrait.flatMap(WeaponTrait::getActionCallback).ifPresent((callback) ->
                callback.releaseUsing(stack, level, entityLiving, timeLeft, this.getDirectAttackDamage()));
        super.releaseUsing(stack, level, entityLiving, timeLeft);
    }

    @Override
    public void onUseTick(@NotNull Level levelIn, @NotNull LivingEntity player, @NotNull ItemStack stack, int count) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        actionTrait.flatMap(WeaponTrait::getActionCallback).ifPresent((callback) ->
                callback.onUsingTick(stack, player, count, this.getDirectAttackDamage()));
        super.onUseTick(levelIn, player, stack, count);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        if (actionTrait.isPresent()) {
            WeaponTrait trait = actionTrait.get();
            if (trait.getActionCallback().isPresent())
                return trait.getActionCallback().get().getUseDuration(stack, entity);
        }

        return super.getUseDuration(stack, entity);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        if (actionTrait.isPresent()) {
            WeaponTrait trait = actionTrait.get();
            if (trait.getActionCallback().isPresent())
                return trait.getActionCallback().get().getUseAnimation(stack);
        }
        return super.getUseAnimation(stack);
    }

    @Override
    public boolean doesSneakBypassUse(@NotNull ItemStack stack, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        Optional<WeaponTrait> actionTrait = this.archetype.getActionTrait();
        if (actionTrait.isPresent()) {
            WeaponTrait trait = actionTrait.get();
            if (trait.getActionCallback().isPresent())
                return trait.getActionCallback().get().doesSneakBypassUse(stack, level, pos, player);
        }
        return super.doesSneakBypassUse(stack, level, pos, player);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Player playerIn) {
        this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onCreateItem(this.material, stack)));
        super.onCraftedBy(stack, levelIn, playerIn);
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ToolAction toolAction) {
        for (WeaponTrait trait : this.traits) {
            // Pass the action to another trait if false
            if (trait.canPerformToolAction(stack, toolAction))
                return true;
        }
        return this.archetype.canPerformToolAction(toolAction);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        for (WeaponTrait trait : this.traits) {
            if (trait.isEnchantmentIncompatible(enchantment))
                return false;
            else if (trait.isEnchantmentCompatible(enchantment))
                return true;
        }
        RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        ResourceLocation enchantmentKey = registryAccess.registry(Registries.ENCHANTMENT)
                .map(registry -> registry.getKey(enchantment))
                .orElse(null);
        return enchantmentKey == null || !enchantmentKey.equals(net.minecraft.world.item.enchantment.Enchantments.SWEEPING_EDGE.location());
    }

    // Removed @Override - signature changed in Forge 1.21.1
    public <T extends LivingEntity> int damageItem(@NotNull ItemStack stack, int amount, T entity, @NotNull Consumer<Item> onBroken) {
        int damage = amount;
        for (WeaponTrait trait : this.traits) {
            if (trait.getGenericCallback().isPresent())
                damage = trait.getGenericCallback().get().onDamageItem(stack, entity, damage);
            if (damage <= 0)
                break;
        }
        return Math.max(0, damage);
    }

    // IWeaponTraitContainer

    @Override
    public SwordBaseItem getAsItem() {
        return this;
    }

    @Override
    public boolean hasWeaponTrait(WeaponTrait prop) {
        return this.traits.contains(prop);
    }

    @Override
    public boolean hasWeaponTraitWithType(String type) {
        return this.traits != null && this.traits.stream().anyMatch((trait) -> trait.getType().equals(type));
    }

    @Override
    public WeaponTrait getFirstWeaponTraitWithType(String type) {
        for (WeaponTrait trait : this.traits) {
            if (trait.getType().equals(type))
                return trait;
        }
        return null;
    }

    @Override
    public List<WeaponTrait> getAllWeaponTraitsWithType(String type) {
        if (this.traits.isEmpty())
            return ImmutableList.of();

        return this.traits.stream().filter((trait) -> trait.getType().equals(type)).toList();
    }

    @Override
    public Collection<WeaponTrait> getAllWeaponTraits() {
        // Traits are immutable anyway so it should be safe to reference them directly
        return this.traits;
    }

    @Override
    public WeaponMaterial getMaterial() {
        return this.material;
    }

    private Optional<IGenericTraitCallback> getGenericCallback(WeaponTrait trait) {
        return trait.getMeleeCallback().isPresent() ? Optional.of(trait.getMeleeCallback().get()) : trait.getGenericCallback().isPresent() ? trait.getGenericCallback() : Optional.empty();
    }

    public void setAttackDamageAndSpeed(float baseDamage, float damageMultiplier, double speed) {
        this.attackDamage = (this.material.getAttackDamageBonus() * damageMultiplier) + baseDamage - 1.0f;
        this.attackSpeed = speed;
    }
}
