package org.xiyu.spartanweaponryunofficial.compat.jei;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

public class WeaponOilSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final WeaponOilSubtypeInterpreter INSTANCE = new WeaponOilSubtypeInterpreter();

    private WeaponOilSubtypeInterpreter() {
    }

    @Override
    public @NotNull String apply(@NotNull ItemStack itemStack, @NotNull UidContext context) {
        if (!ItemStackDataHelper.hasTag(itemStack))
            return null;

        OilEffect weaponOil = OilHelper.getOilFromStack(itemStack);
        Potion potion = OilHelper.getPotionFromStack(itemStack);

        Registry<OilEffect> registry = (Registry<OilEffect>) BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
        String result = registry.getKey(weaponOil).getPath();
        if (weaponOil == OilEffects.POTION.get()) {
            StringBuilder stringBuilder = new StringBuilder(result);
            if (potion != null) {
                stringBuilder.append(":").append(BuiltInRegistries.POTION.getKey(potion).getPath());
                for (MobEffectInstance mobEffect : potion.getEffects()) {
                    stringBuilder.append(";").append(mobEffect);
                }
            }
            result = stringBuilder.toString();
        }
        return result;
    }

}
