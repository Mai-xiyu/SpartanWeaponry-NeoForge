package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ItemAbility;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

/**
 * Base Weapon Trait class. Extend this class or other classes to implement your own Weapon Trait
 * for any weapon.
 *
 * @author ObliviousSpartan
 */
public class WeaponTrait {
    /**
     * Trait Quality determines what colour that a Weapon Trait shows up in the tooltip for any
     * weapon
     *
     * @author ObliviousSpartan
     */
    public enum TraitQuality {
        POSITIVE(ChatFormatting.GREEN),
        NEUTRAL(ChatFormatting.YELLOW),
        NEGATIVE(ChatFormatting.RED);

        private final ChatFormatting formatting;

        TraitQuality(ChatFormatting formattingIn) {
            this.formatting = formattingIn;
        }

        public ChatFormatting getFormatting() {
            return this.formatting;
        }
    }

    /**
     * Reasons that traits are invalid<br>
     * Used to show why the traits are invalid in weapon tooltips
     *
     * @author ObliviousSpartan
     */
    public enum InvalidReason {
        NONE("none"),
        MULTIPLE_ACTION_TRAITS("multiple_action_traits"),
        MATERIAL_ACTION_TRAIT("material_action_trait"),
        WEAPON_NOT_MELEE("weapon_not_melee"),
        WEAPON_NOT_RANGED("weapon_not_ranged"),
        WEAPON_NOT_THROWING("weapon_not_throwing"),
        WEAPON_NOT_SUPPORTED("weapon_not_supported");

        private final String langKey;

        InvalidReason(String langKeySuffixIn) {
            this.langKey =
                    String.format(
                            "tooltip.%s.trait.invalid.%s",
                            SpartanWeaponryAPI.MOD_ID, langKeySuffixIn);
        }

        public String getLanguageKey() {
            return this.langKey;
        }
    }

    public static final ChatFormatting[] DESCRIPTION_FORMAT = {
        ChatFormatting.GRAY, ChatFormatting.ITALIC
    }; // Default Tooltip description formatting
    public static final ChatFormatting[] INVALID_FORMAT = {
        ChatFormatting.RED, ChatFormatting.ITALIC
    }; // Default Invalid Tooltip description formatting

    protected String type;
    protected String modId;
    protected int level = 0;
    protected float magnitude = 0.0f;
    protected TraitQuality quality;
    protected boolean isMelee = false, isRanged = false, isThrowing = false;

    @Deprecated(since = "3.1.1", forRemoval = true)
    protected boolean isAction = false;

    protected MutableComponent types;

