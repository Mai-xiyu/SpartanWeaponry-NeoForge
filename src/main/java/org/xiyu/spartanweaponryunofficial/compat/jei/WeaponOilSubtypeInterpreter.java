package org.xiyu.spartanweaponryunofficial.compat.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

public class WeaponOilSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final WeaponOilSubtypeInterpreter INSTANCE = new WeaponOilSubtypeInterpreter();

    private WeaponOilSubtypeInterpreter() {}

    @Override
    public @Nullable Object getSubtypeData(
            @NotNull ItemStack itemStack, @NotNull UidContext context) {
        String subtype = buildSubtypeString(itemStack);
        return subtype.isEmpty() ? null : subtype;
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(
            @NotNull ItemStack itemStack, @NotNull UidContext context) {
        return buildSubtypeString(itemStack);
    }

    private static String buildSubtypeString(ItemStack itemStack) {
        if (!ItemStackDataHelper.hasTag(itemStack)) return "";

        OilEffect weaponOil = OilHelper.getOilFromStack(itemStack);
        ResourceLocation oilId = OilEffects.registry().getKey(weaponOil);
        if (oilId == null) return "";

        StringBuilder result = new StringBuilder(oilId.getPath());
        if (weaponOil == OilEffects.POTION.get()) {
            Potion potion = OilHelper.getPotionFromStack(itemStack);
            if (potion != null) {
                result.append(":").append(BuiltInRegistries.POTION.getKey(potion).getPath());
                for (MobEffectInstance mobEffect : potion.getEffects()) {
                    result.append(";").append(mobEffect);
                }
            }
        }
        return result.toString();
    }
}
