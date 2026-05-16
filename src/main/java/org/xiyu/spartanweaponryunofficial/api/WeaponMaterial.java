package org.xiyu.spartanweaponryunofficial.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.api.tags.ModWeaponTraitTags;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class WeaponMaterial implements Tier, IReloadable {
    public static final int DEFAULT_PRIMARY_COLOUR = 0x7F7F7F;
    public static final int DEFAULT_SECONDARY_COLOUR = 0xFFFFFF;

    public static final WeaponMaterial WOOD = new WeaponMaterial("wood", SpartanWeaponryAPI.MOD_ID, Tiers.WOOD, ItemTags.PLANKS, ModWeaponTraitTags.WOOD);
    public static final WeaponMaterial STONE = new WeaponMaterial("stone", SpartanWeaponryAPI.MOD_ID, Tiers.STONE, ModItemTags.COBBLESTONE, ModWeaponTraitTags.STONE);
    public static final WeaponMaterial LEATHER = new WeaponMaterial("leather", SpartanWeaponryAPI.MOD_ID, 128, 2.0f, 0.0f, 5, ModItemTags.LEATHER, ModWeaponTraitTags.LEATHER);
    public static final WeaponMaterial COPPER = new WeaponMaterial("copper", SpartanWeaponryAPI.MOD_ID, APIConstants.DefaultMaterialDurabilityCopper, 5.0f, APIConstants.DefaultMaterialDamageCopper, 8, ModItemTags.COPPER_INGOT, ModWeaponTraitTags.COPPER);
    public static final WeaponMaterial IRON = new WeaponMaterial("iron", SpartanWeaponryAPI.MOD_ID, Tiers.IRON, ModItemTags.IRON_INGOT, ModWeaponTraitTags.IRON);
    public static final WeaponMaterial GOLD = new WeaponMaterial("gold", SpartanWeaponryAPI.MOD_ID, Tiers.GOLD, ModItemTags.GOLD_INGOT, ModWeaponTraitTags.GOLD);
    public static final WeaponMaterial DIAMOND = new WeaponMaterial("diamond", SpartanWeaponryAPI.MOD_ID, Tiers.DIAMOND, ModItemTags.DIAMOND, ModWeaponTraitTags.DIAMOND);
    public static final WeaponMaterial NETHERITE = new WeaponMaterial("netherite", SpartanWeaponryAPI.MOD_ID, Tiers.NETHERITE, ModItemTags.NETHERITE_INGOT, ModWeaponTraitTags.NETHERITE);

    public static final WeaponMaterial TIN = new WeaponMaterial("tin", SpartanWeaponryAPI.MOD_ID, 0xBEBED8, 0xD2D2FF, APIConstants.DefaultMaterialDurabilityTin, 5.25f, APIConstants.DefaultMaterialDamageTin, 6, ModItemTags.TIN_INGOT, ModWeaponTraitTags.TIN);
    public static final WeaponMaterial BRONZE = new WeaponMaterial("bronze", SpartanWeaponryAPI.MOD_ID, 0xB36D0A, 0xCC9636, APIConstants.DefaultMaterialDurabilityBronze, 5.75f, APIConstants.DefaultMaterialDamageBronze, 12, ModItemTags.BRONZE_INGOT, ModWeaponTraitTags.BRONZE);
    public static final WeaponMaterial STEEL = new WeaponMaterial("steel", SpartanWeaponryAPI.MOD_ID, 0x858585, 0xBEBEBE, APIConstants.DefaultMaterialDurabilitySteel, 6.5f, APIConstants.DefaultMaterialDamageSteel, 14, ModItemTags.STEEL_INGOT, ModWeaponTraitTags.STEEL);
    public static final WeaponMaterial SILVER = new WeaponMaterial("silver", SpartanWeaponryAPI.MOD_ID, 0xCDCDF0, 0xFFFFFF, APIConstants.DefaultMaterialDurabilitySilver, 5.0f, APIConstants.DefaultMaterialDamageSilver, 16, ModItemTags.SILVER_INGOT, ModWeaponTraitTags.SILVER);
    public static final WeaponMaterial ELECTRUM = new WeaponMaterial("electrum", SpartanWeaponryAPI.MOD_ID, 0xD5BB4F, 0xFFFF95, APIConstants.DefaultMaterialDurabilityElectrum, 3.5f, APIConstants.DefaultMaterialDamageElectrum, 8, ModItemTags.ELECTRUM_INGOT, ModWeaponTraitTags.ELECTRUM);
    public static final WeaponMaterial LEAD = new WeaponMaterial("lead", SpartanWeaponryAPI.MOD_ID, 0x57617D, 0x8B9ED2, APIConstants.DefaultMaterialDurabilityLead, 4.5f, APIConstants.DefaultMaterialDamageLead, 5, ModItemTags.LEAD_INGOT, ModWeaponTraitTags.LEAD);
    public static final WeaponMaterial NICKEL = new WeaponMaterial("nickel", SpartanWeaponryAPI.MOD_ID, 0xDBCF95, 0xF7F7CB, APIConstants.DefaultMaterialDurabilityNickel, 4.5f, APIConstants.DefaultMaterialDamageNickel, 6, ModItemTags.NICKEL_INGOT, ModWeaponTraitTags.NICKEL);
    public static final WeaponMaterial INVAR = new WeaponMaterial("invar", SpartanWeaponryAPI.MOD_ID, 0xAEB6AB, 0xDEE3E0, APIConstants.DefaultMaterialDurabilityInvar, 6.0f, APIConstants.DefaultMaterialDamageInvar, 12, ModItemTags.INVAR_INGOT, ModWeaponTraitTags.INVAR);
    public static final WeaponMaterial CONSTANTAN = new WeaponMaterial("constantan", SpartanWeaponryAPI.MOD_ID, 0xB47C54, 0xF7D6AC, APIConstants.DefaultMaterialDurabilityConstantan, 5.5f, APIConstants.DefaultMaterialDamageConstantan, 7, ModItemTags.CONSTANTAN_INGOT, ModWeaponTraitTags.CONSTANTAN);
    public static final WeaponMaterial PLATINUM = new WeaponMaterial("platinum", SpartanWeaponryAPI.MOD_ID, 0x69DAF0, 0xAAE7FF, APIConstants.DefaultMaterialDurabilityPlatinum, 4.0f, APIConstants.DefaultMaterialDamagePlatinum, 18, ModItemTags.PLATINUM_INGOT, ModWeaponTraitTags.PLATINUM);
    public static final WeaponMaterial ALUMINUM = new WeaponMaterial("aluminum", SpartanWeaponryAPI.MOD_ID, 0xAEBBBF, 0xF9FFFF, APIConstants.DefaultMaterialDurabilityAluminum, 5.0f, APIConstants.DefaultMaterialDamageAluminum, 7, ModItemTags.ALUMINUM_INGOT, ModWeaponTraitTags.ALUMINUM);

    private int durability;
    private final float speed;
    private float baseDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;
    private final TagKey<Item> repairTag;

    private final String name;
    private final String modId;
    private final int colourPrimary, colourSecondary;

    private boolean useCustomDisplayName = false;
    private Function<String, String> translationFunc = null;

    protected List<WeaponTrait> traits = ImmutableList.of();                // *ALL* traits		 TODO: Does this still need to be cached?
    protected List<WeaponTrait> meleeTraits = ImmutableList.of();            // Melee-only traits
    protected List<WeaponTrait> rangedTraits = ImmutableList.of();            // Ranged-only traits
    protected List<WeaponTrait> throwingTraits = ImmutableList.of();        // Throwing-only traits
    protected final TagKey<WeaponTrait> traitsTag;
    protected boolean isValidTag;
    protected Optional<List<Pair<WeaponTrait, WeaponTrait.InvalidReason>>> invalidTraits = Optional.empty();

    /**
     * Creates a builder for addon materials. Existing constructors remain supported; the builder is a
     * named alternative for call sites where positional numeric arguments are hard to audit.
     */
    public static Builder builder(String name, String modId) {
        return new Builder(name, modId);
    }

    public WeaponMaterial(String nameIn, String modIdIn, int colourPrimaryIn, int colourSecondaryIn, int durabilityIn, float speedIn, float baseDamageIn, int enchantabilityIn, TagKey<Item> repairTagIn, TagKey<WeaponTrait> traitsTagIn) {
        this.name = nameIn;
        this.modId = modIdIn;
        this.colourPrimary = colourPrimaryIn;
        this.colourSecondary = colourSecondaryIn;

        this.durability = durabilityIn;
        this.speed = speedIn;
        this.baseDamage = baseDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairTag = repairTagIn;
        this.repairMaterial = new LazyLoadedValue<>(() -> Ingredient.of(repairTagIn));
        this.traitsTag = traitsTagIn;

        ReloadableHandler.addToMaterialReloadList(this);
    }

    public WeaponMaterial(String unlocName, String modIdIn, int maxUses, float efficiency, float baseDamage, int enchantability, TagKey<Item> tag, TagKey<WeaponTrait> traitsTagIn) {
        this(unlocName, modIdIn, DEFAULT_PRIMARY_COLOUR, DEFAULT_SECONDARY_COLOUR, maxUses, efficiency, baseDamage, enchantability, tag, traitsTagIn);
    }

    public WeaponMaterial(String nameIn, String modIdIn, Tier itemTierIn, TagKey<Item> tagIn, TagKey<WeaponTrait> traitsTagIn) {
        this(nameIn, modIdIn, DEFAULT_PRIMARY_COLOUR, DEFAULT_SECONDARY_COLOUR, itemTierIn.getUses(), itemTierIn.getSpeed(),
                itemTierIn.getAttackDamageBonus(), itemTierIn.getEnchantmentValue(), tagIn, traitsTagIn);
    }

    @Override
    public void reload() {
        RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        Registry<WeaponTrait> registry = registryAccess.registry(WeaponTraits.REGISTRY_KEY).orElse(null);
        if (registry == null) {
            Log.error("Weapon Trait registry couldn't be found for weapon material \"" + this.name + "\"!");
            return;
        }
        // Verify the tag and Initialize Weapon Traits
        ImmutableList.Builder<WeaponTrait> builder = ImmutableList.builder();

        this.isValidTag = registry.getTag(this.traitsTag).isPresent();
        if (!this.isValidTag) {
            Log.error("Weapon Trait tag \"" + this.traitsTag.location() + "\" couldn't be found for weapon material \"" + this.name + "\"!");
            return;
        }

        Iterable<Holder<WeaponTrait>> tag = registry.getTagOrEmpty(this.traitsTag);
        this.invalidTraits = Optional.empty();
        List<Pair<WeaponTrait, WeaponTrait.InvalidReason>> invalidTraitList = new ArrayList<>();
        List<String> invalidTraitValues = new ArrayList<>();
        for (Holder<WeaponTrait> holder : tag) {
            WeaponTrait trait = holder.value();
            boolean isActionTrait = trait.isActionTrait();
            if (isActionTrait) {
                invalidTraitList.add(Pair.of(trait, WeaponTrait.InvalidReason.MATERIAL_ACTION_TRAIT));
                invalidTraitValues.add(String.valueOf(registry.getKey(trait)));
            } else {
                builder.add(trait);
            }
        }

        if (!invalidTraitValues.isEmpty()) {
            Log.warn("Found non-material Weapon Traits for weapon material \"" + this.name + "\" which have not been added: " + String.join(", ", invalidTraitValues));
            this.invalidTraits = Optional.of(invalidTraitList);
        }
        this.traits = builder.build();

        this.meleeTraits = this.traits.stream().filter(WeaponType.MELEE.getTraitFilter()).toList();
        this.rangedTraits = this.traits.stream().filter(WeaponType.RANGED.getTraitFilter()).toList();
        this.throwingTraits = this.traits.stream().filter(WeaponType.THROWING.getTraitFilter()).toList();
    }

    public WeaponMaterial setUseCustomDisplayName() {
        this.useCustomDisplayName = true;
        return this;
    }

    public WeaponMaterial setUseCustomDisplayName(Function<String, String> translationFunc) {
        this.translationFunc = translationFunc;
        return this.setUseCustomDisplayName();
    }

    public boolean useCustomDisplayName() {
        return this.useCustomDisplayName;
    }

    public Component translateName() {
        if (this.translationFunc == null)
            return Component.translatable("material." + this.getModId() + "." + this.getMaterialName());
        return Component.literal(this.translationFunc.apply(this.name));
    }

    public String getMaterialName() {
        return this.name;
    }

    public int getPrimaryColour() {
        return this.colourPrimary;
    }

    public int getSecondaryColour() {
        return this.colourSecondary;
    }

    public String getModId() {
        return this.modId;
    }

    @Override
    public int getUses() {
        return this.durability;
    }

    public void setDurability(int maxUses) {
        this.durability = maxUses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.baseDamage;
    }

    public void setAttackDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }


    public int getLevel() {
//		return this.harvestLevel;
        return 0;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    public TagKey<Item> getRepairTag() {
        return this.repairTag;
    }

    public String getRepairTagName() {
        return this.repairTag.location().toString();
    }

    public TagKey<WeaponTrait> getTraitsTag() {
        return this.traitsTag;
    }

    /**
     * Queries if the material has any Weapon Traits
     *
     * @return true if any Weapon Trait bonus exists on this material; false otherwise.
     */
    @Deprecated(since = "3.1.1", forRemoval = true)
    public boolean hasAnyBonusTraits() {
        return this.traits != null && (!this.traits.isEmpty() || this.invalidTraits.isPresent());
    }

    public boolean hasAnyBonusTraits(WeaponType type) {
        List<WeaponTrait> weaponTraits = this.getBonusTraits(type);
        return weaponTraits != null && (!weaponTraits.isEmpty() || this.invalidTraits.isPresent());
    }

    @Deprecated(since = "3.1.1", forRemoval = true)
    public List<WeaponTrait> getBonusTraits() {
        return this.traits;
    }

    public List<WeaponTrait> getBonusTraits(WeaponType type) {
        return switch (type) {
            case MELEE -> this.meleeTraits;
            case RANGED -> this.rangedTraits;
            case THROWING -> this.throwingTraits;
        };
    }

    public void addTagErrorTooltip(ItemStack stack, List<Component> tooltip) {
        if (!this.isValidTag)
            tooltip.add(Component.translatable(String.format("tooltip.%s.trait.invalid.material_tag", SpartanWeaponryAPI.MOD_ID), Component.translatable(String.format("tooltip.%s.material.%s", SpartanWeaponryAPI.MOD_ID, this.name)), this.traitsTag.location().toString()).withStyle(ChatFormatting.DARK_RED));
    }

    public void addTraitsToTooltip(ItemStack stack, WeaponType type, List<Component> tooltip, boolean isShiftPressed) {
        if (this.hasAnyBonusTraits(type)) {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable(String.format("tooltip.%s.trait.material_bonus", ModSpartanWeaponry.ID)).withStyle(ChatFormatting.AQUA));
            this.traits.forEach((trait) -> trait.addTooltip(stack, tooltip, isShiftPressed, WeaponTrait.InvalidReason.NONE));
        }
        if (this.invalidTraits.isPresent()) {
            tooltip.add(Component.empty());
            this.invalidTraits.get().forEach((traitPair) -> traitPair.getLeft().addTooltip(stack, tooltip, isShiftPressed, traitPair.getRight()));
        }
    }

    @Deprecated(since = "3.1.1", forRemoval = true)
    public void addTraitsToTooltip(ItemStack stack, List<Component> tooltip, boolean isShiftPressed) {
        if (this.hasAnyBonusTraits()) {
            this.traits.forEach((trait) -> trait.addTooltip(stack, tooltip, isShiftPressed, WeaponTrait.InvalidReason.NONE));
        }
        this.invalidTraits.ifPresent(pairs -> pairs.forEach((traitPair) -> traitPair.getLeft().addTooltip(stack, tooltip, isShiftPressed, traitPair.getRight())));
    }

    /**
     * Converts RGB color to the integer format expected for material colors
     *
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return The combined integer color format
     */
    public static int colorRGB(byte r, byte g, byte b) {
        return ((int) r << 16) + ((int) g << 8) + b;
    }

    public static final class Builder {
        private final String name;
        private final String modId;

        private int colourPrimary = DEFAULT_PRIMARY_COLOUR;
        private int colourSecondary = DEFAULT_SECONDARY_COLOUR;
        private Integer durability;
        private Float speed;
        private Float baseDamage;
        private Integer enchantability;
        private TagKey<Item> repairTag;
        private TagKey<WeaponTrait> traitsTag;

        private Builder(String name, String modId) {
            this.name = Objects.requireNonNull(name, "name");
            this.modId = Objects.requireNonNull(modId, "modId");
        }

        public Builder colours(int primary, int secondary) {
            this.colourPrimary = primary;
            this.colourSecondary = secondary;
            return this;
        }

        public Builder colors(int primary, int secondary) {
            return this.colours(primary, secondary);
        }

        public Builder tier(Tier tier) {
            Objects.requireNonNull(tier, "tier");
            this.durability = tier.getUses();
            this.speed = tier.getSpeed();
            this.baseDamage = tier.getAttackDamageBonus();
            this.enchantability = tier.getEnchantmentValue();
            return this;
        }

        public Builder durability(int durability) {
            this.durability = durability;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder baseDamage(float baseDamage) {
            this.baseDamage = baseDamage;
            return this;
        }

        public Builder attackDamageBonus(float attackDamageBonus) {
            return this.baseDamage(attackDamageBonus);
        }

        public Builder enchantability(int enchantability) {
            this.enchantability = enchantability;
            return this;
        }

        public Builder enchantmentValue(int enchantmentValue) {
            return this.enchantability(enchantmentValue);
        }

        public Builder repairTag(TagKey<Item> repairTag) {
            this.repairTag = Objects.requireNonNull(repairTag, "repairTag");
            return this;
        }

        public Builder traitsTag(TagKey<WeaponTrait> traitsTag) {
            this.traitsTag = Objects.requireNonNull(traitsTag, "traitsTag");
            return this;
        }

        public WeaponMaterial build() {
            return new WeaponMaterial(
                    this.name,
                    this.modId,
                    this.colourPrimary,
                    this.colourSecondary,
                    require("durability", this.durability),
                    require("speed", this.speed),
                    require("baseDamage", this.baseDamage),
                    require("enchantability", this.enchantability),
                    require("repairTag", this.repairTag),
                    require("traitsTag", this.traitsTag)
            );
        }

        private static <T> T require(String fieldName, T value) {
            if (value == null) {
                throw new IllegalStateException("WeaponMaterial builder is missing required field: " + fieldName);
            }
            return value;
        }
    }
}
