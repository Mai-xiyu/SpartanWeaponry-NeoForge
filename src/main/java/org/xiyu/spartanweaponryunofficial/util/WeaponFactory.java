package org.xiyu.spartanweaponryunofficial.util;

import net.minecraft.world.item.Item;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.item.*;

public class WeaponFactory {
    /**
     * A special functional interface to create Spartan Weaponry weapons with using the same
     * parameters for each one
     *
     * @param <T> An item type to be created using this function
     * @author ObliviousSpartan
     */
    @FunctionalInterface
    public interface WeaponFunction<T extends Item> {
        T create(WeaponMaterial material, Item.Properties property);
    }

    public static final WeaponFunction<SwordBaseItem> DAGGER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.DAGGER,
                            Defaults.DamageBaseDagger,
                            Defaults.DamageMultiplierDagger,
                            Defaults.SpeedDagger,
                            "item." + ModSpartanWeaponry.ID + ".custom_dagger");

    public static final WeaponFunction<SwordBaseItem> PARRYING_DAGGER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.PARRYING_DAGGER,
                            Defaults.DamageBaseParryingDagger,
                            Defaults.DamageMultiplierParryingDagger,
                            Defaults.SpeedParryingDagger,
                            "item." + ModSpartanWeaponry.ID + ".custom_parrying_dagger");

    public static final WeaponFunction<SwordBaseItem> LONGSWORD =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.LONGSWORD,
                            Defaults.DamageBaseLongsword,
                            Defaults.DamageMultiplierLongsword,
                            Defaults.SpeedLongsword,
                            "item." + ModSpartanWeaponry.ID + ".custom_longsword");

    public static final WeaponFunction<SwordBaseItem> KATANA =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.KATANA,
                            Defaults.DamageBaseKatana,
                            Defaults.DamageMultiplierKatana,
                            Defaults.SpeedKatana,
                            "item." + ModSpartanWeaponry.ID + ".custom_katana");

    public static final WeaponFunction<SwordBaseItem> SABER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.SABER,
                            Defaults.DamageBaseSaber,
                            Defaults.DamageMultiplierSaber,
                            Defaults.SpeedSaber,
                            "item." + ModSpartanWeaponry.ID + ".custom_saber");

    public static final WeaponFunction<SwordBaseItem> RAPIER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.RAPIER,
                            Defaults.DamageBaseRapier,
                            Defaults.DamageMultiplierRapier,
                            Defaults.SpeedRapier,
                            "item." + ModSpartanWeaponry.ID + ".custom_rapier");

    public static final WeaponFunction<SwordBaseItem> GREATSWORD =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.GREATSWORD,
                            Defaults.DamageBaseGreatsword,
                            Defaults.DamageMultiplierGreatsword,
                            Defaults.SpeedGreatsword,
                            "item." + ModSpartanWeaponry.ID + ".custom_greatsword");

    public static final WeaponFunction<SwordBaseItem> BATTLE_HAMMER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.BATTLE_HAMMER,
                            Defaults.DamageBaseBattleHammer,
                            Defaults.DamageMultiplierBattleHammer,
                            Defaults.SpeedBattleHammer,
                            "item." + ModSpartanWeaponry.ID + ".custom_hammer");

    public static final WeaponFunction<SwordBaseItem> WARHAMMER =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.WARHAMMER,
                            Defaults.DamageBaseWarhammer,
                            Defaults.DamageMultiplierWarhammer,
                            Defaults.SpeedWarhammer,
                            "item." + ModSpartanWeaponry.ID + ".custom_warhammer");

    public static final WeaponFunction<SwordBaseItem> SPEAR =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.SPEAR,
                            Defaults.DamageBaseSpear,
                            Defaults.DamageMultiplierSpear,
                            Defaults.SpeedSpear,
                            "item." + ModSpartanWeaponry.ID + ".custom_spear");

    public static final WeaponFunction<SwordBaseItem> HALBERD =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.HALBERD,
                            Defaults.DamageBaseHalberd,
                            Defaults.DamageMultiplierHalberd,
                            Defaults.SpeedHalberd,
                            "item." + ModSpartanWeaponry.ID + ".custom_halberd");

    public static final WeaponFunction<SwordBaseItem> PIKE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.PIKE,
                            Defaults.DamageBasePike,
                            Defaults.DamageMultiplierPike,
                            Defaults.SpeedPike,
                            "item." + ModSpartanWeaponry.ID + ".custom_pike");

    public static final WeaponFunction<SwordBaseItem> LANCE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.LANCE,
                            Defaults.DamageBaseLance,
                            Defaults.DamageMultiplierLance,
                            Defaults.SpeedLance,
                            "item." + ModSpartanWeaponry.ID + ".custom_lance");

    public static final WeaponFunction<LongbowItem> LONGBOW =
            (material, prop) ->
                    new LongbowItem(
                            prop, material, "item." + ModSpartanWeaponry.ID + ".custom_longbow");

    public static final WeaponFunction<HeavyCrossbowItem> HEAVY_CROSSBOW =
            (material, prop) ->
                    new HeavyCrossbowItem(
                            prop,
                            material,
                            "item." + ModSpartanWeaponry.ID + ".custom_heavy_crossbow");

    public static final WeaponFunction<ThrowingWeaponItem> THROWING_KNIFE =
            (material, prop) ->
                    new ThrowingKnifeItem(
                            prop,
                            material,
                            WeaponArchetype.THROWING_KNIFE,
                            "item." + ModSpartanWeaponry.ID + ".custom_throwing_knife");

    public static final WeaponFunction<ThrowingWeaponItem> TOMAHAWK =
            (material, prop) ->
                    new TomahawkItem(
                            prop,
                            material,
                            WeaponArchetype.TOMAHAWK,
                            "item." + ModSpartanWeaponry.ID + ".custom_tomahawk");

    public static final WeaponFunction<ThrowingWeaponItem> JAVELIN =
            (material, prop) ->
                    new JavelinItem(
                            prop,
                            material,
                            WeaponArchetype.JAVELIN,
                            "item." + ModSpartanWeaponry.ID + ".custom_javelin");

    public static final WeaponFunction<ThrowingWeaponItem> BOOMERANG =
            (material, prop) ->
                    new BoomerangItem(
                            prop,
                            material,
                            WeaponArchetype.BOOMERANG,
                            "item." + ModSpartanWeaponry.ID + ".custom_boomerang");

    public static final WeaponFunction<SwordBaseItem> BATTLEAXE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.BATTLEAXE,
                            Defaults.DamageBaseBattleaxe,
                            Defaults.DamageMultiplierBattleaxe,
                            Defaults.SpeedBattleaxe,
                            "item." + ModSpartanWeaponry.ID + ".custom_battleaxe");

    public static final WeaponFunction<SwordBaseItem> FLANGED_MACE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.FLANGED_MACE,
                            Defaults.DamageBaseFlangedMace,
                            Defaults.DamageMultiplierFlangedMace,
                            Defaults.SpeedFlangedMace,
                            "item." + ModSpartanWeaponry.ID + ".custom_flanged_mace");

    public static final WeaponFunction<SwordBaseItem> GLAIVE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.GLAIVE,
                            Defaults.DamageBaseGlaive,
                            Defaults.DamageMultiplierGlaive,
                            Defaults.SpeedGlaive,
                            "item." + ModSpartanWeaponry.ID + ".custom_glaive");

    public static final WeaponFunction<SwordBaseItem> QUARTERSTAFF =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.QUARTERSTAFF,
                            Defaults.DamageBaseQuarterstaff,
                            Defaults.DamageMultiplierQuarterstaff,
                            Defaults.SpeedQuarterstaff,
                            "item." + ModSpartanWeaponry.ID + ".custom_quarterstaff");

    public static final WeaponFunction<SwordBaseItem> SCYTHE =
            (material, prop) ->
                    new SwordBaseItem(
                            prop,
                            material,
                            WeaponArchetype.SCYTHE,
                            Defaults.DamageBaseScythe,
                            Defaults.DamageMultiplierScythe,
                            Defaults.SpeedScythe,
                            "item." + ModSpartanWeaponry.ID + ".custom_scythe");
}
