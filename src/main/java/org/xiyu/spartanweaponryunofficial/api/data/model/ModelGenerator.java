package org.xiyu.spartanweaponryunofficial.api.data.model;

import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import org.xiyu.spartanweaponryunofficial.api.ModelOverrides;
import org.xiyu.spartanweaponryunofficial.api.data.OilCoatingTextures;

/**
 * Contains helper methods to generate customised model files based off items from Spartan Weaponry.
 * <br>
 * Add to the addon mod's {@linkplain ItemModelProvider#registerModels()} method to use them
 *
 * @author ObliviousSpartan
 */
public class ModelGenerator {
    protected final ItemModelProvider itemModelProvider;

    public ModelGenerator(ItemModelProvider itemModelProviderIn) {
        this.itemModelProvider = itemModelProviderIn;
    }

    /**
     * Convert registration name to texture file name. For ranged/throwing weapons:
     * "weapontype_material" -> "material_weapontype" Handles all material names used in the mod.
     *
     * @param registrationName The item's registration name
     * @return The corresponding texture file name
     */
    private String convertRegistrationNameToTextureName(String registrationName) {
        // List of all material suffixes
        String[] materials = {
            "_wooden",
            "_stone",
            "_leather",
            "_copper",
            "_iron",
            "_golden",
            "_diamond",
            "_netherite",
            "_tin",
            "_bronze",
            "_steel",
            "_silver",
            "_electrum",
            "_lead",
            "_nickel",
            "_invar",
            "_constantan",
            "_platinum",
            "_aluminum"
        };

        for (String material : materials) {
            if (registrationName.endsWith(material)) {
                String weaponType =
                        registrationName.substring(
                                0, registrationName.length() - material.length());
                // Remove leading underscore from material and prepend to weapon type
                return material.substring(1) + "_" + weaponType;
            }
        }

        return registrationName;
    }

    /**
     * Generates a model using the same base model as most mundane items
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createSimpleModel(Item item) {
        return this.createSimpleModel(item, null, "");
    }

    /**
     * Generates a model using the same base model as most mundane items
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createSimpleModel(Item item, String textureFolderPath) {
        return this.createSimpleModel(item, null, textureFolderPath);
    }

    /**
     * Generates a model using a defined parent model
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param parent The location of the parent model
     * @return The generated models location
     */
    public ResourceLocation createSimpleModel(Item item, ResourceLocation parent) {
        return this.createSimpleModel(item, parent, "");
    }

    /**
     * Generates a model using a defined parent model
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param parent The location of the parent model. If null, the parent model will default to
     *     "minecraft:item/generated"
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createSimpleModel(
            Item item, @Nullable ResourceLocation parent, String textureFolderPath) {
        ResourceLocation parentPath =
                parent != null ? parent : this.itemModelProvider.mcLoc("item/generated");
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        String textureName = convertRegistrationNameToTextureName(itemPath);
        return this.itemModelProvider
                .withExistingParent(itemPath, parentPath)
                .texture("layer0", texturePath + textureName)
                .getLocation();
    }

    /**
     * Generates a model designed for any melee weapon. Will generate overrides for any event that
     * the weapon has the Melee Block or Throwable Weapon Traits
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param coatingTexture The coating texture location, including the path. The path is
     *     independent from the base texture path
     * @return The generated models location
     */
    public ResourceLocation createMeleeWeaponModels(
            Item item, ResourceLocation baseModel, ResourceLocation coatingTexture) {
        return this.createMeleeWeaponModels(item, baseModel, coatingTexture, "");
    }

