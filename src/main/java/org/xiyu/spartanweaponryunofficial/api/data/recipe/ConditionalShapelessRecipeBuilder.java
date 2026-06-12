package org.xiyu.spartanweaponryunofficial.api.data.recipe;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IRecipeOutputExtension;

/** Shapeless recipe builder with optional NeoForge conditions. */
public class ConditionalShapelessRecipeBuilder {
    private final ItemStack result;
    private final int count;
    private final RecipeCategory category;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private String group;
    private final List<ICondition> conditions = new ArrayList<>();

    private ConditionalShapelessRecipeBuilder(
            RecipeCategory categoryIn, ItemStack resultIn, int countIn) {
        this.category = categoryIn;
        this.result = resultIn;
        this.count = countIn;
    }

    public static ConditionalShapelessRecipeBuilder shapeless(ItemStack stackIn) {
        return shapeless(RecipeCategory.COMBAT, stackIn, 1);
    }

    public static ConditionalShapelessRecipeBuilder shapeless(ItemStack stackIn, int countIn) {
        return shapeless(RecipeCategory.COMBAT, stackIn, countIn);
    }

    public static ConditionalShapelessRecipeBuilder shapeless(ItemLike itemIn) {
        return shapeless(RecipeCategory.COMBAT, new ItemStack(itemIn.asItem()), 1);
    }

    public static ConditionalShapelessRecipeBuilder shapeless(ItemLike itemIn, int countIn) {
        return shapeless(RecipeCategory.COMBAT, new ItemStack(itemIn.asItem()), countIn);
    }

    public static ConditionalShapelessRecipeBuilder shapeless(
            RecipeCategory categoryIn, ItemStack stackIn, int countIn) {
        return new ConditionalShapelessRecipeBuilder(categoryIn, stackIn, countIn);
    }

    public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tagIn) {
        return this.requires(Ingredient.of(tagIn));
    }

    public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tagIn, int countIn) {
        return this.requires(Ingredient.of(tagIn), countIn);
    }

    public ConditionalShapelessRecipeBuilder requires(ItemLike itemIn) {
        return this.requires(Ingredient.of(itemIn));
    }

    public ConditionalShapelessRecipeBuilder requires(ItemLike itemIn, int countIn) {
        return this.requires(Ingredient.of(itemIn), countIn);
    }

    public ConditionalShapelessRecipeBuilder requires(Ingredient ingredientIn) {
        this.ingredients.add(ingredientIn);
        return this;
    }

    public ConditionalShapelessRecipeBuilder requires(Ingredient ingredientIn, int countIn) {
        for (int i = 0; i < countIn; i++) this.ingredients.add(ingredientIn);
        return this;
    }

    public ConditionalShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterionIn) {
        this.criteria.put(name, criterionIn);
        return this;
    }

    public ConditionalShapelessRecipeBuilder group(String groupIn) {
        this.group = groupIn;
        return this;
    }

    public ConditionalShapelessRecipeBuilder condition(ICondition conditionIn) {
        this.conditions.add(conditionIn);
        return this;
    }

    public void save(RecipeOutput output) {
        this.save(output, BuiltInRegistries.ITEM.getKey(this.result.getItem()));
    }

    public void save(RecipeOutput output, String save) {
        ResourceLocation resultLoc = BuiltInRegistries.ITEM.getKey(this.result.getItem());
        ResourceLocation saveLoc = ResourceLocation.parse(save);
        if (saveLoc.equals(resultLoc))
            throw new IllegalStateException(
                    "Shapeless recipe "
                            + save
                            + " save argument is redundant as it's the same as the item id!");
        else this.save(output, saveLoc);
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        RecipeOutput conditionedOutput = output;
        if (!this.conditions.isEmpty() && output instanceof IRecipeOutputExtension ext)
            conditionedOutput = ext.withConditions(this.conditions.toArray(ICondition[]::new));

        CraftingBookCategory bookCategory = RecipeBuilder.determineBookCategory(this.category);
        ItemStack outputStack = this.result.copy();
        outputStack.setCount(this.count);
        NonNullList<Ingredient> ingredientList = NonNullList.copyOf(this.ingredients);
        ShapelessRecipe recipe =
                new ShapelessRecipe(
                        this.group == null ? "" : this.group,
                        bookCategory,
                        outputStack,
                        ingredientList);

        ResourceLocation advancementId =
                id.withPrefix("recipes/" + this.category.getFolderName() + "/");
        // RecipeOutput.advancement() already parents the builder to the root recipe advancement
        var advancement = output.advancement();
        this.criteria.forEach(advancement::addCriterion);
        advancement
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        AdvancementHolder advancementHolder = advancement.build(advancementId);
        conditionedOutput.accept(id, recipe, advancementHolder);
    }

    private void validate(ResourceLocation id) {
        if (this.criteria.isEmpty())
            throw new IllegalStateException("Impossible to obtain recipe " + id + "!");
    }
}
