package org.xiyu.spartanweaponryunofficial.api;

import net.minecraft.world.item.Item;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import java.util.Objects;
import java.util.function.BiFunction;

public class SpartanWeaponryAPI {
    public static final int API_VERSION = 13;
    public static final String MOD_ID = "spartan_weaponry_unofficial";

    private static IInternalMethodHandler internalHandler = null;

    /**
     * Addon-facing weapon descriptors for the built-in Spartan Weaponry weapon factories.
     * <p>
     * These descriptors are a stable API layer over the existing {@code createXxx(WeaponMaterial)}
     * methods. They do not register items and they do not replace the existing methods.
     */
    public enum WeaponItemType {
        DAGGER("dagger", WeaponType.MELEE, IInternalMethodHandler::addDagger),
        PARRYING_DAGGER("parrying_dagger", WeaponType.MELEE, IInternalMethodHandler::addParryingDagger),
        LONGSWORD("longsword", WeaponType.MELEE, IInternalMethodHandler::addLongsword),
        KATANA("katana", WeaponType.MELEE, IInternalMethodHandler::addKatana),
        SABER("saber", WeaponType.MELEE, IInternalMethodHandler::addSaber),
        RAPIER("rapier", WeaponType.MELEE, IInternalMethodHandler::addRapier),
        GREATSWORD("greatsword", WeaponType.MELEE, IInternalMethodHandler::addGreatsword),
        BATTLE_HAMMER("battle_hammer", WeaponType.MELEE, IInternalMethodHandler::addBattleHammer),
        WARHAMMER("warhammer", WeaponType.MELEE, IInternalMethodHandler::addWarhammer),
        SPEAR("spear", WeaponType.MELEE, IInternalMethodHandler::addSpear),
        HALBERD("halberd", WeaponType.MELEE, IInternalMethodHandler::addHalberd),
        PIKE("pike", WeaponType.MELEE, IInternalMethodHandler::addPike),
        LANCE("lance", WeaponType.MELEE, IInternalMethodHandler::addLance),
        LONGBOW("longbow", WeaponType.RANGED, IInternalMethodHandler::addLongbow),
        HEAVY_CROSSBOW("heavy_crossbow", WeaponType.RANGED, IInternalMethodHandler::addHeavyCrossbow),
        THROWING_KNIFE("throwing_knife", WeaponType.THROWING, IInternalMethodHandler::addThrowingKnife),
        TOMAHAWK("tomahawk", WeaponType.THROWING, IInternalMethodHandler::addTomahawk),
        JAVELIN("javelin", WeaponType.THROWING, IInternalMethodHandler::addJavelin),
        BOOMERANG("boomerang", WeaponType.THROWING, IInternalMethodHandler::addBoomerang),
        BATTLEAXE("battleaxe", WeaponType.MELEE, IInternalMethodHandler::addBattleaxe),
        FLANGED_MACE("flanged_mace", WeaponType.MELEE, IInternalMethodHandler::addFlangedMace),
        GLAIVE("glaive", WeaponType.MELEE, IInternalMethodHandler::addGlaive),
        QUARTERSTAFF("quarterstaff", WeaponType.MELEE, IInternalMethodHandler::addQuarterstaff),
        SCYTHE("scythe", WeaponType.MELEE, IInternalMethodHandler::addScythe);

        private final String serializedName;
        private final WeaponType weaponType;
        private final BiFunction<IInternalMethodHandler, WeaponMaterial, Item> factory;

        WeaponItemType(String serializedName, WeaponType weaponType, BiFunction<IInternalMethodHandler, WeaponMaterial, Item> factory) {
            this.serializedName = serializedName;
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
        return Objects.requireNonNull(weaponType, "weaponType").create(requireInternalHandler(), material);
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
