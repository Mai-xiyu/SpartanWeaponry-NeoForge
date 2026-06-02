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
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.tags.ModBlockTags;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import java.util.List;
import java.util.Optional;
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

    private static final List<WeaponGroupTagPair> WEAPON_GROUP_TAGS = List.of(
            new WeaponGroupTagPair(ModItemTags.DAGGERS, ModItems.DAGGERS),
            new WeaponGroupTagPair(ModItemTags.PARRYING_DAGGERS, ModItems.PARRYING_DAGGERS),
            new WeaponGroupTagPair(ModItemTags.LONGSWORDS, ModItems.LONGSWORDS),
            new WeaponGroupTagPair(ModItemTags.KATANAS, ModItems.KATANAS),
            new WeaponGroupTagPair(ModItemTags.SABERS, ModItems.SABERS),
            new WeaponGroupTagPair(ModItemTags.RAPIERS, ModItems.RAPIERS),
            new WeaponGroupTagPair(ModItemTags.GREATSWORDS, ModItems.GREATSWORDS),
            new WeaponGroupTagPair(ModItemTags.BATTLE_HAMMERS, ModItems.BATTLE_HAMMERS),
            new WeaponGroupTagPair(ModItemTags.WARHAMMERS, ModItems.WARHAMMERS),
            new WeaponGroupTagPair(ModItemTags.SPEARS, ModItems.SPEARS),
            new WeaponGroupTagPair(ModItemTags.HALBERDS, ModItems.HALBERDS),
            new WeaponGroupTagPair(ModItemTags.PIKES, ModItems.PIKES),
            new WeaponGroupTagPair(ModItemTags.LANCES, ModItems.LANCES),
            new WeaponGroupTagPair(ModItemTags.LONGBOWS, ModItems.LONGBOWS),
            new WeaponGroupTagPair(ModItemTags.HEAVY_CROSSBOWS, ModItems.HEAVY_CROSSBOWS),
            new WeaponGroupTagPair(ModItemTags.THROWING_KNIVES, ModItems.THROWING_KNIVES),
            new WeaponGroupTagPair(ModItemTags.TOMAHAWKS, ModItems.TOMAHAWKS),
            new WeaponGroupTagPair(ModItemTags.JAVELINS, ModItems.JAVELINS),
            new WeaponGroupTagPair(ModItemTags.BOOMERANGS, ModItems.BOOMERANGS),
            new WeaponGroupTagPair(ModItemTags.BATTLEAXES, ModItems.BATTLEAXES),
            new WeaponGroupTagPair(ModItemTags.FLANGED_MACES, ModItems.FLANGED_MACES),
            new WeaponGroupTagPair(ModItemTags.GLAIVES, ModItems.GLAIVES),
            new WeaponGroupTagPair(ModItemTags.QUARTERSTAVES, ModItems.QUARTERSTAVES),
            new WeaponGroupTagPair(ModItemTags.SCYTHES, ModItems.SCYTHES)
    );

    private static final List<MaterialTagPair> MATERIAL_TAGS = List.of(
            new MaterialTagPair(WeaponMaterial.WOOD, ModItemTags.WOODEN_WEAPONS),
            new MaterialTagPair(WeaponMaterial.STONE, ModItemTags.STONE_WEAPONS),
            new MaterialTagPair(WeaponMaterial.LEATHER, ModItemTags.LEATHER_WEAPONS),
            new MaterialTagPair(WeaponMaterial.COPPER, ModItemTags.COPPER_WEAPONS),
            new MaterialTagPair(WeaponMaterial.IRON, ModItemTags.IRON_WEAPONS),
            new MaterialTagPair(WeaponMaterial.GOLD, ModItemTags.GOLDEN_WEAPONS),
            new MaterialTagPair(WeaponMaterial.DIAMOND, ModItemTags.DIAMOND_WEAPONS),
            new MaterialTagPair(WeaponMaterial.NETHERITE, ModItemTags.NETHERITE_WEAPONS),
            new MaterialTagPair(WeaponMaterial.TIN, ModItemTags.TIN_WEAPONS),
            new MaterialTagPair(WeaponMaterial.BRONZE, ModItemTags.BRONZE_WEAPONS),
            new MaterialTagPair(WeaponMaterial.STEEL, ModItemTags.STEEL_WEAPONS),
            new MaterialTagPair(WeaponMaterial.SILVER, ModItemTags.SILVER_WEAPONS),
            new MaterialTagPair(WeaponMaterial.ELECTRUM, ModItemTags.ELECTRUM_WEAPONS),
            new MaterialTagPair(WeaponMaterial.LEAD, ModItemTags.LEAD_WEAPONS),
            new MaterialTagPair(WeaponMaterial.NICKEL, ModItemTags.NICKEL_WEAPONS),
            new MaterialTagPair(WeaponMaterial.INVAR, ModItemTags.INVAR_WEAPONS),
            new MaterialTagPair(WeaponMaterial.CONSTANTAN, ModItemTags.CONSTANTAN_WEAPONS),
            new MaterialTagPair(WeaponMaterial.PLATINUM, ModItemTags.PLATINUM_WEAPONS),
            new MaterialTagPair(WeaponMaterial.ALUMINUM, ModItemTags.ALUMINUM_WEAPONS)
    );

    private static final List<TagKey<Item>> PRIMARY_MELEE_WEAPON_TAGS = List.of(
            ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
            ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
            ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS,
            ModItemTags.PIKES, ModItemTags.LANCES
    );

    private static final List<TagKey<Item>> SECONDARY_MELEE_WEAPON_TAGS = List.of(
            ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES, ModItemTags.GLAIVES,
            ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES
    );

    private static final List<TagKey<Item>> MELEE_WEAPON_TAGS = List.of(
            ModItemTags.DAGGERS, ModItemTags.PARRYING_DAGGERS, ModItemTags.LONGSWORDS, ModItemTags.KATANAS,
            ModItemTags.SABERS, ModItemTags.RAPIERS, ModItemTags.GREATSWORDS, ModItemTags.CLUBS, ModItemTags.CESTUSAE,
            ModItemTags.BATTLE_HAMMERS, ModItemTags.WARHAMMERS, ModItemTags.SPEARS, ModItemTags.HALBERDS,
            ModItemTags.PIKES, ModItemTags.LANCES, ModItemTags.BATTLEAXES, ModItemTags.FLANGED_MACES,
            ModItemTags.GLAIVES, ModItemTags.QUARTERSTAVES, ModItemTags.SCYTHES
    );

    private static final List<TagKey<Item>> RANGED_WEAPON_TAGS = List.of(
            ModItemTags.LONGBOWS, ModItemTags.HEAVY_CROSSBOWS
    );

    private static final List<TagKey<Item>> THROWING_WEAPON_TAGS = List.of(
            ModItemTags.THROWING_KNIVES, ModItemTags.TOMAHAWKS, ModItemTags.JAVELINS, ModItemTags.BOOMERANGS
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

        WEAPON_GROUP_TAGS.forEach(pair -> this.addItems(pair.legacyTag(), pair.items()));
        this.tag(ModItemTags.CLUBS).add(ModItems.WOODEN_CLUB.get(), ModItems.STUDDED_CLUB.get());
        this.tag(ModItemTags.CESTUSAE).add(ModItems.CESTUS.get(), ModItems.STUDDED_CESTUS.get());

        this.addLegacyMaterialTags();

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
        this.addTagReferences(ENCHANTABLE_SWORD, MELEE_WEAPON_TAGS);

        // Throwing weapons - also receive sword enchantments for melee use
        this.addTagReferences(ENCHANTABLE_SWORD, THROWING_WEAPON_TAGS);

        // Sharp weapon enchantments (Sharpness, Smite, Bane of Arthropods)
        this.addTagReferences(ENCHANTABLE_SHARP_WEAPON, MELEE_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_SHARP_WEAPON, THROWING_WEAPON_TAGS);

        // Fire Aspect has its own vanilla tag in 1.21+
        this.addTagReferences(ENCHANTABLE_FIRE_ASPECT, MELEE_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_FIRE_ASPECT, THROWING_WEAPON_TAGS);

        // Weapon enchantments (Knockback, Looting)
        this.addTagReferences(ENCHANTABLE_WEAPON, MELEE_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_WEAPON, THROWING_WEAPON_TAGS);

        // Durability enchantments (Unbreaking, Mending)
        this.addTagReferences(ENCHANTABLE_DURABILITY, PRIMARY_MELEE_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_DURABILITY, RANGED_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_DURABILITY, THROWING_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_DURABILITY, SECONDARY_MELEE_WEAPON_TAGS);

        // Curse of Vanishing
        this.addTagReferences(ENCHANTABLE_VANISHING, PRIMARY_MELEE_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_VANISHING, RANGED_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_VANISHING, THROWING_WEAPON_TAGS);
        this.addTagReferences(ENCHANTABLE_VANISHING, SECONDARY_MELEE_WEAPON_TAGS);
        this.tag(ENCHANTABLE_VANISHING).addTag(ModItemTags.QUIVERS);

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
        this.addTagReferences(Tags.Items.TOOLS, PRIMARY_MELEE_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.TOOLS, RANGED_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.TOOLS, THROWING_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.TOOLS, SECONDARY_MELEE_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.MELEE_WEAPON_TOOLS, MELEE_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.RANGED_WEAPON_TOOLS, RANGED_WEAPON_TAGS);
        this.addTagReferences(Tags.Items.RANGED_WEAPON_TOOLS, THROWING_WEAPON_TAGS);
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
            this.tag(ModItemTags.material(pair.material().getMaterialName())).addTag(pair.legacyTag());
        }
    }

    private void addLegacyMaterialTags() {
        for (MaterialTagPair pair : MATERIAL_TAGS) {
            this.addItems(pair.legacyTag(), this.itemsForMaterial(pair.material()));
        }
    }

    private void addItems(TagKey<Item> targetTag, Item[] items) {
        for (Item item : items) {
            this.tag(targetTag).add(item);
        }
    }

    private Item[] itemsForMaterial(WeaponMaterial material) {
        return WEAPON_GROUP_TAGS.stream()
                .map(WeaponGroupTagPair::group)
                .map(group -> group.getItemForMaterial(material))
                .flatMap(Optional::stream)
                .toArray(Item[]::new);
    }

    private void addTagReferences(TagKey<Item> targetTag, List<TagKey<Item>> tags) {
        tags.forEach(tag -> this.tag(targetTag).addTag(tag));
    }

    private record TypeTagPair(TagKey<Item> legacyTag, TagKey<Item> groupedTag) {
    }

    private record WeaponGroupTagPair(TagKey<Item> legacyTag, ModItems.WeaponItemGroup<? extends Item> group) {
        private Item[] items() {
            return this.group.getAsList().toArray(new Item[0]);
        }
    }

    private record MaterialTagPair(WeaponMaterial material, TagKey<Item> legacyTag) {
    }
}
