package org.xiyu.spartanweaponryunofficial.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.*;
import org.xiyu.spartanweaponryunofficial.api.crafting.condition.TypeDisabledCondition;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.merchant.villager.WeaponsmithTrades;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID)
public class Config {
    public static final Config INSTANCE;
    public static final ModConfigSpec CONFIG_SPEC;

    protected final Predicate<Object> IS_VALID_RESOURCE_LOCATION = (entry) -> Identifier.tryParse(entry.toString()) != null;

    static {
        final Pair<Config, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Config::new);
        INSTANCE = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    // Weapon categories
    public WeaponCategory daggers, parryingDaggers, longswords, katanas, sabers, rapiers, greatswords, clubs, cestus;
    public WeaponCategory battleHammers, warhammers, spears, halberds, pikes, lances;
    public RangedWeaponCategory longbows, heavyCrossbows;
    public ThrowingWeaponCategory throwingKnives, tomahawks, javelins, boomerangs;
    public WeaponCategory battleaxes, flangedMaces, glaives, quarterstaves;
    public WeaponCategory scythes;

    // Material categories
    public MaterialCategory copper, tin, bronze, steel, silver, electrum, lead, nickel, invar, constantan, platinum, aluminum;

    // Explosive settings
    public BooleanValue disableRecipesExplosives, disableTerrainDamage;
    public IntValue fuseTicksDynamite;
    public DoubleValue explosionStrengthDynamite;

    // Projectile settings
    public BooleanValue disableNewArrowRecipes, disableCopperAmmoRecipes, disableDiamondAmmoRecipes, disableNetheriteAmmoRecipes, disableQuiverRecipes;
    public ProjectileCategory arrowWood, arrowCopper, arrowIron, arrowDiamond, arrowNetherite;
    public DoubleValue arrowExplosiveExplosionStrength, arrowExplosiveRangeMultiplier;
    public BoltCategory bolt, boltCopper, boltDiamond, boltNetherite;
    public ConfigValue<List<? extends String>> quiverBowBlacklist;

    // Loot settings
    public BooleanValue addIronWeaponsToVillageWeaponsmith, addBowAndCrossbowLootToVillageFletcher, addDiamondWeaponsToEndCity,
            disableSpawningZombieWithWeapon, disableSpawningSkeletonWithLongbow,
            disableSpawningPiglinWithWeapon, disableSpawningWitherSkeletonWithWeapon;
    public DoubleValue zombieWithMeleeSpawnChanceNormal, zombieWithMeleeSpawnChanceHard,
            skeletonWithLongbowSpawnChanceNormal, skeletonWithLongbowSpawnChanceHard,
            piglinWithMeleeSpawnChanceNormal, piglinWithMeleeSpawnChanceHard,
            witherSkeletonWithMeleeSpawnChanceNormal, witherSkeletonWithMeleeSpawnChanceHard;
    public BooleanValue disableNewHeadDrops;

    // Trading settings
    public BooleanValue disableVillagerTrading;

    // Trait settings
    public DoubleValue damageBonusChestMultiplier, damageBonusHeadMultiplier, damageBonusRidingMultiplier, damageBonusRidingVelocityForMaxBonus,
            damageBonusThrowMultiplier, damageBonusThrowJavelinMultiplier,
            damageBonusUnarmoredMultiplier;
    public BooleanValue damageBonusCheckArmorValue;
    public DoubleValue damageBonusMaxArmorValue,
            damageBonusUndeadMultiplier, damageBonusBackstabMultiplier,
            damageAbsorptionFactor, reach1Value, reach2Value,
            sweep2Percentage, sweep3Percentage, armorPiercePercentage,
            decapitateSkullDropPercentage;
    public IntValue quickStrikeHurtResistTicks;

    // Oil settings
    public BooleanValue disableOilRecipes;
    public IntValue oilUsesNormal;
    public IntValue oilUsesLong;
    public DoubleValue oilDamageModifierNormal;
    public DoubleValue oilDamageModifierStrong;
    public DoubleValue potionOilDurationModifier;
    public ConfigValue<List<? extends String>> potionOilBlacklist;
    public ConfigValue<List<? extends String>> potionOilWhitelist;

    // JEI settings
    public BooleanValue forceShowDisabledItems;

