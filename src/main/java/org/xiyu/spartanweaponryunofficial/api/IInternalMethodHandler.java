package org.xiyu.spartanweaponryunofficial.api;

import net.minecraft.world.item.Item;

/**
 * Internal bridge used by {@link SpartanWeaponryAPI} to delegate weapon construction to the loaded
 * mod.
 *
 * <p>This interface remains public for binary compatibility with the existing API surface, but
 * addon mods should not implement it, cache it, or call it directly. Use {@link SpartanWeaponryAPI}
 * instead.
 *
 * @author ObliviousSpartan
 */
@org.jetbrains.annotations.ApiStatus.Internal
public interface IInternalMethodHandler {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
    // Weapon Creation functions
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * Creates a Dagger item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Dagger item
     */
    Item addDagger(WeaponMaterial material);

    /**
     * Creates a Parrying Dagger item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Dagger item
     */
    Item addParryingDagger(WeaponMaterial material);

    /**
     * Creates a Longsword item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Longsword item
     */
    Item addLongsword(WeaponMaterial material);

    /**
     * Creates a Katana item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Katana item
     */
    Item addKatana(WeaponMaterial material);

    /**
     * Creates a Saber item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Saber item
     */
    Item addSaber(WeaponMaterial material);

    /**
     * Creates a Rapier item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Rapier item
     */
    Item addRapier(WeaponMaterial material);

    /**
     * Creates a Greatsword item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Greatsword item
     */
    Item addGreatsword(WeaponMaterial material);

    /**
     * Creates a Battle Hammer item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Battle Hammer item
     */
    Item addBattleHammer(WeaponMaterial material);

    /**
     * Creates a Warhammer item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Warhammer item
     */
    Item addWarhammer(WeaponMaterial material);

    /**
     * Creates a Spear item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Spear item
     */
    Item addSpear(WeaponMaterial material);

    /**
     * Creates a Halberd item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Halberd item
     */
    Item addHalberd(WeaponMaterial material);

    /**
     * Creates a Pike item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Pike item
     */
    Item addPike(WeaponMaterial material);

    /**
     * Creates a Lance item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Lance item
     */
    Item addLance(WeaponMaterial material);

    /**
     * Creates a Longbow item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Longbow item
     */
    Item addLongbow(WeaponMaterial material);

    /**
     * Creates a Heavy Crossbow item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Heavy Crossbow item
     */
    Item addHeavyCrossbow(WeaponMaterial material);

    /**
     * Creates a Throwing Knife item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Throwing Knife item
     */
    Item addThrowingKnife(WeaponMaterial material);

    /**
     * Creates a Throwing Axe item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Tomahawk item
     */
    Item addTomahawk(WeaponMaterial material);

    /**
     * Creates a Javelin item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Javelin item
     */
    Item addJavelin(WeaponMaterial material);

    /**
     * Creates a Boomerang item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Boomerang item
     */
    Item addBoomerang(WeaponMaterial material);

    /**
     * Creates a Battleaxe item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Battleaxe item
     */
    Item addBattleaxe(WeaponMaterial material);

    /**
     * Creates a Mace item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Mace item
     */
    Item addFlangedMace(WeaponMaterial material);

    /**
     * Creates a Glaive item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Glaive item
     */
    Item addGlaive(WeaponMaterial material);

    /**
     * Creates a Quarterstaff item while adding additional Weapon Properties derived from the
     * weapon's material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Quarterstaff item
     */
    Item addQuarterstaff(WeaponMaterial material);

    /**
     * Creates a Scythe item while adding additional Weapon Properties derived from the weapon's
     * material. Does *NOT* register the item. The addon author will have to do that.
     *
     * @param material The weapon material
     * @return The newly created Scythe item
     */
    Item addScythe(WeaponMaterial material);
}