    /**
     * Generates a model designed for any melee weapon. Will generate overrides for any event that
     * the weapon has the Melee Block or Throwable Weapon Traits
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param coatingTexture The coating texture location, including the path. The path is
     *     independent from the base texture path
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createMeleeWeaponModels(
            Item item,
            ResourceLocation baseModel,
            ResourceLocation coatingTexture,
            String textureFolderPath) {
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        String textureName = convertRegistrationNameToTextureName(itemPath);
        ResourceLocation blockingModel =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_blocking",
                                ResourceLocation.tryBuild(
                                        baseModel.getNamespace(),
                                        baseModel.getPath() + "_blocking"))
                        .customLoader(OilCoatingItemModelBuilder::new)
                        .end()
                        .texture("layer0", texturePath + textureName)
                        .texture("coating", coatingTexture)
                        .getLocation();
        ResourceLocation throwingModel =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_throwing",
                                ResourceLocation.tryBuild(
                                        baseModel.getNamespace(),
                                        baseModel.getPath() + "_throwing"))
                        .customLoader(OilCoatingItemModelBuilder::new)
                        .end()
                        .texture("layer0", texturePath + textureName)
                        .texture("coating", coatingTexture)
                        .getLocation();
        return this.itemModelProvider
                .withExistingParent(itemPath, baseModel)
                .customLoader(OilCoatingItemModelBuilder::new)
                .end()
                .texture("layer0", texturePath + textureName)
                .texture("coating", coatingTexture)
                .override()
                .predicate(ModelOverrides.BLOCKING, 1.0f)
                .model(
                        new ExistingModelFile(
                                blockingModel, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.THROWING, 1.0f)
                .model(
                        new ExistingModelFile(
                                throwingModel, this.itemModelProvider.existingFileHelper))
                .end()
                .getLocation();
    }

    /**
     * Generates a model designed for the Cestus. Will generate overrides for any event that the
     * weapon has the Melee Block or Throwable Weapon Traits
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param coatingTexture Isn't used due to technical constraints of 3D models not having layers
     * @return The generated models location
     */
    public ResourceLocation createCestusModels(
            Item item, ResourceLocation baseModel, ResourceLocation coatingTexture) {
        return this.createCestusModels(item, baseModel, coatingTexture, "");
    }

    /**
     * Generates a model designed for the Cestus. Will generate overrides for any event that the
     * weapon has the Melee Block or Throwable Weapon Traits
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param coatingTexture Isn't used due to technical constraints of 3D models not having layers
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createCestusModels(
            Item item,
            ResourceLocation baseModel,
            ResourceLocation coatingTexture,
            String textureFolderPath) {
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        String textureName = convertRegistrationNameToTextureName(itemPath);
        ResourceLocation blockingModel =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_blocking",
                                ResourceLocation.tryBuild(
                                        baseModel.getNamespace(),
                                        baseModel.getPath() + "_blocking"))
                        .texture("layer0", texturePath + textureName)
                        .getLocation();
        ResourceLocation throwingModel =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_throwing",
                                ResourceLocation.tryBuild(
                                        baseModel.getNamespace(),
                                        baseModel.getPath() + "_throwing"))
                        .texture("layer0", texturePath + textureName)
                        .getLocation();
        return this.itemModelProvider
                .withExistingParent(itemPath, baseModel)
                .texture("layer0", texturePath + textureName)
                .texture("particle", texturePath + textureName)
                .override()
                .predicate(ModelOverrides.BLOCKING, 1.0f)
                .model(
                        new ExistingModelFile(
                                blockingModel, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.THROWING, 1.0f)
                .model(
                        new ExistingModelFile(
                                throwingModel, this.itemModelProvider.existingFileHelper))
                .end()
                .getLocation();
    }

    /**
     * Generates a model designed for any throwing weapon
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param baseThrowingModel The base throwing model location to use. See above parameter for the
     *     location for this
     * @param emptyModel The base empty model location to use. See 'baseModel' parameter for the
     *     location for this
     * @return The generated models location
     */
    public ResourceLocation createThrowingWeaponModels(
            Item item,
            ResourceLocation baseModel,
            ResourceLocation baseThrowingModel,
            ResourceLocation emptyModel) {
        return this.createThrowingWeaponModels(item, baseModel, baseThrowingModel, emptyModel, "");
    }

