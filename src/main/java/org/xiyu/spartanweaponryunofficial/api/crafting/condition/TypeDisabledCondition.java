package org.xiyu.spartanweaponryunofficial.api.crafting.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

public class TypeDisabledCondition implements ICondition {
    public static final List<String> disabledRecipeTypes = new ArrayList<>();

    // Pre-defined values for types
    public static final String DAGGER = "dagger";
    public static final String PARRYING_DAGGER = "parrying_dagger";
    public static final String LONGSWORD = "longsword";
    public static final String KATANA = "katana";
    public static final String SABER = "saber";
    public static final String RAPIER = "rapier";
    public static final String GREATSWORD = "greatsword";
    public static final String CLUB = "club";
    public static final String CESTUS = "cestus";
    public static final String BATTLE_HAMMER = "battle_hammer";
    public static final String WARHAMMER = "warhammer";
    public static final String SPEAR = "spear";
    public static final String HALBERD = "halberd";
    public static final String PIKE = "pike";
    public static final String LANCE = "lance";
    public static final String LONGBOW = "longbow";
    public static final String HEAVY_CROSSBOW = "heavy_crossbow";
    public static final String THROWING_KNIFE = "throwing_knife";
    public static final String TOMAHAWK = "tomahawk";
    public static final String JAVELIN = "javelin";
    public static final String BOOMERANG = "boomerang";
    public static final String BATTLEAXE = "battleaxe";
    public static final String FLANGED_MACE = "flanged_mace";
    public static final String GLAIVE = "glaive";
    public static final String QUARTERSTAFF = "quarterstaff";
    public static final String SCYTHE = "scythe";
    public static final String COPPER_AMMO = "copper_ammo";
    public static final String DIAMOND_AMMO = "diamond_ammo";
    public static final String NETHERITE_AMMO = "netherite_ammo";
    public static final String ARROWS = "arrows";
    public static final String BOLTS = "bolts";
    public static final String QUIVER = "quiver";
    public static final String EXPLOSIVES = "explosives";
    public static final String OIL = "oil";

    public static final String COPPER = "copper";
    public static final String TIN = "tin";
    public static final String BRONZE = "bronze";
    public static final String STEEL = "steel";
    public static final String SILVER = "silver";
    public static final String ELECTRUM = "electrum";
    public static final String LEAD = "lead";
    public static final String NICKEL = "nickel";
    public static final String INVAR = "invar";
    public static final String CONSTANTAN = "constantan";
    public static final String PLATINUM = "platinum";
    public static final String ALUMINUM = "aluminum";

    public static final ResourceLocation NAME =
            ResourceLocation.fromNamespaceAndPath(SpartanWeaponryAPI.MOD_ID, "type_disabled");
    public static final MapCodec<TypeDisabledCondition> CODEC =
            RecordCodecBuilder.mapCodec(
                    instance ->
                            instance.group(
                                            Codec.STRING
                                                    .listOf()
                                                    .fieldOf("disabled")
                                                    .forGetter(condition -> condition.types))
                                    .apply(instance, TypeDisabledCondition::new));
    private final List<String> types;

    public TypeDisabledCondition(List<String> types) {
        this.types = types;
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(@NotNull IContext context) {
        for (String type : this.types) {
            if (disabledRecipeTypes.contains(type)) return false;
        }
        return true;
    }
}
