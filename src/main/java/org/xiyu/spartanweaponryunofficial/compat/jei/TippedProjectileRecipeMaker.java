package org.xiyu.spartanweaponryunofficial.compat.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;

public class TippedProjectileRecipeMaker {
    public static List<RecipeHolder<CraftingRecipe>> getRecipes(
            Item projectile, Item tippedProjectile) {
        List<RecipeHolder<CraftingRecipe>> list = new ArrayList<>();
        String recipeGroup = "jei.spartan_weaponry_unofficial.tipped_projectile";

        for (Potion potionType : BuiltInRegistries.POTION) {
            if (potionType != Potions.WATER.value()
                    && potionType != Potions.MUNDANE.value()
                    && potionType != Potions.THICK.value()
                    && potionType != Potions.AWKWARD.value()) {
                ItemStack projStack = new ItemStack(projectile);
                var potionHolder = BuiltInRegistries.POTION.wrapAsHolder(potionType);
                ItemStack potionStack =
                        PotionContents.createItemStack(Items.LINGERING_POTION, potionHolder);
                ItemStack outputStack =
                        PotionContents.createItemStack(tippedProjectile, potionHolder);
                outputStack.setCount(8);

                Ingredient projIngredient = Ingredient.of(projStack);
                Ingredient potionIngredient = Ingredient.of(potionStack);

                Map<Character, Ingredient> ingredientMap = new HashMap<>();
                ingredientMap.put('P', projIngredient);
                ingredientMap.put('L', potionIngredient);

                List<String> pattern = List.of("PPP", "PLP", "PPP");

                String potionId = BuiltInRegistries.POTION.getKey(potionType).getPath();
                // Use '/' prefix to mark as synthetic recipe so EMI doesn't try to look it up in
                // recipe manager
                ResourceLocation recipeResLoc =
                        ResourceLocation.fromNamespaceAndPath(
                                "spartan_weaponry_unofficial",
                                "/tipped_projectile."
                                        + BuiltInRegistries.ITEM
                                                .getKey(outputStack.getItem())
                                                .getPath()
                                        + ".effect."
                                        + potionId);
                ShapedRecipePattern recipePattern = ShapedRecipePattern.of(ingredientMap, pattern);
                ShapedRecipe recipe =
                        new ShapedRecipe(
                                recipeGroup, CraftingBookCategory.MISC, recipePattern, outputStack);
                list.add(new RecipeHolder<>(recipeResLoc, recipe));
            }
        }
        return list;
    }
}
