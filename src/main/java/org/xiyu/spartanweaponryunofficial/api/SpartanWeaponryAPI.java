package org.xiyu.spartanweaponryunofficial.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Main addon-facing entry point for Spartan Weaponry Unofficial.
 * <p>
 * Addons should create compatible weapons through {@link #createWeapon(WeaponItemType, WeaponMaterial)}
 * or the legacy {@code createXxx(WeaponMaterial)} wrappers, then register the returned item in their
 * own registry. Classification and tag helper methods are metadata helpers; they do not register
 * game content or write data files by themselves.
 * <p>
 * The internal handler bridge is initialized by Spartan Weaponry during mod construction and is not
 * part of the addon extension surface.
 */
public class SpartanWeaponryAPI {
    public static final int API_VERSION = 14;
    public static final String MOD_ID = "spartan_weaponry_unofficial";

    private static IInternalMethodHandler internalHandler = null;
    private static final Map<Item, WeaponClassification> weaponClassifications = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Addon-facing weapon descriptors for the built-in Spartan Weaponry weapon factories.
     * <p>
     * These descriptors are a stable API layer over the existing {@code createXxx(WeaponMaterial)}
     * methods. They do not register items and they do not replace the existing methods.
     */
    public enum WeaponItemType {
        DAGGER("dagger", "daggers", WeaponType.MELEE, IInternalMethodHandler::addDagger),
        PARRYING_DAGGER("parrying_dagger", "parrying_daggers", WeaponType.MELEE, IInternalMethodHandler::addParryingDagger),
        LONGSWORD("longsword", "longswords", WeaponType.MELEE, IInternalMethodHandler::addLongsword),
        KATANA("katana", "katanas", WeaponType.MELEE, IInternalMethodHandler::addKatana),
        SABER("saber", "sabers", WeaponType.MELEE, IInternalMethodHandler::addSaber),
        RAPIER("rapier", "rapiers", WeaponType.MELEE, IInternalMethodHandler::addRapier),
        GREATSWORD("greatsword", "greatswords", WeaponType.MELEE, IInternalMethodHandler::addGreatsword),
        BATTLE_HAMMER("battle_hammer", "battle_hammers", WeaponType.MELEE, IInternalMethodHandler::addBattleHammer),
        WARHAMMER("warhammer", "warhammers", WeaponType.MELEE, IInternalMethodHandler::addWarhammer),
        SPEAR("spear", "spears", WeaponType.MELEE, IInternalMethodHandler::addSpear),
        HALBERD("halberd", "halberds", WeaponType.MELEE, IInternalMethodHandler::addHalberd),
        PIKE("pike", "pikes", WeaponType.MELEE, IInternalMethodHandler::addPike),
        LANCE("lance", "lances", WeaponType.MELEE, IInternalMethodHandler::addLance),
        LONGBOW("longbow", "longbows", WeaponType.RANGED, IInternalMethodHandler::addLongbow),
        HEAVY_CROSSBOW("heavy_crossbow", "heavy_crossbows", WeaponType.RANGED, IInternalMethodHandler::addHeavyCrossbow),
        THROWING_KNIFE("throwing_knife", "throwing_knives", WeaponType.THROWING, IInternalMethodHandler::addThrowingKnife),
        TOMAHAWK("tomahawk", "tomahawks", WeaponType.THROWING, IInternalMethodHandler::addTomahawk),
        JAVELIN("javelin", "javelins", WeaponType.THROWING, IInternalMethodHandler::addJavelin),
        BOOMERANG("boomerang", "boomerangs", WeaponType.THROWING, IInternalMethodHandler::addBoomerang),
        BATTLEAXE("battleaxe", "battleaxes", WeaponType.MELEE, IInternalMethodHandler::addBattleaxe),
        FLANGED_MACE("flanged_mace", "flanged_maces", WeaponType.MELEE, IInternalMethodHandler::addFlangedMace),
        GLAIVE("glaive", "glaives", WeaponType.MELEE, IInternalMethodHandler::addGlaive),
        QUARTERSTAFF("quarterstaff", "quarterstaves", WeaponType.MELEE, IInternalMethodHandler::addQuarterstaff),
        SCYTHE("scythe", "scythes", WeaponType.MELEE, IInternalMethodHandler::addScythe);

        private final String serializedName;
        private final String pluralName;
        private final WeaponType weaponType;
        private final BiFunction<IInternalMethodHandler, WeaponMaterial, Item> factory;

        WeaponItemType(String serializedName, String pluralName, WeaponType weaponType, BiFunction<IInternalMethodHandler, WeaponMaterial, Item> factory) {
            this.serializedName = serializedName;
            this.pluralName = pluralName;
            this.weaponType = weaponType;
            this.factory = factory;
        }

        /**
         * Returns the stable lowercase id segment used by this weapon type, for example
         * {@code longsword} or {@code heavy_crossbow}.
         */
        public String getSerializedName() {
            return this.serializedName;
        }

        /**
         * Returns the plural id segment used for grouped weapon item tags, for example
         * {@code longswords}, {@code heavy_crossbows}, or {@code throwing_knives}.
         */
        public String getPluralName() {
            return this.pluralName;
        }

        /**
         * Returns the item tag path used for this weapon type under the Spartan Weaponry namespace.
         */
        public String getTagPath() {
            return "weapons/" + this.pluralName;
        }

        /**
         * Returns the broad trait category used by this weapon type.
         */
        public WeaponType getWeaponType() {
            return this.weaponType;
        }

        private Item create(IInternalMethodHandler handler, WeaponMaterial material) {
            return this.factory.apply(handler, material);
        }
    }

    /**
     * Use this method in your addon mod to ensure that the API is of the correct version.
     * Use in your mod class constructor. This will throw an exception if the loaded
     * Spartan Weaponry API is older than the requested version.
     *
     * @param modId   The addon mod id requesting the API version
     * @param version The minimum expected version
     */
    public static void assertAPIVersion(String modId, int version) {
        if (version > API_VERSION) {
            throw new IllegalStateException("Spartan Weaponry API version mismatch for addon \"" + modId
                    + "\": expected at least " + version + ", but loaded " + API_VERSION + ".");
        }
    }

    /**
     * Creates a weapon item using an addon-facing descriptor.
     * <p>
     * This is equivalent to the matching {@code createXxx(WeaponMaterial)} method. The returned
     * item is not registered; the caller is responsible for registering the item and recipe.
     *
     * @param weaponType The built-in Spartan Weaponry weapon type to create
     * @param material   The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createWeapon(WeaponItemType weaponType, WeaponMaterial material) {
        WeaponItemType type = Objects.requireNonNull(weaponType, "weaponType");
        WeaponMaterial weaponMaterial = Objects.requireNonNull(material, "material");
        return classifyWeapon(type.create(requireInternalHandler(), weaponMaterial), type, weaponMaterial);
    }

    /**
     * Records weapon classification metadata on an item and returns the same item.
     * <p>
     * This is called automatically by {@link #createWeapon(WeaponItemType, WeaponMaterial)}
     * and the legacy {@code createXxx(WeaponMaterial)} methods. Addons that create compatible
     * items without these factories can call this method to opt into classification queries and
     * data-generation tag helpers.
     *
     * @param item       The item to classify
     * @param weaponType The weapon type represented by the item
     * @param material   The material represented by the item
     * @return The same item instance, for convenient use in registration lambdas
     */
    public static <T extends Item> T classifyWeapon(T item, WeaponItemType weaponType, WeaponMaterial material) {
        T weapon = Objects.requireNonNull(item, "item");
        weaponClassifications.put(weapon, new WeaponClassification(weaponType, material));
        return weapon;
    }

    /**
     * Returns classification metadata for an item if it was created or classified through this API.
     */
    public static Optional<WeaponClassification> getWeaponClassification(Item item) {
        return Optional.ofNullable(weaponClassifications.get(Objects.requireNonNull(item, "item")));
    }

    /**
     * Returns classification metadata for a registered item id if it was created or classified through this API.
     */
    public static Optional<WeaponClassification> getWeaponClassification(ResourceLocation itemId) {
        Item item = BuiltInRegistries.ITEM.get(Objects.requireNonNull(itemId, "itemId"));
        return item != null && itemId.equals(BuiltInRegistries.ITEM.getKey(item)) ? getWeaponClassification(item) : Optional.empty();
    }

    /**
     * Returns a snapshot of the item classifications known to the API.
     * <p>
     * The map only contains items created through this API or manually passed to
     * {@link #classifyWeapon(Item, WeaponItemType, WeaponMaterial)}. Datapacks can still add items to
     * the same tags without appearing in this runtime metadata snapshot.
     */
    public static Map<Item, WeaponClassification> getKnownWeaponClassifications() {
        synchronized (weaponClassifications) {
            return Map.copyOf(weaponClassifications);
        }
    }

    /**
     * Emits standard item tag assignments for every classified weapon.
     * <p>
     * This is intended for data providers. A typical provider can call
     * {@code SpartanWeaponryAPI.forEachKnownWeaponTag((tag, item) -> this.tag(tag).add(item));}.
     * Runtime tag membership is still controlled by generated datapack JSON, not this method.
     */
    public static void forEachKnownWeaponTag(BiConsumer<TagKey<Item>, Item> consumer) {
        getKnownWeaponClassifications().forEach((item, classification) -> emitWeaponTags(item, classification, consumer));
    }

    /**
     * Emits standard item tag assignments only for classified weapons whose registered id uses the given namespace.
     */
    public static void forEachKnownWeaponTag(String namespace, BiConsumer<TagKey<Item>, Item> consumer) {
        Objects.requireNonNull(namespace, "namespace");
        getKnownWeaponClassifications().forEach((item, classification) -> {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (namespace.equals(itemId.getNamespace())) {
                emitWeaponTags(item, classification, consumer);
            }
        });
    }

    public static TagKey<Item> getWeaponTag(WeaponItemType weaponType) {
        return ModItemTags.weaponType(weaponType);
    }

    public static TagKey<Item> getMaterialTag(WeaponMaterial material) {
        return ModItemTags.material(Objects.requireNonNull(material, "material"));
    }

    public static TagKey<Item> getMaterialTag(String materialName) {
        return ModItemTags.material(materialName);
    }

    public static TagKey<Item> getNamespaceTag(String namespace) {
        return ModItemTags.namespace(namespace);
    }

    public static TagKey<Item> getNamespaceTag(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(Objects.requireNonNull(item, "item"));
        return getNamespaceTag(itemId.getNamespace());
    }

    private static void emitWeaponTags(Item item, WeaponClassification classification, BiConsumer<TagKey<Item>, Item> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(ModItemTags.WEAPONS, item);
        consumer.accept(classification.weaponTag(), item);
        consumer.accept(classification.materialTag(), item);
        consumer.accept(getNamespaceTag(item), item);
    }

    //---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
    // Weapon Creation methods
    //---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * Creates a new dagger using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createDagger(WeaponMaterial material) {
        return createWeapon(WeaponItemType.DAGGER, material);
    }

    /**
     * Creates a new parrying dagger using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createParryingDagger(WeaponMaterial material) {
        return createWeapon(WeaponItemType.PARRYING_DAGGER, material);
    }

    /**
     * Creates a new longsword using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createLongsword(WeaponMaterial material) {
        return createWeapon(WeaponItemType.LONGSWORD, material);
    }

    /**
     * Creates a new katana using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createKatana(WeaponMaterial material) {
        return createWeapon(WeaponItemType.KATANA, material);
    }

    /**
     * Creates a new saber using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createSaber(WeaponMaterial material) {
        return createWeapon(WeaponItemType.SABER, material);
    }

    /**
     * Creates a new rapier using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createRapier(WeaponMaterial material) {
        return createWeapon(WeaponItemType.RAPIER, material);
    }

    /**
     * Creates a new greatsword using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createGreatsword(WeaponMaterial material) {
        return createWeapon(WeaponItemType.GREATSWORD, material);
    }

    /**
     * Creates a new battle hammer using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createBattleHammer(WeaponMaterial material) {
        return createWeapon(WeaponItemType.BATTLE_HAMMER, material);
    }

    /**
     * Creates a new warhammer using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createWarhammer(WeaponMaterial material) {
        return createWeapon(WeaponItemType.WARHAMMER, material);
    }

    /**
     * Creates a new spear using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createSpear(WeaponMaterial material) {
        return createWeapon(WeaponItemType.SPEAR, material);
    }

    /**
     * Creates a new halberd using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createHalberd(WeaponMaterial material) {
        return createWeapon(WeaponItemType.HALBERD, material);
    }

    /**
     * Creates a new pike using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createPike(WeaponMaterial material) {
        return createWeapon(WeaponItemType.PIKE, material);
    }

    /**
     * Creates a new lance using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createLance(WeaponMaterial material) {
        return createWeapon(WeaponItemType.LANCE, material);
    }

    /**
     * Creates a new longbow using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createLongbow(WeaponMaterial material) {
        return createWeapon(WeaponItemType.LONGBOW, material);
    }

    /**
     * Creates a new heavy crossbow using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createHeavyCrossbow(WeaponMaterial material) {
        return createWeapon(WeaponItemType.HEAVY_CROSSBOW, material);
    }

    /**
     * Creates a new throwing knife using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createThrowingKnife(WeaponMaterial material) {
        return createWeapon(WeaponItemType.THROWING_KNIFE, material);
    }

    /**
     * Creates a new tomahawk using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createTomahawk(WeaponMaterial material) {
        return createWeapon(WeaponItemType.TOMAHAWK, material);
    }

    /**
     * Creates a new javelin using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createJavelin(WeaponMaterial material) {
        return createWeapon(WeaponItemType.JAVELIN, material);
    }

    /**
     * Creates a new boomerang using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createBoomerang(WeaponMaterial material) {
        return createWeapon(WeaponItemType.BOOMERANG, material);
    }

    /**
     * Creates a new battleaxe using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createBattleaxe(WeaponMaterial material) {
        return createWeapon(WeaponItemType.BATTLEAXE, material);
    }

    /**
     * Creates a new flanged mace using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createFlangedMace(WeaponMaterial material) {
        return createWeapon(WeaponItemType.FLANGED_MACE, material);
    }

    /**
     * Creates a new glaive using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createGlaive(WeaponMaterial material) {
        return createWeapon(WeaponItemType.GLAIVE, material);
    }

    /**
     * Creates a new quarterstaff using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createQuarterstaff(WeaponMaterial material) {
        return createWeapon(WeaponItemType.QUARTERSTAFF, material);
    }

    /**
     * Creates a new scythe using the specified material. The caller is responsible for registering the weapon item and recipe.
     *
     * @param material The material that the weapon is made of
     * @return The newly created weapon
     */
    public static Item createScythe(WeaponMaterial material) {
        return createWeapon(WeaponItemType.SCYTHE, material);
    }

    //---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
    // Internal methods. DO NOT USE!
    //---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * This is used to initialize the API and its internal handler and should only be called once during execution.<br>
     * This is already called during Spartan Weaponry's mod construction. Calling it a second time will cause a crash.
     */
    public static void init(IInternalMethodHandler handler) {
        if (internalHandler != null) {
            throw new IllegalStateException("Something attempted to replace the Spartan Weaponry API internal handler.\n"
                    + "Remove the mod that has tampered with that handler.");
        }

        internalHandler = Objects.requireNonNull(handler, "handler");
        Log.info("Spartan Weaponry API version " + API_VERSION + " has been initialized!");
    }

    private static IInternalMethodHandler requireInternalHandler() {
        if (internalHandler == null) {
            throw new IllegalStateException("Spartan Weaponry API has not been initialized yet. "
                    + "Weapon creation is only available after Spartan Weaponry finishes mod construction.");
        }
        return internalHandler;
    }
}
