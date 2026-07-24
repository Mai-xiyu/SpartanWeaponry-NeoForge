package org.xiyu.spartanweaponryunofficial.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.ModelOverrides;
import org.xiyu.spartanweaponryunofficial.api.data.model.BaseModels;
import org.xiyu.spartanweaponryunofficial.api.data.model.ModelGenerator;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    private static final List<WeaponModelGroup> WEAPON_MODEL_GROUPS =
            List.of(
                    new WeaponModelGroup(ModItems.DAGGERS, ModelGenerator::createDaggerModels),
                    new WeaponModelGroup(
                            ModItems.PARRYING_DAGGERS, ModelGenerator::createParryingDaggerModels),
                    new WeaponModelGroup(ModItems.LONGSWORDS, ModelGenerator::createLongswordModel),
                    new WeaponModelGroup(ModItems.KATANAS, ModelGenerator::createKatanaModel),
                    new WeaponModelGroup(ModItems.SABERS, ModelGenerator::createSaberModel),
                    new WeaponModelGroup(ModItems.RAPIERS, ModelGenerator::createRapierModel),
                    new WeaponModelGroup(
                            ModItems.GREATSWORDS, ModelGenerator::createGreatswordModel),
                    new WeaponModelGroup(
                            ModItems.BATTLE_HAMMERS, ModelGenerator::createBattleHammerModel),
                    new WeaponModelGroup(ModItems.WARHAMMERS, ModelGenerator::createWarhammerModel),
                    new WeaponModelGroup(ModItems.SPEARS, ModelGenerator::createSpearModel),
                    new WeaponModelGroup(ModItems.HALBERDS, ModelGenerator::createHalberdModel),
                    new WeaponModelGroup(ModItems.PIKES, ModelGenerator::createPikeModel),
                    new WeaponModelGroup(ModItems.LANCES, ModelGenerator::createLanceModel),
                    new WeaponModelGroup(ModItems.LONGBOWS, ModelGenerator::createLongbowModels),
                    new WeaponModelGroup(
                            ModItems.HEAVY_CROSSBOWS, ModelGenerator::createHeavyCrossbowModels),
                    new WeaponModelGroup(
                            ModItems.THROWING_KNIVES, ModelGenerator::createThrowingKnifeModels),
                    new WeaponModelGroup(ModItems.TOMAHAWKS, ModelGenerator::createTomahawkModels),
                    new WeaponModelGroup(ModItems.JAVELINS, ModelGenerator::createJavelinModels),
                    new WeaponModelGroup(
                            ModItems.BOOMERANGS, ModelGenerator::createBoomerangModels),
                    new WeaponModelGroup(ModItems.BATTLEAXES, ModelGenerator::createBattleaxeModel),
                    new WeaponModelGroup(
                            ModItems.FLANGED_MACES, ModelGenerator::createFlangedMaceModel),
                    new WeaponModelGroup(ModItems.GLAIVES, ModelGenerator::createGlaiveModel),
                    new WeaponModelGroup(
                            ModItems.QUARTERSTAVES, ModelGenerator::createQuarterstaffModel),
                    new WeaponModelGroup(ModItems.SCYTHES, ModelGenerator::createScytheModel));

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModSpartanWeaponry.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        final ModelGenerator generator = new ModelGenerator(this);

        // Generate the models
        generator.createSimpleModel(ModItems.SIMPLE_HANDLE.get(), this.mcLoc("item/handheld"));
        generator.createSimpleModel(ModItems.HANDLE.get(), this.mcLoc("item/handheld"));
        generator.createSimpleModel(ModItems.SIMPLE_POLE.get(), BaseModels.POLE);
        generator.createSimpleModel(ModItems.POLE.get(), BaseModels.POLE);
        generator.createSimpleModel(ModItems.EXPLOSIVE_CHARGE.get());
        generator.createSimpleModel(ModItems.GREASE_BALL.get());

        // TODO: Allow vanilla sword models that support oils to generate again
        /*        ImmutableList.of(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD).
        forEach((sword) -> generator.createVanillaSwordModels(sword));*/

        WEAPON_MODEL_GROUPS.forEach(group -> group.createModels(generator));
        generator.createClubModel(ModItems.WOODEN_CLUB.get());
        generator.createClubModel(ModItems.STUDDED_CLUB.get());
        generator.createCestusModel(ModItems.CESTUS.get());
        generator.createCestusModel(ModItems.STUDDED_CESTUS.get());

        generator.createSimpleModel(ModItems.WOODEN_ARROW.get());
        this.createTippedArrowModel(ModItems.TIPPED_WOODEN_ARROW.get());
        generator.createSimpleModel(ModItems.COPPER_ARROW.get());
        this.createTippedArrowModel(ModItems.TIPPED_COPPER_ARROW.get());
        generator.createSimpleModel(ModItems.IRON_ARROW.get());
        this.createTippedArrowModel(ModItems.TIPPED_IRON_ARROW.get());
        generator.createSimpleModel(ModItems.DIAMOND_ARROW.get());
        this.createTippedArrowModel(ModItems.TIPPED_DIAMOND_ARROW.get());
        generator.createSimpleModel(ModItems.NETHERITE_ARROW.get());
        this.createTippedArrowModel(ModItems.TIPPED_NETHERITE_ARROW.get());
        generator.createSimpleModel(ModItems.EXPLOSIVE_ARROW.get());
        generator.createSimpleModel(ModItems.BOLT.get());
        this.createTippedBoltModel(ModItems.TIPPED_BOLT.get());
        generator.createSimpleModel(ModItems.SPECTRAL_BOLT.get());
        generator.createSimpleModel(ModItems.COPPER_BOLT.get());
        this.createTippedBoltModel(ModItems.TIPPED_COPPER_BOLT.get());
        generator.createSimpleModel(ModItems.DIAMOND_BOLT.get());
        this.createTippedBoltModel(ModItems.TIPPED_DIAMOND_BOLT.get());
        generator.createSimpleModel(ModItems.NETHERITE_BOLT.get());
        this.createTippedBoltModel(ModItems.TIPPED_NETHERITE_BOLT.get());

        this.createQuiverModels(ModItems.SMALL_ARROW_QUIVER.get(), 3);
        this.createQuiverModels(ModItems.MEDIUM_ARROW_QUIVER.get(), 3);
        this.createQuiverModels(ModItems.LARGE_ARROW_QUIVER.get(), 5);
        this.createQuiverModels(ModItems.HUGE_ARROW_QUIVER.get(), 5);
        this.createQuiverModels(ModItems.SMALL_BOLT_QUIVER.get(), 3);
        this.createQuiverModels(ModItems.MEDIUM_BOLT_QUIVER.get(), 3);
        this.createQuiverModels(ModItems.LARGE_BOLT_QUIVER.get(), 5);
        this.createQuiverModels(ModItems.HUGE_BOLT_QUIVER.get(), 5);

        generator.createSimpleModel(ModItems.QUIVER_COMPARTMENT.get());
        generator.createSimpleModel(ModItems.MEDIUM_QUIVER_BRACE.get());
        generator.createSimpleModel(ModItems.LARGE_QUIVER_BRACE.get());
        generator.createSimpleModel(ModItems.HUGE_QUIVER_BRACE.get());

        generator.createSimpleModel(ModItems.DYNAMITE.get());

        this.createWeaponOilModel(ModItems.WEAPON_OIL.get());
    }

    protected ResourceLocation createTippedArrowModel(Item item) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        return this.withExistingParent(itemPath, this.mcLoc("item/generated"))
                .texture("layer0", "item/" + itemPath + "_base")
                .texture("layer1", "item/tipped_arrow_head")
                .getLocation();
    }

    protected ResourceLocation createTippedBoltModel(Item item) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        return this.withExistingParent(itemPath, this.mcLoc("item/generated"))
                .texture("layer0", "item/" + itemPath + "_base")
                .texture("layer1", "item/tipped_bolt_head")
                .getLocation();
    }

    protected ResourceLocation createQuiverModels(Item item, int variantCount) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        List<ResourceLocation> variants = new ArrayList<>();
        for (int i = 0; i < variantCount; i++) {
            String modelVariant = itemPath + "_" + (i + 1);
            variants.add(
                    this.withExistingParent(modelVariant, this.mcLoc("item/generated"))
                            .texture("layer0", "item/" + modelVariant)
                            .getLocation());
        }
        ItemModelBuilder modelBuilder =
                this.withExistingParent(itemPath, this.mcLoc("item/generated"))
                        .texture("layer0", "item/" + itemPath + "_base");
        for (int j = 0; j < variants.size(); j++) {
            modelBuilder
                    .override()
                    .predicate(ModelOverrides.ARROW, j + 1)
                    .model(new ExistingModelFile(variants.get(j), this.existingFileHelper))
                    .end();
        }
        return modelBuilder.getLocation();
    }

    protected ResourceLocation createWeaponOilModel(Item item) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        return this.withExistingParent(itemPath, this.mcLoc("item/generated"))
                .texture("layer0", "item/" + itemPath + "_bottle")
                .texture("layer1", "item/" + itemPath + "_bottle_overlay")
                .getLocation();
    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Item Models";
    }

    private record WeaponModelGroup(
            ModItems.WeaponItemGroup<? extends Item> items,
            BiConsumer<ModelGenerator, Item> modelFactory) {
        private void createModels(ModelGenerator generator) {
            for (Item item : this.items.getAsList()) {
                this.modelFactory.accept(generator, item);
            }
        }
    }
}
