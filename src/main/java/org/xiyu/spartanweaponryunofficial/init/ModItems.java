package org.xiyu.spartanweaponryunofficial.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.item.*;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;
import org.xiyu.spartanweaponryunofficial.util.WeaponFactory;
import org.xiyu.spartanweaponryunofficial.util.WeaponFactory.WeaponFunction;

public class ModItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(ModSpartanWeaponry.ID);

    public static class WeaponItemsMelee {
        public final DeferredHolder<Item, SwordBaseItem> wood, stone, copper, iron, gold, diamond, netherite;
        public final DeferredHolder<Item, SwordBaseItem> tin, bronze, steel, silver, lead, nickel, invar, constantan, platinum, electrum, aluminum;

        public WeaponItemsMelee(DeferredRegister.Items register, String weaponName, WeaponFunction<SwordBaseItem> factory) {
            this.wood = register.registerItem(weaponName + "_wooden", props -> factory.create(WeaponMaterial.WOOD, props));
            this.stone = register.registerItem("stone_" + weaponName, props -> factory.create(WeaponMaterial.STONE, props));
            this.copper = register.registerItem("copper_" + weaponName, props -> factory.create(WeaponMaterial.COPPER, props));
            this.iron = register.registerItem("iron_" + weaponName, props -> factory.create(WeaponMaterial.IRON, props));
            this.gold = register.registerItem("golden_" + weaponName, props -> factory.create(WeaponMaterial.GOLD, props));
            this.diamond = register.registerItem("diamond_" + weaponName, props -> factory.create(WeaponMaterial.DIAMOND, props));
            this.netherite = register.registerItem("netherite_" + weaponName, props -> factory.create(WeaponMaterial.NETHERITE, props), props -> props.fireResistant());

            this.tin = register.registerItem("tin_" + weaponName, props -> factory.create(WeaponMaterial.TIN, props));
            this.bronze = register.registerItem("bronze_" + weaponName, props -> factory.create(WeaponMaterial.BRONZE, props));
            this.steel = register.registerItem("steel_" + weaponName, props -> factory.create(WeaponMaterial.STEEL, props));
            this.silver = register.registerItem("silver_" + weaponName, props -> factory.create(WeaponMaterial.SILVER, props));
            this.electrum = register.registerItem("electrum_" + weaponName, props -> factory.create(WeaponMaterial.ELECTRUM, props));
            this.lead = register.registerItem("lead_" + weaponName, props -> factory.create(WeaponMaterial.LEAD, props));
            this.nickel = register.registerItem("nickel_" + weaponName, props -> factory.create(WeaponMaterial.NICKEL, props));
            this.invar = register.registerItem("invar_" + weaponName, props -> factory.create(WeaponMaterial.INVAR, props));
            this.constantan = register.registerItem("constantan_" + weaponName, props -> factory.create(WeaponMaterial.CONSTANTAN, props));
            this.platinum = register.registerItem("platinum_" + weaponName, props -> factory.create(WeaponMaterial.PLATINUM, props));
            this.aluminum = register.registerItem("aluminum_" + weaponName, props -> factory.create(WeaponMaterial.ALUMINUM, props));
        }
		
		public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed)
		{
			getAsList().forEach((weapon) -> weapon.setAttackDamageAndSpeed(baseDamage, damageMultiplier, speed));
		}

        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return ImmutableList.of(new ItemStack(this.wood.get()), new ItemStack(this.stone.get()), new ItemStack(this.copper.get()), new ItemStack(this.iron.get()),
                    new ItemStack(this.gold.get()), new ItemStack(this.diamond.get()), new ItemStack(this.netherite.get()));
        }

        public ImmutableList<ItemStack> getModdedItemStacks() {
            return ImmutableList.of(new ItemStack(this.tin.get()), new ItemStack(this.bronze.get()), new ItemStack(this.steel.get()), new ItemStack(this.silver.get()),
                    new ItemStack(this.electrum.get()), new ItemStack(this.lead.get()), new ItemStack(this.nickel.get()), new ItemStack(this.invar.get()),
                    new ItemStack(this.constantan.get()), new ItemStack(this.platinum.get()), new ItemStack(this.aluminum.get()));
        }

        public ImmutableList<SwordBaseItem> getAsList() {
            return ImmutableList.of(this.wood.get(), this.stone.get(), this.copper.get(), this.iron.get(), this.gold.get(), this.diamond.get(), this.netherite.get(),
                    this.tin.get(), this.bronze.get(), this.steel.get(), this.silver.get(), this.electrum.get(), this.lead.get(), this.nickel.get(), this.invar.get(), this.constantan.get(), this.platinum.get(), this.aluminum.get());
        }
    }

    public static class WeaponItemsRanged {
        public final DeferredHolder<Item, Item> wood, leather, copper, iron, gold, diamond, netherite;
        public final DeferredHolder<Item, Item> tin, bronze, steel, silver, electrum, lead, nickel, invar, constantan, platinum, aluminum;

        public WeaponItemsRanged(DeferredRegister.Items register, String weaponName, WeaponFunction<? extends Item> factory) {
            // Determine if this weapon type needs 'strengthened' in the name
            boolean isStrengthenedWeapon = weaponName.equals("longbow") || weaponName.equals("heavy_crossbow");
            String strengthenedSuffix = isStrengthenedWeapon ? "_strengthened" : "";

            this.wood = register.registerItem(weaponName + "_wooden" + strengthenedSuffix, props -> factory.create(WeaponMaterial.WOOD, props));
            this.leather = register.registerItem(weaponName + "_leather" + strengthenedSuffix, props -> factory.create(WeaponMaterial.LEATHER, props));
            this.copper = register.registerItem(weaponName + "_copper" + strengthenedSuffix, props -> factory.create(WeaponMaterial.COPPER, props));
            this.iron = register.registerItem(weaponName + "_iron" + strengthenedSuffix, props -> factory.create(WeaponMaterial.IRON, props));
            this.gold = register.registerItem(weaponName + "_golden" + strengthenedSuffix, props -> factory.create(WeaponMaterial.GOLD, props));
            this.diamond = register.registerItem(weaponName + "_diamond" + strengthenedSuffix, props -> factory.create(WeaponMaterial.DIAMOND, props));
            this.netherite = register.registerItem(weaponName + "_netherite" + strengthenedSuffix, props -> factory.create(WeaponMaterial.NETHERITE, props), props -> props.fireResistant());

            this.tin = register.registerItem(weaponName + "_tin" + strengthenedSuffix, props -> factory.create(WeaponMaterial.TIN, props));
            this.bronze = register.registerItem(weaponName + "_bronze" + strengthenedSuffix, props -> factory.create(WeaponMaterial.BRONZE, props));
            this.steel = register.registerItem(weaponName + "_steel" + strengthenedSuffix, props -> factory.create(WeaponMaterial.STEEL, props));
            this.silver = register.registerItem(weaponName + "_silver" + strengthenedSuffix, props -> factory.create(WeaponMaterial.SILVER, props));
            this.electrum = register.registerItem(weaponName + "_electrum" + strengthenedSuffix, props -> factory.create(WeaponMaterial.ELECTRUM, props));
            this.lead = register.registerItem(weaponName + "_lead" + strengthenedSuffix, props -> factory.create(WeaponMaterial.LEAD, props));
            this.nickel = register.registerItem(weaponName + "_nickel" + strengthenedSuffix, props -> factory.create(WeaponMaterial.NICKEL, props));
            this.invar = register.registerItem(weaponName + "_invar" + strengthenedSuffix, props -> factory.create(WeaponMaterial.INVAR, props));
            this.constantan = register.registerItem(weaponName + "_constantan" + strengthenedSuffix, props -> factory.create(WeaponMaterial.CONSTANTAN, props));
            this.platinum = register.registerItem(weaponName + "_platinum" + strengthenedSuffix, props -> factory.create(WeaponMaterial.PLATINUM, props));
            this.aluminum = register.registerItem(weaponName + "_aluminum" + strengthenedSuffix, props -> factory.create(WeaponMaterial.ALUMINUM, props));
        }

        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return ImmutableList.of(new ItemStack(this.wood.get()), new ItemStack(this.leather.get()), new ItemStack(this.copper.get()), new ItemStack(this.iron.get()),
                    new ItemStack(this.gold.get()), new ItemStack(this.diamond.get()), new ItemStack(this.netherite.get()));
        }

        public ImmutableList<ItemStack> getModdedItemStacks() {
            return ImmutableList.of(new ItemStack(this.tin.get()), new ItemStack(this.bronze.get()), new ItemStack(this.steel.get()), new ItemStack(this.silver.get()),
                    new ItemStack(this.electrum.get()), new ItemStack(this.lead.get()), new ItemStack(this.nickel.get()), new ItemStack(this.invar.get()),
                    new ItemStack(this.constantan.get()), new ItemStack(this.platinum.get()), new ItemStack(this.aluminum.get()));
        }

        public ImmutableList<Item> getAsList() {
            return ImmutableList.of(this.wood.get(), this.leather.get(), this.copper.get(), this.iron.get(), this.gold.get(), this.diamond.get(), this.netherite.get(),
                    this.tin.get(), this.bronze.get(), this.steel.get(), this.silver.get(), this.electrum.get(), this.lead.get(), this.nickel.get(), this.invar.get(), this.constantan.get(), this.platinum.get(), this.aluminum.get());
        }
    }

    public static class WeaponItemsThrowing {
        public DeferredHolder<Item, ThrowingWeaponItem> wood, stone, copper, iron, gold, diamond, netherite;
        public DeferredHolder<Item, ThrowingWeaponItem> tin, bronze, steel, silver, electrum, lead, nickel, invar, constantan, platinum, aluminum;

        public WeaponItemsThrowing(DeferredRegister.Items register, String weaponName, WeaponFunction<ThrowingWeaponItem> factory) {
            this.wood = register.registerItem(weaponName + "_wooden", props -> factory.create(WeaponMaterial.WOOD, props));
            this.stone = register.registerItem(weaponName + "_stone", props -> factory.create(WeaponMaterial.STONE, props));
            this.copper = register.registerItem(weaponName + "_copper", props -> factory.create(WeaponMaterial.COPPER, props));
            this.iron = register.registerItem(weaponName + "_iron", props -> factory.create(WeaponMaterial.IRON, props));
            this.gold = register.registerItem(weaponName + "_golden", props -> factory.create(WeaponMaterial.GOLD, props));
            this.diamond = register.registerItem(weaponName + "_diamond", props -> factory.create(WeaponMaterial.DIAMOND, props));
            this.netherite = register.registerItem(weaponName + "_netherite", props -> factory.create(WeaponMaterial.NETHERITE, props), props -> props.fireResistant());

            this.tin = register.registerItem(weaponName + "_tin", props -> factory.create(WeaponMaterial.TIN, props));
            this.bronze = register.registerItem(weaponName + "_bronze", props -> factory.create(WeaponMaterial.BRONZE, props));
            this.steel = register.registerItem(weaponName + "_steel", props -> factory.create(WeaponMaterial.STEEL, props));
            this.silver = register.registerItem(weaponName + "_silver", props -> factory.create(WeaponMaterial.SILVER, props));
            this.electrum = register.registerItem(weaponName + "_electrum", props -> factory.create(WeaponMaterial.ELECTRUM, props));
            this.lead = register.registerItem(weaponName + "_lead", props -> factory.create(WeaponMaterial.LEAD, props));
            this.nickel = register.registerItem(weaponName + "_nickel", props -> factory.create(WeaponMaterial.NICKEL, props));
            this.invar = register.registerItem(weaponName + "_invar", props -> factory.create(WeaponMaterial.INVAR, props));
            this.constantan = register.registerItem(weaponName + "_constantan", props -> factory.create(WeaponMaterial.CONSTANTAN, props));
            this.platinum = register.registerItem(weaponName + "_platinum", props -> factory.create(WeaponMaterial.PLATINUM, props));
            this.aluminum = register.registerItem(weaponName + "_aluminum", props -> factory.create(WeaponMaterial.ALUMINUM, props));
        }
		
		public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed, int chargeTicks)
		{
			getAsList().forEach((weapon) -> weapon.updateFromConfig(baseDamage, damageMultiplier, speed, chargeTicks));
		}

        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return ImmutableList.of(this.wood.get().makeTabStack(), this.stone.get().makeTabStack(), this.copper.get().makeTabStack(), this.iron.get().makeTabStack(),
                    this.gold.get().makeTabStack(), this.diamond.get().makeTabStack(), this.netherite.get().makeTabStack());
        }

        public ImmutableList<ItemStack> getModdedItemStacks() {
            return ImmutableList.of(this.tin.get().makeTabStack(), this.bronze.get().makeTabStack(), this.steel.get().makeTabStack(), this.silver.get().makeTabStack(),
                    this.electrum.get().makeTabStack(), this.lead.get().makeTabStack(), this.nickel.get().makeTabStack(), this.invar.get().makeTabStack(),
                    this.constantan.get().makeTabStack(), this.platinum.get().makeTabStack(), this.aluminum.get().makeTabStack());
        }

        public ImmutableList<ThrowingWeaponItem> getAsList() {
            return ImmutableList.of(this.wood.get(), this.stone.get(), this.copper.get(), this.iron.get(), this.gold.get(), this.diamond.get(), this.netherite.get(),
                    this.tin.get(), this.bronze.get(), this.steel.get(), this.silver.get(), this.electrum.get(), this.lead.get(), this.nickel.get(), this.invar.get(), this.constantan.get(), this.platinum.get(), this.aluminum.get());
        }
    }

    // Basic Items
    public static final DeferredHolder<Item, Item> SIMPLE_HANDLE = REGISTRY.registerItem("simple_handle", BasicItem::new);
    public static final DeferredHolder<Item, Item> HANDLE = REGISTRY.registerItem("handle", BasicItem::new);
    public static final DeferredHolder<Item, Item> SIMPLE_POLE = REGISTRY.registerItem("simple_pole", BasicItem::new);
    public static final DeferredHolder<Item, Item> POLE = REGISTRY.registerItem("pole", BasicItem::new);
    public static final DeferredHolder<Item, Item> EXPLOSIVE_CHARGE = REGISTRY.registerItem("explosive_charge", BasicItem::new);

    // Weapons
    public static final WeaponItemsMelee DAGGERS = new WeaponItemsMelee(REGISTRY, "dagger", WeaponFactory.DAGGER);
    public static final WeaponItemsMelee PARRYING_DAGGERS = new WeaponItemsMelee(REGISTRY, "parrying_dagger", WeaponFactory.PARRYING_DAGGER);
    public static final WeaponItemsMelee LONGSWORDS = new WeaponItemsMelee(REGISTRY, "longsword", WeaponFactory.LONGSWORD);
    public static final WeaponItemsMelee KATANAS = new WeaponItemsMelee(REGISTRY, "katana", WeaponFactory.KATANA);
    public static final WeaponItemsMelee SABERS = new WeaponItemsMelee(REGISTRY, "saber", WeaponFactory.SABER);
    public static final WeaponItemsMelee RAPIERS = new WeaponItemsMelee(REGISTRY, "rapier", WeaponFactory.RAPIER);
    public static final WeaponItemsMelee GREATSWORDS = new WeaponItemsMelee(REGISTRY, "greatsword", WeaponFactory.GREATSWORD);

    public static final DeferredHolder<Item, SwordBaseItem> WOODEN_CLUB = REGISTRY.registerItem("wooden_club", props -> new SwordBaseItem(props, WeaponMaterial.WOOD, WeaponArchetype.CLUB, Defaults.DamageBaseClub, Defaults.DamageMultiplierClub, Defaults.SpeedClub));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CLUB = REGISTRY.registerItem("studded_club", props -> new SwordBaseItem(props, WeaponMaterial.IRON, WeaponArchetype.CLUB, Defaults.DamageBaseClub, Defaults.DamageMultiplierClub, Defaults.SpeedClub));

    public static final DeferredHolder<Item, SwordBaseItem> CESTUS = REGISTRY.registerItem("cestus", props -> new SwordBaseItem(props, WeaponMaterial.LEATHER, WeaponArchetype.CESTUS, Defaults.DamageBaseCestus, Defaults.DamageMultiplierCestus, Defaults.SpeedCestus));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CESTUS = REGISTRY.registerItem("studded_cestus", props -> new SwordBaseItem(props, WeaponMaterial.IRON, WeaponArchetype.CESTUS, Defaults.DamageBaseCestus, Defaults.DamageMultiplierCestus, Defaults.SpeedCestus));

    public static final WeaponItemsMelee BATTLE_HAMMERS = new WeaponItemsMelee(REGISTRY, "battle_hammer", WeaponFactory.BATTLE_HAMMER);
    public static final WeaponItemsMelee WARHAMMERS = new WeaponItemsMelee(REGISTRY, "warhammer", WeaponFactory.WARHAMMER);
    public static final WeaponItemsMelee SPEARS = new WeaponItemsMelee(REGISTRY, "spear", WeaponFactory.SPEAR);
    public static final WeaponItemsMelee HALBERDS = new WeaponItemsMelee(REGISTRY, "halberd", WeaponFactory.HALBERD);
    public static final WeaponItemsMelee PIKES = new WeaponItemsMelee(REGISTRY, "pike", WeaponFactory.PIKE);
    public static final WeaponItemsMelee LANCES = new WeaponItemsMelee(REGISTRY, "lance", WeaponFactory.LANCE);

    public static final WeaponItemsRanged LONGBOWS = new WeaponItemsRanged(REGISTRY, "longbow", WeaponFactory.LONGBOW);
    public static final WeaponItemsRanged HEAVY_CROSSBOWS = new WeaponItemsRanged(REGISTRY, "heavy_crossbow", WeaponFactory.HEAVY_CROSSBOW);

    public static final WeaponItemsThrowing THROWING_KNIVES = new WeaponItemsThrowing(REGISTRY, "throwing_knife", WeaponFactory.THROWING_KNIFE);
    public static final WeaponItemsThrowing TOMAHAWKS = new WeaponItemsThrowing(REGISTRY, "tomahawk", WeaponFactory.TOMAHAWK);
    public static final WeaponItemsThrowing JAVELINS = new WeaponItemsThrowing(REGISTRY, "javelin", WeaponFactory.JAVELIN);
    public static final WeaponItemsThrowing BOOMERANGS = new WeaponItemsThrowing(REGISTRY, "boomerang", WeaponFactory.BOOMERANG);

    public static final WeaponItemsMelee BATTLEAXES = new WeaponItemsMelee(REGISTRY, "battleaxe", WeaponFactory.BATTLEAXE);
    public static final WeaponItemsMelee FLANGED_MACES = new WeaponItemsMelee(REGISTRY, "flanged_mace", WeaponFactory.FLANGED_MACE);
    public static final WeaponItemsMelee GLAIVES = new WeaponItemsMelee(REGISTRY, "glaive", WeaponFactory.GLAIVE);
    public static final WeaponItemsMelee QUARTERSTAVES = new WeaponItemsMelee(REGISTRY, "quarterstaff", WeaponFactory.QUARTERSTAFF);
    public static final WeaponItemsMelee SCYTHES = new WeaponItemsMelee(REGISTRY, "scythe", WeaponFactory.SCYTHE);

    // Arrows
    public static final DeferredHolder<Item, ArrowBaseItem> WOODEN_ARROW = REGISTRY.registerItem("wooden_arrow", props -> new ArrowBaseItem(props, Defaults.BaseDamageArrowWood, Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_WOODEN_ARROW = REGISTRY.registerItem("tipped_wooden_arrow", props -> new ArrowBaseTippedItem(props, "wooden_arrow", Defaults.BaseDamageArrowWood, Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> COPPER_ARROW = REGISTRY.registerItem("copper_arrow", props -> new ArrowBaseItem(props, Defaults.BaseDamageArrowCopper, Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_COPPER_ARROW = REGISTRY.registerItem("tipped_copper_arrow", props -> new ArrowBaseTippedItem(props, "copper_arrow", Defaults.BaseDamageArrowCopper, Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> IRON_ARROW = REGISTRY.registerItem("iron_arrow", props -> new ArrowBaseItem(props, Defaults.BaseDamageArrowIron, Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_IRON_ARROW = REGISTRY.registerItem("tipped_iron_arrow", props -> new ArrowBaseTippedItem(props, "iron_arrow", Defaults.BaseDamageArrowIron, Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> DIAMOND_ARROW = REGISTRY.registerItem("diamond_arrow", props -> new ArrowBaseItem(props, Defaults.BaseDamageArrowDiamond, Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_DIAMOND_ARROW = REGISTRY.registerItem("tipped_diamond_arrow", props -> new ArrowBaseTippedItem(props, "diamond_arrow", Defaults.BaseDamageArrowDiamond, Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> NETHERITE_ARROW = REGISTRY.registerItem("netherite_arrow", props -> new ArrowBaseItem(props, Defaults.BaseDamageArrowNetherite, Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_NETHERITE_ARROW = REGISTRY.registerItem("tipped_netherite_arrow", props -> new ArrowBaseTippedItem(props, "netherite_arrow", Defaults.BaseDamageArrowNetherite, Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, Item> EXPLOSIVE_ARROW = REGISTRY.registerItem("explosive_arrow", props -> new ArrowExplosiveItem(props, Defaults.RangeMultiplierArrowExplosive));

    public static final DeferredHolder<Item, BoltItem> BOLT = REGISTRY.registerItem("bolt", props -> new BoltItem(props, Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> TIPPED_BOLT = REGISTRY.registerItem("tipped_bolt", props -> new BoltTippedItem(props, "bolt", Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> SPECTRAL_BOLT = REGISTRY.registerItem("spectral_bolt", props -> new BoltSpectralItem(props, Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> COPPER_BOLT = REGISTRY.registerItem("copper_bolt", props -> new BoltItem(props, Defaults.BaseDamageBoltCopper, Defaults.RangeMultiplierBoltCopper, Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> TIPPED_COPPER_BOLT = REGISTRY.registerItem("tipped_copper_bolt", props -> new BoltTippedItem(props, "copper_bolt", Defaults.BaseDamageBoltCopper, Defaults.RangeMultiplierBoltCopper, Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> DIAMOND_BOLT = REGISTRY.registerItem("diamond_bolt", props -> new BoltItem(props, Defaults.BaseDamageBoltDiamond, Defaults.RangeMultiplierBoltDiamond, Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> TIPPED_DIAMOND_BOLT = REGISTRY.registerItem("tipped_diamond_bolt", props -> new BoltTippedItem(props, "diamond_bolt", Defaults.BaseDamageBoltDiamond, Defaults.RangeMultiplierBoltDiamond, Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> NETHERITE_BOLT = REGISTRY.registerItem("netherite_bolt", props -> new BoltItem(props, Defaults.BaseDamageBoltNetherite, Defaults.RangeMultiplierBoltNetherite, Defaults.ArmorPiercingFactorBoltNetherite));
    public static final DeferredHolder<Item, BoltItem> TIPPED_NETHERITE_BOLT = REGISTRY.registerItem("tipped_netherite_bolt", props -> new BoltTippedItem(props, "netherite_bolt", Defaults.BaseDamageBoltNetherite, Defaults.RangeMultiplierBoltNetherite, Defaults.ArmorPiercingFactorBoltNetherite));

    public static final DeferredHolder<Item, Item> SMALL_ARROW_QUIVER = REGISTRY.registerItem("small_arrow_quiver", props -> new QuiverArrowItem(props.stacksTo(1), Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_ARROW_QUIVER = REGISTRY.registerItem("medium_arrow_quiver", props -> new QuiverArrowItem(props.stacksTo(1), Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_ARROW_QUIVER = REGISTRY.registerItem("large_arrow_quiver", props -> new QuiverArrowItem(props.stacksTo(1), Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_ARROW_QUIVER = REGISTRY.registerItem("huge_arrow_quiver", props -> new QuiverArrowItem(props.stacksTo(1), Defaults.SlotsQuiverHuge));
    public static final DeferredHolder<Item, Item> SMALL_BOLT_QUIVER = REGISTRY.registerItem("small_bolt_quiver", props -> new QuiverBoltItem(props.stacksTo(1), Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_BOLT_QUIVER = REGISTRY.registerItem("medium_bolt_quiver", props -> new QuiverBoltItem(props.stacksTo(1), Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_BOLT_QUIVER = REGISTRY.registerItem("large_bolt_quiver", props -> new QuiverBoltItem(props.stacksTo(1), Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_BOLT_QUIVER = REGISTRY.registerItem("huge_bolt_quiver", props -> new QuiverBoltItem(props.stacksTo(1), Defaults.SlotsQuiverHuge));

    public static final DeferredHolder<Item, Item> QUIVER_COMPARTMENT = REGISTRY.registerItem("quiver_compartment", QuiverSmithingTemplateItem::new);
    public static final DeferredHolder<Item, Item> MEDIUM_QUIVER_BRACE = REGISTRY.registerItem("medium_quiver_brace", BasicItem::new);
    public static final DeferredHolder<Item, Item> LARGE_QUIVER_BRACE = REGISTRY.registerItem("large_quiver_brace", BasicItem::new);
    public static final DeferredHolder<Item, Item> HUGE_QUIVER_BRACE = REGISTRY.registerItem("huge_quiver_brace", BasicItem::new);

/*	public static final RegistryObject<Item> MEDIUM_QUIVER_UPGRADE_KIT = REGISTRY.register("medium_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
	public static final RegistryObject<Item> LARGE_QUIVER_UPGRADE_KIT = REGISTRY.register("large_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
	public static final RegistryObject<Item> HUGE_QUIVER_UPGRADE_KIT = REGISTRY.register("huge_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));*/

    public static final DeferredHolder<Item, Item> DYNAMITE = REGISTRY.registerItem("dynamite", DynamiteItem::new);

    public static final DeferredHolder<Item, WeaponOilItem> WEAPON_OIL = REGISTRY.registerItem("weapon_oil", props -> new WeaponOilItem(props.stacksTo(6).craftRemainder(Items.GLASS_BOTTLE)));

    public static final DeferredHolder<Item, Item> BLAZE_HEAD = REGISTRY.registerItem("blaze_head", props -> new ExtendedSkullItem(ModBlocks.BLAZE_HEAD.get(), ModBlocks.BLAZE_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ENDERMAN_HEAD = REGISTRY.registerItem("enderman_head", props -> new ExtendedSkullItem(ModBlocks.ENDERMAN_HEAD.get(), ModBlocks.ENDERMAN_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> SPIDER_HEAD = REGISTRY.registerItem("spider_head", props -> new ExtendedSkullItem(ModBlocks.SPIDER_HEAD.get(), ModBlocks.SPIDER_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> CAVE_SPIDER_HEAD = REGISTRY.registerItem("cave_spider_head", props -> new ExtendedSkullItem(ModBlocks.CAVE_SPIDER_HEAD.get(), ModBlocks.CAVE_SPIDER_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ZOMBIFIED_PIGLIN_HEAD = REGISTRY.registerItem("zombified_piglin_head", props -> new ExtendedSkullItem(ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(), ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> HUSK_HEAD = REGISTRY.registerItem("husk_head", props -> new ExtendedSkullItem(ModBlocks.HUSK_HEAD.get(), ModBlocks.HUSK_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> STRAY_SKULL = REGISTRY.registerItem("stray_skull", props -> new ExtendedSkullItem(ModBlocks.STRAY_SKULL.get(), ModBlocks.STRAY_WALL_SKULL.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> DROWNED_HEAD = REGISTRY.registerItem("drowned_head", props -> new ExtendedSkullItem(ModBlocks.DROWNED_HEAD.get(), ModBlocks.DROWNED_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ILLAGER_HEAD = REGISTRY.registerItem("illager_head", props -> new ExtendedSkullItem(ModBlocks.ILLAGER_HEAD.get(), ModBlocks.ILLAGER_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> WITCH_HEAD = REGISTRY.registerItem("witch_head", props -> new ExtendedSkullItem(ModBlocks.WITCH_HEAD.get(), ModBlocks.WITCH_WALL_HEAD.get(), props.rarity(Rarity.UNCOMMON), Direction.DOWN));

}