    /**
     * Generates a model designed for any throwing weapon
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param baseModel The base model location to use. These values are conveniently stored here
     *     {@linkplain BaseModels}
     * @param baseThrowingModel The base throwing model location to use. See above parameter for the
     *     location for this
     * @param emptyModel The base empty model location to use. See 'baseModel' parameter for the
     *     location for this
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createThrowingWeaponModels(
            Item item,
            ResourceLocation baseModel,
            ResourceLocation baseThrowingModel,
            ResourceLocation emptyModel,
            String textureFolderPath) {
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        String textureName = convertRegistrationNameToTextureName(itemPath);
        ResourceLocation throwingModel =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_throwing", baseThrowingModel)
                        .texture("layer0", texturePath + textureName)
                        .getLocation();
        return this.itemModelProvider
                .withExistingParent(itemPath, baseModel)
                .texture("layer0", texturePath + textureName)
                .override()
                .predicate(ModelOverrides.THROWING, 1.0f)
                .predicate(ModelOverrides.EMPTY, 0.0f)
                .model(
                        new ExistingModelFile(
                                throwingModel, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.EMPTY, 1.0f)
                .model(new ExistingModelFile(emptyModel, this.itemModelProvider.existingFileHelper))
                .end()
                .getLocation();
    }

    /**
     * Generates standard and throwing models using the same base model as a Vanilla Sword
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createVanillaSwordModels(Item item) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        String textureName = convertRegistrationNameToTextureName(itemPath);
        return this.itemModelProvider
                .withExistingParent(itemPath, "minecraft:item/handheld")
                .customLoader(OilCoatingItemModelBuilder::new)
                .end()
                .texture("layer0", "minecraft:item/" + itemPath)
                .texture("coating", OilCoatingTextures.VANILLA_SWORD)
                .getLocation();
    }

    /**
     * Generates standard and throwing models using the same base model as a Dagger
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createDaggerModels(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.DAGGER, OilCoatingTextures.DAGGER);
    }

    /**
     * Generates standard and throwing models using the same base model as a Dagger
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createDaggerModels(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.DAGGER, OilCoatingTextures.DAGGER, textureFolderPath);
    }

    /**
     * Generates standard and blocking models using the same base model as a Parrying Dagger
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createParryingDaggerModels(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.PARRYING_DAGGER, OilCoatingTextures.PARRYING_DAGGER);
    }

    /**
     * Generates standard and blocking models using the same base model as a Parrying Dagger
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createParryingDaggerModels(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item,
                BaseModels.PARRYING_DAGGER,
                OilCoatingTextures.PARRYING_DAGGER,
                textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Longsword
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createLongswordModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.LONGSWORD, OilCoatingTextures.LONGSWORD);
    }

    /**
     * Generates a model using the same base model as a Longsword
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createLongswordModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.LONGSWORD, OilCoatingTextures.LONGSWORD, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Katana
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createKatanaModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.KATANA, OilCoatingTextures.KATANA);
    }

    /**
     * Generates a model using the same base model as a Katana
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createKatanaModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.KATANA, OilCoatingTextures.KATANA, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Saber
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createSaberModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.SABER, OilCoatingTextures.SABER);
    }

    /**
     * Generates a model using the same base model as a Saber
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createSaberModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.SABER, OilCoatingTextures.SABER, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Rapier
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createRapierModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.RAPIER, OilCoatingTextures.RAPIER);
    }

    /**
     * Generates a model using the same base model as a Rapier
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createRapierModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.RAPIER, OilCoatingTextures.RAPIER, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Greatsword
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createGreatswordModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.GREATSWORD, OilCoatingTextures.GREATSWORD);
    }

    /**
     * Generates a model using the same base model as a Greatsword
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createGreatswordModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.GREATSWORD, OilCoatingTextures.GREATSWORD, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Club
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createClubModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.CLUB, OilCoatingTextures.CLUB);
    }

    /**
     * Generates a model using the same base model as a Club
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createClubModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.CLUB, OilCoatingTextures.CLUB, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Cestus
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createCestusModel(Item item) {
        return this.createCestusModels(item, BaseModels.CESTUS, OilCoatingTextures.CESTUS);
    }

    /**
     * Generates a model using the same base model as a Cestus
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createCestusModel(Item item, String textureFolderPath) {
        return this.createCestusModels(
                item, BaseModels.CESTUS, OilCoatingTextures.CESTUS, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Battle Hammer
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createBattleHammerModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.BATTLE_HAMMER, OilCoatingTextures.BATTLE_HAMMER);
    }

    /**
     * Generates a model using the same base model as a Battle Hammer
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createBattleHammerModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item,
                BaseModels.BATTLE_HAMMER,
                OilCoatingTextures.BATTLE_HAMMER,
                textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Warhammer
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createWarhammerModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.WARHAMMER, OilCoatingTextures.WARHAMMER);
    }

    /**
     * Generates a model using the same base model as a Warhammer
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createWarhammerModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.WARHAMMER, OilCoatingTextures.WARHAMMER, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Spear
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createSpearModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.SPEAR, OilCoatingTextures.SPEAR);
    }

    /**
     * Generates a model using the same base model as a Spear
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createSpearModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.SPEAR, OilCoatingTextures.SPEAR, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Halberd
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createHalberdModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.HALBERD, OilCoatingTextures.HALBERD);
    }

    /**
     * Generates a model using the same base model as a Halberd
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createHalberdModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.HALBERD, OilCoatingTextures.HALBERD, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Pike
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createPikeModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.PIKE, OilCoatingTextures.PIKE);
    }

    /**
     * Generates a model using the same base model as a Pike
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createPikeModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.PIKE, OilCoatingTextures.PIKE, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Lance
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createLanceModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.LANCE, OilCoatingTextures.LANCE);
    }

    /**
     * Generates a model using the same base model as a Lance
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createLanceModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.LANCE, OilCoatingTextures.LANCE, textureFolderPath);
    }

    /**
     * Generates standard and 3 drawing models using the same base model as a Longbow
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createLongbowModels(Item item) {
        return this.createLongbowModels(item, "");
    }

    /**
     * Generates standard and 3 drawing models using the same base model as a Longbow
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createLongbowModels(Item item, String textureFolderPath) {
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        // Convert new format (longbow_material_strengthened) to texture file format
        // (material_longbow)
        String textureName = itemPath;
        if (itemPath.startsWith("longbow_") && itemPath.endsWith("_strengthened")) {
            String material =
                    itemPath.substring(
                            "longbow_".length(), itemPath.length() - "_strengthened".length());
            textureName = material + "_longbow";
        } else {
            textureName = convertRegistrationNameToTextureName(itemPath);
        }
        ResourceLocation pulling0 =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_pulling_0", BaseModels.LONGBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_0")
                        .getLocation();
        ResourceLocation pulling1 =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_pulling_1", BaseModels.LONGBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_1")
                        .getLocation();
        ResourceLocation pulling2 =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_pulling_2", BaseModels.LONGBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_2")
                        .getLocation();
        return this.itemModelProvider
                .withExistingParent(itemPath, BaseModels.LONGBOW)
                .texture("layer0", texturePath + textureName + "_standby")
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .model(new ExistingModelFile(pulling0, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .predicate(ModelOverrides.PULL, 0.65f)
                .model(new ExistingModelFile(pulling1, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .predicate(ModelOverrides.PULL, 0.9f)
                .model(new ExistingModelFile(pulling2, this.itemModelProvider.existingFileHelper))
                .end()
                .getLocation();
    }

    /**
     * Generates 1 standard, 3 drawing, 1 loaded and 1 firing models using the same base model as a
     * Heavy Crossbow
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createHeavyCrossbowModels(Item item) {
        return this.createHeavyCrossbowModels(item, "");
    }

    /**
     * Generates 1 standard, 3 drawing, 1 loaded and 1 firing models using the same base model as a
     * Heavy Crossbow
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createHeavyCrossbowModels(Item item, String textureFolderPath) {
        String texturePath =
                textureFolderPath.isEmpty() ? "item/" : "item/" + textureFolderPath + "/";
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        // Convert new format (heavy_crossbow_material_strengthened) to texture file format
        // (material_heavy_crossbow)
        String textureName = itemPath;
        if (itemPath.startsWith("heavy_crossbow_") && itemPath.endsWith("_strengthened")) {
            String material =
                    itemPath.substring(
                            "heavy_crossbow_".length(),
                            itemPath.length() - "_strengthened".length());
            textureName = material + "_heavy_crossbow";
        } else {
            textureName = convertRegistrationNameToTextureName(itemPath);
        }
        ResourceLocation pulling0 =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_pulling_0", BaseModels.HEAVY_CROSSBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_0")
                        .getLocation();
        ResourceLocation pulling1 =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_pulling_1", BaseModels.HEAVY_CROSSBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_1")
                        .getLocation();
        ResourceLocation pulling2 =
                this.itemModelProvider
                        .withExistingParent(
                                itemPath + "_pulling_2", BaseModels.HEAVY_CROSSBOW_PULLING)
                        .texture("layer0", texturePath + textureName + "_pulling_2")
                        .getLocation();
        ResourceLocation loaded =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_loaded", BaseModels.HEAVY_CROSSBOW_LOADED)
                        .texture("layer0", texturePath + textureName + "_loaded")
                        .getLocation();
        ResourceLocation firing =
                this.itemModelProvider
                        .withExistingParent(itemPath + "_firing", BaseModels.HEAVY_CROSSBOW_FIRING)
                        .texture("layer0", texturePath + textureName + "_loaded")
                        .getLocation();
        return this.itemModelProvider
                .withExistingParent(itemPath, BaseModels.HEAVY_CROSSBOW)
                .texture("layer0", texturePath + textureName + "_standby")
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .model(new ExistingModelFile(pulling0, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .predicate(ModelOverrides.PULL, 0.65f)
                .model(new ExistingModelFile(pulling1, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .predicate(ModelOverrides.PULL, 1.0f)
                .model(new ExistingModelFile(pulling2, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.CHARGED, 1.0f)
                .model(new ExistingModelFile(loaded, this.itemModelProvider.existingFileHelper))
                .end()
                .override()
                .predicate(ModelOverrides.PULLING, 1.0f)
                .predicate(ModelOverrides.CHARGED, 1.0f)
                .model(new ExistingModelFile(firing, this.itemModelProvider.existingFileHelper))
                .end()
                .getLocation();
    }

    /**
     * Generates standard and throwing models using the same base model as a Throwing Knife
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createThrowingKnifeModels(Item item) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.THROWING_KNIFE,
                BaseModels.THROWING_KNIFE_THROWING,
                BaseModels.THROWING_KNIFE_EMPTY);
    }

    /**
     * Generates standard and throwing models using the same base model as a Throwing Knife
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createThrowingKnifeModels(Item item, String textureFolderPath) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.THROWING_KNIFE,
                BaseModels.THROWING_KNIFE_THROWING,
                BaseModels.THROWING_KNIFE_EMPTY,
                textureFolderPath);
    }

    /**
     * Generates standard and throwing models using the same base model as a Tomahawk
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createTomahawkModels(Item item) {
        return this.createThrowingWeaponModels(
                item, BaseModels.TOMAHAWK, BaseModels.TOMAHAWK_THROWING, BaseModels.TOMAHAWK_EMPTY);
    }

    /**
     * Generates standard and throwing models using the same base model as a Tomahawk
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createTomahawkModels(Item item, String textureFolderPath) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.TOMAHAWK,
                BaseModels.TOMAHAWK_THROWING,
                BaseModels.TOMAHAWK_EMPTY,
                textureFolderPath);
    }

    /**
     * Generates standard and throwing models using the same base model as a Javelin
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createJavelinModels(Item item) {
        return this.createThrowingWeaponModels(
                item, BaseModels.JAVELIN, BaseModels.JAVELIN_THROWING, BaseModels.JAVELIN_EMPTY);
    }

    /**
     * Generates standard and throwing models using the same base model as a Javelin
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createJavelinModels(Item item, String textureFolderPath) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.JAVELIN,
                BaseModels.JAVELIN_THROWING,
                BaseModels.JAVELIN_EMPTY,
                textureFolderPath);
    }

    /**
     * Generates standard and throwing models using the same base model as a Boomerang
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createBoomerangModels(Item item) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.BOOMERANG,
                BaseModels.BOOMERANG_THROWING,
                BaseModels.BOOMERANG_EMPTY);
    }

    /**
     * Generates standard and throwing models using the same base model as a Boomerang
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createBoomerangModels(Item item, String textureFolderPath) {
        return this.createThrowingWeaponModels(
                item,
                BaseModels.BOOMERANG,
                BaseModels.BOOMERANG_THROWING,
                BaseModels.BOOMERANG_EMPTY,
                textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Battleaxe
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createBattleaxeModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.BATTLEAXE, OilCoatingTextures.BATTLEAXE);
    }

    /**
     * Generates a model using the same base model as a Battleaxe
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createBattleaxeModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.BATTLEAXE, OilCoatingTextures.BATTLEAXE, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Flanged Mace
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createFlangedMaceModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.FLANGED_MACE, OilCoatingTextures.FLANGED_MACE);
    }

    /**
     * Generates a model using the same base model as a Flanged Mace
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createFlangedMaceModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.FLANGED_MACE, OilCoatingTextures.FLANGED_MACE, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Glaive
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createGlaiveModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.GLAIVE, OilCoatingTextures.GLAIVE);
    }

    /**
     * Generates a model using the same base model as a Glaive
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createGlaiveModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.GLAIVE, OilCoatingTextures.GLAIVE, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Quarterstaff
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createQuarterstaffModel(Item item) {
        return this.createMeleeWeaponModels(
                item, BaseModels.QUARTERSTAFF, OilCoatingTextures.QUARTERSTAFF);
    }

    /**
     * Generates a model using the same base model as a Quarterstaff
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createQuarterstaffModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.QUARTERSTAFF, OilCoatingTextures.QUARTERSTAFF, textureFolderPath);
    }

    /**
     * Generates a model using the same base model as a Scythe
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @return The generated models location
     */
    public ResourceLocation createScytheModel(Item item) {
        return this.createMeleeWeaponModels(item, BaseModels.SCYTHE, OilCoatingTextures.SCYTHE);
    }

    /**
     * Generates a model using the same base model as a Scythe
     *
     * @param item The item to generate the model for. The registry name is used for the texture
     *     name
     * @param textureFolderPath The texture folder path to look for the texture in
     * @return The generated models location
     */
    public ResourceLocation createScytheModel(Item item, String textureFolderPath) {
        return this.createMeleeWeaponModels(
                item, BaseModels.SCYTHE, OilCoatingTextures.SCYTHE, textureFolderPath);
    }
}
