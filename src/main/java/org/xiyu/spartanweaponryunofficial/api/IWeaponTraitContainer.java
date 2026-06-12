package org.xiyu.spartanweaponryunofficial.api;

import java.util.Collection;
import java.util.List;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

/**
 * Interface for any item that contains Weapon Traits. Allows access to weapon traits when needed
 *
 * @param <T> The item type
 * @author ObliviousSpartan
 */
public interface IWeaponTraitContainer<T extends Item> {
    /** Returns the item representation of this Trait Container interface */
    T getAsItem();

    /**
     * Queries if the Weapon already has the specified Weapon Trait
     *
     * @param prop The Weapon Trait to check for
     * @return true if the Weapon Trait exists on this weapon; false otherwise
     */
    boolean hasWeaponTrait(WeaponTrait prop);

    /**
     * Queries if the Weapon already has the specified Weapon Trait type
     *
     * @param type The Weapon Trait type to check for
     * @return true if the Weapon Trait exists on this weapon; false otherwise
     */
    boolean hasWeaponTraitWithType(String type);

    /**
     * Retrieves the first Weapon Trait with the specified property type. Any other matches will be
     * ignored
     *
     * @param type The Weapon Trait type to check for
     * @return The first Weapon Trait that matches; {@code null} otherwise
     */
    @Nullable WeaponTrait getFirstWeaponTraitWithType(String type);

    /**
     * Retrieves all Weapon Traits in this weapon with the specified property type
     *
     * @param type The Weapon Trait type to check for
     * @return A list of the Weapon Traits that match; an empty list otherwise
     */
    List<WeaponTrait> getAllWeaponTraitsWithType(String type);

    /**
     * Returns a copy of all the Weapon Traits in the current weapon NOTE: This returns all the
     * Weapon Traits granted by a Material Bonus as well
     */
    Collection<WeaponTrait> getAllWeaponTraits();

    /**
     * Returns the material the weapon is made of. Allows Weapon Traits to access the material
     * directly
     */
    WeaponMaterial getMaterial();
}
