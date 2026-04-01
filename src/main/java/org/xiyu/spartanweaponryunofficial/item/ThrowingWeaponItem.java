package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.fml.loading.FMLEnvironment;
import org.xiyu.spartanweaponryunofficial.client.InputHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.*;
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
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class ThrowingWeaponItem extends Item implements IWeaponTraitContainer<ThrowingWeaponItem>, IReloadable, IHudCrosshair {
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

    public ThrowingWeaponItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, float weaponSpeed, int maxAmmoCapacity, int chargeTicks) {
        super(prop.durability(materialIn.getUses() / 4));
        this.material = materialIn;
        this.setAttackDamage(weaponBaseDamage, weaponDamageMultiplier);
        this.setAttackSpeed(weaponSpeed);
        this.maxAmmo = maxAmmoCapacity;
        this.setChargeTicks(chargeTicks);

        // TODO: ItemProperties removed in 26.1
        // ClientHelper.registerThrowingWeaponPropertyOverrides(this);

        this.archetype = archetypeIn;
        ReloadableHandler.addToItemReloadList(this);
    }

    public ThrowingWeaponItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, float weaponBaseDamage, float weaponDamageMultiplier, float weaponSpeed, int maxAmmoCapacity, int chargeTicks, String customDisplayNameIn) {
        this(prop, material, archetypeIn, weaponBaseDamage, weaponDamageMultiplier, weaponSpeed, maxAmmoCapacity, chargeTicks);
        if (material.useCustomDisplayName())
            this.customDisplayName = customDisplayNameIn;
    }

    @Override
    public void reload() {
        // Update attack damage and speed from config (via archetype)
        this.setAttackDamage(this.archetype.getBaseDamage(), this.archetype.getDamageMultiplier());
        this.setAttackSpeed(this.archetype.getAttackSpeed());
        // Update charge ticks from config (via archetype)
        this.setChargeTicks(this.archetype.getChargeTicks());

        ImmutableList.Builder<WeaponTrait> builder = ImmutableList.builder();
        builder.addAll(this.archetype.getTraits());
        builder.addAll(this.material.getBonusTraits(this.archetype.getType()));
        this.traits = builder.build();

        // Initialize the weapon's attribute modifier map
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.getDirectAttackDamage(), AttributeModifier.Operation.ADD_VALUE));
        mapBuilder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, this.attackSpeed - 4.0d, AttributeModifier.Operation.ADD_VALUE));

        // Add melee attributes from Weapon Traits
