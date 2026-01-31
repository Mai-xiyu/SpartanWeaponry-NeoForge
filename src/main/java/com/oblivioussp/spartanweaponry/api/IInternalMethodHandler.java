package com.oblivioussp.spartanweaponry.api;

import net.minecraft.world.item.Item;

/**
 * Basic Internal method handler interface. Do NOT create your own version of this. It is required for the API to work!
 * @author ObliviousSpartan
 *
 */
public interface IInternalMethodHandler 
{
	//---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
	// Weapon Creation functions
	//---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
	
	/**
	 * Creates a Dagger item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Dagger item
	 */
	public abstract Item addDagger(WeaponMaterial material);
	
	
	/**
	 * Creates a Parrying Dagger item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Dagger item
	 */
	public abstract Item addParryingDagger(WeaponMaterial material);
	
	/**
	 * Creates a Longsword item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Longsword item
	 */
	public abstract Item addLongsword(WeaponMaterial material);

	/**
	 * Creates a Katana item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Katana item
	 */
	public abstract Item addKatana(WeaponMaterial material);
	
	/**
	 * Creates a Saber item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Saber item
	 */
	public abstract Item addSaber(WeaponMaterial material);

	/**
	 * Creates a Rapier item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Rapier item
	 */
	public abstract Item addRapier(WeaponMaterial material);
	
	/**
	 * Creates a Greatsword item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Greatsword item
	 */
	public abstract Item addGreatsword(WeaponMaterial material);

	/**
	 * Creates a Battle Hammer item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Battle Hammer item
	 */
	public abstract Item addBattleHammer(WeaponMaterial material);
	
	/**
	 * Creates a Warhammer item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Warhammer item
	 */
	public abstract Item addWarhammer(WeaponMaterial material);

	/**
	 * Creates a Spear item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Spear item
	 */
	public abstract Item addSpear(WeaponMaterial material);

	/**
	 * Creates a Halberd item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Halberd item
	 */
	public abstract Item addHalberd(WeaponMaterial material);

	/**
	 * Creates a Pike item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Pike item
	 */
	public abstract Item addPike(WeaponMaterial material);
	
	/**
	 * Creates a Lance item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Lance item
	 */
	public abstract Item addLance(WeaponMaterial material);
	
	/**
	 * Creates a Longbow item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Longbow item
	 */
	public abstract Item addLongbow(WeaponMaterial material);
	
	/**
	 * Creates a Heavy Crossbow item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Heavy Crossbow item
	 */
	public abstract Item addHeavyCrossbow(WeaponMaterial material);

	/**
	 * Creates a Throwing Knife item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @param group The Creative Tab the item will show up in
	 * @return The newly created Throwing Knife item
	 */
	public abstract Item addThrowingKnife(WeaponMaterial material);

	/**
	 * Creates a Throwing Axe item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Tomahawk item
	 */
	public abstract Item addTomahawk(WeaponMaterial material);

	/**
	 * Creates a Javelin item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Javelin item
	 */
	public abstract Item addJavelin(WeaponMaterial material);

	/**
	 * Creates a Boomerang item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Boomerang item
	 */
	public abstract Item addBoomerang(WeaponMaterial material);

	/**
	 * Creates a Battleaxe item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Battleaxe item
	 */
	public abstract Item addBattleaxe(WeaponMaterial material);

	/**
	 * Creates a Mace item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Mace item
	 */
	public abstract Item addFlangedMace(WeaponMaterial material);
	
	/**
	 * Creates a Glaive item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Mace item
	 */
	public abstract Item addGlaive(WeaponMaterial material);

	/**
	 * Creates a Quarterstaff item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Mace item
	 */
	public abstract Item addQuarterstaff(WeaponMaterial material);

	/**
	 * Creates a Scythe item while adding additional Weapon Properties derived from the weapon's material. Does *NOT* register the item. The addon author will have to do that.
	 * @param material The weapon material
	 * @return The newly created Mace item
	 */
	public abstract Item addScythe(WeaponMaterial material);
}
