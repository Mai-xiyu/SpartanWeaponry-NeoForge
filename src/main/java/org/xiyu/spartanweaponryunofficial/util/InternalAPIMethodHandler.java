package org.xiyu.spartanweaponryunofficial.util;

import net.minecraft.world.item.Item;
import org.xiyu.spartanweaponryunofficial.api.IInternalMethodHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.util.WeaponFactory.WeaponFunction;

public class InternalAPIMethodHandler implements IInternalMethodHandler {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
    // Weapon Creation functions
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

    @Override
    public Item addDagger(WeaponMaterial material) {
        return create(WeaponFactory.DAGGER, material);
    }

    @Override
    public Item addParryingDagger(WeaponMaterial material) {
        return create(WeaponFactory.PARRYING_DAGGER, material);
    }

    @Override
    public Item addLongsword(WeaponMaterial material) {
        return create(WeaponFactory.LONGSWORD, material);
    }

    @Override
    public Item addKatana(WeaponMaterial material) {
        return create(WeaponFactory.KATANA, material);
    }

    @Override
    public Item addSaber(WeaponMaterial material) {
        return create(WeaponFactory.SABER, material);
    }

    @Override
    public Item addRapier(WeaponMaterial material) {
        return create(WeaponFactory.RAPIER, material);
    }

    @Override
    public Item addGreatsword(WeaponMaterial material) {
        return create(WeaponFactory.GREATSWORD, material);
    }

    @Override
    public Item addBattleHammer(WeaponMaterial material) {
        return create(WeaponFactory.BATTLE_HAMMER, material);
    }

    @Override
    public Item addWarhammer(WeaponMaterial material) {
        return create(WeaponFactory.WARHAMMER, material);
    }

    @Override
    public Item addSpear(WeaponMaterial material) {
        return create(WeaponFactory.SPEAR, material);
    }

    @Override
    public Item addHalberd(WeaponMaterial material) {
        return create(WeaponFactory.HALBERD, material);
    }

    @Override
    public Item addPike(WeaponMaterial material) {
        return create(WeaponFactory.PIKE, material);
    }

    @Override
    public Item addLance(WeaponMaterial material) {
        return create(WeaponFactory.LANCE, material);
    }

    @Override
    public Item addLongbow(WeaponMaterial material) {
        return create(WeaponFactory.LONGBOW, material);
    }

    @Override
    public Item addHeavyCrossbow(WeaponMaterial material) {
        return create(WeaponFactory.HEAVY_CROSSBOW, material);
    }

    @Override
    public Item addThrowingKnife(WeaponMaterial material) {
        return create(WeaponFactory.THROWING_KNIFE, material);
    }

    @Override
    public Item addTomahawk(WeaponMaterial material) {
        return create(WeaponFactory.TOMAHAWK, material);
    }

    @Override
    public Item addJavelin(WeaponMaterial material) {
        return create(WeaponFactory.JAVELIN, material);
    }

    @Override
    public Item addBoomerang(WeaponMaterial material) {
        return create(WeaponFactory.BOOMERANG, material);
    }

    @Override
    public Item addBattleaxe(WeaponMaterial material) {
        return create(WeaponFactory.BATTLEAXE, material);
    }

    @Override
    public Item addFlangedMace(WeaponMaterial material) {
        return create(WeaponFactory.FLANGED_MACE, material);
    }

    @Override
    public Item addGlaive(WeaponMaterial material) {
        return create(WeaponFactory.GLAIVE, material);
    }

    @Override
    public Item addQuarterstaff(WeaponMaterial material) {
        return create(WeaponFactory.QUARTERSTAFF, material);
    }

    @Override
    public Item addScythe(WeaponMaterial material) {
        return create(WeaponFactory.SCYTHE, material);
    }

    private static Item create(WeaponFunction<? extends Item> factory, WeaponMaterial material) {
        return factory.create(material, new Item.Properties());
    }
}
