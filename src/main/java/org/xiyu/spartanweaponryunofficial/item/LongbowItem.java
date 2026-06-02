package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.IRangedTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

public class LongbowItem extends BowItem implements IReloadable {
    protected WeaponMaterial material;
    protected float drawTime = 1.25f;
    protected float maxVelocity;
    protected String modId = null;
    protected String customDisplayName = null;
    protected boolean doCraftCheck = true;
    protected boolean canBeCrafted = true;

    protected final WeaponType type = WeaponType.RANGED;
    protected List<WeaponTrait> rangedTraits = ImmutableList.of();
    protected ItemAttributeModifiers modifiers;

    public LongbowItem(Item.Properties prop, WeaponMaterial material) {
        super(prop.durability((int) (material.getUses() * 2.0f)));
        this.material = material;
        this.maxVelocity = Defaults.MultiplierLongbow;

        if (FMLEnvironment.dist.isClient()) ClientHelper.registerLongbowPropertyOverrides(this);

        ReloadableHandler.addToItemReloadList(this);
    }

    public LongbowItem(Item.Properties prop, WeaponMaterial material, String customDisplayName) {
        this(prop, material);
        if (material.useCustomDisplayName()) this.customDisplayName = customDisplayName;
    }

    @Override
    public void reload() {
        this.drawTime = 1.25f;
        this.rangedTraits = WeaponTraitResolver.resolveMaterialTraits(this.material, this.type);

        for (WeaponTrait trait : this.rangedTraits) {
            Optional<IRangedTraitCallback> opt = trait.getRangedCallback();
            opt.ifPresent(
                    iRangedTraitCallback ->
                            this.drawTime =
                                    iRangedTraitCallback.modifyLongbowDrawTime(
                                            this.material, this.drawTime));
        }
        this.modifiers = WeaponAttributeBuilder.buildGenericTraitItemAttributes(this.rangedTraits);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return this.modifiers != null ? this.modifiers : super.getDefaultAttributeModifiers(stack);
    }

    /** Called when the player stops using an Item (stops holding the right mouse button). */
    @Override
    public void releaseUsing(
            @NotNull ItemStack stack,
            @NotNull Level level,
            @NotNull LivingEntity entityLiving,
            int timeLeft) {
        if (entityLiving instanceof Player player) {
            RegistryAccess registryAccess = level.registryAccess();
            boolean flag =
                    player.getAbilities().instabuild
                            || EnchantmentHelper.getItemEnchantmentLevel(
                                            registryAccess
                                                    .registryOrThrow(Registries.ENCHANTMENT)
                                                    .getHolderOrThrow(Enchantments.INFINITY),
                                            stack)
                                    > 0;
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) itemstack = new ItemStack(Items.ARROW);

                float f = this.getArrowSpeed(i);

                if (f >= 0.1D) {
                    boolean flag1 =
                            player.getAbilities().instabuild
                                    || (itemstack.getItem() instanceof ArrowItem
                                            && ((ArrowItem) itemstack.getItem())
                                                    .isInfinite(itemstack, stack, player));

                    if (!level.isClientSide) {
                        ArrowItem itemarrow =
                                ((ArrowItem)
                                        (itemstack.getItem() instanceof ArrowItem
                                                ? itemstack.getItem()
                                                : Items.ARROW));
                        AbstractArrow entityarrow =
                                itemarrow.createArrow(level, itemstack, player, stack);
                        entityarrow.shootFromRotation(
                                player, player.xRotO, player.yRotO, 0.0f, f * 3.0f, 0.5f);

                        for (WeaponTrait trait : this.rangedTraits)
                            trait.getRangedCallback()
                                    .ifPresent(
                                            (callback) ->
                                                    callback.onProjectileSpawn(
                                                            this.material, entityarrow));

                        if (f >= this.maxVelocity) entityarrow.setCritArrow(true);

                        int j =
                                EnchantmentHelper.getItemEnchantmentLevel(
                                        registryAccess
                                                .registryOrThrow(Registries.ENCHANTMENT)
                                                .getHolderOrThrow(Enchantments.POWER),
                                        stack);
                        if (j > 0)
                            entityarrow.setBaseDamage(
                                    entityarrow.getBaseDamage() + j * 0.5d + 0.5d);

                        int k =
                                EnchantmentHelper.getItemEnchantmentLevel(
                                        registryAccess
                                                .registryOrThrow(Registries.ENCHANTMENT)
                                                .getHolderOrThrow(Enchantments.PUNCH),
                                        stack);
                        if (k > 0)
                            entityarrow.setDeltaMovement(
                                    entityarrow.getDeltaMovement().add(0.0D, 0.1D * k, 0.0D));

                        if (EnchantmentHelper.getItemEnchantmentLevel(
                                        registryAccess
                                                .registryOrThrow(Registries.ENCHANTMENT)
                                                .getHolderOrThrow(Enchantments.FLAME),
                                        stack)
                                > 0) entityarrow.igniteForSeconds(100.0F);

                        EquipmentSlot breakSlot =
                                player.getUsedItemHand() == InteractionHand.MAIN_HAND
                                        ? EquipmentSlot.MAINHAND
                                        : EquipmentSlot.OFFHAND;
                        stack.hurtAndBreak(1, player, breakSlot);

                        if (flag1
                                || player.getAbilities().instabuild
                                        && (itemstack.getItem() == Items.SPECTRAL_ARROW
                                                || itemstack.getItem() == Items.TIPPED_ARROW))
                            entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                        level.addFreshEntity(entityarrow);
                    }

                    level.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.NEUTRAL,
                            1.0F,
                            1.0F / (level.random.nextFloat() * 0.4f + 1.2f) + f * 0.5f);

                    if (!flag1) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) player.getInventory().removeItem(itemstack);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    /** Gets the velocity of the arrow entity from the longbow's charge */
    public float getArrowSpeed(int charge) {
        float f = charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > this.maxVelocity) {
            f = this.maxVelocity;
        }