    public WeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        this.type = typeIn;
        this.modId = modIdIn;
        this.quality = qualityIn;
    }

    @Override
    public String toString() {
        return String.format(
                "WeaponTrait{Type: %s:%s - Level: %d - Magnitude: %f - Quality: %s}",
                this.modId, this.type, this.level, this.magnitude, this.quality.toString());
    }

    /**
     * Retrieves the type of Weapon Trait as a string literal so multiple trait variants can be
     * grouped together for searching purposes
     */
    public String getType() {
        return this.type;
    }

    /** Gets the level for this Weapon Trait */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the level for this Weapon Trait. Used for initialising the Weapon Trait
     *
     * @return The updated Weapon Trait
     */
    public WeaponTrait setLevel(int value) {
        this.level = value;
        return this;
    }

    /**
     * Retrieves the magnitude of the Weapon Traits to use for implementing mechanics for each Trait
     */
    public float getMagnitude() {
        return this.magnitude;
    }

    /**
     * Sets the magnitude for this Weapon Trait. Used for editing config values or initialising the
     * Weapon Trait
     *
     * @return The updated Weapon Trait
     */
    public WeaponTrait setMagnitude(float value) {
        this.magnitude = value;
        return this;
    }

    /** Mark this Weapon Trait as Melee. Used for Melee and Throwing Weapons (for melee attacks) */
    public WeaponTrait setMelee() {
        this.isMelee = true;
        return this;
    }

    /** Mark this Weapon Trait as Ranged. Used for the Longbow and Heavy Crossbow */
    public WeaponTrait setRanged() {
        this.isRanged = true;
        return this;
    }

    /** Mark this Weapon Trait as Throwing. Used specifically for Throwing Weapons */
    public WeaponTrait setThrowing() {
        this.isThrowing = true;
        return this;
    }

    /**
     * Marks this Weapon Trait as an action trait. Only one of these traits can be used in any tag
     * and cannot be used in Material Trait Tags<br>
     * Used for Weapon Traits that are performed as actions (e.g. Throwable & Melee Block)
     */
    @Deprecated(since = "3.1.1", forRemoval = true)
    public WeaponTrait setActionTrait() {
        this.isAction = true;
        return this;
    }

    /**
     * Marks this Weapon trait as Melee, Ranged, and Throwing. Used for generic traits that adjust
     * attack, loading, firing speed, for example
     */
    public WeaponTrait setUniversal() {
        this.isMelee = true;
        this.isRanged = true;
        this.isThrowing = true;
        return this;
    }

    /**
     * DEPRECATED: Use {@link #setUniversal()} instead!<br>
     * Marks this Weapon trait as Melee, Ranged, Throwing, and optionally an action trait. Used for
     * generic traits that adjust attack, loading, firing speed, for example
     */
    @Deprecated(since = "3.1.1", forRemoval = true)
    public WeaponTrait setUniversal(boolean isActionIn) {
        this.isMelee = true;
        this.isRanged = true;
        this.isThrowing = true;
        this.isAction = isActionIn;
        return this;
    }

    /**
     * Retrieves the Weapon Trait's generic callback, if it exists. Use this method instead of using
     * the "instanceof" check
     *
     * @return The callback, wrapped in an {@link Optional} if it exists; an empty {@link Optional}
     *     otherwise.
     */
    public Optional<IGenericTraitCallback> getGenericCallback() {
        return Optional.empty();
    }

    /**
     * Retrieves the Weapon Trait's Melee callback, if it exists. Use this method instead of using
     * the "instanceof" check
     *
     * @return The callback, wrapped in an {@link Optional} if it exists; an empty {@link Optional}
     *     otherwise.
     */
    public Optional<IMeleeTraitCallback> getMeleeCallback() {
        return Optional.empty();
    }

    /**
     * Retrieves the Weapon Trait's Ranged (Longbows/Heavy Crossbows) callback. Use this method
     * instead of using the "instanceof" check
     *
     * @return The callback, wrapped in an {@link Optional} if it exists; an empty {@link Optional}
     *     otherwise.
     */
    public Optional<IRangedTraitCallback> getRangedCallback() {
        return Optional.empty();
    }

    /**
     * Retrieves the Weapon Trait's Throwing weapons callback. Use this method instead of using the
     * "instanceof" check
     *
     * @return The callback, wrapped in an {@link Optional} if it exists; an empty {@link Optional}
     *     otherwise.
     */
    public Optional<IThrowingTraitCallback> getThrowingCallback() {
        return Optional.empty();
    }

    /**
     * Retrieves the Weapon Trait's Action callback. Use this method instead of using the
     * "instanceof" check
     *
     * @return The callback, wrapped in an {@link Optional} if it exists; an empty {@link Optional}
     *     otherwise.
     */
    public Optional<IActionTraitCallback> getActionCallback() {
        return Optional.empty();
    }

    /** If true, this will show up on the Trait tooltip for any Melee weapons. */
    public final boolean isMeleeTrait() {
        return this.isMelee || this.getMeleeCallback().isPresent();
    }

    /** If true, this will show up on the Trait tooltip for any Ranged weapons */
    public final boolean isRangedTrait() {
        return this.isRanged || this.getRangedCallback().isPresent();
    }

    /** If true, this will show up on the Trait tooltip for any Throwing weapons */
    public final boolean isThrowingTrait() {
        return this.isThrowing || this.getThrowingCallback().isPresent();
    }

    /**
     * If true, this trait is defined as an action trait, which is restricted to only one per weapon
     * archetype to prevent conflicts between traits
     */
    public final boolean isActionTrait() {
        return this.getActionCallback().isPresent();
    }

    /**
     * Queries if the Enchantment is compatible with the weapon containing this trait
     *
     * @param enchantIn The enchantment to check
     * @return true if the enchantment is compatible with the weapon with this trait, false
     *     otherwise
     */
    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        return false;
    }

    /**
     * Queries if the Enchantment is incompatible with the weapon containing this trait
     *
     * @param enchantIn The enchantment to check
     * @return true if the enchantment is incompatible with the weapon with this trait, false
     *     otherwise
     */
    public boolean isEnchantmentIncompatible(Enchantment enchantIn) {
        return false;
    }

    /**
     * Queries if the ToolAction can be performed using the weapon containing this trait
     *
     * @param stack The weapon
     * @param action The tool action
     * @return true if the tool action can be performed, false otherwise
     */
    public boolean canPerformToolAction(ItemStack stack, ItemAbility action) {
        return false;
    }

    /**
     * The main tooltip method used to display the Weapon Trait on a applicable weapon. Don't
     * attempt to override this (I don't think you can anyway). Use {@link
     * WeaponTrait#addTooltipTitle} or {@link WeaponTrait#addTooltipDescription} to change those
     * specific parts instead.
     */
    public final void addTooltip(
            ItemStack stack,
            List<Component> tooltip,
            boolean isShiftPressed,
            InvalidReason invalidReason) {
        if (invalidReason == InvalidReason.NONE)
            this.addTooltipTitle(stack, tooltip, this.quality.getFormatting());
        else
            this.addTooltipTitle(
                    stack,
                    tooltip,
                    ChatFormatting.DARK_RED,
                    ChatFormatting.BOLD,
                    ChatFormatting.STRIKETHROUGH);

        if (isShiftPressed) {
            if (this.types == null) this.initTooltipTypes();
            else tooltip.add(this.types);
            if (I18n.exists(String.format("tooltip.%s.trait.%s.desc", this.modId, this.type))) {
                if (invalidReason == InvalidReason.NONE) this.addTooltipDescription(stack, tooltip);
                else
                    tooltip.add(
                            tooltipIndent()
                                    .append(
                                            Component.translatable(
                                                            String.format(
                                                                    invalidReason.getLanguageKey()))
                                                    .withStyle(INVALID_FORMAT)));
            }
        }
    }

    /**
     * The main tooltip method used to display the Weapon Trait on a applicable weapon. Don't
     * attempt to override this (I don't think you can anyway). Use {@link
     * WeaponTrait#addTooltipTitle} or {@link WeaponTrait#addTooltipDescription} to change those
     * specific parts instead.
     */
    public final void addTooltip(ItemStack stack, List<Component> tooltip, boolean isShiftPressed) {
        this.addTooltip(stack, tooltip, isShiftPressed, InvalidReason.NONE);
    }

    /**
     * Initialises and caches the tooltip types so it doesn't need to be recalculated every render
     * tick, since they are hardcoded anyway
     */
    protected final void initTooltipTypes() {
        List<MutableComponent> traitTypesList = new ArrayList<>();
        Component comma = Component.literal(", ");

        if (this.isActionTrait())
            traitTypesList.add(
                    Component.translatable(
                            String.format(
                                    "tooltip.%s.trait.type.action", SpartanWeaponryAPI.MOD_ID)));
        if (this.isMeleeTrait())
            traitTypesList.add(
                    Component.translatable(
                            String.format(
                                    "tooltip.%s.trait.type.melee", SpartanWeaponryAPI.MOD_ID)));
        if (this.isRangedTrait())
            traitTypesList.add(
                    Component.translatable(
                            String.format(
                                    "tooltip.%s.trait.type.ranged", SpartanWeaponryAPI.MOD_ID)));
        if (this.isThrowingTrait())
            traitTypesList.add(
                    Component.translatable(
                            String.format(
                                    "tooltip.%s.trait.type.throwing", SpartanWeaponryAPI.MOD_ID)));

        this.types =
                Component.literal("  [")
                        .append(
                                ComponentUtils.formatList(
                                        traitTypesList, comma, Function.identity()))
                        .append(Component.literal("]"))
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY);
    }

    /**
     * Override this method to edit how your Weapon Trait displays the title text on a applicable
     * weapon.
     */
    protected void addTooltipTitle(
            ItemStack stack, List<Component> tooltip, ChatFormatting... formatting) {
        // Don't add the level to tooltip if not specified
        MutableComponent titleText = Component.literal("- ").withStyle(formatting);
        if (this.level == 0)
            tooltip.add(
                    titleText.append(
                            Component.translatable(
                                    String.format("tooltip.%s.trait.%s", this.modId, this.type))));
        else
            tooltip.add(
                    titleText.append(
                            Component.translatable(
                                    String.format("tooltip.%s.trait.%s", this.modId, this.type),
                                    Component.translatable("enchantment.level." + this.level))));
    }

    /**
     * Override this method to edit how your Weapon Trait displays the description text on a
     * applicable weapon. This will only show when the [SHIFT] key is pressed.
     */
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        tooltip.add(
                tooltipIndent()
                        .append(
                                Component.translatable(
                                                String.format(
                                                        "tooltip.%s.trait.%s.desc",
                                                        this.modId, this.type))
                                        .withStyle(DESCRIPTION_FORMAT)));
    }

    /** Creates a tooltip component of two spaces */
    protected static MutableComponent tooltipIndent() {
        return Component.literal("  ");
    }
}
