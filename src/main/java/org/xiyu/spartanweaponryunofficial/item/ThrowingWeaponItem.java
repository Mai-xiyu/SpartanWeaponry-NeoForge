package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.*;
import org.xiyu.spartanweaponryunofficial.api.trait.IThrowingTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.client.gui.HudCrosshairThrowingWeapon;
import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

public class ThrowingWeaponItem extends Item
        implements IWeaponTraitContainer<ThrowingWeaponItem>, IReloadable, IHudCrosshair {
    private static final int MIN_THROW_CHARGE_TICKS = 3;

    public static final String NBT_AMMO_USED = "AmmoUsed";
    public static final String NBT_UUID = "UUID";
    public static final String NBT_ORIGINAL = "Original";
    public static final String NBT_RECOVERED = "Recovered";

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

    protected List<WeaponTrait> traits = ImmutableList.of();

    public ThrowingWeaponItem(
            Item.Properties prop,
            WeaponMaterial materialIn,
            WeaponArchetype archetypeIn,
            float weaponBaseDamage,
            float weaponDamageMultiplier,
            float weaponSpeed,
            int maxAmmoCapacity,
            int chargeTicks) {
        super(prop.durability(materialIn.getUses() / 4));
        this.material = materialIn;
        this.setAttackDamage(weaponBaseDamage, weaponDamageMultiplier);
        this.setAttackSpeed(weaponSpeed);
        this.maxAmmo = maxAmmoCapacity;
        this.setChargeTicks(chargeTicks);

        if (FMLEnvironment.dist.isClient())
            ClientHelper.registerThrowingWeaponPropertyOverrides(this);

        this.archetype = archetypeIn;
        ReloadableHandler.addToItemReloadList(this);
    }

    public ThrowingWeaponItem(
            Item.Properties prop,
            WeaponMaterial material,
            WeaponArchetype archetypeIn,
            float weaponBaseDamage,
            float weaponDamageMultiplier,
            float weaponSpeed,
            int maxAmmoCapacity,
            int chargeTicks,
            String customDisplayNameIn) {
        this(
                prop,
                material,
                archetypeIn,
                weaponBaseDamage,
                weaponDamageMultiplier,
                weaponSpeed,
                maxAmmoCapacity,
                chargeTicks);
        if (material.useCustomDisplayName()) this.customDisplayName = customDisplayNameIn;
    }

    @Override
    public void reload() {
        // Update attack damage and speed from config (via archetype)
        this.setAttackDamage(this.archetype.getBaseDamage(), this.archetype.getDamageMultiplier());
        this.setAttackSpeed(this.archetype.getAttackSpeed());
        // Update charge ticks from config (via archetype)
        this.setChargeTicks(this.archetype.getChargeTicks());

        this.traits = WeaponTraitResolver.resolveTraits(this.archetype, this.material);
        this.modifiers =
                WeaponAttributeBuilder.buildMainHandAttributes(
                        this.getDirectAttackDamage(), this.attackSpeed, this.traits);

        WeaponTrait extraDamageTrait =
                this.getFirstWeaponTraitWithType(WeaponTraits.TYPE_DAMAGE_BONUS_THROWN);
        this.throwDamageMultiplier =
                extraDamageTrait != null ? extraDamageTrait.getMagnitude() : 1.0f;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return this.modifiers != null ? this.modifiers : super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public void inventoryTick(
            @NotNull ItemStack stack,
            @NotNull Level level,
            @NotNull Entity entity,
            int itemSlot,
            boolean isSelected) {
        this.normalizeStackState(stack, level, true);

        if (entity instanceof LivingEntity living) {

            if (this.traits != null) {
                this.traits.forEach(
                        (trait) ->
                                WeaponTraitResolver.getGenericCallback(trait)
                                        .ifPresent(
                                                (callback) ->
                                                        callback.onItemUpdate(
                                                                this.material,
                                                                stack,
                                                                level,
                                                                living,
                                                                itemSlot,
                                                                isSelected)));
            }
        }
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return this.archetype.isBladed() && state.is(Blocks.COBWEB) ? 15.0f : 1.0f;
    }

    @Override
    public boolean mineBlock(
            @NotNull ItemStack stack,
            Level level,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull LivingEntity entityLiving) {
        // Make the throwing weapon take damage when digging
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0f) {
            this.damageThrowingWeapon(stack, 2, entityLiving);
        }
        return false;
    }

    public void damageThrowingWeapon(ItemStack stack, int damage, LivingEntity entity) {
        // stack.damageItem(damage, entity);
        if (stack.isDamageableItem()
                && ThrowingWeaponStackState.hasAmmoRemaining(
                        stack, this.getMaxAmmo(stack, entity.level()))
                && (!(entity instanceof Player) || !((Player) entity).getAbilities().instabuild)) {
            int currentDamage = stack.getDamageValue();
            int maxDamage = stack.getMaxDamage();
            int newDamage = currentDamage + damage;
            if (newDamage >= maxDamage) {
                ThrowingWeaponStackState.incrementAmmoUsed(stack);

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
        if (this.customDisplayName == null) return super.getName(stack);
        return Component.translatable(this.customDisplayName, this.material.translateName());
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            Item.@NotNull TooltipContext tooltipContext,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flagIn) {
        Level level = tooltipContext.level();
        this.normalizeStackState(stack, level, false);
        boolean isShiftPressed = Screen.hasShiftDown();

        if (this.doCraftCheck && level != null) {
            this.canBeCrafted =
                    WeaponTooltipBuilder.checkBuiltInMaterialCraftability(
                            this.material, this.canBeCrafted);
            this.doCraftCheck = false;
        }

        if (!this.canBeCrafted)
            WeaponTooltipBuilder.addUncraftableMaterialTooltip(this.material, tooltip);

        this.archetype.addTagErrorTooltip(stack, tooltip);
        this.material.addTagErrorTooltip(stack, tooltip);

        if (ThrowingWeaponStackState.isNotOriginal(stack))
            tooltip.add(
                    Component.translatable(
                                    String.format(
                                            "tooltip.%s.throwable.not_original",
                                            ModSpartanWeaponry.ID))
                            .withStyle(ChatFormatting.DARK_RED));
        if (flagIn.isAdvanced())
            ThrowingWeaponStackState.getUuid(stack)
                    .ifPresent(
                            uuid ->
                                    tooltip.add(
                                            Component.literal("UUID: " + ChatFormatting.GRAY + uuid)
                                                    .withStyle(ChatFormatting.DARK_PURPLE)));
        int mxAmmo = this.getMaxAmmo(stack, level);
        tooltip.add(
                Component.translatable(
                                String.format("tooltip.%s.throwable.ammo", ModSpartanWeaponry.ID),
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.throwable.ammo.value",
                                                        ModSpartanWeaponry.ID),
                                                ThrowingWeaponStackState.getAmmoRemaining(
                                                        stack, mxAmmo),
                                                mxAmmo)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(
                Component.translatable(
                                String.format(
                                        "tooltip.%s.throwable.charge_time", ModSpartanWeaponry.ID),
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.throwable.charge_time.value",
                                                        ModSpartanWeaponry.ID),
                                                this.getMaxChargeTicks(stack, level) / 20.0f)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));

        if (this.traits != null && !this.traits.isEmpty()) {
            WeaponTooltipBuilder.addTraitHeader(tooltip, isShiftPressed, ChatFormatting.AQUA);
            this.archetype.addTraitsToTooltip(stack, tooltip, isShiftPressed);

            this.material.addTraitsToTooltip(
                    stack, this.archetype.getType(), tooltip, isShiftPressed);
            tooltip.add(Component.empty());
        }

        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }

    @Override
    public boolean hurtEnemy(
            @NotNull ItemStack stack, @NotNull LivingEntity target, LivingEntity attacker) {
        this.traits.forEach(
                (trait) ->
                        trait.getMeleeCallback()
                                .ifPresent(
                                        (callback) ->
                                                callback.onHitEntity(
                                                        this.material,
                                                        stack,
                                                        target,
                                                        attacker,
                                                        null)));

        // Deal double durability damage when used as a melee weapon
        if (ThrowingWeaponStackState.hasAmmoRemaining(
                stack, this.getMaxAmmo(stack, attacker.level())))
            this.damageThrowingWeapon(stack, 2, attacker);

        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level levelIn, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        this.normalizeStackState(stack, levelIn, true);
        // Check if we have ammo left
        if (ThrowingWeaponStackState.hasAmmoRemaining(stack, this.getMaxAmmo(stack, levelIn))) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void releaseUsing(
            @NotNull ItemStack stack,
            @NotNull Level levelIn,
            @NotNull LivingEntity entityLiving,
            int timeLeft) {
        if (entityLiving instanceof Player player) {

            int maxCharge = this.getMaxChargeTicks(stack, levelIn);
            int charge = Math.min(this.getUseDuration(stack, entityLiving) - timeLeft, maxCharge);

            if (!levelIn.isClientSide
                    && charge > 2
                    && ThrowingWeaponStackState.hasAmmoRemaining(
                            stack, this.getMaxAmmo(stack, levelIn))) {
                ThrowingWeaponEntity thrown =
                        this.createThrowingWeaponEntity(levelIn, player, stack, charge);
                float chargePerc = (charge / (float) maxCharge);

                if (thrown == null) return;

                thrown.setWeapon(stack);
                int velocityBonus =
                        ModEnchantments.getLevel(
                                levelIn.registryAccess(), ModEnchantments.PROPEL, stack);

                // Different charge behavior based on weapon type:
                // Throwing Knife: charge affects velocity/distance, damage stays constant
                // Javelin: charge affects damage and velocity, distance stays similar
                float velocityMultiplier;
                double damageMultiplier;

                if (this.archetype == WeaponArchetype.THROWING_KNIFE) {
                    // Throwing knife: longer charge = faster and further, but same damage
                    velocityMultiplier =
                            (chargePerc * 1.5f + 0.5f); // 0.5x to 2.0x velocity based on charge
                    damageMultiplier = 1.0; // Constant damage
                } else if (this.archetype == WeaponArchetype.JAVELIN) {
                    // Javelin: longer charge = more damage and faster, but distance stays similar
                    velocityMultiplier =
                            (chargePerc * 0.5f
                                    + 0.8f); // 0.8x to 1.3x velocity (less range variation)
                    damageMultiplier =
                            (this.throwDamageMultiplier - 1.0f) * chargePerc
                                    + 1.0f; // Full damage scaling
                } else {
                    // Other throwing weapons (tomahawk, boomerang): original behavior
                    velocityMultiplier = (chargePerc * 0.9f + 0.1f);
                    damageMultiplier = (this.throwDamageMultiplier - 1.0f) * chargePerc + 1.0f;
                }

                thrown.shootFromRotation(
                        player,
                        player.xRotO,
                        player.yRotO,
                        0.0F,
                        this.throwVelocity * ((velocityBonus * 0.2f) + 1) * velocityMultiplier,
                        0.5f);

                this.traits.forEach(
                        (trait) ->
                                trait.getThrowingCallback()
                                        .ifPresent(
                                                (callback) ->
                                                        callback.onThrowingProjectileSpawn(
                                                                this.material, thrown)));

                thrown.setBaseDamage((this.getDirectAttackDamage() + 1.0d) * damageMultiplier);

                // Apply enchantments as necessary
                int j =
                        ModEnchantments.getLevel(
                                levelIn.registryAccess(), ModEnchantments.RAZORS_EDGE, stack);
                if (j > 0) {
                    thrown.setBaseDamage(thrown.getBaseDamage() + j * 0.5D + 0.5D);
                }
                if (ModEnchantments.getLevel(
                                levelIn.registryAccess(), ModEnchantments.INCENDIARY, stack)
                        > 0) {
                    thrown.igniteForSeconds(100.0F);
                }

                if (player.getAbilities().instabuild)
                    thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                else if (thrown.isValidThrowingWeapon()) {
                    thrown.pickup = AbstractArrow.Pickup.ALLOWED;
                    // Use ammo system - increment ammo used counter
                    ThrowingWeaponStackState.incrementAmmoUsed(stack);
                }

                stack.setDamageValue(0);
                levelIn.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        this.getThrowingSound(),
                        SoundSource.PLAYERS,
                        0.5F,
                        0.4F / (levelIn.random.nextFloat() * 0.4F + 0.8F));
                levelIn.addFreshEntity(thrown);

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void onCraftedBy(
            @NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Player playerIn) {
        this.traits.forEach(
                (trait) ->
                        WeaponTraitResolver.getGenericCallback(trait)
                                .ifPresent(
                                        (callback) -> callback.onCreateItem(this.material, stack)));

        this.initNBT(stack, true);
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility toolAction) {
        for (WeaponTrait trait : this.traits) {
            // Pass the action to another trait if false
            if (trait.canPerformToolAction(stack, toolAction)) return true;
        }
        return this.archetype.canPerformToolAction(toolAction);
    }

    public ItemStack makeTabStack() {
        ItemStack stack = new ItemStack(this);

        this.initNBT(stack, false);
        return stack;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return this.material.getEnchantmentValue();
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.LOYALTY)
                || super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public <T extends LivingEntity> int damageItem(
            @NotNull ItemStack stack, int amount, T entity, @NotNull Consumer<Item> onBroken) {
        return WeaponTraitResolver.applyDamageCallbacks(this.traits, stack, entity, amount);
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
            if (trait.getType().equals(type)) return trait;
        }
        return null;
    }

    @Override
    public List<WeaponTrait> getAllWeaponTraitsWithType(String type) {
        if (this.traits.isEmpty()) return ImmutableList.of();

        return this.traits.stream().filter((trait) -> trait.getType().equals(type)).toList();
    }

    @Override
    public Collection<WeaponTrait> getAllWeaponTraits() {
        // Traits are immutable after reloading anyway so it should be safe to reference them
        // directly
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
        this.attackDamage =
                (this.material.getAttackDamageBonus() * damageMultiplier) + baseDamage - 1.0f;
    }

    public void setAttackSpeed(double speed) {
        this.attackSpeed = speed;
    }

    public void setChargeTicks(int chargeTicks) {
        this.maxChargeTicks = chargeTicks;
    }

    public void updateFromConfig(
            float baseDamage, float damageMultiplier, double speed, int chargeTicks) {
        this.setAttackDamage(baseDamage, damageMultiplier);
        this.setAttackSpeed(speed);
        this.setChargeTicks(chargeTicks);
    }

    /**
     * Creates a new Throwing Weapon Entity that is used as a projectile.
     *
     * @param levelIn The World instance
     * @param player The Player throwing the weapon
     * @param stack The Throwing Weapon Item
     * @param charge The total time (in ticks) that the weapon is held for before throwing it
     */
    public ThrowingWeaponEntity createThrowingWeaponEntity(
            Level levelIn, Player player, ItemStack stack, int charge) {
        return new ThrowingWeaponEntity(ModEntities.THROWING_WEAPON.get(), player, levelIn, stack);
    }

    protected SoundEvent getThrowingSound() {
        return ModSounds.THROWN_WEAPON_THROW.get();
    }

    protected void initNBT(ItemStack stack, boolean initUUID) {
        ThrowingWeaponStackState.init(stack, initUUID);
    }

    public void normalizeStackState(ItemStack stack, Level level, boolean ensureUuid) {
        ThrowingWeaponStackState.normalize(stack, this.getMaxAmmo(stack, level), ensureUuid);
    }

    public int getMaxAmmo(ItemStack stack, RegistryAccess access) {
        if (access == null) return this.maxAmmo;
        int level = ModEnchantments.getLevel(access, ModEnchantments.EXPANSE, stack);
        // Find the value to increase by per level (if ammo increase is too small e.g. Boomerang;
        // then use ammo + 1 per level instead)
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
        if (access == null) return Math.max(MIN_THROW_CHARGE_TICKS, this.maxChargeTicks);
        int chargeTicks =
                (int)
                        (this.maxChargeTicks
                                * (1
                                        - ModEnchantments.getLevel(
                                                        access, ModEnchantments.SUPERCHARGE, stack)
                                                * 0.2f));
        if (this.traits != null)
            for (WeaponTrait trait : this.traits) {
                Optional<IThrowingTraitCallback> opt = trait.getThrowingCallback();
                if (opt.isPresent())
                    chargeTicks = opt.get().modifyThrowingChargeTime(this.material, chargeTicks);
            }
        return Math.max(MIN_THROW_CHARGE_TICKS, chargeTicks);
    }

    public int getMaxChargeTicks(ItemStack stack, Level level) {
        return level != null
                ? this.getMaxChargeTicks(stack, level.registryAccess())
                : this.maxChargeTicks;
    }

    @Override
    public ICrosshairOverlay getCrosshairHudElement() {
        return HudCrosshairThrowingWeapon::render;
    }
}