        return f;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return this.material.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return this.material.getRepairIngredient().test(repair);
    }

    @Override
    public <T extends LivingEntity> int damageItem(
            @NotNull ItemStack stack, int amount, T entity, @NotNull Consumer<Item> onBroken) {
        return WeaponTraitResolver.applyDamageCallbacks(this.rangedTraits, stack, entity, amount);
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
        boolean isShiftPressed = Screen.hasShiftDown();

        if (this.doCraftCheck && tooltipContext.level() != null) {
            this.canBeCrafted =
                    WeaponTooltipBuilder.checkBuiltInMaterialCraftability(
                            this.material, this.canBeCrafted);
            this.doCraftCheck = false;
        }

        if (!this.canBeCrafted)
            WeaponTooltipBuilder.addUncraftableMaterialTooltip(this.material, tooltip);

        this.material.addTagErrorTooltip(stack, tooltip);

        if (this.material.hasAnyBonusTraits(this.type)) {
            if (this.rangedTraits != null && !this.rangedTraits.isEmpty()) {
                WeaponTooltipBuilder.addTraitHeader(
                        tooltip, isShiftPressed, ChatFormatting.DARK_AQUA);
                tooltip.add(
                        Component.translatable(
                                        String.format(
                                                "tooltip.%s.trait.material_bonus",
                                                ModSpartanWeaponry.ID))
                                .withStyle(ChatFormatting.AQUA));

                this.rangedTraits.forEach(
                        (trait) -> trait.addTooltip(stack, tooltip, isShiftPressed));
                tooltip.add(Component.empty());
            }
        }

        if (isShiftPressed) {
            tooltip.add(
                    Component.translatable(
                                    String.format("tooltip.%s.description", ModSpartanWeaponry.ID),
                                    Component.translatable(
                                                    "tooltip."
                                                            + ModSpartanWeaponry.ID
                                                            + ".showing_details")
                                            .withStyle(ChatFormatting.DARK_GRAY))
                            .withStyle(ChatFormatting.GOLD));
            tooltip.add(
                    Component.translatable(
                                    String.format("tooltip.%s.longbow.desc", ModSpartanWeaponry.ID))
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            tooltip.add(
                    Component.translatable(
                                    String.format(
                                            "tooltip.%s.longbow.desc_2", ModSpartanWeaponry.ID))
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else
            tooltip.add(
                    Component.translatable(
                                    String.format("tooltip.%s.description", ModSpartanWeaponry.ID),
                                    Component.translatable(
                                                    "tooltip."
                                                            + ModSpartanWeaponry.ID
                                                            + ".show_details",
                                                    ChatFormatting.AQUA + "SHIFT")
                                            .withStyle(ChatFormatting.DARK_GRAY))
                            .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());
        tooltip.add(
                Component.translatable(
                                String.format(
                                        "tooltip.%s.modifiers.ammo.type", ModSpartanWeaponry.ID),
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.modifiers.ammo.arrow",
                                                        ModSpartanWeaponry.ID))
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(
                Component.translatable(
                                String.format(
                                        "tooltip.%s.modifiers.longbow.draw_length",
                                        ModSpartanWeaponry.ID),
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.modifiers.longbow.draw_length.value",
                                                        ModSpartanWeaponry.ID),
                                                this.drawTime)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(
                Component.translatable(
                                String.format(
                                        "tooltip.%s.modifiers.longbow.speed_multiplier",
                                        ModSpartanWeaponry.ID),
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.modifiers.longbow.speed_multiplier.value",
                                                        ModSpartanWeaponry.ID),
                                                this.maxVelocity)
                                        .withStyle(ChatFormatting.GRAY))
                        .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.empty());
    }

    // ---- ---- ---- ---- ---- ---- ---- ----
    // Internal methods
    // ---- ---- ---- ---- ---- ---- ---- ----
    public float getNockProgress(ItemStack stack, LivingEntity shooter) {
        return shooter.getTicksUsingItem() / (20.0F * this.drawTime);
    }
}
