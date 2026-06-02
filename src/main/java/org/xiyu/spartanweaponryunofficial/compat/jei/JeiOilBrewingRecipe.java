package org.xiyu.spartanweaponryunofficial.compat.jei;

import com.google.common.collect.ImmutableList;
import java.util.List;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.item.crafting.OilBrewingRecipe;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

public class JeiOilBrewingRecipe implements IJeiBrewingRecipe {
    private final List<ItemStack> baseOils;
    private final List<ItemStack> ingredients;
    private final ItemStack output;
    private final ResourceLocation uid;
    private int brewingSteps = Integer.MAX_VALUE;

    public JeiOilBrewingRecipe(
            List<ItemStack> baseOilsIn, List<ItemStack> ingredientsIn, ItemStack outputIn) {
        this.baseOils = ImmutableList.copyOf(baseOilsIn);
        this.ingredients = ImmutableList.copyOf(ingredientsIn);
        this.output = outputIn;
        ResourceLocation outputLocation = BuiltInRegistries.ITEM.getKey(outputIn.getItem());
        Registry<OilEffect> registry =
                (Registry<OilEffect>)
                        BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
        this.uid =
                ResourceLocation.tryBuild(
                        outputLocation.getNamespace(),
                        outputLocation.getPath()
                                + "."
                                + registry.getKey(OilHelper.getOilFromStack(outputIn)).getPath()
                                + "_from_brewing");
    }

    @Override
    public @Unmodifiable @NotNull List<ItemStack> getPotionInputs() {
        return this.baseOils;
    }

    @Override
    public @Unmodifiable @NotNull List<ItemStack> getIngredients() {
        return this.ingredients;
    }

    @Override
    public @NotNull ItemStack getPotionOutput() {
        return this.output;
    }

    @Override
    public int getBrewingSteps() {
        // Check and see if the value isn't cached first
        if (this.brewingSteps == Integer.MAX_VALUE) {
            // Calculate the brewing steps value then cache it
            OilEffect oilEffect = OilHelper.getOilFromStack(this.output);
            this.brewingSteps = OilBrewingRecipe.getBrewingSteps(oilEffect);
        }
        return this.brewingSteps;
    }

    @Override
    public @Nullable ResourceLocation getUid() {
        return this.uid;
    }
}
