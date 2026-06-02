package org.xiyu.spartanweaponryunofficial.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;

import java.util.Objects;

/**
 * Addon-facing metadata describing a Spartan Weaponry weapon item.
 * <p>
 * Classifications are attached automatically to items created through
 * {@link SpartanWeaponryAPI#createWeapon(SpartanWeaponryAPI.WeaponItemType, WeaponMaterial)}
 * and the legacy {@code createXxx(WeaponMaterial)} factories. This metadata is intended for runtime
 * lookup and data-generation helpers; it is not a substitute for real item tag JSON in datapacks.
 */
public record WeaponClassification(SpartanWeaponryAPI.WeaponItemType weaponItemType, WeaponMaterial material) {
    public WeaponClassification {
        Objects.requireNonNull(weaponItemType, "weaponItemType");
        Objects.requireNonNull(material, "material");
    }

    public String weaponTypeName() {
        return this.weaponItemType.getSerializedName();
    }

    public String materialName() {
        return this.material.getMaterialName();
    }

    public String materialModId() {
        return this.material.getModId();
    }

    public TagKey<Item> weaponTag() {
        return ModItemTags.weaponType(this.weaponItemType);
    }

    public TagKey<Item> materialTag() {
        return ModItemTags.material(this.material);
    }
}
