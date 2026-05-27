package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI.WeaponItemType;
import org.xiyu.spartanweaponryunofficial.api.tags.ModBlockTags;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    private static final List<TypeTagPair> WEAPON_TYPE_TAGS = List.of(
            new TypeTagPair(ModItemTags.DAGGERS, ModItemTags.weaponType(WeaponItemType.DAGGER)),
            new TypeTagPair(ModItemTags.PARRYING_DAGGERS, ModItemTags.weaponType(WeaponItemType.PARRYING_DAGGER)),
            new TypeTagPair(ModItemTags.LONGSWORDS, ModItemTags.weaponType(WeaponItemType.LONGSWORD)),
            new TypeTagPair(ModItemTags.KATANAS, ModItemTags.weaponType(WeaponItemType.KATANA)),
            new TypeTagPair(ModItemTags.SABERS, ModItemTags.weaponType(WeaponItemType.SABER)),
            new TypeTagPair(ModItemTags.RAPIERS, ModItemTags.weaponType(WeaponItemType.RAPIER)),
            new TypeTagPair(ModItemTags.GREATSWORDS, ModItemTags.weaponType(WeaponItemType.GREATSWORD)),
            new TypeTagPair(ModItemTags.CLUBS, ModItemTags.weaponType("clubs")),
            new TypeTagPair(ModItemTags.CESTUSAE, ModItemTags.weaponType("cestus")),
            new TypeTagPair(ModItemTags.BATTLE_HAMMERS, ModItemTags.weaponType(WeaponItemType.BATTLE_HAMMER)),
            new TypeTagPair(ModItemTags.WARHAMMERS, ModItemTags.weaponType(WeaponItemType.WARHAMMER)),
            new TypeTagPair(ModItemTags.SPEARS, ModItemTags.weaponType(WeaponItemType.SPEAR)),
            new TypeTagPair(ModItemTags.HALBERDS, ModItemTags.weaponType(WeaponItemType.HALBERD)),
            new TypeTagPair(ModItemTags.PIKES, ModItemTags.weaponType(WeaponItemType.PIKE)),
            new TypeTagPair(ModItemTags.LANCES, ModItemTags.weaponType(WeaponItemType.LANCE)),
            new TypeTagPair(ModItemTags.LONGBOWS, ModItemTags.weaponType(WeaponItemType.LONGBOW)),
            new TypeTagPair(ModItemTags.HEAVY_CROSSBOWS, ModItemTags.weaponType(WeaponItemType.HEAVY_CROSSBOW)),
            new TypeTagPair(ModItemTags.THROWING_KNIVES, ModItemTags.weaponType(WeaponItemType.THROWING_KNIFE)),
            new TypeTagPair(ModItemTags.TOMAHAWKS, ModItemTags.weaponType(WeaponItemType.TOMAHAWK)),
            new TypeTagPair(ModItemTags.JAVELINS, ModItemTags.weaponType(WeaponItemType.JAVELIN)),
            new TypeTagPair(ModItemTags.BOOMERANGS, ModItemTags.weaponType(WeaponItemType.BOOMERANG)),
            new TypeTagPair(ModItemTags.BATTLEAXES, ModItemTags.weaponType(WeaponItemType.BATTLEAXE)),
            new TypeTagPair(ModItemTags.FLANGED_MACES, ModItemTags.weaponType(WeaponItemType.FLANGED_MACE)),
            new TypeTagPair(ModItemTags.GLAIVES, ModItemTags.weaponType(WeaponItemType.GLAIVE)),
            new TypeTagPair(ModItemTags.QUARTERSTAVES, ModItemTags.weaponType(WeaponItemType.QUARTERSTAFF)),
            new TypeTagPair(ModItemTags.SCYTHES, ModItemTags.weaponType(WeaponItemType.SCYTHE))
    );

    private static final List<MaterialTagPair> MATERIAL_TAGS = List.of(
            new MaterialTagPair("wood", ModItemTags.WOODEN_WEAPONS),
            new MaterialTagPair("stone", ModItemTags.STONE_WEAPONS),
            new MaterialTagPair("leather", ModItemTags.LEATHER_WEAPONS),
            new MaterialTagPair("copper", ModItemTags.COPPER_WEAPONS),
            new MaterialTagPair("iron", ModItemTags.IRON_WEAPONS),
            new MaterialTagPair("gold", ModItemTags.GOLDEN_WEAPONS),
            new MaterialTagPair("diamond", ModItemTags.DIAMOND_WEAPONS),
            new MaterialTagPair("netherite", ModItemTags.NETHERITE_WEAPONS),
            new MaterialTagPair("tin", ModItemTags.TIN_WEAPONS),
            new MaterialTagPair("bronze", ModItemTags.BRONZE_WEAPONS),
            new MaterialTagPair("steel", ModItemTags.STEEL_WEAPONS),
            new MaterialTagPair("silver", ModItemTags.SILVER_WEAPONS),
            new MaterialTagPair("electrum", ModItemTags.ELECTRUM_WEAPONS),
            new MaterialTagPair("lead", ModItemTags.LEAD_WEAPONS),
            new MaterialTagPair("nickel", ModItemTags.NICKEL_WEAPONS),
            new MaterialTagPair("invar", ModItemTags.INVAR_WEAPONS),
            new MaterialTagPair("constantan", ModItemTags.CONSTANTAN_WEAPONS),
            new MaterialTagPair("platinum", ModItemTags.PLATINUM_WEAPONS),
            new MaterialTagPair("aluminum", ModItemTags.ALUMINUM_WEAPONS)
    );

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagLookup, ExistingFileHelper existingFileHelper) {
        super(output, registry, blockTagLookup, ModSpartanWeaponry.ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.@NotNull Provider registry) {
        final TagKey<Item> CURIOS_BACK = ItemTags.create(ResourceLocation.parse("curios:back"));
        final TagKey<Item> CURIOS_QUIVER = ItemTags.create(ResourceLocation.parse("curios:quiver"));

        // Minecraft 1.21+ enchantment tags - required for enchanting table and anvil to work
        final TagKey<Item> ENCHANTABLE_SWORD = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/sword"));
        final TagKey<Item> ENCHANTABLE_FIRE_ASPECT = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/fire_aspect"));
        final TagKey<Item> ENCHANTABLE_SHARP_WEAPON = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/sharp_weapon"));
        final TagKey<Item> ENCHANTABLE_WEAPON = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/weapon"));
        final TagKey<Item> ENCHANTABLE_DURABILITY = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/durability"));
        final TagKey<Item> ENCHANTABLE_VANISHING = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/vanishing"));
        final TagKey<Item> ENCHANTABLE_BOW = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/bow"));
        final TagKey<Item> ENCHANTABLE_CROSSBOW = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/crossbow"));
        final TagKey<Item> ENCHANTABLE_TRIDENT = ItemTags.create(ResourceLocation.parse("minecraft:enchantable/trident"));

        // Mod-specific enchantable tags for mod enchantments to work at enchanting table
        final TagKey<Item> ENCHANTABLE_THROWING_WEAPON = ItemTags.create(ResourceLocation.parse("spartan_weaponry_unofficial:enchantable/throwing_weapon"));
        final TagKey<Item> ENCHANTABLE_HEAVY_CROSSBOW = ItemTags.create(ResourceLocation.parse("spartan_weaponry_unofficial:enchantable/heavy_crossbow"));
        final TagKey<Item> ENCHANTABLE_BOOMERANG = ItemTags.create(ResourceLocation.parse("spartan_weaponry_unofficial:enchantable/boomerang"));

        // Tags in the Spartan Weaponry domain
        this.tag(ModItemTags.HANDLES).add(ModItems.SIMPLE_HANDLE.get(), ModItems.HANDLE.get());
        this.tag(ModItemTags.POLES).add(ModItems.SIMPLE_POLE.get(), ModItems.POLE.get());

        this.tag(ModItemTags.DAGGERS).add(ModItems.DAGGERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.PARRYING_DAGGERS).add(ModItems.PARRYING_DAGGERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.LONGSWORDS).add(ModItems.LONGSWORDS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.KATANAS).add(ModItems.KATANAS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.SABERS).add(ModItems.SABERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.RAPIERS).add(ModItems.RAPIERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.GREATSWORDS).add(ModItems.GREATSWORDS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.CLUBS).add(ModItems.WOODEN_CLUB.get(), ModItems.STUDDED_CLUB.get());
        this.tag(ModItemTags.CESTUSAE).add(ModItems.CESTUS.get(), ModItems.STUDDED_CESTUS.get());
        this.tag(ModItemTags.BATTLE_HAMMERS).add(ModItems.BATTLE_HAMMERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.WARHAMMERS).add(ModItems.WARHAMMERS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.SPEARS).add(ModItems.SPEARS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.HALBERDS).add(ModItems.HALBERDS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.PIKES).add(ModItems.PIKES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.LANCES).add(ModItems.LANCES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.LONGBOWS).add(ModItems.LONGBOWS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.HEAVY_CROSSBOWS).add(ModItems.HEAVY_CROSSBOWS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.THROWING_KNIVES).add(ModItems.THROWING_KNIVES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.TOMAHAWKS).add(ModItems.TOMAHAWKS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.JAVELINS).add(ModItems.JAVELINS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.BOOMERANGS).add(ModItems.BOOMERANGS.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.BATTLEAXES).add(ModItems.BATTLEAXES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.FLANGED_MACES).add(ModItems.FLANGED_MACES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.GLAIVES).add(ModItems.GLAIVES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.QUARTERSTAVES).add(ModItems.QUARTERSTAVES.getAsList().toArray(new Item[0]));
        this.tag(ModItemTags.SCYTHES).add(ModItems.SCYTHES.getAsList().toArray(new Item[0]));

        this.tag(ModItemTags.WOODEN_WEAPONS).add(ModItems.DAGGERS.wood.get(), ModItems.PARRYING_DAGGERS.wood.get(), ModItems.LONGSWORDS.wood.get(), ModItems.KATANAS.wood.get(), ModItems.SABERS.wood.get(),
                ModItems.RAPIERS.wood.get(), ModItems.GREATSWORDS.wood.get(), ModItems.BATTLE_HAMMERS.wood.get(), ModItems.WARHAMMERS.wood.get(), ModItems.SPEARS.wood.get(), ModItems.HALBERDS.wood.get(), ModItems.PIKES.wood.get(),
                ModItems.LANCES.wood.get(), ModItems.LONGBOWS.wood.get(), ModItems.HEAVY_CROSSBOWS.wood.get(), ModItems.THROWING_KNIVES.wood.get(), ModItems.TOMAHAWKS.wood.get(), ModItems.JAVELINS.wood.get(),
                ModItems.BOOMERANGS.wood.get(), ModItems.BATTLEAXES.wood.get(), ModItems.FLANGED_MACES.wood.get(), ModItems.GLAIVES.wood.get(), ModItems.QUARTERSTAVES.wood.get(), ModItems.SCYTHES.wood.get());
        this.tag(ModItemTags.STONE_WEAPONS).add(ModItems.DAGGERS.stone.get(), ModItems.PARRYING_DAGGERS.stone.get(), ModItems.LONGSWORDS.stone.get(), ModItems.KATANAS.stone.get(), ModItems.SABERS.stone.get(),
                ModItems.RAPIERS.stone.get(), ModItems.GREATSWORDS.stone.get(), ModItems.BATTLE_HAMMERS.stone.get(), ModItems.WARHAMMERS.stone.get(), ModItems.SPEARS.stone.get(), ModItems.HALBERDS.stone.get(), ModItems.PIKES.stone.get(),
                ModItems.LANCES.stone.get(), ModItems.THROWING_KNIVES.stone.get(), ModItems.TOMAHAWKS.stone.get(), ModItems.JAVELINS.stone.get(),
                ModItems.BOOMERANGS.stone.get(), ModItems.BATTLEAXES.stone.get(), ModItems.FLANGED_MACES.stone.get(), ModItems.GLAIVES.stone.get(), ModItems.QUARTERSTAVES.stone.get(), ModItems.SCYTHES.stone.get());
        this.tag(ModItemTags.LEATHER_WEAPONS).add(ModItems.LONGBOWS.leather.get(), ModItems.HEAVY_CROSSBOWS.leather.get());
        this.tag(ModItemTags.COPPER_WEAPONS).add(ModItems.DAGGERS.copper.get(), ModItems.PARRYING_DAGGERS.copper.get(), ModItems.LONGSWORDS.copper.get(), ModItems.KATANAS.copper.get(), ModItems.SABERS.copper.get(),
                ModItems.RAPIERS.copper.get(), ModItems.GREATSWORDS.copper.get(), ModItems.BATTLE_HAMMERS.copper.get(), ModItems.WARHAMMERS.copper.get(), ModItems.SPEARS.copper.get(), ModItems.HALBERDS.copper.get(), ModItems.PIKES.copper.get(),
                ModItems.LANCES.copper.get(), ModItems.LONGBOWS.copper.get(), ModItems.HEAVY_CROSSBOWS.copper.get(), ModItems.THROWING_KNIVES.copper.get(), ModItems.TOMAHAWKS.copper.get(), ModItems.JAVELINS.copper.get(),
                ModItems.BOOMERANGS.copper.get(), ModItems.BATTLEAXES.copper.get(), ModItems.FLANGED_MACES.copper.get(), ModItems.GLAIVES.copper.get(), ModItems.QUARTERSTAVES.copper.get(), ModItems.SCYTHES.copper.get());
        this.tag(ModItemTags.IRON_WEAPONS).add(ModItems.DAGGERS.iron.get(), ModItems.PARRYING_DAGGERS.iron.get(), ModItems.LONGSWORDS.iron.get(), ModItems.KATANAS.iron.get(), ModItems.SABERS.iron.get(),
                ModItems.RAPIERS.iron.get(), ModItems.GREATSWORDS.iron.get(), ModItems.BATTLE_HAMMERS.iron.get(), ModItems.WARHAMMERS.iron.get(), ModItems.SPEARS.iron.get(), ModItems.HALBERDS.iron.get(), ModItems.PIKES.iron.get(),
                ModItems.LANCES.iron.get(), ModItems.LONGBOWS.iron.get(), ModItems.HEAVY_CROSSBOWS.iron.get(), ModItems.THROWING_KNIVES.iron.get(), ModItems.TOMAHAWKS.iron.get(), ModItems.JAVELINS.iron.get(),
                ModItems.BOOMERANGS.iron.get(), ModItems.BATTLEAXES.iron.get(), ModItems.FLANGED_MACES.iron.get(), ModItems.GLAIVES.iron.get(), ModItems.QUARTERSTAVES.iron.get(), ModItems.SCYTHES.iron.get());
        this.tag(ModItemTags.GOLDEN_WEAPONS).add(ModItems.DAGGERS.gold.get(), ModItems.PARRYING_DAGGERS.gold.get(), ModItems.LONGSWORDS.gold.get(), ModItems.KATANAS.gold.get(), ModItems.SABERS.gold.get(),
                ModItems.RAPIERS.gold.get(), ModItems.GREATSWORDS.gold.get(), ModItems.BATTLE_HAMMERS.gold.get(), ModItems.WARHAMMERS.gold.get(), ModItems.SPEARS.gold.get(), ModItems.HALBERDS.gold.get(), ModItems.PIKES.gold.get(),
                ModItems.LANCES.gold.get(), ModItems.LONGBOWS.gold.get(), ModItems.HEAVY_CROSSBOWS.gold.get(), ModItems.THROWING_KNIVES.gold.get(), ModItems.TOMAHAWKS.gold.get(), ModItems.JAVELINS.gold.get(),
                ModItems.BOOMERANGS.gold.get(), ModItems.BATTLEAXES.gold.get(), ModItems.FLANGED_MACES.gold.get(), ModItems.GLAIVES.gold.get(), ModItems.QUARTERSTAVES.gold.get(), ModItems.SCYTHES.gold.get());
        this.tag(ModItemTags.DIAMOND_WEAPONS).add(ModItems.DAGGERS.diamond.get(), ModItems.PARRYING_DAGGERS.diamond.get(), ModItems.LONGSWORDS.diamond.get(), ModItems.KATANAS.diamond.get(), ModItems.SABERS.diamond.get(),
                ModItems.RAPIERS.diamond.get(), ModItems.GREATSWORDS.diamond.get(), ModItems.BATTLE_HAMMERS.diamond.get(), ModItems.WARHAMMERS.diamond.get(), ModItems.SPEARS.diamond.get(), ModItems.HALBERDS.diamond.get(), ModItems.PIKES.diamond.get(),
                ModItems.LANCES.diamond.get(), ModItems.LONGBOWS.diamond.get(), ModItems.HEAVY_CROSSBOWS.diamond.get(), ModItems.THROWING_KNIVES.diamond.get(), ModItems.TOMAHAWKS.diamond.get(), ModItems.JAVELINS.diamond.get(),
                ModItems.BOOMERANGS.diamond.get(), ModItems.BATTLEAXES.diamond.get(), ModItems.FLANGED_MACES.diamond.get(), ModItems.GLAIVES.diamond.get(), ModItems.QUARTERSTAVES.diamond.get(), ModItems.SCYTHES.diamond.get());
        this.tag(ModItemTags.NETHERITE_WEAPONS).add(ModItems.DAGGERS.netherite.get(), ModItems.PARRYING_DAGGERS.netherite.get(), ModItems.LONGSWORDS.netherite.get(), ModItems.KATANAS.netherite.get(), ModItems.SABERS.netherite.get(),
                ModItems.RAPIERS.netherite.get(), ModItems.GREATSWORDS.netherite.get(), ModItems.BATTLE_HAMMERS.netherite.get(), ModItems.WARHAMMERS.netherite.get(), ModItems.SPEARS.netherite.get(), ModItems.HALBERDS.netherite.get(), ModItems.PIKES.netherite.get(),
                ModItems.LANCES.netherite.get(), ModItems.LONGBOWS.netherite.get(), ModItems.HEAVY_CROSSBOWS.netherite.get(), ModItems.THROWING_KNIVES.netherite.get(), ModItems.TOMAHAWKS.netherite.get(), ModItems.JAVELINS.netherite.get(),
                ModItems.BOOMERANGS.netherite.get(), ModItems.BATTLEAXES.netherite.get(), ModItems.FLANGED_MACES.netherite.get(), ModItems.GLAIVES.netherite.get(), ModItems.QUARTERSTAVES.netherite.get(), ModItems.SCYTHES.netherite.get());

        this.tag(ModItemTags.TIN_WEAPONS).add(ModItems.DAGGERS.tin.get(), ModItems.PARRYING_DAGGERS.tin.get(), ModItems.LONGSWORDS.tin.get(), ModItems.KATANAS.tin.get(), ModItems.SABERS.tin.get(),
                ModItems.RAPIERS.tin.get(), ModItems.GREATSWORDS.tin.get(), ModItems.BATTLE_HAMMERS.tin.get(), ModItems.WARHAMMERS.tin.get(), ModItems.SPEARS.tin.get(), ModItems.HALBERDS.tin.get(), ModItems.PIKES.tin.get(),
                ModItems.LANCES.tin.get(), ModItems.LONGBOWS.tin.get(), ModItems.HEAVY_CROSSBOWS.tin.get(), ModItems.THROWING_KNIVES.tin.get(), ModItems.TOMAHAWKS.tin.get(), ModItems.JAVELINS.tin.get(),
                ModItems.BOOMERANGS.tin.get(), ModItems.BATTLEAXES.tin.get(), ModItems.FLANGED_MACES.tin.get(), ModItems.GLAIVES.tin.get(), ModItems.QUARTERSTAVES.tin.get(), ModItems.SCYTHES.tin.get());
        this.tag(ModItemTags.BRONZE_WEAPONS).add(ModItems.DAGGERS.bronze.get(), ModItems.PARRYING_DAGGERS.bronze.get(), ModItems.LONGSWORDS.bronze.get(), ModItems.KATANAS.bronze.get(), ModItems.SABERS.bronze.get(),
                ModItems.RAPIERS.bronze.get(), ModItems.GREATSWORDS.bronze.get(), ModItems.BATTLE_HAMMERS.bronze.get(), ModItems.WARHAMMERS.bronze.get(), ModItems.SPEARS.bronze.get(), ModItems.HALBERDS.bronze.get(), ModItems.PIKES.bronze.get(),
                ModItems.LANCES.bronze.get(), ModItems.LONGBOWS.bronze.get(), ModItems.HEAVY_CROSSBOWS.bronze.get(), ModItems.THROWING_KNIVES.bronze.get(), ModItems.TOMAHAWKS.bronze.get(), ModItems.JAVELINS.bronze.get(),
                ModItems.BOOMERANGS.bronze.get(), ModItems.BATTLEAXES.bronze.get(), ModItems.FLANGED_MACES.bronze.get(), ModItems.GLAIVES.bronze.get(), ModItems.QUARTERSTAVES.bronze.get(), ModItems.SCYTHES.bronze.get());
        this.tag(ModItemTags.STEEL_WEAPONS).add(ModItems.DAGGERS.steel.get(), ModItems.PARRYING_DAGGERS.steel.get(), ModItems.LONGSWORDS.steel.get(), ModItems.KATANAS.steel.get(), ModItems.SABERS.steel.get(),
                ModItems.RAPIERS.steel.get(), ModItems.GREATSWORDS.steel.get(), ModItems.BATTLE_HAMMERS.steel.get(), ModItems.WARHAMMERS.steel.get(), ModItems.SPEARS.steel.get(), ModItems.HALBERDS.steel.get(), ModItems.PIKES.steel.get(),
                ModItems.LANCES.steel.get(), ModItems.LONGBOWS.steel.get(), ModItems.HEAVY_CROSSBOWS.steel.get(), ModItems.THROWING_KNIVES.steel.get(), ModItems.TOMAHAWKS.steel.get(), ModItems.JAVELINS.steel.get(),
                ModItems.BOOMERANGS.steel.get(), ModItems.BATTLEAXES.steel.get(), ModItems.FLANGED_MACES.steel.get(), ModItems.GLAIVES.steel.get(), ModItems.QUARTERSTAVES.steel.get(), ModItems.SCYTHES.steel.get());
        this.tag(ModItemTags.SILVER_WEAPONS).add(ModItems.DAGGERS.silver.get(), ModItems.PARRYING_DAGGERS.silver.get(), ModItems.LONGSWORDS.silver.get(), ModItems.KATANAS.silver.get(), ModItems.SABERS.silver.get(),
                ModItems.RAPIERS.silver.get(), ModItems.GREATSWORDS.silver.get(), ModItems.BATTLE_HAMMERS.silver.get(), ModItems.WARHAMMERS.silver.get(), ModItems.SPEARS.silver.get(), ModItems.HALBERDS.silver.get(), ModItems.PIKES.silver.get(),
                ModItems.LANCES.silver.get(), ModItems.LONGBOWS.silver.get(), ModItems.HEAVY_CROSSBOWS.silver.get(), ModItems.THROWING_KNIVES.silver.get(), ModItems.TOMAHAWKS.silver.get(), ModItems.JAVELINS.silver.get(),
                ModItems.BOOMERANGS.silver.get(), ModItems.BATTLEAXES.silver.get(), ModItems.FLANGED_MACES.silver.get(), ModItems.GLAIVES.silver.get(), ModItems.QUARTERSTAVES.silver.get(), ModItems.SCYTHES.silver.get());
        this.tag(ModItemTags.ELECTRUM_WEAPONS).add(ModItems.DAGGERS.electrum.get(), ModItems.PARRYING_DAGGERS.electrum.get(), ModItems.LONGSWORDS.electrum.get(), ModItems.KATANAS.electrum.get(), ModItems.SABERS.electrum.get(),
                ModItems.RAPIERS.electrum.get(), ModItems.GREATSWORDS.electrum.get(), ModItems.BATTLE_HAMMERS.electrum.get(), ModItems.WARHAMMERS.electrum.get(), ModItems.SPEARS.electrum.get(), ModItems.HALBERDS.electrum.get(), ModItems.PIKES.electrum.get(),
                ModItems.LANCES.electrum.get(), ModItems.LONGBOWS.electrum.get(), ModItems.HEAVY_CROSSBOWS.electrum.get(), ModItems.THROWING_KNIVES.electrum.get(), ModItems.TOMAHAWKS.electrum.get(), ModItems.JAVELINS.electrum.get(),
                ModItems.BOOMERANGS.electrum.get(), ModItems.BATTLEAXES.electrum.get(), ModItems.FLANGED_MACES.electrum.get(), ModItems.GLAIVES.electrum.get(), ModItems.QUARTERSTAVES.electrum.get(), ModItems.SCYTHES.electrum.get());
        this.tag(ModItemTags.LEAD_WEAPONS).add(ModItems.DAGGERS.lead.get(), ModItems.PARRYING_DAGGERS.lead.get(), ModItems.LONGSWORDS.lead.get(), ModItems.KATANAS.lead.get(), ModItems.SABERS.lead.get(),
                ModItems.RAPIERS.lead.get(), ModItems.GREATSWORDS.lead.get(), ModItems.BATTLE_HAMMERS.lead.get(), ModItems.WARHAMMERS.lead.get(), ModItems.SPEARS.lead.get(), ModItems.HALBERDS.lead.get(), ModItems.PIKES.lead.get(),
                ModItems.LANCES.lead.get(), ModItems.LONGBOWS.lead.get(), ModItems.HEAVY_CROSSBOWS.lead.get(), ModItems.THROWING_KNIVES.lead.get(), ModItems.TOMAHAWKS.lead.get(), ModItems.JAVELINS.lead.get(),
                ModItems.BOOMERANGS.lead.get(), ModItems.BATTLEAXES.lead.get(), ModItems.FLANGED_MACES.lead.get(), ModItems.GLAIVES.lead.get(), ModItems.QUARTERSTAVES.lead.get(), ModItems.SCYTHES.lead.get());
        this.tag(ModItemTags.NICKEL_WEAPONS).add(ModItems.DAGGERS.nickel.get(), ModItems.PARRYING_DAGGERS.nickel.get(), ModItems.LONGSWORDS.nickel.get(), ModItems.KATANAS.nickel.get(), ModItems.SABERS.nickel.get(),
                ModItems.RAPIERS.nickel.get(), ModItems.GREATSWORDS.nickel.get(), ModItems.BATTLE_HAMMERS.nickel.get(), ModItems.WARHAMMERS.nickel.get(), ModItems.SPEARS.nickel.get(), ModItems.HALBERDS.nickel.get(), ModItems.PIKES.nickel.get(),
                ModItems.LANCES.nickel.get(), ModItems.LONGBOWS.nickel.get(), ModItems.HEAVY_CROSSBOWS.nickel.get(), ModItems.THROWING_KNIVES.nickel.get(), ModItems.TOMAHAWKS.nickel.get(), ModItems.JAVELINS.nickel.get(),
                ModItems.BOOMERANGS.nickel.get(), ModItems.BATTLEAXES.nickel.get(), ModItems.FLANGED_MACES.nickel.get(), ModItems.GLAIVES.nickel.get(), ModItems.QUARTERSTAVES.nickel.get(), ModItems.SCYTHES.nickel.get());
        this.tag(ModItemTags.INVAR_WEAPONS).add(ModItems.DAGGERS.invar.get(), ModItems.PARRYING_DAGGERS.invar.get(), ModItems.LONGSWORDS.invar.get(), ModItems.KATANAS.invar.get(), ModItems.SABERS.invar.get(),
                ModItems.RAPIERS.invar.get(), ModItems.GREATSWORDS.invar.get(), ModItems.BATTLE_HAMMERS.invar.get(), ModItems.WARHAMMERS.invar.get(), ModItems.SPEARS.invar.get(), ModItems.HALBERDS.invar.get(), ModItems.PIKES.invar.get(),
                ModItems.LANCES.invar.get(), ModItems.LONGBOWS.invar.get(), ModItems.HEAVY_CROSSBOWS.invar.get(), ModItems.THROWING_KNIVES.invar.get(), ModItems.TOMAHAWKS.invar.get(), ModItems.JAVELINS.invar.get(),
                ModItems.BOOMERANGS.invar.get(), ModItems.BATTLEAXES.invar.get(), ModItems.FLANGED_MACES.invar.get(), ModItems.GLAIVES.invar.get(), ModItems.QUARTERSTAVES.invar.get(), ModItems.SCYTHES.invar.get());
        this.tag(ModItemTags.CONSTANTAN_WEAPONS).add(ModItems.DAGGERS.constantan.get(), ModItems.PARRYING_DAGGERS.constantan.get(), ModItems.LONGSWORDS.constantan.get(), ModItems.KATANAS.constantan.get(), ModItems.SABERS.constantan.get(),
                ModItems.RAPIERS.constantan.get(), ModItems.GREATSWORDS.constantan.get(), ModItems.BATTLE_HAMMERS.constantan.get(), ModItems.WARHAMMERS.constantan.get(), ModItems.SPEARS.constantan.get(), ModItems.HALBERDS.constantan.get(), ModItems.PIKES.constantan.get(),
                ModItems.LANCES.constantan.get(), ModItems.LONGBOWS.constantan.get(), ModItems.HEAVY_CROSSBOWS.constantan.get(), ModItems.THROWING_KNIVES.constantan.get(), ModItems.TOMAHAWKS.constantan.get(), ModItems.JAVELINS.constantan.get(),
                ModItems.BOOMERANGS.constantan.get(), ModItems.BATTLEAXES.constantan.get(), ModItems.FLANGED_MACES.constantan.get(), ModItems.GLAIVES.constantan.get(), ModItems.QUARTERSTAVES.constantan.get(), ModItems.SCYTHES.constantan.get());
        this.tag(ModItemTags.PLATINUM_WEAPONS).add(ModItems.DAGGERS.platinum.get(), ModItems.PARRYING_DAGGERS.platinum.get(), ModItems.LONGSWORDS.platinum.get(), ModItems.KATANAS.platinum.get(), ModItems.SABERS.platinum.get(),
                ModItems.RAPIERS.platinum.get(), ModItems.GREATSWORDS.platinum.get(), ModItems.BATTLE_HAMMERS.platinum.get(), ModItems.WARHAMMERS.platinum.get(), ModItems.SPEARS.platinum.get(), ModItems.HALBERDS.platinum.get(), ModItems.PIKES.platinum.get(),
                ModItems.LANCES.platinum.get(), ModItems.LONGBOWS.platinum.get(), ModItems.HEAVY_CROSSBOWS.platinum.get(), ModItems.THROWING_KNIVES.platinum.get(), ModItems.TOMAHAWKS.platinum.get(), ModItems.JAVELINS.platinum.get(),
                ModItems.BOOMERANGS.platinum.get(), ModItems.BATTLEAXES.platinum.get(), ModItems.FLANGED_MACES.platinum.get(), ModItems.GLAIVES.platinum.get(), ModItems.QUARTERSTAVES.platinum.get(), ModItems.SCYTHES.platinum.get());
        this.tag(ModItemTags.ALUMINUM_WEAPONS).add(ModItems.DAGGERS.aluminum.get(), ModItems.PARRYING_DAGGERS.aluminum.get(), ModItems.LONGSWORDS.aluminum.get(), ModItems.KATANAS.aluminum.get(), ModItems.SABERS.aluminum.get(),
                ModItems.RAPIERS.aluminum.get(), ModItems.GREATSWORDS.aluminum.get(), ModItems.BATTLE_HAMMERS.aluminum.get(), ModItems.WARHAMMERS.aluminum.get(), ModItems.SPEARS.aluminum.get(), ModItems.HALBERDS.aluminum.get(), ModItems.PIKES.aluminum.get(),
                ModItems.LANCES.aluminum.get(), ModItems.LONGBOWS.aluminum.get(), ModItems.HEAVY_CROSSBOWS.aluminum.get(), ModItems.THROWING_KNIVES.aluminum.get(), ModItems.TOMAHAWKS.aluminum.get(), ModItems.JAVELINS.aluminum.get(),
                ModItems.BOOMERANGS.aluminum.get(), ModItems.BATTLEAXES.aluminum.get(), ModItems.FLANGED_MACES.aluminum.get(), ModItems.GLAIVES.aluminum.get(), ModItems.QUARTERSTAVES.aluminum.get(), ModItems.SCYTHES.aluminum.get());

        this.addGroupedWeaponTags();

        this.tag(ModItemTags.ARROWS).add(ModItems.WOODEN_ARROW.get(), ModItems.TIPPED_WOODEN_ARROW.get(), ModItems.COPPER_ARROW.get(), ModItems.TIPPED_COPPER_ARROW.get(), ModItems.IRON_ARROW.get(), ModItems.TIPPED_IRON_ARROW.get(), ModItems.DIAMOND_ARROW.get(), ModItems.TIPPED_DIAMOND_ARROW.get(),
                ModItems.NETHERITE_ARROW.get(), ModItems.TIPPED_NETHERITE_ARROW.get(), ModItems.EXPLOSIVE_ARROW.get());
        this.tag(ModItemTags.BOLTS).add(ModItems.BOLT.get(), ModItems.TIPPED_BOLT.get(), ModItems.SPECTRAL_BOLT.get(), ModItems.COPPER_BOLT.get(), ModItems.TIPPED_COPPER_BOLT.get(), ModItems.DIAMOND_BOLT.get(), ModItems.TIPPED_DIAMOND_BOLT.get(), ModItems.NETHERITE_BOLT.get(), ModItems.TIPPED_NETHERITE_BOLT.get());
        this.tag(ModItemTags.COPPER_PROJECTILES).add(ModItems.COPPER_ARROW.get(), ModItems.TIPPED_COPPER_ARROW.get(), ModItems.COPPER_BOLT.get(), ModItems.TIPPED_COPPER_BOLT.get());
        this.tag(ModItemTags.DIAMOND_PROJECTILES).add(ModItems.DIAMOND_ARROW.get(), ModItems.TIPPED_DIAMOND_ARROW.get(), ModItems.DIAMOND_BOLT.get(), ModItems.TIPPED_DIAMOND_BOLT.get());
        this.tag(ModItemTags.NETHERITE_PROJECTILES).add(ModItems.NETHERITE_ARROW.get(), ModItems.TIPPED_NETHERITE_ARROW.get(), ModItems.NETHERITE_BOLT.get(), ModItems.TIPPED_NETHERITE_BOLT.get());

        this.tag(ModItemTags.ARROW_QUIVERS).add(ModItems.SMALL_ARROW_QUIVER.get(), ModItems.MEDIUM_ARROW_QUIVER.get(), ModItems.LARGE_ARROW_QUIVER.get(), ModItems.HUGE_ARROW_QUIVER.get());
        this.tag(ModItemTags.BOLT_QUIVERS).add(ModItems.SMALL_BOLT_QUIVER.get(), ModItems.MEDIUM_BOLT_QUIVER.get(), ModItems.LARGE_BOLT_QUIVER.get(), ModItems.HUGE_BOLT_QUIVER.get());
        this.tag(ModItemTags.QUIVERS).addTags(ModItemTags.ARROW_QUIVERS, ModItemTags.BOLT_QUIVERS);
        this.tag(ModItemTags.SMALL_QUIVERS).add(ModItems.SMALL_ARROW_QUIVER.get(), ModItems.SMALL_BOLT_QUIVER.get());
        this.tag(ModItemTags.UPGRADED_QUIVERS).add(ModItems.MEDIUM_ARROW_QUIVER.get(), ModItems.MEDIUM_BOLT_QUIVER.get(), ModItems.LARGE_ARROW_QUIVER.get(), ModItems.LARGE_BOLT_QUIVER.get()).addTag(ModItemTags.UPGRADED_QUIVERS_MAX);
        this.tag(ModItemTags.UPGRADED_QUIVERS_MAX).add(ModItems.HUGE_ARROW_QUIVER.get(), ModItems.HUGE_BOLT_QUIVER.get());

        this.tag(ModItemTags.EXPLOSIVES).add(ModItems.EXPLOSIVE_ARROW.get(), ModItems.DYNAMITE.get());
        this.tag(ModItemTags.HEADS).add(ModItems.BLAZE_HEAD.get(), ModItems.ENDERMAN_HEAD.get(), ModItems.SPIDER_HEAD.get(), ModItems.CAVE_SPIDER_HEAD.get(), ModItems.ZOMBIFIED_PIGLIN_HEAD.get(),
                ModItems.HUSK_HEAD.get(), ModItems.STRAY_SKULL.get(), ModItems.DROWNED_HEAD.get(), ModItems.ILLAGER_HEAD.get(), ModItems.WITCH_HEAD.get());

        this.tag(ModItemTags.OILABLE_WEAPONS).add(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD).
                addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS, ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS,
                        ModItemTags.CLUBS, ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES, ModItemTags.LANCES,
                        ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES);

        this.tag(ModItemTags.THROWING_WEAPONS).addTags(ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);
        this.tag(ModItemTags.HAS_CUSTOM_CROSSHAIR).addTags(ModItemTags.THROWING_WEAPONS, ModItemTags.HEAVY_CROSSBOWS);

        this.tag(ModItemTags.ZOMBIE_SPAWN_WEAPONS).add(ModItems.DAGGERS.iron.get(), ModItems.LONGSWORDS.iron.get(), ModItems.KATANAS.iron.get(), ModItems.SABERS.iron.get(), ModItems.RAPIERS.iron.get(),
                ModItems.GREATSWORDS.iron.get(), ModItems.BATTLE_HAMMERS.iron.get(), ModItems.WARHAMMERS.iron.get(), ModItems.BATTLEAXES.iron.get(), ModItems.FLANGED_MACES.iron.get());
        this.tag(ModItemTags.SKELETON_SPAWN_LONGBOWS).add(ModItems.LONGBOWS.wood.get(), ModItems.LONGBOWS.leather.get(), ModItems.LONGBOWS.iron.get());
        this.tag(ModItemTags.PIGLIN_SPAWN_WEAPONS).add(ModItems.DAGGERS.gold.get(), ModItems.LONGSWORDS.gold.get(), ModItems.KATANAS.gold.get(), ModItems.SABERS.gold.get(), ModItems.RAPIERS.gold.get(),
                ModItems.GREATSWORDS.gold.get(), ModItems.BATTLE_HAMMERS.gold.get(), ModItems.WARHAMMERS.gold.get(), ModItems.FLANGED_MACES.gold.get());
        this.tag(ModItemTags.PIGLIN_BRUTE_SPAWN_WEAPONS).add(ModItems.HALBERDS.gold.get(), ModItems.BATTLEAXES.gold.get());
        this.tag(ModItemTags.WITHER_SKELETON_SPAWN_WEAPONS).add(ModItems.DAGGERS.stone.get(), ModItems.LONGSWORDS.stone.get(), ModItems.KATANAS.stone.get(), ModItems.SABERS.stone.get(), ModItems.RAPIERS.stone.get(),
                ModItems.GREATSWORDS.stone.get(), ModItems.BATTLE_HAMMERS.stone.get(), ModItems.WARHAMMERS.stone.get(), ModItems.BATTLEAXES.stone.get(), ModItems.FLANGED_MACES.stone.get());

        // Empty material tags to add modded material support
        this.tag(ModItemTags.COPPER_INGOT);
        this.tag(ModItemTags.TIN_INGOT);
        this.tag(ModItemTags.BRONZE_INGOT);
        this.tag(ModItemTags.STEEL_INGOT);
        this.tag(ModItemTags.SILVER_INGOT);
        this.tag(ModItemTags.ELECTRUM_INGOT);
        this.tag(ModItemTags.LEAD_INGOT);
        this.tag(ModItemTags.NICKEL_INGOT);
        this.tag(ModItemTags.INVAR_INGOT);
        this.tag(ModItemTags.CONSTANTAN_INGOT);
        this.tag(ModItemTags.PLATINUM_INGOT);
        this.tag(ModItemTags.FORGE_ALUMINUM_INGOT);
        this.tag(ModItemTags.FORGE_ALUMINIUM_INGOT);
        this.tag(ModItemTags.ALUMINUM_INGOT).addTags(ModItemTags.FORGE_ALUMINUM_INGOT, ModItemTags.FORGE_ALUMINIUM_INGOT);

        // TODO: Implement nugget tags for smelting/blasting recipes later
/*		tag(ModItemTags.TIN_NUGGET);
		tag(ModItemTags.BRONZE_NUGGET);
		tag(ModItemTags.STEEL_NUGGET);
		tag(ModItemTags.SILVER_NUGGET);
		tag(ModItemTags.ELECTRUM_NUGGET);
		tag(ModItemTags.LEAD_NUGGET);
		tag(ModItemTags.NICKEL_NUGGET);
		tag(ModItemTags.INVAR_NUGGET);
		tag(ModItemTags.CONSTANTAN_NUGGET);
		tag(ModItemTags.PLATINUM_NUGGET);
		tag(ModItemTags.ALUMINUM_NUGGET);*/

        // Tags in vanilla Minecraft's domain
        this.tag(ItemTags.ARROWS).addTag(ModItemTags.ARROWS);
        this.tag(ItemTags.SWORDS).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS);

        // Minecraft 1.21+ enchantment compatibility tags
        // Melee weapons - can receive sword enchantments (Sharpness, Smite, Bane of Arthropods, etc.)
        this.tag(ENCHANTABLE_SWORD).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES,
                ModItemTags.SCYTHES);

        // Throwing weapons - also receive sword enchantments for melee use
        this.tag(ENCHANTABLE_SWORD).addTags(ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);

        // Sharp weapon enchantments (Sharpness, Smite, Bane of Arthropods)
        this.tag(ENCHANTABLE_SHARP_WEAPON).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES,
                ModItemTags.SCYTHES, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);

        // Fire Aspect has its own vanilla tag in 1.21+
        this.tag(ENCHANTABLE_FIRE_ASPECT).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES,
                ModItemTags.SCYTHES, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);

        // Weapon enchantments (Knockback, Looting)
        this.tag(ENCHANTABLE_WEAPON).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES,
                ModItemTags.SCYTHES, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);

        // Durability enchantments (Unbreaking, Mending)
        this.tag(ENCHANTABLE_DURABILITY).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.LONGBOWS, ModItemTags.HEAVY_CROSSBOWS, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS,
                ModItemTags.JAVELINS, ModItemTags.BOOMERANGS, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES,
                ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES);

        // Curse of Vanishing
        this.tag(ENCHANTABLE_VANISHING).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
                ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
                ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES,
                ModItemTags.LANCES, ModItemTags.LONGBOWS, ModItemTags.HEAVY_CROSSBOWS, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS,
                ModItemTags.JAVELINS, ModItemTags.BOOMERANGS, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES,
                ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES, ModItemTags.QUIVERS);

        // Bow enchantments (Power, Punch, Flame, Infinity)
        this.tag(ENCHANTABLE_BOW).addTag(ModItemTags.LONGBOWS);

        // Crossbow enchantments (Quick Charge, Multishot, Piercing)
        this.tag(ENCHANTABLE_CROSSBOW).addTag(ModItemTags.HEAVY_CROSSBOWS);

        // Trident enchantments (Loyalty, Riptide, Channeling, Impaling) - for throwing weapons
        this.tag(ENCHANTABLE_TRIDENT).addTags(ModItemTags.JAVELINS);

        // Mod-specific enchantable tags - for mod's custom enchantments to work at enchanting table
        this.tag(ENCHANTABLE_THROWING_WEAPON).addTag(ModItemTags.THROWING_WEAPONS);
        this.tag(ENCHANTABLE_HEAVY_CROSSBOW).addTag(ModItemTags.HEAVY_CROSSBOWS);
        this.tag(ENCHANTABLE_BOOMERANG).addTag(ModItemTags.BOOMERANGS);

        // Tags in Forge's domain
        final TagKey<Item> HEADS_TAG = ItemTags.create(ResourceLocation.parse("c:heads"));
        this.tag(HEADS_TAG).addTag(ModItemTags.HEADS);
        this.tag(Tags.Items.TOOLS).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS, ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS,
                ModItemTags.CLUBS, ModItemTags.CESTUSAE, ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES, ModItemTags.LANCES,
                ModItemTags.LONGBOWS, ModItemTags.HEAVY_CROSSBOWS, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS, ModItemTags.BATTLEAXES,
                ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES);
        this.tag(Tags.Items.MELEE_WEAPON_TOOLS).addTags(ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS, ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS,
                ModItemTags.CLUBS, ModItemTags.CESTUSAE, ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS, ModItemTags.PIKES, ModItemTags.LANCES,
                ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES);
        this.tag(Tags.Items.RANGED_WEAPON_TOOLS).addTags(ModItemTags.LONGBOWS, ModItemTags.HEAVY_CROSSBOWS, ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS,
                ModItemTags.JAVELINS, ModItemTags.BOOMERANGS);
        this.tag(Tags.Items.TOOLS_BOW).addTag(ModItemTags.LONGBOWS);
        this.tag(Tags.Items.TOOLS_CROSSBOW).addTag(ModItemTags.HEAVY_CROSSBOWS);
        this.tag(Tags.Items.TOOLS_SPEAR).addTags(ModItemTags.SPEARS, ModItemTags.JAVELINS);
        this.tag(Tags.Items.TOOLS_MACE).addTag(ModItemTags.FLANGED_MACES);
        this.tag(ModItemTags.RAW_MEAT).add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.MUTTON, Items.RABBIT);
        this.copy(ModBlockTags.GRASS, ModItemTags.GRASS);

        // Tags in Curios' domain
        this.tag(CURIOS_BACK).addTag(ModItemTags.QUIVERS);
        this.tag(CURIOS_QUIVER).addTag(ModItemTags.QUIVERS);
    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Item Tags";
    }

    private void addGroupedWeaponTags() {
        for (TypeTagPair pair : WEAPON_TYPE_TAGS) {
            this.tag(pair.groupedTag()).addTag(pair.legacyTag());
            this.tag(ModItemTags.WEAPONS).addTag(pair.groupedTag());
            this.tag(ModItemTags.namespace(ModSpartanWeaponry.ID)).addTag(pair.groupedTag());
        }

        for (MaterialTagPair pair : MATERIAL_TAGS) {
            this.tag(ModItemTags.material(pair.materialName())).addTag(pair.legacyTag());
        }
    }

    private record TypeTagPair(TagKey<Item> legacyTag, TagKey<Item> groupedTag) {
    }

    private record MaterialTagPair(String materialName, TagKey<Item> legacyTag) {
    }
}
