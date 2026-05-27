package org.xiyu.spartanweaponryunofficial.item.crafting;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;
import org.xiyu.spartanweaponryunofficial.util.WeaponOilConfig;

public class PotionToOilBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        Potion inputPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion().map(Holder::value).orElse(null);
        return inputPotion != null && WeaponOilConfig.areRecipesEnabled() && input.is(Items.POTION) && OilHelper.isValidPotion(inputPotion);
    }

    @Override
    public boolean isIngredient(@NotNull ItemStack ingredient) {
        return WeaponOilConfig.areRecipesEnabled() && ingredient.is(ModItems.GREASE_BALL.get());
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
        if (this.isInput(input) && this.isIngredient(ingredient)) {
            Potion inputPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .potion().map(Holder::value).orElse(null);
            if (inputPotion != null)
                return OilHelper.makePotionOilStack(inputPotion);
        }

        return ItemStack.EMPTY;
    }
}