//		if(traits != null)
        this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onModifyAttributes(mapBuilder)));

        var builtModifiers = mapBuilder.build();
        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
        builtModifiers.forEach((attribute, modifier) ->
                attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
        this.modifiers = attributeBuilder.build();

        WeaponTrait extraDamageTrait = this.getFirstWeaponTraitWithType(WeaponTraits.TYPE_DAMAGE_BONUS_THROWN);
        this.throwDamageMultiplier = extraDamageTrait != null ? extraDamageTrait.getMagnitude() : 1.0f;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return this.modifiers != null ? this.modifiers : super.getDefaultAttributeModifiers(stack);
    }

    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        this.initNBT(stack, true);

        if (entity instanceof LivingEntity living) {

            if (this.traits != null) {
                this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onItemUpdate(this.material, stack, level, living, itemSlot, isSelected)));
            }
        }
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return this.archetype.isBladed() && state.is(Blocks.COBWEB) ? 15.0f : 1.0f;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state, @NotNull BlockPos pos,
                             @NotNull LivingEntity entityLiving) {
        // Make the throwing weapon take damage when digging
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0f) {
            this.damageThrowingWeapon(stack, 2, entityLiving);
        }
        return false;
    }

    public void damageThrowingWeapon(ItemStack stack, int damage, LivingEntity entity) {
        //stack.damageItem(damage, entity);
        if (stack.isDamageableItem() && ItemStackDataHelper.getTag(stack).getIntOr(NBT_AMMO_USED, 0) < this.getMaxAmmo(stack, entity.level()) &&
                (!(entity instanceof Player) || !((Player) entity).getAbilities().instabuild)) {
            int currentDamage = stack.getDamageValue();
            int maxDamage = stack.getMaxDamage();
            int newDamage = currentDamage + damage;
            if (newDamage >= maxDamage) {
                int ammo = ItemStackDataHelper.getTag(stack).getIntOr(NBT_AMMO_USED, 0);
                int updatedAmmo = ammo + 1;
                ItemStackDataHelper.updateTag(stack, tag -> tag.putInt(NBT_AMMO_USED, updatedAmmo));

                if (entity instanceof Player) {
                    ((Player) entity).awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
                }

                stack.setDamageValue(0);
            } else {
                stack.setDamageValue(newDamage);
            }
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (this.customDisplayName == null)
            return super.getName(stack);
        return Component.translatable(this.customDisplayName, this.material.translateName());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay,
                                @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        this.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        tooltip.forEach(tooltipAdder);
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        boolean isShiftPressed = FMLEnvironment.getDist().isClient() && InputHelper.isShiftDown();

        if (this.doCraftCheck) {
            this.canBeCrafted = BuiltInRegistries.ITEM.getTagOrEmpty(this.material.getRepairTag()).iterator().hasNext();
            this.doCraftCheck = false;
        }

        if (!this.canBeCrafted)
            tooltip.add(Component.translatable(String.format("tooltip.%s.uncraftable_missing_material", ModSpartanWeaponry.ID), this.material.getRepairTagName()).withStyle(ChatFormatting.RED));

        this.archetype.addTagErrorTooltip(stack, tooltip);
        this.material.addTagErrorTooltip(stack, tooltip);

        var stackTag = ItemStackDataHelper.getTag(stack);
        if (stackTag.contains(NBT_ORIGINAL) && !stackTag.getBooleanOr(NBT_ORIGINAL, true))
            tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.not_original", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.DARK_RED));
        Optional<UUID> stackUuid = getWeaponUuid(stackTag);
        if (stackUuid.isPresent() && flagIn.isAdvanced())
            tooltip.add(Component.literal("UUID: " + ChatFormatting.GRAY + stackUuid.get()).withStyle(ChatFormatting.DARK_PURPLE));
        int mxAmmo = this.getMaxAmmo(stack, tooltipContext.level());
        tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.ammo", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.throwable.ammo.value", ModSpartanWeaponry.ID), mxAmmo - stackTag.getIntOr(NBT_AMMO_USED, 0), mxAmmo).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable(String.format("tooltip.%s.throwable.charge_time", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.throwable.charge_time.value", ModSpartanWeaponry.ID), this.getMaxChargeTicks(stack, tooltipContext.level()) / 20.0f).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));

        if (this.traits != null && !this.traits.isEmpty()) {
            if (isShiftPressed)
                tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
            else
                tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));

            this.archetype.addTraitsToTooltip(stack, tooltip, isShiftPressed);

            this.material.addTraitsToTooltip(stack, this.archetype.getType(), tooltip, isShiftPressed);
            tooltip.add(Component.empty());
        }


    }

    public void hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, LivingEntity attacker) {
        this.traits.forEach((trait) -> trait.getMeleeCallback().ifPresent((callback) -> callback.onHitEntity(this.material, stack, target, attacker, null)));

        // Deal double durability damage when used as a melee weapon
        if (ItemStackDataHelper.getTag(stack).getIntOr(NBT_AMMO_USED, 0) < this.getMaxAmmo(stack, attacker.level()))
            this.damageThrowingWeapon(stack, 2, attacker);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level levelIn, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Check if we have ammo left
        if (ItemStackDataHelper.getTag(stack).getIntOr(NBT_AMMO_USED, 0) < this.getMaxAmmo(stack, levelIn)) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {

            int maxCharge = this.getMaxChargeTicks(stack, levelIn);
            int charge = Math.min(this.getUseDuration(stack, entityLiving) - timeLeft, maxCharge);

            if (!levelIn.isClientSide() && charge > 2 && ItemStackDataHelper.getTag(stack).getIntOr(NBT_AMMO_USED, 0) < this.getMaxAmmo(stack, levelIn)) {
                ThrowingWeaponEntity thrown = this.createThrowingWeaponEntity(levelIn, player, stack, charge);
                float chargePerc = (charge / (float) maxCharge);

                if (thrown == null) return false;

                thrown.setWeapon(stack);
                int velocityBonus = ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.PROPEL, stack);

                // Different charge behavior based on weapon type:
                // Throwing Knife: charge affects velocity/distance, damage stays constant
                // Javelin: charge affects damage and velocity, distance stays similar
                float velocityMultiplier;
                double damageMultiplier;

                if (this.archetype == WeaponArchetype.THROWING_KNIFE) {
                    // Throwing knife: longer charge = faster and further, but same damage
                    velocityMultiplier = (chargePerc * 1.5f + 0.5f);  // 0.5x to 2.0x velocity based on charge
                    damageMultiplier = 1.0;  // Constant damage
                } else if (this.archetype == WeaponArchetype.JAVELIN) {
                    // Javelin: longer charge = more damage and faster, but distance stays similar
                    velocityMultiplier = (chargePerc * 0.5f + 0.8f);  // 0.8x to 1.3x velocity (less range variation)
                    damageMultiplier = (this.throwDamageMultiplier - 1.0f) * chargePerc + 1.0f;  // Full damage scaling
                } else {
                    // Other throwing weapons (tomahawk, boomerang): original behavior
                    velocityMultiplier = (chargePerc * 0.9f + 0.1f);
                    damageMultiplier = (this.throwDamageMultiplier - 1.0f) * chargePerc + 1.0f;
                }

                thrown.shootFromRotation(player, player.xRotO, player.yRotO, 0.0F, this.throwVelocity * ((velocityBonus * 0.2f) + 1) * velocityMultiplier, 0.5f);

                this.traits.forEach((trait) -> trait.getThrowingCallback().ifPresent((callback) -> callback.onThrowingProjectileSpawn(this.material, thrown)));

                thrown.setBaseDamage((this.getDirectAttackDamage() + 1.0d) * damageMultiplier);

                // Apply enchantments as necessary
                int j = ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.RAZORS_EDGE, stack);
                if (j > 0) {
                    thrown.setBaseDamage(thrown.getBaseDamage() + j * 0.5D + 0.5D);
                }
                if (ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.INCENDIARY, stack) > 0) {
                    thrown.igniteForSeconds(100.0F);
                }
		            /*int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
		            if (k > 0)
		            {
		            	thrown.setKnockbackStrength(k);
		            }*/

                if (player.getAbilities().instabuild)
                    thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                else if (thrown.isValidThrowingWeapon()) {
                    // Use ammo system - increment ammo used counter
                    ItemStackDataHelper.updateTag(stack, tag -> tag.putInt(NBT_AMMO_USED, tag.getIntOr(NBT_AMMO_USED, 0) + 1));
                }

                stack.setDamageValue(0);
                levelIn.playSound(null, player.getX(), player.getY(), player.getZ(), this.getThrowingSound(), SoundSource.PLAYERS, 0.5F, 0.4F / (levelIn.getRandom().nextFloat() * 0.4F + 0.8F));
                levelIn.addFreshEntity(thrown);

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
        return true;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Player playerIn) {
        this.onCraftedBy(stack, playerIn.level(), playerIn);
        super.onCraftedBy(stack, playerIn);
    }

    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Player playerIn) {
        this.traits.forEach((trait) -> this.getGenericCallback(trait).ifPresent((callback) -> callback.onCreateItem(this.material, stack)));
        this.initNBT(stack, true);
    }

    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility toolAction) {
        for (WeaponTrait trait : this.traits) {
            // Pass the action to another trait if false
            if (trait.canPerformToolAction(stack, toolAction))
                return true;
        }
        return this.archetype.canPerformToolAction(toolAction);
    }

    public ItemStack makeTabStack() {
        ItemStack stack = new ItemStack(this);

        this.initNBT(stack, false);
        return stack;
    }

    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return this.material.getEnchantmentValue();
    }

    // In Minecraft 1.21+, enchantment compatibility is primarily handled via item tags
    // This method is kept for backwards compatibility but returns true by default
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // Allow all enchantments - let the tag system handle compatibility
        return true;
    }

    @Override
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
    public ThrowingWeaponItem getAsItem() {
        return this;
    }

    @Override
    public boolean hasWeaponTrait(WeaponTrait prop) {
        return this.traits.contains(prop);
    }

    @Override
    public boolean hasWeaponTraitWithType(String type) {
        return this.traits.stream().anyMatch((trait) -> trait.getType().equals(type));
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
        // Traits are immutable after reloading anyway so it should be safe to reference them directly
        return this.traits;
    }

    @Override
    public WeaponMaterial getMaterial() {
        return this.material;
    }

    // New methods
    public float getDirectAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(float baseDamage, float damageMultiplier) {
        this.attackDamage = (this.material.getAttackDamageBonus() * damageMultiplier) + baseDamage - 1.0f;
    }

    public void setAttackSpeed(double speed) {
        this.attackSpeed = speed;
    }

    public void setChargeTicks(int chargeTicks) {
        this.maxChargeTicks = chargeTicks;
    }

    private Optional<IGenericTraitCallback> getGenericCallback(WeaponTrait trait) {
        return trait.getMeleeCallback().isPresent() ? Optional.of(trait.getMeleeCallback().get()) : trait.getGenericCallback().isPresent() ? trait.getGenericCallback() : Optional.empty();
    }

    public void updateFromConfig(float baseDamage, float damageMultiplier, double speed, int chargeTicks) {
        this.setAttackDamage(baseDamage, damageMultiplier);
        this.setAttackSpeed(speed);
        this.setChargeTicks(chargeTicks);
    }

    /**
     * Creates a new Throwing Weapon Entity that is used as a projectile.
     *
     * @param levelIn The World instance
     * @param player  The Player throwing the weapon
     * @param stack   The Throwing Weapon Item
     * @param charge  The total time (in ticks) that the weapon is held for before throwing it
     */
    public ThrowingWeaponEntity createThrowingWeaponEntity(Level levelIn, Player player, ItemStack stack, int charge) {
        return new ThrowingWeaponEntity(ModEntities.THROWING_WEAPON.get(), player, levelIn, stack);
    }

    protected SoundEvent getThrowingSound() {
        return ModSounds.THROWN_WEAPON_THROW.get();
    }

    protected void initNBT(ItemStack stack, boolean initUUID) {
        ItemStackDataHelper.updateTag(stack, tag -> {
            if (!tag.contains(NBT_AMMO_USED)) {
                // And, because I don't think it would be a good idea to transfer the ammo value from the old version to the new one
                // Just fill 'er up
                tag.putInt(NBT_AMMO_USED, 0);
            }
            // Initialise UUID tag if necessary, and flag as original stack
            if (initUUID && !getWeaponUuid(tag).isPresent()) {
                tag.putString(NBT_UUID, UUID.randomUUID().toString());
                tag.putBoolean(NBT_ORIGINAL, true);
            }
        });
    }

    public static Optional<UUID> getWeaponUuid(CompoundTag tag) {
        if (!tag.contains(NBT_UUID)) {
            return Optional.empty();
        }

        String uuidRaw = tag.getStringOr(NBT_UUID, "");
        if (uuidRaw.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(uuidRaw));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public int getMaxAmmo(ItemStack stack, RegistryAccess access) {
        if (access == null)
            return this.maxAmmo;
        int level = ModEnchantments.getLevel(access, ModEnchantments.EXPANSE, stack);
        // Find the value to increase by per level (if ammo increase is too small e.g. Boomerang; then use ammo + 1 per level instead)
        int increasePerLevel = Math.max((int) (this.maxAmmo * 0.25f), 1);
        return this.maxAmmo + (increasePerLevel * level);
    }

    public int getMaxAmmo(ItemStack stack, Level level) {
        return level != null ? this.getMaxAmmo(stack, level.registryAccess()) : this.maxAmmo;
    }

    public int getMaxAmmoBase() {
        return this.maxAmmo;
    }

    public int getMaxChargeTicks(ItemStack stack, RegistryAccess access) {
        if (access == null)
            return this.maxChargeTicks;
        int chargeTicks = (int) (this.maxChargeTicks * (1 - ModEnchantments.getLevel(access, ModEnchantments.SUPERCHARGE, stack) * 0.2f));
        if (this.traits != null)
            for (WeaponTrait trait : this.traits) {
                Optional<IThrowingTraitCallback> opt = trait.getThrowingCallback();
                if (opt.isPresent())
                    chargeTicks = opt.get().modifyThrowingChargeTime(this.material, chargeTicks);
            }
        return chargeTicks;
    }

    public int getMaxChargeTicks(ItemStack stack, Level level) {
        return level != null ? this.getMaxChargeTicks(stack, level.registryAccess()) : this.maxChargeTicks;
    }

    @Override
    public ICrosshairOverlay getCrosshairHudElement() {
        return HudCrosshairThrowingWeapon::render;
    }
}


