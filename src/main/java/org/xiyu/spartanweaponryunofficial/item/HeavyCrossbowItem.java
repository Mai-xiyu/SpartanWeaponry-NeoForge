package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.RegistryOps;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.InputHelper;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.api.trait.IGenericTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.IRangedTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.gui.HudCrosshairHeavyCrossbow;
import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HeavyCrossbowItem extends CrossbowItem implements IReloadable, IHudLoadState, IHudCrosshair {
    protected WeaponMaterial material;
    protected int loadTicks;
    protected int aimTicks;
    protected String customDisplayName = null;

    protected final WeaponType type = WeaponType.RANGED;
    protected List<WeaponTrait> rangedTraits;
    protected ItemAttributeModifiers modifiers;

    public static final String NBT_CHARGED = "Charged";
    public static final String NBT_PROJECTILE = "Projectile";
    public static final Predicate<ItemStack> BOLT = (stack) -> stack.is(ModItemTags.BOLTS);

    public HeavyCrossbowItem(Item.Properties prop, WeaponMaterial material) {
        super(prop.durability((int) (material.getUses() * 1.5f)));
        this.material = material;
        this.loadTicks = Defaults.CrossbowTicksToLoad;
        this.aimTicks = Defaults.CrossbowInaccuracyTicks;
        ReloadableHandler.addToItemReloadList(this);
    }

    public HeavyCrossbowItem(Item.Properties prop, WeaponMaterial material, String customDisplayName) {
        this(prop, material);
        if (material.useCustomDisplayName()) {
            this.customDisplayName = customDisplayName;
        }
    }

    // ---- ---- ---- ---- ---- ---- ---- ----
    // Overriding methods
    // ---- ---- ---- ---- ---- ---- ---- ----
    @Override
    public void reload() {
        this.loadTicks = Defaults.CrossbowTicksToLoad;
        this.aimTicks = Defaults.CrossbowInaccuracyTicks;
        this.rangedTraits = this.material.getBonusTraits(this.type);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        for (WeaponTrait trait : this.rangedTraits) {
            Optional<IRangedTraitCallback> opt = trait.getRangedCallback();
            if (opt.isPresent()) {
                IRangedTraitCallback callback = opt.get();
                this.loadTicks = callback.modifyHeavyCrossbowLoadTime(this.material, this.loadTicks);
                this.aimTicks = callback.modifyHeavyCrossbowAimTime(this.material, this.aimTicks);
            }
            Optional<IGenericTraitCallback> generic = trait.getGenericCallback();
            generic.ifPresent((callback) -> callback.onModifyAttributes(mapBuilder));
        }
        var builtModifiers = mapBuilder.build();
        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
        builtModifiers.forEach((attribute, modifier) ->
                attributeBuilder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute), modifier, EquipmentSlotGroup.MAINHAND));
        this.modifiers = attributeBuilder.build();
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return this.modifiers != null ? this.modifiers : super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return BOLT;
    }

    @Override
    public @NotNull Predicate<ItemStack> getSupportedHeldProjectiles() {
        return BOLT;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            RegistryAccess registryAccess = levelIn.registryAccess();

            if (this.getLoadProgress(stack, entityLiving) == 1.0f) {
                // Load the Crossbow
                ItemStackDataHelper.updateTag(stack, tag -> tag.putBoolean(NBT_CHARGED, true));
                ItemStack bolt;
                int count = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT), stack) > 0 ? 3 : 1;

                bolt = entityLiving.getProjectile(stack);
                if (bolt.isEmpty() || !BOLT.test(bolt))
                    bolt = new ItemStack(ModItems.BOLT.get(), count);

                // Create a copy of the bolt, then save it to NBT via Codec.
                ItemStack boltToStore = bolt.copy();
                boltToStore.setCount(count);
                DynamicOps<Tag> ops = registryAccess.createSerializationContext(NbtOps.INSTANCE);
                Tag encodedBolt = ItemStack.CODEC.encodeStart(ops, boltToStore).getOrThrow();
                ItemStackDataHelper.updateTag(stack, tag -> tag.put(NBT_PROJECTILE, encodedBolt));

                if (!player.getAbilities().instabuild) {
                    bolt.shrink(1);
                    if (bolt.isEmpty())
                        player.getInventory().removeItem(bolt);
                }
                levelIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.0F / (levelIn.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
            } else {
                // Fire the Crossbow
                boolean isCharged = ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_CHARGED, false);
                if (!isCharged) {
                    return false;
                }

                CompoundTag projectileTag = ItemStackDataHelper.getTag(stack).getCompoundOrEmpty(NBT_PROJECTILE);
                ItemStack itemstack = ItemStack.EMPTY;

                if (!projectileTag.isEmpty()) {
                    DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
                    itemstack = ItemStack.OPTIONAL_CODEC.parse(ops, projectileTag).result().orElse(ItemStack.EMPTY);
                }

                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(ModItems.BOLT.get());
                }

                int i = this.getUseDuration(stack, player) - timeLeft;
                if (i < 0) return false;

                if (!itemstack.isEmpty()) {
                    if (!levelIn.isClientSide()) {
                        BoltItem itemBolt = ((BoltItem) (itemstack.getItem() instanceof BoltItem ? itemstack.getItem() : ModItems.BOLT.get()));

                        boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof BoltItem && ((BoltItem) itemstack.getItem()).isInfinite(itemstack, stack, player));

                        // Account for lack of accuracy.
                        int stackAimTicks = this.getAimTicks(stack, levelIn);
                        int inaccuracy = Mth.clamp(stackAimTicks - i, 0, stackAimTicks);
                        float inaccuracyModifier = 0.0f;

                        if (inaccuracy != 0)
                            inaccuracyModifier = 12.0f * ((float) inaccuracy / stackAimTicks);

                        // Fire projectiles.
                        this.spawnProjectile(stack, itemBolt, itemstack, levelIn, player, flag1, inaccuracyModifier, 0.0f);
                        if (itemstack.getCount() > 1) {
                            this.spawnProjectile(stack, itemBolt, itemstack, levelIn, player, flag1, inaccuracyModifier, -10.0f);
                            this.spawnProjectile(stack, itemBolt, itemstack, levelIn, player, flag1, inaccuracyModifier, 10.0f);
                        }
                        int damage = itemstack.getCount() > 1 ? 3 : 1;
                        EquipmentSlot breakSlot = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                        stack.hurtAndBreak(damage, player, breakSlot);

                        ItemStackDataHelper.updateTag(stack, stackTag -> {
                            stackTag.putBoolean(NBT_CHARGED, false);
                            stackTag.put(NBT_PROJECTILE, new CompoundTag());
                        });
                    }

                    levelIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.0F / (levelIn.getRandom().nextFloat() * 0.4F + 1.2F) + 1.5f * 0.5F);

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
        return true;
    }

    /**
     * Gets the velocity of the bolt entity from the crossbow's charge
     */
    public static float getBoltVelocity(BoltEntity bolt) {
        return 4.5f * bolt.getRangeMultiplier();
    }

    protected void spawnProjectile(ItemStack crossbow, BoltItem boltItem, ItemStack boltStack, Level levelIn, Player player, boolean creativeOrInfinite, float inaccuracyModifier, float projectileAngle) {
        BoltEntity bolt = boltItem.createBolt(levelIn, boltStack, player, crossbow);

        if (bolt == null) return;

        RegistryAccess registryAccess = levelIn.registryAccess();
        bolt.setCritArrow(true);
        bolt.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        int pierceLvl = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PIERCING), crossbow);

        Vec3 upVector = player.getUpVector(1.0f);
        Quaternionf quat = new Quaternionf().setAngleAxis((projectileAngle * (Mth.PI / 180.0f)), upVector.x, upVector.y, upVector.z);
        Vector3f velocityVec = player.getViewVector(1.0f).toVector3f().rotate(quat);
        bolt.shoot(velocityVec.x, velocityVec.y, velocityVec.z, getBoltVelocity(bolt), inaccuracyModifier);

        for (WeaponTrait trait : this.rangedTraits)
            trait.getRangedCallback().ifPresent((callback) -> callback.onProjectileSpawn(this.material, bolt));

        int j = EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.POWER), crossbow);

        if (j > 0) {
            bolt.setBaseDamage(2.0 + j * 0.5D + 0.5D);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FLAME), crossbow) > 0) {
            bolt.igniteForSeconds(5.0F);
        }

        if (creativeOrInfinite || projectileAngle != 0.0f) {
            bolt.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        levelIn.addFreshEntity(bolt);
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        if (!ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_CHARGED, false))
            return ItemUseAnimation.CROSSBOW;
        return ItemUseAnimation.BOW;
    }

    @Override
    public @NotNull InteractionResult use(Level levelIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        ItemStack ammoStack = playerIn.getProjectile(stack);
        boolean hasAmmo = !ammoStack.isEmpty();

        RegistryAccess registryAccess = levelIn.registryAccess();
        if (!playerIn.getAbilities().instabuild && !hasAmmo && !ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_CHARGED, false) && EnchantmentHelper.getItemEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), stack) == 0) {
            return InteractionResult.FAIL;
        }
        playerIn.startUsingItem(handIn);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level levelIn, @NotNull LivingEntity livingEntityIn, @NotNull ItemStack stack, int count) {
        if (!levelIn.isClientSide() && !ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_CHARGED, false) && livingEntityIn instanceof Player) {
            float loadProgress = this.getLoadProgress(stack, livingEntityIn);
            SoundEvent loadingSound = null;
            if (loadProgress == 0.0f)
                loadingSound = SoundEvents.CROSSBOW_LOADING_START.value();
            else if (loadProgress == 0.5f || loadProgress == 0.9f)
                loadingSound = SoundEvents.CROSSBOW_LOADING_MIDDLE.value();
            if (loadingSound != null)
                levelIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), loadingSound, SoundSource.PLAYERS, 0.5f, 1.0f);
        }
    }

    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return this.material != null ? this.material.getEnchantmentValue() : 1;
    }

    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return this.material.getRepairIngredient().test(repair);
    }

    @Override
    public <T extends LivingEntity> int damageItem(@NotNull ItemStack stack, int amount, T entity, @NotNull Consumer<Item> onBroken) {
        int damage = amount;
        for (WeaponTrait trait : this.rangedTraits) {
            if (trait.getGenericCallback().isPresent())
                damage = trait.getGenericCallback().get().onDamageItem(stack, entity, damage);
            if (damage <= 0)
                break;
        }
        return Math.max(0, damage);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (this.customDisplayName == null) {
            return super.getName(stack);
        }
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

        Level levelIn = tooltipContext.level();

        if (ItemStackDataHelper.getTag(stack).contains(NBT_CHARGED)) {
            RegistryAccess registryAccess = levelIn != null ? levelIn.registryAccess() : null;
            if (registryAccess != null) {
                CompoundTag projectileTag = ItemStackDataHelper.getTag(stack).getCompoundOrEmpty(NBT_PROJECTILE);
                if (!projectileTag.isEmpty()) {
                    DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
                    ItemStack bolt = ItemStack.OPTIONAL_CODEC.parse(ops, projectileTag).result().orElse(ItemStack.EMPTY);
                    if (!bolt.isEmpty()) {
                        tooltip.add(Component.translatable(String.format("tooltip.%s.heavy_crossbow.loaded_bolt", ModSpartanWeaponry.ID), String.format("[%s x%d]", ChatFormatting.AQUA + bolt.getHoverName().getString() + ChatFormatting.WHITE, bolt.getCount())));
                        tooltip.add(Component.empty());
                    }
                }
            }
        }

        if (this.material.hasAnyBonusTraits(this.type)) {
            if (this.rangedTraits != null && !this.rangedTraits.isEmpty()) {
                if (isShiftPressed)
                    tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
                else
                    tooltip.add(Component.translatable(String.format("tooltip.%s.traits", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.DARK_AQUA + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
                tooltip.add(Component.translatable(String.format("tooltip.%s.trait.material_bonus", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.AQUA));

                this.rangedTraits.forEach((trait) -> trait.addTooltip(stack, tooltip, isShiftPressed));
                tooltip.add(Component.empty());
            }
        }

        if (isShiftPressed) {
            tooltip.add(Component.translatable(String.format("tooltip.%s.description", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".showing_details").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable(String.format("tooltip.%s.heavy_crossbow.desc", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            tooltip.add(Component.translatable(String.format("tooltip.%s.heavy_crossbow.desc_2", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            tooltip.add(Component.translatable(String.format("tooltip.%s.heavy_crossbow.desc_3", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else
            tooltip.add(Component.translatable(String.format("tooltip.%s.description", ModSpartanWeaponry.ID), Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".show_details", ChatFormatting.AQUA + "SHIFT").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.ammo.type", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.ammo.bolt", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.heavy_crossbow.load_time", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.heavy_crossbow.load_time.value", ModSpartanWeaponry.ID), (float) this.getFullLoadTicks(stack, levelIn) / 20.0f).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable(String.format("tooltip.%s.modifiers.heavy_crossbow.aim_time", ModSpartanWeaponry.ID), Component.translatable(String.format("tooltip.%s.modifiers.heavy_crossbow.aim_time.value", ModSpartanWeaponry.ID), (float) this.getAimTicks(stack, levelIn) / 20.0f).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.empty());
    }

    @Override
    public @NotNull Component getHighlightTip(@NotNull ItemStack item, @NotNull Component displayName) {
        if (ItemStackDataHelper.hasTag(item)) {
            CompoundTag projectileTag = ItemStackDataHelper.getTag(item).getCompoundOrEmpty(NBT_PROJECTILE);
            if (!projectileTag.isEmpty()) {
                // Try to parse bolt name without registry access - just show loaded state
                return Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".highlight_heavy_crossbow_loaded", displayName);
            }
        }
        return super.getHighlightTip(item, displayName);
    }

    // ---- ---- ---- ---- ---- ---- ---- ----
    // IHudLoadState
    // ---- ---- ---- ---- ---- ---- ---- ----
    @Override
    public boolean isLoaded(ItemStack stack) {
        return ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_CHARGED, false);
    }

    @Override
    public float getLoadProgress(ItemStack stack, LivingEntity living) {
        Level level = living != null ? living.level() : null;
        return !this.isLoaded(stack) ? Mth.clamp((float) this.getLoadingTicks(stack, living) / (float) this.getFullLoadTicks(stack, level), 0.0f, 1.0f) : 0.0f;
    }

    // ---- ---- ---- ---- ---- ---- ---- ----
    // Internal methods
    // ---- ---- ---- ---- ---- ---- ---- ----

    public int getFullLoadTicks(ItemStack stack) {
        return this.loadTicks;
    }

    public int getFullLoadTicks(ItemStack stack, RegistryAccess registryAccess) {
        if (registryAccess == null)
            return this.loadTicks;
        var enchantmentRegistry = registryAccess.lookup(Registries.ENCHANTMENT);
        if (enchantmentRegistry.isEmpty())
            return this.loadTicks;
        int i = EnchantmentHelper.getItemEnchantmentLevel(enchantmentRegistry.get().getOrThrow(Enchantments.QUICK_CHARGE), stack);
        return Mth.clamp(this.loadTicks - 5 * i, 0, this.loadTicks);
    }

    public int getFullLoadTicks(ItemStack stack, Level level) {
        return level != null ? this.getFullLoadTicks(stack, level.registryAccess()) : this.loadTicks;
    }

    public int getLoadingTicks(ItemStack stack, LivingEntity living) {
        return living.getTicksUsingItem();
    }

    public int getAimTicks(ItemStack stack, RegistryAccess access) {
        if (access == null)
            return this.aimTicks;
        int i = ModEnchantments.getLevel(access, ModEnchantments.SHARPSHOOTER, stack);
        return Mth.clamp(this.aimTicks - 2 * i, 0, this.aimTicks);
    }

    public int getAimTicks(ItemStack stack, Level level) {
        return level != null ? this.getAimTicks(stack, level.registryAccess()) : this.aimTicks;
    }

    @Override
    public ICrosshairOverlay getCrosshairHudElement() {
        return HudCrosshairHeavyCrossbow::render;
    }
}