    private Config(ModConfigSpec.Builder builder) {
        this.daggers = new WeaponCategory(builder, "dagger", "Daggers", Defaults.SpeedDagger, Defaults.DamageBaseDagger, Defaults.DamageMultiplierDagger, TypeDisabledCondition.DAGGER);
        this.parryingDaggers = new WeaponCategory(builder, "parrying_dagger", "Parrying Daggers", Defaults.SpeedParryingDagger, Defaults.DamageBaseParryingDagger, Defaults.DamageMultiplierParryingDagger, TypeDisabledCondition.PARRYING_DAGGER);
        this.longswords = new WeaponCategory(builder, "longsword", "Longswords", Defaults.SpeedLongsword, Defaults.DamageBaseLongsword, Defaults.DamageMultiplierLongsword, TypeDisabledCondition.LONGSWORD);
        this.katanas = new WeaponCategory(builder, "katana", "Katanas", Defaults.SpeedKatana, Defaults.DamageBaseKatana, Defaults.DamageMultiplierKatana, TypeDisabledCondition.KATANA);
        this.sabers = new WeaponCategory(builder, "saber", "Sabers", Defaults.SpeedSaber, Defaults.DamageBaseSaber, Defaults.DamageMultiplierSaber, TypeDisabledCondition.SABER);
        this.rapiers = new WeaponCategory(builder, "rapier", "Rapiers", Defaults.SpeedRapier, Defaults.DamageBaseRapier, Defaults.DamageMultiplierRapier, TypeDisabledCondition.RAPIER);
        this.greatswords = new WeaponCategory(builder, "greatsword", "Greatswords", Defaults.SpeedGreatsword, Defaults.DamageBaseGreatsword, Defaults.DamageMultiplierGreatsword, TypeDisabledCondition.GREATSWORD);
        this.clubs = new WeaponCategory(builder, "club", "Clubs", Defaults.SpeedClub, Defaults.DamageBaseClub, Defaults.DamageMultiplierClub, TypeDisabledCondition.CLUB);
        this.cestus = new WeaponCategory(builder, "cestus", "Cestusae", Defaults.SpeedCestus, Defaults.DamageBaseCestus, Defaults.DamageMultiplierCestus, TypeDisabledCondition.CESTUS);
        this.battleHammers = new WeaponCategory(builder, "battle_hammer", "Battle Hammers", Defaults.SpeedBattleHammer, Defaults.DamageBaseBattleHammer, Defaults.DamageMultiplierBattleHammer, TypeDisabledCondition.BATTLE_HAMMER);
        this.warhammers = new WeaponCategory(builder, "warhammer", "Warhammers", Defaults.SpeedWarhammer, Defaults.DamageBaseWarhammer, Defaults.DamageMultiplierWarhammer, TypeDisabledCondition.WARHAMMER);
        this.spears = new WeaponCategory(builder, "spear", "Spears", Defaults.SpeedSpear, Defaults.DamageBaseSpear, Defaults.DamageMultiplierSpear, TypeDisabledCondition.SPEAR);
        this.halberds = new WeaponCategory(builder, "halberd", "Halberds", Defaults.SpeedHalberd, Defaults.DamageBaseHalberd, Defaults.DamageMultiplierHalberd, TypeDisabledCondition.HALBERD);
        this.pikes = new WeaponCategory(builder, "pike", "Pikes", Defaults.SpeedPike, Defaults.DamageBasePike, Defaults.DamageMultiplierPike, TypeDisabledCondition.PIKE);
        this.lances = new WeaponCategory(builder, "lance", "Lances", Defaults.SpeedLance, Defaults.DamageBaseLance, Defaults.DamageMultiplierLance, TypeDisabledCondition.LANCE);
        this.longbows = new RangedWeaponCategory(builder, "longbow", "Longbows", TypeDisabledCondition.LONGBOW);
        this.heavyCrossbows = new RangedWeaponCategory(builder, "heavy_crossbow", "Heavy Crossbows", TypeDisabledCondition.HEAVY_CROSSBOW);
        this.throwingKnives = new ThrowingWeaponCategory(builder, "throwing_knife", "Throwing Knives", Defaults.MeleeSpeedThrowingKnife, Defaults.DamageBaseThrowingKnife, Defaults.DamageMultiplierThrowingKnife, Defaults.ChargeTicksThrowingKnife, TypeDisabledCondition.THROWING_KNIFE);
        this.tomahawks = new ThrowingWeaponCategory(builder, "tomahawk", "Tomahawks", Defaults.MeleeSpeedTomahawk, Defaults.DamageBaseTomahawk, Defaults.DamageMultiplierTomahawk, Defaults.ChargeTicksTomahawk, TypeDisabledCondition.TOMAHAWK);
        this.javelins = new ThrowingWeaponCategory(builder, "javelin", "Javelins", Defaults.MeleeSpeedJavelin, Defaults.DamageBaseJavelin, Defaults.DamageMultiplierJavelin, Defaults.ChargeTicksJavelin, TypeDisabledCondition.JAVELIN);
        this.boomerangs = new ThrowingWeaponCategory(builder, "boomerang", "Boomerangs", Defaults.MeleeSpeedBoomerang, Defaults.DamageBaseBoomerang, Defaults.DamageMultiplierBoomerang, Defaults.ChargeTicksBoomerang, TypeDisabledCondition.BOOMERANG);
        this.battleaxes = new WeaponCategory(builder, "battleaxe", "Battleaxes", Defaults.SpeedBattleaxe, Defaults.DamageBaseBattleaxe, Defaults.DamageMultiplierBattleaxe, TypeDisabledCondition.BATTLEAXE);
        this.flangedMaces = new WeaponCategory(builder, "flanged_mace", "Flanged Maces", Defaults.SpeedFlangedMace, Defaults.DamageBaseFlangedMace, Defaults.DamageMultiplierFlangedMace, TypeDisabledCondition.FLANGED_MACE);
        this.glaives = new WeaponCategory(builder, "glaive", "Glaives", Defaults.SpeedGlaive, Defaults.DamageBaseGlaive, Defaults.DamageMultiplierGlaive, TypeDisabledCondition.GLAIVE);
        this.quarterstaves = new WeaponCategory(builder, "quarterstaff", "Quarterstaves", Defaults.SpeedQuarterstaff, Defaults.DamageBaseQuarterstaff, Defaults.DamageMultiplierQuarterstaff, TypeDisabledCondition.QUARTERSTAFF);
        this.scythes = new WeaponCategory(builder, "scythes", "Scythes", Defaults.SpeedScythe, Defaults.DamageBaseScythe, Defaults.DamageMultiplierScythe, TypeDisabledCondition.SCYTHE);

        this.copper = new MaterialCategory(builder, "copper", APIConstants.DefaultMaterialDamageCopper, APIConstants.DefaultMaterialDurabilityCopper, TypeDisabledCondition.COPPER);
        this.tin = new MaterialCategory(builder, "tin", APIConstants.DefaultMaterialDamageTin, APIConstants.DefaultMaterialDurabilityTin, TypeDisabledCondition.TIN);
        this.bronze = new MaterialCategory(builder, "bronze", APIConstants.DefaultMaterialDamageBronze, APIConstants.DefaultMaterialDurabilityBronze, TypeDisabledCondition.BRONZE);
        this.steel = new MaterialCategory(builder, "steel", APIConstants.DefaultMaterialDamageSteel, APIConstants.DefaultMaterialDurabilitySteel, TypeDisabledCondition.STEEL);
        this.silver = new MaterialCategory(builder, "silver", APIConstants.DefaultMaterialDamageSilver, APIConstants.DefaultMaterialDurabilitySilver, TypeDisabledCondition.SILVER);
        this.electrum = new MaterialCategory(builder, "electrum", APIConstants.DefaultMaterialDamageElectrum, APIConstants.DefaultMaterialDurabilityElectrum, TypeDisabledCondition.ELECTRUM);
        this.lead = new MaterialCategory(builder, "lead", APIConstants.DefaultMaterialDamageLead, APIConstants.DefaultMaterialDurabilityLead, TypeDisabledCondition.LEAD);
        this.nickel = new MaterialCategory(builder, "nickel", APIConstants.DefaultMaterialDamageNickel, APIConstants.DefaultMaterialDurabilityNickel, TypeDisabledCondition.NICKEL);
        this.invar = new MaterialCategory(builder, "invar", APIConstants.DefaultMaterialDamageInvar, APIConstants.DefaultMaterialDurabilityInvar, TypeDisabledCondition.INVAR);
        this.constantan = new MaterialCategory(builder, "constantan", APIConstants.DefaultMaterialDamageConstantan, APIConstants.DefaultMaterialDurabilityConstantan, TypeDisabledCondition.CONSTANTAN);
        this.platinum = new MaterialCategory(builder, "platinum", APIConstants.DefaultMaterialDamagePlatinum, APIConstants.DefaultMaterialDurabilityPlatinum, TypeDisabledCondition.PLATINUM);
        this.aluminum = new MaterialCategory(builder, "aluminum", APIConstants.DefaultMaterialDamageAluminum, APIConstants.DefaultMaterialDurabilityAluminum, TypeDisabledCondition.ALUMINUM);

        builder.push("explosives");
        this.disableRecipesExplosives = builder.comment("Disables all recipes for explosive related items|禁用所有爆炸物相关物品的配方")
                .translation("config." + ModSpartanWeaponry.ID + ".explosive.disable_recipe")
                .worldRestart()
                .define("disable_recipe", false);
        this.disableTerrainDamage = builder.comment("Disables terrain damage for explosives in this mod such as Dynamite and Explosive Arrows. Is overridden by the 'mobGriefing' gamerule.|禁用本模组爆炸物（如炸药、爆炸箭）对地形的破坏。会被 'mobGriefing' 游戏规则覆盖。")
                .translation("config." + ModSpartanWeaponry.ID + ".explosive.disable_terrain_damage")
                .define("disable_terrain_damage", false);
        this.fuseTicksDynamite = builder.comment("Time (in ticks) it takes for Dynamite to explode|炸药引爆所需时间（tick）")
                .translation("config." + ModSpartanWeaponry.ID + ".explosive.fuse_ticks_dynamite")
                .defineInRange("fuse_ticks_dynamite", Defaults.FuseTicksDynamite, 20, 600);
        this.explosionStrengthDynamite = builder.comment("Explosion strength for Dynamite|炸药的爆炸强度")
                .translation("config." + ModSpartanWeaponry.ID + ".explosive.explosion_strength_dynamite")
                .defineInRange("explosion_strength_dynamite", Defaults.ExplosionStrengthDynamite, 0.1f, 10.0f);
        builder.pop();

        builder.push("projectiles");
        this.disableNewArrowRecipes = builder.comment("Disables Recipes for all new Arrows.|禁用所有新增箭矢的配方。")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.disable_new_arrow_recipes")
                .worldRestart()
                .define("disable_new_arrow_recipes", false);
        this.disableCopperAmmoRecipes = builder.comment("Disables Recipes for both Copper Arrows and Copper Bolts.|禁用铜箭与铜弩箭的配方。")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.disable_copper_ammo_recipes")
                .worldRestart()
                .define("disable_copper_ammo_recipes", false);
        this.disableDiamondAmmoRecipes = builder.comment("Disables Recipes for both Diamond Arrows and Diamond Bolts.|禁用钻石箭与钻石弩箭的配方。")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.disable_diamond_ammo_recipes")
                .worldRestart()
                .define("disable_diamond_ammo_recipes", false);
        this.disableNetheriteAmmoRecipes = builder.comment("Disables Recipes for both Netherite Arrows and Netherite Bolts.|禁用下界合金箭与下界合金弩箭的配方。")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.disable_netherite_ammo_recipes")
                .worldRestart()
                .define("disable_netherite_ammo_recipes", false);
        this.disableQuiverRecipes = builder.comment("Disables all variants of the Arrow Quiver and the Bolt Quiver in this mod|禁用本模组所有箭袋与弩箭袋的配方。")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.disable_quiver_recipes")
                .worldRestart()
                .define("disable_quiver_recipes", false);

        this.arrowWood = new ProjectileCategory(builder, "wood", "arrow", Defaults.BaseDamageArrowWood, Defaults.RangeMultiplierArrowWood);
        this.arrowCopper = new ProjectileCategory(builder, "copper", "arrow", Defaults.BaseDamageArrowCopper, Defaults.RangeMultiplierArrowCopper);
        this.arrowIron = new ProjectileCategory(builder, "iron", "arrow", Defaults.BaseDamageArrowIron, Defaults.RangeMultiplierArrowIron);
        this.arrowDiamond = new ProjectileCategory(builder, "diamond", "arrow", Defaults.BaseDamageArrowDiamond, Defaults.RangeMultiplierArrowDiamond);
        this.arrowNetherite = new ProjectileCategory(builder, "netherite", "arrow", Defaults.BaseDamageArrowNetherite, Defaults.RangeMultiplierArrowNetherite);
        this.bolt = new BoltCategory(builder, "", "bolt", Defaults.BaseDamageBolt, Defaults.RangeMultiplierBolt, Defaults.ArmorPiercingFactorBolt);
        this.boltCopper = new BoltCategory(builder, "copper", "bolt", Defaults.BaseDamageBoltCopper, Defaults.RangeMultiplierBoltCopper, Defaults.ArmorPiercingFactorBoltCopper);
        this.boltDiamond = new BoltCategory(builder, "diamond", "bolt", Defaults.BaseDamageBoltDiamond, Defaults.RangeMultiplierBoltDiamond, Defaults.ArmorPiercingFactorBoltDiamond);
        this.boltNetherite = new BoltCategory(builder, "netherite", "bolt", Defaults.BaseDamageBoltNetherite, Defaults.RangeMultiplierBoltNetherite, Defaults.ArmorPiercingFactorBoltNetherite);

        builder.push("explosive");
        this.arrowExplosiveExplosionStrength = builder.comment("Base damage for explosive arrows|爆炸箭基础伤害")
                .translation("config." + ModSpartanWeaponry.ID + ".arrow.explosion_strength")
                .defineInRange("base_damage", Defaults.ExplosionStrengthArrowExplosive, 0.1d, 10.0d);
        this.arrowExplosiveRangeMultiplier = builder.comment("Range muliplier for explosive arrows|爆炸箭射程倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".arrow.range_multiplier")
                .defineInRange("range_multiplier", Defaults.RangeMultiplierArrowExplosive, 0.1d, 100.0d);
        builder.pop();
        this.quiverBowBlacklist = builder.comment("Bows in this blacklist will not get Arrows pulled out of the Arrow Quiver. Use the registry ID of the bow to add to this. e.g. \"minecraft:bow\"|黑名单中的弓不会从箭袋中取箭。使用弓的注册 ID 添加，例如 \"minecraft:bow\"")
                .translation("config." + ModSpartanWeaponry.ID + ".projectile.quiver_bow_blacklist")
//								.<String>defineList("quiver_bow_blacklist", Defaults.QuiverArrowBlacklist, /*(value) -> ForgeRegistries.ITEMS.containsKey(Identifier.tryBuild((String)value))*/ (value) -> value.getClass() == String.class);
                .defineListAllowEmpty(List.of("quiver_bow_blacklist"), () -> Defaults.QuiverArrowBlacklist, this.IS_VALID_RESOURCE_LOCATION);
        builder.pop();

        builder.push("loot");
        this.addIronWeaponsToVillageWeaponsmith = builder.comment("Set to false to disable spawning Iron Weapons in Village Weaponsmith chests via loot table injection|设为 false 禁用通过战利品表注入在村庄铁匠箱中生成铁制武器")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.add_iron_weapons_to_village_blacksmith")
                .worldRestart()
                .define("add_iron_weapons_to_village_blacksmith", true);
        this.addBowAndCrossbowLootToVillageFletcher = builder.comment("Set to false to disable spawning Longbow and Heavy Crossbow-related loot in Village Fletcher chests via loot table injection|设为 false 禁用通过战利品表注入在村庄制箭师箱中生成长弓/重弩相关战利品")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.add_bow_and_crossbow_loot_to_village_fletcher")
                .worldRestart()
                .define("add_bow_and_crossbow_loot_to_village_fletcher", true);
        this.addDiamondWeaponsToEndCity = builder.comment("Set to false to disable spawning Diamond Weapons in End City chests via loot table injection|设为 false 禁用通过战利品表注入在末地城箱中生成钻石武器")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.add_diamond_weapons_to_end_city")
                .worldRestart()
                .define("add_diamond_weapons_to_end_city", true);
        this.zombieWithMeleeSpawnChanceNormal = builder.comment("Chance for Zombies to spawn with Iron Melee Weapons on all difficulties apart from Hard and Hardcore|僵尸在除困难/极限外难度生成时手持铁制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.zombie_with_melee_spawn_chance_normal")
                .defineInRange("zombie_with_melee_spawn_chance_normal", Defaults.zombieWithMeleeSpawnChanceNormal, 0.0, 1.0);
        this.zombieWithMeleeSpawnChanceHard = builder.comment("Chance for Zombies to spawn with Iron Melee Weapons on Hard or Hardcore difficulty|僵尸在困难/极限难度生成时手持铁制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.zombie_with_melee_spawn_chance_hard")
                .defineInRange("zombie_with_melee_spawn_chance_hard", Defaults.zombieWithMeleeSpawnChanceHard, 0.0, 1.0);
        this.disableSpawningZombieWithWeapon = builder.comment("Set to true to disable spawning a Zombie with any weapons from this mod|设为 true 禁用僵尸手持本模组武器生成")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.disable_spawning_zombie_with_weapon")
                .define("disable_spawning_zombie_with_weapon", false);
        this.skeletonWithLongbowSpawnChanceNormal = builder.comment("Chance for Skeletons to spawn with various Longbows on all difficulties apart from Hard and Hardcore|骷髅在除困难/极限外难度生成时手持各类长弓的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.skeleton_with_longbow_spawn_chance_normal")
                .defineInRange("skeleton_with_longbow_spawn_chance_normal", Defaults.skeletonWithLongbowSpawnChanceNormal, 0.0, 1.0);
        this.skeletonWithLongbowSpawnChanceHard = builder.comment("Chance for Skeletons to spawn with various Longbows on Hard or Hardcore difficulty|骷髅在困难/极限难度生成时手持各类长弓的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.skeleton_with_longbow_spawn_chance_hard")
                .defineInRange("skeleton_with_longbow_spawn_chance_hard", Defaults.skeletonWithLongbowSpawnChanceHard, 0.0, 1.0);
        this.disableSpawningSkeletonWithLongbow = builder.comment("Set to true to disable spawning a Skeleton with any Longbow from this mod|设为 true 禁用骷髅手持本模组长弓生成")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.disable_spawning_skeleton_with_longbow")
                .define("disable_spawning_skeleton_with_longbow", false);
        this.piglinWithMeleeSpawnChanceNormal = builder.comment("Chance for Piglins and Piglin Brutes to spawn with Golden Melee Weapons on all difficulties apart from Hard and Hardcore|猪灵与猪灵蛮兵在除困难/极限外难度生成时手持金制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.piglin_with_melee_spawn_chance_normal")
                .defineInRange("piglin_with_melee_spawn_chance_normal", Defaults.piglinWithMeleeSpawnChanceNormal, 0.0, 1.0);
        this.piglinWithMeleeSpawnChanceHard = builder.comment("Chance for Piglins and Piglin Brutes to spawn with Golden Melee Weapons on Hard or Hardcore difficulty|猪灵与猪灵蛮兵在困难/极限难度生成时手持金制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.piglin_with_melee_spawn_chance_hard")
                .defineInRange("piglin_with_melee_spawn_chance_hard", Defaults.piglinWithMeleeSpawnChanceHard, 0.0, 1.0);
        this.disableSpawningPiglinWithWeapon = builder.comment("Set to true to disable spawning a Piglin or Piglin Brute with any weapons from this mod|设为 true 禁用猪灵/猪灵蛮兵手持本模组武器生成")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.disable_spawning_piglin_with_weapon")
                .define("disable_spawning_piglin_with_weapon", false);
        this.witherSkeletonWithMeleeSpawnChanceNormal = builder.comment("Chance for Wither Skeletons to spawn with Stone Melee Weapons on all difficulties apart from Hard and Hardcore|凋灵骷髅在除困难/极限外难度生成时手持石制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.wither_skeleton_with_melee_spawn_chance_normal")
                .defineInRange("wither_skeleton_with_melee_spawn_chance_normal", Defaults.witherSkeletonWithMeleeSpawnChanceNormal, 0.0, 1.0);
        this.witherSkeletonWithMeleeSpawnChanceHard = builder.comment("Chance for Wither Skeletons to spawn with Stone Melee Weapons on Hard or Hardcore difficulty|凋灵骷髅在困难/极限难度生成时手持石制近战武器的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.wither_skeleton_with_melee_spawn_chance_hard")
                .defineInRange("wither_skeleton_with_melee_spawn_chance_hard", Defaults.witherSkeletonWithMeleeSpawnChanceHard, 0.0, 1.0);
        this.disableSpawningWitherSkeletonWithWeapon = builder.comment("Set to true to disable spawning a Wither Skeleton with any weapons from this mod|设为 true 禁用凋灵骷髅手持本模组武器生成")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.disable_spawning_wither_skeleton_with_weapon")
                .define("disable_spawning_wither_skeleton_with_weapon", false);
        this.disableNewHeadDrops = builder.comment("Set to true to disable the new mob heads from being dropped from mobs using the Decapitate Weapon Trait from this mod.|设为 true 禁用拥有“斩首”特性的武器掉落新头颅")
                .translation("config." + ModSpartanWeaponry.ID + ".loot.disable_new_head_drops")
                .define("disable_new_head_drops", false);
        builder.pop();

        builder.push("trading");
        this.disableVillagerTrading = builder.comment("Set to true to disable Villagers (Weaponsmiths and Fletchers) from trading weapons from this mod|设为 true 禁用村民（武器匠、制箭师）交易本模组武器")
                .translation("config." + ModSpartanWeaponry.ID + ".trading.disabled")
                .define("disable", false);
        builder.pop();

        builder.push("traits");
        builder.push("damage_bonus");
        this.damageBonusChestMultiplier = builder.comment("Changes the \"Chest Damage Bonus\" Weapon Trait multiplier value|调整“胸部伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.chest_multiplier")
                .defineInRange("chest_multiplier", Defaults.DamageBonusChestMultiplier, 1.0, 50.0);
        this.damageBonusHeadMultiplier = builder.comment("Changes the \"Head Damage Bonus\" Weapon Trait multiplier value|调整“头部伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.head_multiplier")
                .defineInRange("head_multiplier", Defaults.DamageBonusHeadMultiplier, 1.0, 50.0);
        this.damageBonusRidingMultiplier = builder.comment("Changes the \"Riding Damage Bonus\" Weapon Trait multiplier value|调整“骑乘伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.riding_multiplier")
                .defineInRange("riding_multiplier", Defaults.DamageBonusRidingMultiplier, 1.0, 50.0);
			damageBonusRidingVelocityForMaxBonus = builder.comment("Velocity required for the \"Riding Damage Bonus\" Weapon Trait to award the max bonus|触发\"骑乘伤害加成\"特性最高加成所需的速度")
						.translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.riding_velocity_for_max_bonus")
						.defineInRange("riding_velocity_for_max_bonus", Defaults.DamageBonusRidingVelocityMax, 0.0, 10.0);
        this.damageBonusThrowMultiplier = builder.comment("Changes the \"Throwing Damage Bonus\" Weapon Trait multiplier value|调整“投掷伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.throw_multiplier")
                .defineInRange("throw_multiplier", Defaults.DamageBonusThrowMultiplier, 1.0, 50.0);
        this.damageBonusThrowJavelinMultiplier = builder.comment("Changes the \"Chest Damage Bonus\" Weapon Trait multiplier value|调整“标枪投掷伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.throw_javelin_multiplier")
                .defineInRange("throw_javelin_multiplier", Defaults.DamageBonusThrowJavelinMultiplier, 1.0, 50.0);
        this.damageBonusUnarmoredMultiplier = builder.comment("Changes the \"Unarmored Damage Bonus\" Weapon Trait multiplier value|调整“无甲伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.unarmored_multiplier")
                .defineInRange("unarmored_multiplier", Defaults.DamageBonusUnarmoredMultiplier, 1.0, 50.0);
        this.damageBonusCheckArmorValue = builder.comment("If set to true, any damage bonus that checks for armor will only apply if the hit mob has less than the total armor value threshold, while still checking for armor|设为 true 时，任何基于护甲判定的伤害加成仅在目标护甲值低于阈值时生效（仍会检查护甲）")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.check_armor_value")
                .define("check_armor_value", false);
        this.damageBonusMaxArmorValue = builder.comment("Max armor value allowed for any damage bonus that checks for armor to apply, without any armor equipped|允许触发护甲判定伤害加成的最大护甲值")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.max_armor_value")
                .defineInRange("max_armor_value", Defaults.DamageBonusMaxArmorValue, 1.0, 50.0);
        this.damageBonusUndeadMultiplier = builder.comment("Changes the \"Undead Damage Bonus\" Weapon Trait multiplier value|调整“亡灵伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.undead_multiplier")
                .defineInRange("undead_multiplier", Defaults.DamageBonusUndeadMultiplier, 1.0, 50.0);
        this.damageBonusBackstabMultiplier = builder.comment("Changes the \"Backstab Damage Bonus\" Weapon Trait multiplier value|调整“背刺伤害加成”特性的倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_bonus.backstab_multiplier")
                .defineInRange("backstab_multiplier", Defaults.DamageBonusBackstabMultiplier, 1.0, 50.0);
        builder.pop();
        builder.push("damage_absorption");
        this.damageAbsorptionFactor = builder.comment("Changes the percentage of damage absorbed by the \"Damage Absorption\" Weapon Trait|调整“伤害吸收”特性的吸收比例")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.damage_absorption_factor")
                .defineInRange("damage_absorption_factor", Defaults.DamageAbsorptionFactor, 0.0, 1.0);
        builder.pop();
        builder.push("reach");
        this.reach1Value = builder.comment("Changes the reach of any weapons with the \"Reach I\" Weapon Trait|调整“攻击距离 I”特性的攻击距离")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.reach1.value")
                .defineInRange("reach1_value", Defaults.Reach1Value, 5.0, 15.0);
        this.reach2Value = builder.comment("Changes the reach of any weapons with the \"Reach II\" Weapon Trait|调整“攻击距离 II”特性的攻击距离")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.reach2.value")
                .defineInRange("reach2_value", Defaults.Reach2Value, 5.0, 15.0);
        builder.pop();
        builder.push("sweep");
        this.sweep2Percentage = builder.comment("Changes the factor of damage inflicted to enemies when sweep attacked on weapons with the \"Sweep II\" Weapon Trait|调整拥有“横扫 II”特性的武器横扫时对敌人造成的伤害系数")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.sweep2.percentage")
                .defineInRange("sweep2_percentage", Defaults.Sweep2Percentage, 0.0, 1.0);
        this.sweep3Percentage = builder.comment("Changes the factor of damage inflicted to enemies when sweep attacked on weapons with the \"Sweep III\" Weapon Trait|调整拥有“横扫 III”特性的武器横扫时对敌人造成的伤害系数")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.sweep3.percentage")
                .defineInRange("sweep3_percentage", Defaults.Sweep3Percentage, 0.0, 1.0);
        builder.pop();
        builder.push("armor_pierce");
        this.armorPiercePercentage = builder.comment("Changes the percentage of damage that ignores armor on weapons with the \"Armor Piercing\" Weapon Trait|调整拥有“破甲”特性的武器无视护甲的伤害百分比")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.armor_pierce.percentage")
                .defineInRange("percentage", Defaults.ArmorPiercePercentage, 0.0, 100.0);
        builder.pop();
        builder.push("quick_strike");
        this.quickStrikeHurtResistTicks = builder.comment("Tweaks the hurt resistance ticks for weapons that use the \"Quick Strike\" Weapon Trait|调整拥有“快速打击”特性的武器造成的无敌帧时长（tick）")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.quick_strike.hurt_resistance_ticks")
                .defineInRange("hurt_resistance_ticks", Defaults.QuickStrikeHurtResistTicks, 10, 20);
        builder.pop();
        builder.push("decapitate");
        this.decapitateSkullDropPercentage = builder.comment("Tweaks the percentage of Skull drops from weapons with the \"Decapitate\" Weapon Trait|调整拥有“斩首”特性的武器掉落头颅的概率")
                .translation("config." + ModSpartanWeaponry.ID + ".traits.decapitate.skull_drop_percentage")
                .defineInRange("skull_drop_percentage", Defaults.DecapitateSkullDropPercentage, 0.0, 100.0);
        builder.pop();
        builder.pop();

        builder.push("oil");
        this.disableOilRecipes = builder.comment("Set to true to disable crafting recipes for oils|设为 true 禁用所有油配方")
                .translation("config." + ModSpartanWeaponry.ID + ".disable_oil_recipes")
                .worldRestart()
                .define("disable_oil_recipes", false);
        this.oilUsesNormal = builder.comment("Max uses for standard oils before the effect wears off|普通油的最大使用次数")
                .translation("config." + ModSpartanWeaponry.ID + ".oil_uses_normal")
                .worldRestart()
                .defineInRange("oil_uses_normal", Defaults.OIL_USES_NORMAL, 1, 1000);
        this.oilUsesLong = builder.comment("Max uses for sustained oils before the effect wears off|持续型油的最大使用次数")
                .translation("config." + ModSpartanWeaponry.ID + ".oil_uses_long")
                .worldRestart()
                .defineInRange("oil_uses_long", Defaults.OIL_USES_LONG, 1, 1000);
        this.oilDamageModifierNormal = builder.comment("Damage modifier that standard oils inflict|普通油的伤害倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".oil_damage_modifier_normal")
                .worldRestart()
                .defineInRange("oil_damage_modifier_normal", Defaults.OIL_DAMAGE_MODIFIER_NORMAL, 0.0001d, 1.0d);
        this.oilDamageModifierStrong = builder.comment("Damage modifier that potent oils inflict|强效油的伤害倍率")
                .translation("config." + ModSpartanWeaponry.ID + ".oil_damage_modifier_strong")
                .worldRestart()
                .defineInRange("oil_damage_modifier_strong", Defaults.OIL_DAMAGE_MODIFIER_STRONG, 0.0001d, 1.0d);
        this.potionOilDurationModifier = builder.comment("Duration modifier for potion oils, based on the original potion effects|药水油的持续时间倍率（基于原药水效果）")
                .translation("config." + ModSpartanWeaponry.ID + ".potion_oil_duration_modifier")
                .worldRestart()
                .defineInRange("potion_oil_duration_modifier", Defaults.OIL_POTION_DURATION_MODIFIER, 0.0001d, 1.0d);
        this.potionOilBlacklist = builder.comment("Blacklist for potions to prevent them to be made into oils. By default, only potions with negative effects can be made into oils. Adding already disabled potions to this blacklist will do nothing|药水黑名单，阻止被制作成油。默认仅负面药水可制成油；已被禁用的药水加入黑名单不会产生效果")
                .translation("config." + ModSpartanWeaponry.ID + ".potion_oil_blacklist")
                .worldRestart()
                .defineListAllowEmpty(List.of("potion_oil_blacklist"), ArrayList::new, this.IS_VALID_RESOURCE_LOCATION);
        this.potionOilWhitelist = builder.comment("Whitelist for potions to allow them to be made into oils. By default, only potions with negative effects can be made into oils. Adding already enabled potions to this whitelist will do nothing|药水白名单，允许被制作成油。默认仅负面药水可制成油；已被允许的药水加入白名单不会产生效果")
                .translation("config." + ModSpartanWeaponry.ID + ".potion_oil_whitelist")
                .worldRestart()
                .defineListAllowEmpty(List.of("potion_oil_whitelist"), ArrayList::new, this.IS_VALID_RESOURCE_LOCATION);
        builder.pop();

        builder.push("jei");
        this.forceShowDisabledItems = builder.comment("Set to true to forcibly show disabled items in JEI, even if they cannot be crafted. Should be useful for modpack makers defining their own recipes.|设为 true 在 JEI 中强制显示已禁用物品，即使无法合成。对整合包作者自定义配方有用。")
                .translation("config." + ModSpartanWeaponry.ID + ".jei.force_show_disabled_items")
                .worldRestart()
                .define("force_show_disabled_items", false);
        builder.pop();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent ev) {
        if (ev.getConfig().getSpec() != CONFIG_SPEC)
            return;

        Log.info("Updating config settings!");
        TypeDisabledCondition.disabledRecipeTypes.clear();

        updateMaterialValues(WeaponMaterial.COPPER, INSTANCE.copper);
        updateMaterialValues(WeaponMaterial.TIN, INSTANCE.tin);
        updateMaterialValues(WeaponMaterial.BRONZE, INSTANCE.bronze);
        updateMaterialValues(WeaponMaterial.STEEL, INSTANCE.steel);
        updateMaterialValues(WeaponMaterial.SILVER, INSTANCE.silver);
        updateMaterialValues(WeaponMaterial.ELECTRUM, INSTANCE.electrum);
        updateMaterialValues(WeaponMaterial.LEAD, INSTANCE.lead);
        updateMaterialValues(WeaponMaterial.NICKEL, INSTANCE.nickel);
        updateMaterialValues(WeaponMaterial.INVAR, INSTANCE.invar);
        updateMaterialValues(WeaponMaterial.CONSTANTAN, INSTANCE.constantan);
        updateMaterialValues(WeaponMaterial.PLATINUM, INSTANCE.platinum);
        updateMaterialValues(WeaponMaterial.ALUMINUM, INSTANCE.aluminum);

		ModItems.DAGGERS.updateSettingsFromConfig(INSTANCE.daggers.baseDamage.get().floatValue(), INSTANCE.daggers.damageMultipler.get().floatValue(), INSTANCE.daggers.speed.get().doubleValue());
        INSTANCE.daggers.updateDisabledRecipeList();
		ModItems.PARRYING_DAGGERS.updateSettingsFromConfig(INSTANCE.parryingDaggers.baseDamage.get().floatValue(), INSTANCE.parryingDaggers.damageMultipler.get().floatValue(), INSTANCE.parryingDaggers.speed.get().doubleValue());
        INSTANCE.parryingDaggers.updateDisabledRecipeList();
		ModItems.LONGSWORDS.updateSettingsFromConfig(INSTANCE.longswords.baseDamage.get().floatValue(), INSTANCE.longswords.damageMultipler.get().floatValue(), INSTANCE.longswords.speed.get().doubleValue());
        INSTANCE.longswords.updateDisabledRecipeList();
		ModItems.KATANAS.updateSettingsFromConfig(INSTANCE.katanas.baseDamage.get().floatValue(), INSTANCE.katanas.damageMultipler.get().floatValue(), INSTANCE.katanas.speed.get().doubleValue());
        INSTANCE.katanas.updateDisabledRecipeList();
		ModItems.SABERS.updateSettingsFromConfig(INSTANCE.sabers.baseDamage.get().floatValue(), INSTANCE.sabers.damageMultipler.get().floatValue(), INSTANCE.sabers.speed.get().doubleValue());
        INSTANCE.sabers.updateDisabledRecipeList();
		ModItems.RAPIERS.updateSettingsFromConfig(INSTANCE.rapiers.baseDamage.get().floatValue(), INSTANCE.rapiers.damageMultipler.get().floatValue(), INSTANCE.rapiers.speed.get().doubleValue());
        INSTANCE.rapiers.updateDisabledRecipeList();
		ModItems.GREATSWORDS.updateSettingsFromConfig(INSTANCE.greatswords.baseDamage.get().floatValue(), INSTANCE.greatswords.damageMultipler.get().floatValue(), INSTANCE.greatswords.speed.get().doubleValue());
        INSTANCE.greatswords.updateDisabledRecipeList();

		ImmutableList.of(ModItems.WOODEN_CLUB, ModItems.STUDDED_CLUB).forEach((club) -> club.get().setAttackDamageAndSpeed(INSTANCE.clubs.baseDamage.get().floatValue(), INSTANCE.clubs.damageMultipler.get().floatValue(), INSTANCE.clubs.speed.get().doubleValue()));
        INSTANCE.clubs.updateDisabledRecipeList();
		ImmutableList.of(ModItems.CESTUS, ModItems.STUDDED_CESTUS).forEach((club) -> club.get().setAttackDamageAndSpeed(INSTANCE.cestus.baseDamage.get().floatValue(), INSTANCE.cestus.damageMultipler.get().floatValue(), INSTANCE.cestus.speed.get().doubleValue()));
        INSTANCE.cestus.updateDisabledRecipeList();

		ModItems.BATTLE_HAMMERS.updateSettingsFromConfig(INSTANCE.battleHammers.baseDamage.get().floatValue(), INSTANCE.battleHammers.damageMultipler.get().floatValue(), INSTANCE.battleHammers.speed.get().doubleValue());
        INSTANCE.battleHammers.updateDisabledRecipeList();
		ModItems.WARHAMMERS.updateSettingsFromConfig(INSTANCE.warhammers.baseDamage.get().floatValue(), INSTANCE.warhammers.damageMultipler.get().floatValue(), INSTANCE.warhammers.speed.get().doubleValue());
        INSTANCE.warhammers.updateDisabledRecipeList();
		ModItems.SPEARS.updateSettingsFromConfig(INSTANCE.spears.baseDamage.get().floatValue(), INSTANCE.spears.damageMultipler.get().floatValue(), INSTANCE.spears.speed.get().doubleValue());
        INSTANCE.spears.updateDisabledRecipeList();
		ModItems.HALBERDS.updateSettingsFromConfig(INSTANCE.halberds.baseDamage.get().floatValue(), INSTANCE.halberds.damageMultipler.get().floatValue(), INSTANCE.halberds.speed.get().doubleValue());
        INSTANCE.halberds.updateDisabledRecipeList();
		ModItems.PIKES.updateSettingsFromConfig(INSTANCE.pikes.baseDamage.get().floatValue(), INSTANCE.pikes.damageMultipler.get().floatValue(), INSTANCE.pikes.speed.get().doubleValue());
        INSTANCE.pikes.updateDisabledRecipeList();
		ModItems.LANCES.updateSettingsFromConfig(INSTANCE.lances.baseDamage.get().floatValue(), INSTANCE.lances.damageMultipler.get().floatValue(), INSTANCE.lances.speed.get().doubleValue());
        INSTANCE.lances.updateDisabledRecipeList();

        // Updating configurable values for Longbows and Heavy Crossbows are not required
        INSTANCE.longbows.updateDisabledRecipeList();
        INSTANCE.heavyCrossbows.updateDisabledRecipeList();

		ModItems.THROWING_KNIVES.updateSettingsFromConfig(INSTANCE.throwingKnives.baseDamage.get().floatValue(), INSTANCE.throwingKnives.damageMultipler.get().floatValue(), INSTANCE.throwingKnives.speed.get().doubleValue(), INSTANCE.throwingKnives.chargeTicks.get());
        INSTANCE.throwingKnives.updateDisabledRecipeList();
		ModItems.TOMAHAWKS.updateSettingsFromConfig(INSTANCE.tomahawks.baseDamage.get().floatValue(), INSTANCE.tomahawks.damageMultipler.get().floatValue(), INSTANCE.tomahawks.speed.get().doubleValue(), INSTANCE.tomahawks.chargeTicks.get());
        INSTANCE.tomahawks.updateDisabledRecipeList();
		ModItems.JAVELINS.updateSettingsFromConfig(INSTANCE.javelins.baseDamage.get().floatValue(), INSTANCE.javelins.damageMultipler.get().floatValue(), INSTANCE.javelins.speed.get().doubleValue(), INSTANCE.javelins.chargeTicks.get());
        INSTANCE.javelins.updateDisabledRecipeList();

		ModItems.BOOMERANGS.updateSettingsFromConfig(INSTANCE.boomerangs.baseDamage.get().floatValue(), INSTANCE.boomerangs.damageMultipler.get().floatValue(), INSTANCE.boomerangs.speed.get().doubleValue(), INSTANCE.boomerangs.chargeTicks.get());
        INSTANCE.boomerangs.updateDisabledRecipeList();
		ModItems.BATTLEAXES.updateSettingsFromConfig(INSTANCE.battleaxes.baseDamage.get().floatValue(), INSTANCE.battleaxes.damageMultipler.get().floatValue(), INSTANCE.battleaxes.speed.get().doubleValue());
        INSTANCE.battleaxes.updateDisabledRecipeList();
		ModItems.FLANGED_MACES.updateSettingsFromConfig(INSTANCE.flangedMaces.baseDamage.get().floatValue(), INSTANCE.flangedMaces.damageMultipler.get().floatValue(), INSTANCE.flangedMaces.speed.get().doubleValue());
        INSTANCE.flangedMaces.updateDisabledRecipeList();

		ModItems.GLAIVES.updateSettingsFromConfig(INSTANCE.glaives.baseDamage.get().floatValue(), INSTANCE.glaives.damageMultipler.get().floatValue(), INSTANCE.glaives.speed.get().doubleValue());
        INSTANCE.glaives.updateDisabledRecipeList();
		ModItems.QUARTERSTAVES.updateSettingsFromConfig(INSTANCE.quarterstaves.baseDamage.get().floatValue(), INSTANCE.quarterstaves.damageMultipler.get().floatValue(), INSTANCE.quarterstaves.speed.get().doubleValue());
        INSTANCE.quarterstaves.updateDisabledRecipeList();

		ModItems.SCYTHES.updateSettingsFromConfig(INSTANCE.scythes.baseDamage.get().floatValue(), INSTANCE.scythes.damageMultipler.get().floatValue(), INSTANCE.scythes.speed.get().doubleValue());
        INSTANCE.scythes.updateDisabledRecipeList();

        updateDisabledRecipe(TypeDisabledCondition.ARROWS, INSTANCE.disableNewArrowRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.COPPER_AMMO, INSTANCE.disableCopperAmmoRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.DIAMOND_AMMO, INSTANCE.disableDiamondAmmoRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.NETHERITE_AMMO, INSTANCE.disableNetheriteAmmoRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.QUIVER, INSTANCE.disableQuiverRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.BOLTS, INSTANCE.heavyCrossbows.disableRecipes.get());
        updateDisabledRecipe(TypeDisabledCondition.OIL, INSTANCE.disableOilRecipes.get());

        ModItems.WOODEN_ARROW.get().updateFromConfig(INSTANCE.arrowWood.baseDamage.get().floatValue(), INSTANCE.arrowWood.rangeMultiplier.get().floatValue());
        ModItems.TIPPED_WOODEN_ARROW.get().updateFromConfig(INSTANCE.arrowWood.baseDamage.get().floatValue(), INSTANCE.arrowWood.rangeMultiplier.get().floatValue());
        ModItems.COPPER_ARROW.get().updateFromConfig(INSTANCE.arrowCopper.baseDamage.get().floatValue(), INSTANCE.arrowCopper.rangeMultiplier.get().floatValue());
        ModItems.TIPPED_COPPER_ARROW.get().updateFromConfig(INSTANCE.arrowCopper.baseDamage.get().floatValue(), INSTANCE.arrowCopper.rangeMultiplier.get().floatValue());
        ModItems.IRON_ARROW.get().updateFromConfig(INSTANCE.arrowIron.baseDamage.get().floatValue(), INSTANCE.arrowIron.rangeMultiplier.get().floatValue());
        ModItems.TIPPED_IRON_ARROW.get().updateFromConfig(INSTANCE.arrowIron.baseDamage.get().floatValue(), INSTANCE.arrowIron.rangeMultiplier.get().floatValue());
        ModItems.DIAMOND_ARROW.get().updateFromConfig(INSTANCE.arrowDiamond.baseDamage.get().floatValue(), INSTANCE.arrowDiamond.rangeMultiplier.get().floatValue());
        ModItems.TIPPED_DIAMOND_ARROW.get().updateFromConfig(INSTANCE.arrowDiamond.baseDamage.get().floatValue(), INSTANCE.arrowDiamond.rangeMultiplier.get().floatValue());
        ModItems.NETHERITE_ARROW.get().updateFromConfig(INSTANCE.arrowNetherite.baseDamage.get().floatValue(), INSTANCE.arrowNetherite.rangeMultiplier.get().floatValue());
        ModItems.TIPPED_NETHERITE_ARROW.get().updateFromConfig(INSTANCE.arrowNetherite.baseDamage.get().floatValue(), INSTANCE.arrowNetherite.rangeMultiplier.get().floatValue());
        ModItems.BOLT.get().updateFromConfig(INSTANCE.bolt.baseDamage.get().floatValue(), INSTANCE.bolt.rangeMultiplier.get().floatValue(), INSTANCE.bolt.armorPiercingFactor.get().floatValue());
        ModItems.TIPPED_BOLT.get().updateFromConfig(INSTANCE.bolt.baseDamage.get().floatValue(), INSTANCE.bolt.rangeMultiplier.get().floatValue(), INSTANCE.bolt.armorPiercingFactor.get().floatValue());
        ModItems.SPECTRAL_BOLT.get().updateFromConfig(INSTANCE.bolt.baseDamage.get().floatValue(), INSTANCE.bolt.rangeMultiplier.get().floatValue(), INSTANCE.bolt.armorPiercingFactor.get().floatValue());
        ModItems.COPPER_BOLT.get().updateFromConfig(INSTANCE.boltCopper.baseDamage.get().floatValue(), INSTANCE.boltCopper.rangeMultiplier.get().floatValue(), INSTANCE.boltCopper.armorPiercingFactor.get().floatValue());
        ModItems.TIPPED_COPPER_BOLT.get().updateFromConfig(INSTANCE.boltCopper.baseDamage.get().floatValue(), INSTANCE.boltCopper.rangeMultiplier.get().floatValue(), INSTANCE.boltCopper.armorPiercingFactor.get().floatValue());
        ModItems.DIAMOND_BOLT.get().updateFromConfig(INSTANCE.boltDiamond.baseDamage.get().floatValue(), INSTANCE.boltDiamond.rangeMultiplier.get().floatValue(), INSTANCE.boltDiamond.armorPiercingFactor.get().floatValue());
        ModItems.TIPPED_DIAMOND_BOLT.get().updateFromConfig(INSTANCE.boltDiamond.baseDamage.get().floatValue(), INSTANCE.boltDiamond.rangeMultiplier.get().floatValue(), INSTANCE.boltDiamond.armorPiercingFactor.get().floatValue());
        ModItems.NETHERITE_BOLT.get().updateFromConfig(INSTANCE.boltNetherite.baseDamage.get().floatValue(), INSTANCE.boltNetherite.rangeMultiplier.get().floatValue(), INSTANCE.boltNetherite.armorPiercingFactor.get().floatValue());
        ModItems.TIPPED_NETHERITE_BOLT.get().updateFromConfig(INSTANCE.boltNetherite.baseDamage.get().floatValue(), INSTANCE.boltNetherite.rangeMultiplier.get().floatValue(), INSTANCE.boltNetherite.armorPiercingFactor.get().floatValue());

        updateDisabledRecipe(TypeDisabledCondition.EXPLOSIVES, INSTANCE.disableRecipesExplosives.get());

        // Update Weapon Traits
        WeaponTraits.DAMAGE_BONUS_CHEST.get().setMagnitude(INSTANCE.damageBonusChestMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_HEAD.get().setMagnitude(INSTANCE.damageBonusHeadMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_RIDING.get().setMagnitude(INSTANCE.damageBonusRidingMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_THROWN_1.get().setMagnitude(INSTANCE.damageBonusThrowMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_THROWN_2.get().setMagnitude(INSTANCE.damageBonusThrowJavelinMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_UNARMORED.get().setMagnitude(INSTANCE.damageBonusUnarmoredMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_UNDEAD.get().setMagnitude(INSTANCE.damageBonusUndeadMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_BONUS_BACKSTAB.get().setMagnitude(INSTANCE.damageBonusBackstabMultiplier.get().floatValue());
        WeaponTraits.DAMAGE_ABSORB.get().setMagnitude(INSTANCE.damageAbsorptionFactor.get().floatValue());
        WeaponTraits.REACH_1.get().setMagnitude(INSTANCE.reach1Value.get().floatValue());
        WeaponTraits.REACH_2.get().setMagnitude(INSTANCE.reach2Value.get().floatValue());
        WeaponTraits.SWEEP_2.get().setMagnitude(INSTANCE.sweep2Percentage.get().floatValue());
        WeaponTraits.SWEEP_3.get().setMagnitude(INSTANCE.sweep3Percentage.get().floatValue());
        WeaponTraits.ARMOR_PIERCING.get().setMagnitude(INSTANCE.armorPiercePercentage.get().floatValue());
        WeaponTraits.QUICK_STRIKE.get().setMagnitude(INSTANCE.quickStrikeHurtResistTicks.get().floatValue());
        WeaponTraits.DECAPITATE.get().setMagnitude(INSTANCE.decapitateSkullDropPercentage.get().floatValue());

        // Update Oils
        for (var holder : OilEffects.REGISTRY.getEntries()) {
            OilEffect effect = holder.get();
            switch (effect.getType()) {
                case STANDARD:
                    effect.updateFromConfig(INSTANCE.oilUsesNormal.get(), INSTANCE.oilDamageModifierNormal.get().floatValue());
                    break;
                case SUSTAINED:
                    effect.updateFromConfig(INSTANCE.oilUsesLong.get(), INSTANCE.oilDamageModifierNormal.get().floatValue());
                    break;
                case POTENT:
                    effect.updateFromConfig(INSTANCE.oilUsesNormal.get(), INSTANCE.oilDamageModifierStrong.get().floatValue());
                    break;
                case EFFECT_ONLY:
                    effect.updateFromConfig(INSTANCE.oilUsesNormal.get(), 0.0f);
                default:
                    break;
            }
        }

        // Update values required API-side
        APIConfigValues.damageBonusCheckArmorValue = INSTANCE.damageBonusCheckArmorValue.get();
        APIConfigValues.damageBonusMaxArmorValue = INSTANCE.damageBonusMaxArmorValue.get().floatValue();
		APIConfigValues.damageBonusRidingVelocityForMaxBonus = INSTANCE.damageBonusRidingVelocityForMaxBonus.get().floatValue();

        WeaponsmithTrades.initTradeLists();

        // Debug crap
		/*Log.info("Disabled Recipes:");
		if(INSTANCE.disabledRecipeTypes.isEmpty())
			Log.info("- None!");
		for(String type : INSTANCE.disabledRecipeTypes)
		{
			Log.info("- " + type);
		}*/
    }

    public static void updateDisabledRecipe(String type, boolean disabled) {
        boolean containsValue = TypeDisabledCondition.disabledRecipeTypes.contains(type);
        if (!containsValue && disabled)
            TypeDisabledCondition.disabledRecipeTypes.add(type);
        else if (containsValue)
            TypeDisabledCondition.disabledRecipeTypes.remove(type);
    }

    private static void updateMaterialValues(WeaponMaterial material, MaterialCategory category) {
        material.setAttackDamage(category.damage.get().floatValue());
        material.setDurability(category.durability.get());
        category.updateDisabledRecipeList();
    }


    public static class WeaponCategory {
        public BooleanValue disableRecipes;
        public DoubleValue speed;
        public DoubleValue baseDamage;
        public DoubleValue damageMultipler;
        private final String typeDisabledName;

        protected WeaponCategory(ModConfigSpec.Builder builder, String weaponClass, String weaponPlural, float defaultSpeed, float defaultBaseDamage, float defaultDamageMuliplier, String typeDisabledNameIn) {
            builder.push(weaponClass);
            this.typeDisabledName = typeDisabledNameIn;
            this.disableRecipes = builder.comment("Disables all recipes for all " + weaponPlural + ".|禁用所有" + weaponPlural + "的配方。")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.disable")
                    .worldRestart()
                    .define("disable", false);
            this.speed = builder.comment("Attack speed of " + weaponPlural + ".|" + weaponPlural + "的攻击速度")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.speed")
                    .worldRestart()
                    .defineInRange("speed", defaultSpeed, 0.0d, 4.0d);
            this.baseDamage = builder.comment("Base Damage of " + weaponPlural + ".|" + weaponPlural + "的基础伤害")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.base_damage")
                    .worldRestart()
                    .defineInRange("base_damage", defaultBaseDamage, 0.1d, 100.0d);
            this.damageMultipler = builder.comment("Damage Multiplier for " + weaponPlural + ".|" + weaponPlural + "的伤害倍率")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.damage_multiplier")
                    .worldRestart()
                    .defineInRange("damage_multiplier", defaultDamageMuliplier, 0.1d, 10.0d);
            builder.pop();
        }

        public void updateDisabledRecipeList() {
            updateDisabledRecipe(this.typeDisabledName, this.disableRecipes.get());
        }
    }

    public static class RangedWeaponCategory {
        public BooleanValue disableRecipes;
        private final String typeDisabledName;

        protected RangedWeaponCategory(ModConfigSpec.Builder builder, String weaponClass, String weaponPlural, String typeDisabledNameIn) {
            builder.push(weaponClass);
            this.typeDisabledName = typeDisabledNameIn;
            this.disableRecipes = builder.comment("Disables all recipes for all " + weaponPlural + ".|禁用所有" + weaponPlural + "的配方。")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.disable")
                    .worldRestart()
                    .define("disable", false);
            builder.pop();
        }

        public void updateDisabledRecipeList() {
            updateDisabledRecipe(this.typeDisabledName, this.disableRecipes.get());
        }
    }

    public static class ThrowingWeaponCategory {
        public BooleanValue disableRecipes;
        public DoubleValue speed;
        public DoubleValue baseDamage;
        public DoubleValue damageMultipler;
        public IntValue chargeTicks;
        private final String typeDisabledName;

        protected ThrowingWeaponCategory(ModConfigSpec.Builder builder, String weaponClass, String weaponPlural, float defaultSpeed, float defaultBaseDamage, float defaultDamageMuliplier, int defaultChargeTicks, String typeDisabledNameIn) {
            builder.push(weaponClass);
            this.typeDisabledName = typeDisabledNameIn;
            this.disableRecipes = builder.comment("Disables all recipes for all " + weaponPlural + ".|禁用所有" + weaponPlural + "的配方。")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.disable")
                    .worldRestart()
                    .define("disable", false);
            this.speed = builder.comment("Attack speed of " + weaponPlural + ".|" + weaponPlural + "的攻击速度")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.speed")
                    .worldRestart()
                    .defineInRange("speed", defaultSpeed, 0.0d, 4.0d);
            this.baseDamage = builder.comment("Base Damage of " + weaponPlural + ".|" + weaponPlural + "的基础伤害")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.base_damage")
                    .worldRestart()
                    .defineInRange("base_damage", defaultBaseDamage, 0.1d, 100.0d);
            this.damageMultipler = builder.comment("Damage Multiplier for " + weaponPlural + ".|" + weaponPlural + "的伤害倍率")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.damage_multiplier")
                    .worldRestart()
                    .defineInRange("damage_multiplier", defaultDamageMuliplier, 0.1d, 10.0d);
            this.chargeTicks = builder.comment("Charge time in ticks for " + weaponPlural + ".|" + weaponPlural + "的蓄力时间（tick）")
                    .translation("config." + ModSpartanWeaponry.ID + ".weapon.charge_ticks")
                    .defineInRange("charge_ticks", defaultChargeTicks, 1, 1000);
            builder.pop();
        }

        public void updateDisabledRecipeList() {
            updateDisabledRecipe(this.typeDisabledName, this.disableRecipes.get());
        }
    }

    public static class MaterialCategory {
        public DoubleValue damage;
        public IntValue durability;
        public BooleanValue disableRecipes;
        private final String materialName;
        private final String typeDisabledName;

        private MaterialCategory(ModConfigSpec.Builder builder, String materialName, float damage, int durability, String typeDisabledName) {
            builder.push(materialName);
            this.materialName = materialName;
            this.typeDisabledName = typeDisabledName;
            this.damage = builder.comment("Base Damage for " + this.materialName + " weapons|" + this.materialName + "武器的基础伤害")
                    .translation("config." + ModSpartanWeaponry.ID + ".material.base_damage")
                    .defineInRange("base_damage", damage, 0.1d, 100.0d);
            this.durability = builder.comment("Durability for " + this.materialName + " weapons|" + this.materialName + "武器的耐久度")
                    .translation("config." + ModSpartanWeaponry.ID + ".material.durability")
                    .defineInRange("durability", durability, 1, 100000);
            this.disableRecipes = builder.comment("Set to true to disable " + this.materialName + " weapons|设为 true 禁用" + this.materialName + "武器")
                    .translation("config." + ModSpartanWeaponry.ID + ".material.disable")
                    .worldRestart()
                    .define("disable", false);
            builder.pop();
        }

        public void updateDisabledRecipeList() {
            updateDisabledRecipe(this.typeDisabledName, this.disableRecipes.get());
        }
    }

    public static class ProjectileCategory {
        public DoubleValue baseDamage;
        public DoubleValue rangeMultiplier;

        private ProjectileCategory(ModConfigSpec.Builder builder, String materialName, String projectileName, float baseDamage, float rangeMultiplier) {
            String projName = materialName == null || materialName.isBlank() ? projectileName : materialName + " " + projectileName;
            String category = materialName == null || materialName.isBlank() ? projectileName : materialName + "_" + projectileName;
            builder.push(category);
            this.baseDamage = builder.comment("Base damage for " + projName + "s|" + projName + "的基础伤害")
                    .translation("config." + ModSpartanWeaponry.ID + ".arrow.base_damage")
                    .defineInRange("base_damage", baseDamage, 0.1d, 100.0d);
            this.rangeMultiplier = builder.comment("Range muliplier for " + projName + "s|" + projName + "的射程倍率")
                    .translation("config." + ModSpartanWeaponry.ID + ".arrow.range_multiplier")
                    .defineInRange("range_multiplier", rangeMultiplier, 0.1d, 100.0d);
            builder.pop();
        }
    }

    public static class BoltCategory {
        public DoubleValue baseDamage;
        public DoubleValue rangeMultiplier;
        public DoubleValue armorPiercingFactor;

        protected BoltCategory(ModConfigSpec.Builder builder, String materialName, String projectileName, float baseDamage, float rangeMultiplier, float armorPiercingFactor) {
            String projName = materialName == null || materialName.isBlank() ? projectileName : materialName + " " + projectileName;
            String category = materialName == null || materialName.isBlank() ? projectileName : materialName + "_" + projectileName;
            builder.push(category);
            this.baseDamage = builder.comment("Base damage for " + projName + "s|" + projName + "的基础伤害")
                    .translation("config." + ModSpartanWeaponry.ID + ".arrow.base_damage")
                    .defineInRange("base_damage", baseDamage, 0.1d, 100.0d);
            this.rangeMultiplier = builder.comment("Range muliplier for " + projName + "s|" + projName + "的射程倍率")
                    .translation("config." + ModSpartanWeaponry.ID + ".arrow.range_multiplier")
                    .defineInRange("range_multiplier", rangeMultiplier, 0.1d, 100.0d);
            this.armorPiercingFactor = builder.comment("Armor Piercing factor for " + projName + "s|" + projName + "的破甲系数")
                    .translation("config." + ModSpartanWeaponry.ID + ".bolt.armor_piercing_factor")
                    .defineInRange("armor_piercing_factor", armorPiercingFactor, 0.0d, 1.0d);
            builder.pop();
        }
    }
}
