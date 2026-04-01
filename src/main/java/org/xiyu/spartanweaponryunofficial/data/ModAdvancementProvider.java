package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.advancement.criterion.BrewOilTrigger;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new SpartanWeaponryAdvancements()));
    }

    public static class SpartanWeaponryAdvancements implements AdvancementSubProvider {
        @Override
        public void generate(@NotNull Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
            AdvancementHolder root = Advancement.Builder.advancement().display(ModItems.LONGSWORDS.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".root.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".root.desc"),
                            Identifier.fromNamespaceAndPath("minecraft", "textures/block/anvil.png"), AdvancementType.TASK, false, false, false).addCriterion("has_handle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HANDLE.get())).addCriterion("has_pole", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.POLE.get())).requirements(AdvancementRequirements.Strategy.OR).
                    save(saver, ModSpartanWeaponry.ID + ":root");

            AdvancementHolder daggers = Advancement.Builder.advancement().parent(root).display(ModItems.DAGGERS.stone.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_dagger.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_dagger.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_dagger", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.DAGGERS).build())).save(saver, ModSpartanWeaponry.ID + ":dagger");
            Advancement.Builder.advancement().parent(root).display(ModItems.PARRYING_DAGGERS.gold.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_parrying_dagger.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_parrying_dagger.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_parrying_dagger", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.PARRYING_DAGGERS).build())).save(saver, ModSpartanWeaponry.ID + ":parrying_dagger");
            AdvancementHolder longswords = Advancement.Builder.advancement().parent(root).display(ModItems.LONGSWORDS.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_longsword.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_longsword.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_longsword", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.LONGSWORDS).build())).save(saver, ModSpartanWeaponry.ID + ":longsword");
            AdvancementHolder katanas = Advancement.Builder.advancement().parent(root).display(ModItems.KATANAS.stone.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_katana.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_katana.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_katana", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.KATANAS).build())).save(saver, ModSpartanWeaponry.ID + ":katana");
            AdvancementHolder sabers = Advancement.Builder.advancement().parent(katanas).display(ModItems.SABERS.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_saber.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_saber.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_saber", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.SABERS).build())).save(saver, ModSpartanWeaponry.ID + ":saber");
            Advancement.Builder.advancement().parent(sabers).display(ModItems.RAPIERS.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_rapier.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_rapier.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_rapier", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.RAPIERS).build())).save(saver, ModSpartanWeaponry.ID + ":rapier");
            Advancement.Builder.advancement().parent(longswords).display(ModItems.GREATSWORDS.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_greatsword.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_greatsword.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_greatsword", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.GREATSWORDS).build())).save(saver, ModSpartanWeaponry.ID + ":greatsword");
            Advancement.Builder.advancement().parent(root).display(ModItems.WOODEN_CLUB.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_club.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_club.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_club", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.CLUBS).build())).save(saver, ModSpartanWeaponry.ID + ":club");
            Advancement.Builder.advancement().parent(root).display(ModItems.CESTUS.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_cestus.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_cestus.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_cestus", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.CESTUSAE).build())).save(saver, ModSpartanWeaponry.ID + ":cestus");
            AdvancementHolder battleHammers = Advancement.Builder.advancement().parent(root).display(ModItems.BATTLE_HAMMERS.gold.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_battle_hammer.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_battle_hammer.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_battle_hammer", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.BATTLE_HAMMERS).build())).save(saver, ModSpartanWeaponry.ID + ":battle_hammer");
            Advancement.Builder.advancement().parent(battleHammers).display(ModItems.WARHAMMERS.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_warhammer.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_warhammer.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_warhammer", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.WARHAMMERS).build())).save(saver, ModSpartanWeaponry.ID + ":warhammer");
            AdvancementHolder spears = Advancement.Builder.advancement().parent(root).display(ModItems.SPEARS.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_spear.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_spear.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_spear", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.SPEARS).build())).save(saver, ModSpartanWeaponry.ID + ":spear");
            Advancement.Builder.advancement().parent(spears).display(ModItems.HALBERDS.gold.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_halberd.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_halberd.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_halberd", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.HALBERDS).build())).save(saver, ModSpartanWeaponry.ID + ":halberd");
            Advancement.Builder.advancement().parent(spears).display(ModItems.PIKES.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_pike.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_pike.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_pike", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.PIKES).build())).save(saver, ModSpartanWeaponry.ID + ":pike");
            Advancement.Builder.advancement().parent(spears).display(ModItems.LANCES.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_lance.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_lance.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_lance", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.LANCES).build())).save(saver, ModSpartanWeaponry.ID + ":lance");
            AdvancementHolder longbows = Advancement.Builder.advancement().parent(root).display(ModItems.LONGBOWS.wood.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_longbow.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_longbow.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_longbow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.LONGBOWS).build())).save(saver, ModSpartanWeaponry.ID + ":longbow");
            Advancement.Builder.advancement().parent(longbows).display(ModItems.HEAVY_CROSSBOWS.wood.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_heavy_crossbow.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_heavy_crossbow.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_heavy_crossbow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.HEAVY_CROSSBOWS).build())).save(saver, ModSpartanWeaponry.ID + ":heavy_crossbow");
            Advancement.Builder.advancement().parent(daggers).display(ModItems.THROWING_KNIVES.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_throwing_knife.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_throwing_knife.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_throwing_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.THROWING_KNIVES).build())).save(saver, ModSpartanWeaponry.ID + ":throwing_knife");
            Advancement.Builder.advancement().parent(root).display(ModItems.TOMAHAWKS.gold.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_tomahawk.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_tomahawk.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_tomahawk", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.TOMAHAWKS).build())).save(saver, ModSpartanWeaponry.ID + ":tomahawk");
            Advancement.Builder.advancement().parent(root).display(ModItems.JAVELINS.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_javelin.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_javelin.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_javelin", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.JAVELINS).build())).save(saver, ModSpartanWeaponry.ID + ":javelin");
            Advancement.Builder.advancement().parent(daggers).display(ModItems.BOOMERANGS.wood.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_boomerang.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_boomerang.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_boomerang", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.BOOMERANGS).build())).save(saver, ModSpartanWeaponry.ID + ":boomerang");
            Advancement.Builder.advancement().parent(root).display(ModItems.BATTLEAXES.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_battleaxe.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_battleaxe.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_battleaxe", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.BATTLEAXES).build())).save(saver, ModSpartanWeaponry.ID + ":battleaxe");
            Advancement.Builder.advancement().parent(root).display(ModItems.FLANGED_MACES.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_flanged_mace.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_flanged_mace.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_flanged_mace", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.FLANGED_MACES).build())).save(saver, ModSpartanWeaponry.ID + ":flanged_mace");
            AdvancementHolder glaives = Advancement.Builder.advancement().parent(spears).display(ModItems.GLAIVES.iron.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_glaive.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_glaive.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_glaive", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.GLAIVES).build())).save(saver, ModSpartanWeaponry.ID + ":glaive");
            Advancement.Builder.advancement().parent(root).display(ModItems.QUARTERSTAVES.gold.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_quarterstaff.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_quarterstaff.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_quarterstaff", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.QUARTERSTAVES).build())).save(saver, ModSpartanWeaponry.ID + ":quarterstaff");
            AdvancementHolder scythes = Advancement.Builder.advancement().parent(glaives).display(ModItems.SCYTHES.diamond.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_scythe.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_scythe.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_scythe", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.SCYTHES).build())).save(saver, ModSpartanWeaponry.ID + ":scythe");

            AdvancementHolder quivers = Advancement.Builder.advancement().parent(longbows).display(ModItems.SMALL_ARROW_QUIVER.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_quiver.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".craft_quiver.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_quiver", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.SMALL_QUIVERS).build())).save(saver, ModSpartanWeaponry.ID + ":small_quiver");
            AdvancementHolder upgradeQuiver = Advancement.Builder.advancement().parent(quivers).display(ModItems.LARGE_ARROW_QUIVER.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_quiver.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_quiver.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_upgraded_quiver", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.UPGRADED_QUIVERS).build())).save(saver, ModSpartanWeaponry.ID + ":upgrade_quiver");
            Advancement.Builder.advancement().parent(upgradeQuiver).display(ModItems.HUGE_ARROW_QUIVER.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_quiver_max.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_quiver_max.desc"),
                    null, AdvancementType.GOAL, true, true, false).addCriterion("has_max_upgraded_quiver", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.UPGRADED_QUIVERS_MAX).build())).save(saver, ModSpartanWeaponry.ID + ":upgrade_quiver_max");

            Advancement.Builder.advancement().parent(root).display(ModItems.BATTLEAXES.netherite.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_netherite.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".upgrade_netherite.desc"),
                    null, AdvancementType.GOAL, true, true, false).addCriterion("has_netherite_weapon", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, ModItemTags.NETHERITE_WEAPONS).build())).save(saver, ModSpartanWeaponry.ID + ":upgrade_netherite_weapon");

            Advancement.Builder.advancement().parent(scythes).display(ModItems.ZOMBIFIED_PIGLIN_HEAD.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".collect_heads.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".collect_heads.desc"),
                            null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(200).build()).
                    addCriterion("creeper_head", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CREEPER_HEAD)).
                    addCriterion("skeleton_skull", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SKELETON_SKULL)).
                    addCriterion("wither_skeleton_skull", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WITHER_SKELETON_SKULL)).
                    addCriterion("zombie_head", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ZOMBIE_HEAD)).
                    addCriterion("blaze_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BLAZE_HEAD.get())).
                    addCriterion("enderman_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ENDERMAN_HEAD.get())).
                    addCriterion("spider_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SPIDER_HEAD.get())).
                    addCriterion("cave_spider_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CAVE_SPIDER_HEAD.get())).
                    addCriterion("piglin_head", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PIGLIN_HEAD)).
                    addCriterion("zombified_piglin_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ZOMBIFIED_PIGLIN_HEAD.get())).
                    addCriterion("husk_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HUSK_HEAD.get())).
                    addCriterion("stray_skull", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAY_SKULL.get())).
                    addCriterion("drowned_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DROWNED_HEAD.get())).
                    addCriterion("illager_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ILLAGER_HEAD.get())).
                    addCriterion("witch_head", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WITCH_HEAD.get())).save(saver, ModSpartanWeaponry.ID + ":collect_heads");

            Advancement.Builder.advancement().parent(root).display(ModItems.WEAPON_OIL.get(), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".brew_oil.title"), Component.translatable("advancement." + ModSpartanWeaponry.ID + ".brew_oil.desc"),
                    null, AdvancementType.TASK, true, true, false).addCriterion("has_brewed_oil", BrewOilTrigger.TriggerInstance.brewedOil()).save(saver, ModSpartanWeaponry.ID + ":brew_oil");
        }

    }
}
