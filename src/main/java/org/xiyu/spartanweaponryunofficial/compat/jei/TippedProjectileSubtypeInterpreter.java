package org.xiyu.spartanweaponryunofficial.compat.jei;

import java.util.Optional;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TippedProjectileSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final TippedProjectileSubtypeInterpreter INSTANCE =
            new TippedProjectileSubtypeInterpreter();

    private TippedProjectileSubtypeInterpreter() {}

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
        if (!itemStack.has(DataComponents.POTION_CONTENTS)) return "";

        Optional<Holder<Potion>> potionHolder =
                itemStack
                        .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                        .potion();
        if (potionHolder.isEmpty()) return "";
        Potion potionType = potionHolder.get().value();
        StringBuilder stringBuilder = new StringBuilder(Potion.getName(potionHolder, ""));
        for (MobEffectInstance effect : potionType.getEffects()) {
            stringBuilder.append(";").append(effect);
        }

        return stringBuilder.toString();
    }
}
