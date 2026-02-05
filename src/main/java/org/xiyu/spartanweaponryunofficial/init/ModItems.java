package org.xiyu.spartanweaponryunofficial.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, ModSpartanWeaponry.ID);

    public static class WeaponItemsMelee {
        public final DeferredHolder<Item, SwordBaseItem> wood, stone, copper, iron, gold, diamond, netherite;
        public final DeferredHolder<Item, SwordBaseItem> tin, bronze, steel, silver, lead, nickel, invar, constantan, platinum, electrum, aluminum;

        public WeaponItemsMelee(DeferredRegister<Item> register, String weaponName, WeaponFunction<SwordBaseItem> factory) {
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            this.wood = register.register("wooden_" + weaponName, () -> factory.create(WeaponMaterial.WOOD, propVanilla));
            this.stone = register.register("stone_" + weaponName, () -> factory.create(WeaponMaterial.STONE, propVanilla));
            this.copper = register.register("copper_" + weaponName, () -> factory.create(WeaponMaterial.COPPER, propVanilla));
            this.iron = register.register("iron_" + weaponName, () -> factory.create(WeaponMaterial.IRON, propVanilla));
            this.gold = register.register("golden_" + weaponName, () -> factory.create(WeaponMaterial.GOLD, propVanilla));
            this.diamond = register.register("diamond_" + weaponName, () -> factory.create(WeaponMaterial.DIAMOND, propVanilla));
            this.netherite = register.register("netherite_" + weaponName, () -> factory.create(WeaponMaterial.NETHERITE, new Item.Properties().fireResistant()));

            this.tin = register.register("tin_" + weaponName, () -> factory.create(WeaponMaterial.TIN, propModded));
            this.bronze = register.register("bronze_" + weaponName, () -> factory.create(WeaponMaterial.BRONZE, propModded));
            this.steel = register.register("steel_" + weaponName, () -> factory.create(WeaponMaterial.STEEL, propModded));
            this.silver = register.register("silver_" + weaponName, () -> factory.create(WeaponMaterial.SILVER, propModded));
            this.electrum = register.register("electrum_" + weaponName, () -> factory.create(WeaponMaterial.ELECTRUM, propModded));
            this.lead = register.register("lead_" + weaponName, () -> factory.create(WeaponMaterial.LEAD, propModded));
            this.nickel = register.register("nickel_" + weaponName, () -> factory.create(WeaponMaterial.NICKEL, propModded));
            this.invar = register.register("invar_" + weaponName, () -> factory.create(WeaponMaterial.INVAR, propModded));
            this.constantan = register.register("constantan_" + weaponName, () -> factory.create(WeaponMaterial.CONSTANTAN, propModded));
            this.platinum = register.register("platinum_" + weaponName, () -> factory.create(WeaponMaterial.PLATINUM, propModded));
            this.aluminum = register.register("aluminum_" + weaponName, () -> factory.create(WeaponMaterial.ALUMINUM, propModded));
        }
		
/*		public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed)
		{
			getAsList().forEach((weapon) -> weapon.setAttackDamageAndSpeed(baseDamage, damageMultiplier, speed));
		}*/

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

        public WeaponItemsRanged(DeferredRegister<Item> register, String weaponName, WeaponFunction<? extends Item> factory) {
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            this.wood = register.register("wooden_" + weaponName, () -> factory.create(WeaponMaterial.WOOD, propVanilla));
            this.leather = register.register("leather_" + weaponName, () -> factory.create(WeaponMaterial.LEATHER, propVanilla));
            this.copper = register.register("copper_" + weaponName, () -> factory.create(WeaponMaterial.COPPER, propVanilla));
            this.iron = register.register("iron_" + weaponName, () -> factory.create(WeaponMaterial.IRON, propVanilla));
            this.gold = register.register("golden_" + weaponName, () -> factory.create(WeaponMaterial.GOLD, propVanilla));
            this.diamond = register.register("diamond_" + weaponName, () -> factory.create(WeaponMaterial.DIAMOND, propVanilla));
            this.netherite = register.register("netherite_" + weaponName, () -> factory.create(WeaponMaterial.NETHERITE, new Item.Properties().fireResistant()));

            this.tin = register.register("tin_" + weaponName, () -> factory.create(WeaponMaterial.TIN, propModded));
            this.bronze = register.register("bronze_" + weaponName, () -> factory.create(WeaponMaterial.BRONZE, propModded));
            this.steel = register.register("steel_" + weaponName, () -> factory.create(WeaponMaterial.STEEL, propModded));
            this.silver = register.register("silver_" + weaponName, () -> factory.create(WeaponMaterial.SILVER, propModded));
            this.electrum = register.register("electrum_" + weaponName, () -> factory.create(WeaponMaterial.ELECTRUM, propModded));
            this.lead = register.register("lead_" + weaponName, () -> factory.create(WeaponMaterial.LEAD, propModded));
            this.nickel = register.register("nickel_" + weaponName, () -> factory.create(WeaponMaterial.NICKEL, propModded));
            this.invar = register.register("invar_" + weaponName, () -> factory.create(WeaponMaterial.INVAR, propModded));
            this.constantan = register.register("constantan_" + weaponName, () -> factory.create(WeaponMaterial.CONSTANTAN, propModded));
            this.platinum = register.register("platinum_" + weaponName, () -> factory.create(WeaponMaterial.PLATINUM, propModded));
            this.aluminum = register.register("aluminum_" + weaponName, () -> factory.create(WeaponMaterial.ALUMINUM, propModded));
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

        public WeaponItemsThrowing(DeferredRegister<Item> register, String weaponName, WeaponFunction<ThrowingWeaponItem> factory) {
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            this.wood = register.register("wooden_" + weaponName, () -> factory.create(WeaponMaterial.WOOD, propVanilla));
            this.stone = register.register("stone_" + weaponName, () -> factory.create(WeaponMaterial.STONE, propVanilla));
            this.copper = register.register("copper_" + weaponName, () -> factory.create(WeaponMaterial.COPPER, propVanilla));
            this.iron = register.register("iron_" + weaponName, () -> factory.create(WeaponMaterial.IRON, propVanilla));
            this.gold = register.register("golden_" + weaponName, () -> factory.create(WeaponMaterial.GOLD, propVanilla));
            this.diamond = register.register("diamond_" + weaponName, () -> factory.create(WeaponMaterial.DIAMOND, propVanilla));
            this.netherite = register.register("netherite_" + weaponName, () -> factory.create(WeaponMaterial.NETHERITE, new Item.Properties().fireResistant()));

            this.tin = register.register("tin_" + weaponName, () -> factory.create(WeaponMaterial.TIN, propModded));
            this.bronze = register.register("bronze_" + weaponName, () -> factory.create(WeaponMaterial.BRONZE, propModded));
            this.steel = register.register("steel_" + weaponName, () -> factory.create(WeaponMaterial.STEEL, propModded));
            this.silver = register.register("silver_" + weaponName, () -> factory.create(WeaponMaterial.SILVER, propModded));
            this.electrum = register.register("electrum_" + weaponName, () -> factory.create(WeaponMaterial.ELECTRUM, propModded));
            this.lead = register.register("lead_" + weaponName, () -> factory.create(WeaponMaterial.LEAD, propModded));
            this.nickel = register.register("nickel_" + weaponName, () -> factory.create(WeaponMaterial.NICKEL, propModded));
            this.invar = register.register("invar_" + weaponName, () -> factory.create(WeaponMaterial.INVAR, propModded));
            this.constantan = register.register("constantan_" + weaponName, () -> factory.create(WeaponMaterial.CONSTANTAN, propModded));
            this.platinum = register.register("platinum_" + weaponName, () -> factory.create(WeaponMaterial.PLATINUM, propModded));
            this.aluminum = register.register("aluminum_" + weaponName, () -> factory.create(WeaponMaterial.ALUMINUM, propModded));
        }
		
/*		public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed, int chargeTicks)
		{
			getAsList().forEach((weapon) -> weapon.updateFromConfig(baseDamage, damageMultiplier, speed, chargeTicks));
		}*/

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
    public static final DeferredHolder<Item, Item> SIMPLE_HANDLE = REGISTRY.register("simple_handle", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HANDLE = REGISTRY.register("handle", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SIMPLE_POLE = REGISTRY.register("simple_pole", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> POLE = REGISTRY.register("pole", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXPLOSIVE_CHARGE = REGISTRY.register("explosive_charge", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GREASE_BALL = REGISTRY.register("grease_ball", () -> new BasicItem(new Item.Properties()));

    // Weapons
    public static final WeaponItemsMelee DAGGERS = new WeaponItemsMelee(REGISTRY, "dagger", WeaponFactory.DAGGER);
    public static final WeaponItemsMelee PARRYING_DAGGERS = new WeaponItemsMelee(REGISTRY, "parrying_dagger", WeaponFactory.PARRYING_DAGGER);
    public static final WeaponItemsMelee LONGSWORDS = new WeaponItemsMelee(REGISTRY, "longsword", WeaponFactory.LONGSWORD);
    public static final WeaponItemsMelee KATANAS = new WeaponItemsMelee(REGISTRY, "katana", WeaponFactory.KATANA);
    public static final WeaponItemsMelee SABERS = new WeaponItemsMelee(REGISTRY, "saber", WeaponFactory.SABER);
    public static final WeaponItemsMelee RAPIERS = new WeaponItemsMelee(REGISTRY, "rapier", WeaponFactory.RAPIER);
    public static final WeaponItemsMelee GREATSWORDS = new WeaponItemsMelee(REGISTRY, "greatsword", WeaponFactory.GREATSWORD);

    public static final DeferredHolder<Item, SwordBaseItem> WOODEN_CLUB = REGISTRY.register("wooden_club", () -> new SwordBaseItem(new Item.Properties(), WeaponMaterial.WOOD, WeaponArchetype.CLUB, Defaults.DamageBaseClub, Defaults.DamageMultiplierClub, Defaults.SpeedClub));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CLUB = REGISTRY.register("studded_club", () -> new SwordBaseItem(new Item.Properties(), WeaponMaterial.IRON, WeaponArchetype.CLUB, Defaults.DamageBaseClub, Defaults.DamageMultiplierClub, Defaults.SpeedClub));

    public static final DeferredHolder<Item, SwordBaseItem> CESTUS = REGISTRY.register("cestus", () -> new SwordBaseItem(new Item.Properties(), WeaponMaterial.LEATHER, WeaponArchetype.CESTUS, Defaults.DamageBaseCestus, Defaults.DamageMultiplierCestus, Defaults.SpeedCestus));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CESTUS = REGISTRY.register("studded_cestus", () -> new SwordBaseItem(new Item.Properties(), WeaponMaterial.IRON, WeaponArchetype.CESTUS, Defaults.DamageBaseCestus, Defaults.DamageMultiplierCestus, Defaults.SpeedCestus));

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
    public static final DeferredHolder<Item, ArrowBaseItem> WOODEN_ARROW = REGISTRY.register("wooden_arrow", () -> new ArrowBaseItem(Defaults.BaseDamageArrowWood, Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_WOODEN_ARROW = REGISTRY.register("tipped_wooden_arrow", () -> new ArrowBaseTippedItem("wooden_arrow", Defaults.BaseDamageArrowWood, Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> COPPER_ARROW = REGISTRY.register("copper_arrow", () -> new ArrowBaseItem(Defaults.BaseDamageArrowCopper, Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_COPPER_ARROW = REGISTRY.register("tipped_copper_arrow", () -> new ArrowBaseTippedItem("copper_arrow", Defaults.BaseDamageArrowCopper, Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> IRON_ARROW = REGISTRY.register("iron_arrow", () -> new ArrowBaseItem(Defaults.BaseDamageArrowIron, Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_IRON_ARROW = REGISTRY.register("tipped_iron_arrow", () -> new ArrowBaseTippedItem("iron_arrow", Defaults.BaseDamageArrowIron, Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> DIAMOND_ARROW = REGISTRY.register("diamond_arrow", () -> new ArrowBaseItem(Defaults.BaseDamageArrowDiamond, Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_DIAMOND_ARROW = REGISTRY.register("tipped_diamond_arrow", () -> new ArrowBaseTippedItem("diamond_arrow", Defaults.BaseDamageArrowDiamond, Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> NETHERITE_ARROW = REGISTRY.register("netherite_arrow", () -> new ArrowBaseItem(Defaults.BaseDamageArrowNetherite, Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_NETHERITE_ARROW = REGISTRY.register("tipped_netherite_arrow", () -> new ArrowBaseTippedItem("netherite_arrow", Defaults.BaseDamageArrowNetherite, Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, Item> EXPLOSIVE_ARROW = REGISTRY.register("explosive_arrow", () -> new ArrowExplosiveItem(Defaults.RangeMultiplierArrowExplosive));

    public static final DeferredHolder<Item, BoltItem> BOLT = REGISTRY.register("bolt", () -> new BoltItem(Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> TIPPED_BOLT = REGISTRY.register("tipped_bolt", () -> new BoltTippedItem("bolt", Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> SPECTRAL_BOLT = REGISTRY.register("spectral_bolt", () -> new BoltSpectralItem(Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> COPPER_BOLT = REGISTRY.register("copper_bolt", () -> new BoltItem(Defaults.BaseDamageBoltCopper, Defaults.RangeMultiplierBoltCopper, Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> TIPPED_COPPER_BOLT = REGISTRY.register("tipped_copper_bolt", () -> new BoltTippedItem("copper_bolt", Defaults.BaseDamageBoltCopper, Defaults.RangeMultiplierBoltCopper, Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> DIAMOND_BOLT = REGISTRY.register("diamond_bolt", () -> new BoltItem(Defaults.BaseDamageBoltDiamond, Defaults.RangeMultiplierBoltDiamond, Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> TIPPED_DIAMOND_BOLT = REGISTRY.register("tipped_diamond_bolt", () -> new BoltTippedItem("diamond_bolt", Defaults.BaseDamageBoltDiamond, Defaults.RangeMultiplierBoltDiamond, Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> NETHERITE_BOLT = REGISTRY.register("netherite_bolt", () -> new BoltItem(Defaults.BaseDamageBoltNetherite, Defaults.RangeMultiplierBoltNetherite, Defaults.ArmorPiercingFactorBoltNetherite));
    public static final DeferredHolder<Item, BoltItem> TIPPED_NETHERITE_BOLT = REGISTRY.register("tipped_netherite_bolt", () -> new BoltTippedItem("netherite_bolt", Defaults.BaseDamageBoltNetherite, Defaults.RangeMultiplierBoltNetherite, Defaults.ArmorPiercingFactorBoltNetherite));

    public static final DeferredHolder<Item, Item> SMALL_ARROW_QUIVER = REGISTRY.register("small_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_ARROW_QUIVER = REGISTRY.register("medium_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_ARROW_QUIVER = REGISTRY.register("large_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_ARROW_QUIVER = REGISTRY.register("huge_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverHuge));
    public static final DeferredHolder<Item, Item> SMALL_BOLT_QUIVER = REGISTRY.register("small_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_BOLT_QUIVER = REGISTRY.register("medium_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_BOLT_QUIVER = REGISTRY.register("large_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_BOLT_QUIVER = REGISTRY.register("huge_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverHuge));

    public static final DeferredHolder<Item, Item> QUIVER_COMPARTMENT = REGISTRY.register("quiver_compartment", QuiverSmithingTemplateItem::new);
    public static final DeferredHolder<Item, Item> MEDIUM_QUIVER_BRACE = REGISTRY.register("medium_quiver_brace", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LARGE_QUIVER_BRACE = REGISTRY.register("large_quiver_brace", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HUGE_QUIVER_BRACE = REGISTRY.register("huge_quiver_brace", () -> new BasicItem(new Item.Properties()));

/*	public static final RegistryObject<Item> MEDIUM_QUIVER_UPGRADE_KIT = REGISTRY.register("medium_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
	public static final RegistryObject<Item> LARGE_QUIVER_UPGRADE_KIT = REGISTRY.register("large_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
	public static final RegistryObject<Item> HUGE_QUIVER_UPGRADE_KIT = REGISTRY.register("huge_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));*/

    public static final DeferredHolder<Item, Item> DYNAMITE = REGISTRY.register("dynamite", () -> new DynamiteItem(new Item.Properties()));

    public static final DeferredHolder<Item, WeaponOilItem> WEAPON_OIL = REGISTRY.register("weapon_oil", WeaponOilItem::new);

    public static final DeferredHolder<Item, Item> BLAZE_HEAD = REGISTRY.register("blaze_head", () -> new ExtendedSkullItem(ModBlocks.BLAZE_HEAD.get(), ModBlocks.BLAZE_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ENDERMAN_HEAD = REGISTRY.register("enderman_head", () -> new ExtendedSkullItem(ModBlocks.ENDERMAN_HEAD.get(), ModBlocks.ENDERMAN_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> SPIDER_HEAD = REGISTRY.register("spider_head", () -> new ExtendedSkullItem(ModBlocks.SPIDER_HEAD.get(), ModBlocks.SPIDER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> CAVE_SPIDER_HEAD = REGISTRY.register("cave_spider_head", () -> new ExtendedSkullItem(ModBlocks.CAVE_SPIDER_HEAD.get(), ModBlocks.CAVE_SPIDER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ZOMBIFIED_PIGLIN_HEAD = REGISTRY.register("zombified_piglin_head", () -> new ExtendedSkullItem(ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(), ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> HUSK_HEAD = REGISTRY.register("husk_head", () -> new ExtendedSkullItem(ModBlocks.HUSK_HEAD.get(), ModBlocks.HUSK_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> STRAY_SKULL = REGISTRY.register("stray_skull", () -> new ExtendedSkullItem(ModBlocks.STRAY_SKULL.get(), ModBlocks.STRAY_WALL_SKULL.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> DROWNED_HEAD = REGISTRY.register("drowned_head", () -> new ExtendedSkullItem(ModBlocks.DROWNED_HEAD.get(), ModBlocks.DROWNED_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> ILLAGER_HEAD = REGISTRY.register("illager_head", () -> new ExtendedSkullItem(ModBlocks.ILLAGER_HEAD.get(), ModBlocks.ILLAGER_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
    public static final DeferredHolder<Item, Item> WITCH_HEAD = REGISTRY.register("witch_head", () -> new ExtendedSkullItem(ModBlocks.WITCH_HEAD.get(), ModBlocks.WITCH_WALL_HEAD.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));

}
