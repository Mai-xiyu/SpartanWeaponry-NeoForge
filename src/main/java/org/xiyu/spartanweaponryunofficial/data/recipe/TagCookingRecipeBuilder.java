package org.xiyu.spartanweaponryunofficial.data.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;
import org.xiyu.spartanweaponryunofficial.item.crafting.ITagCookingRecipe;
import org.xiyu.spartanweaponryunofficial.item.crafting.TagBlastingRecipe;
import org.xiyu.spartanweaponryunofficial.item.crafting.TagSmeltingRecipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mostly a copy of {@linkplain SimpleCookingRecipeBuilder} changed to accomodate tag results
 *
 * @author ObliviousSpartan
 */
public class TagCookingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory recipeCategory;
    private final CookingBookCategory cookingBookCategory;
    private final ImmutableMap<String, Item> ingredientMap;
    private final TagKey<Item> resultTag;
    private final float experience;
    private final int cookingTime;
    private final ImmutableList.Builder<String> disabledTypesBuilder;
    private final RecipeSerializer<? extends ITagCookingRecipe> serializer;
    private final RecipeFactory<? extends ITagCookingRecipe> recipeFactory;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private TagCookingRecipeBuilder(RecipeCategory recipeCategoryIn, CookingBookCategory cookingBookCategoryIn, ImmutableMap<String, Item> ingredientMapIn, TagKey<Item> resultTagIn, float experienceIn, int cookingTimeIn, RecipeSerializer<? extends ITagCookingRecipe> serializerIn, RecipeFactory<? extends ITagCookingRecipe> recipeFactoryIn) {
        this.recipeCategory = recipeCategoryIn;
        this.cookingBookCategory = cookingBookCategoryIn;
        this.ingredientMap = ingredientMapIn;
        this.resultTag = resultTagIn;
        this.experience = experienceIn;
        this.cookingTime = cookingTimeIn;
        this.serializer = serializerIn;
        this.recipeFactory = recipeFactoryIn;
        this.disabledTypesBuilder = new ImmutableList.Builder<>();
    }

    public static TagCookingRecipeBuilder smelting(ImmutableMap<String, Item> ingredientMapIn, RecipeCategory recipeCategoryIn, TagKey<Item> resultTagIn, float experienceIn, int cookingTimeIn) {
        return new TagCookingRecipeBuilder(recipeCategoryIn, CookingBookCategory.MISC, ingredientMapIn, resultTagIn, experienceIn, cookingTimeIn, ModRecipeSerializers.TAGGED_SMELTING.get(), TagSmeltingRecipe::new);
    }

    public static TagCookingRecipeBuilder blasting(ImmutableMap<String, Item> ingredientMapIn, RecipeCategory recipeCategoryIn, TagKey<Item> resultTagIn, float experienceIn, int cookingTimeIn) {
        return new TagCookingRecipeBuilder(recipeCategoryIn, CookingBookCategory.MISC, ingredientMapIn, resultTagIn, experienceIn, cookingTimeIn, ModRecipeSerializers.TAGGED_BLASTING.get(), TagBlastingRecipe::new);
    }

    @Override
    public @NotNull TagCookingRecipeBuilder unlockedBy(@NotNull String nameIn, @NotNull Criterion<?> criterionIn) {
        this.criteria.put(nameIn, criterionIn);
        return this;
    }

    @Override
    public @NotNull TagCookingRecipeBuilder group(String groupIn) {
        this.group = groupIn;
        return this;
    }

    public TagCookingRecipeBuilder addDisabledTypes(String... disableTypes) {
        this.disabledTypesBuilder.add(disableTypes);
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return Items.BARRIER;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation idIn) {
        Ingredient inputIngredient = Ingredient.of(this.ingredientMap.values().toArray(Item[]::new));
        ITagCookingRecipe recipe = this.recipeFactory.create(this.group == null ? "" : this.group, this.cookingBookCategory, inputIngredient, this.resultTag, this.experience, this.cookingTime);
        ResourceLocation advancementId = idIn.withPrefix("recipes/" + this.recipeCategory.getFolderName() + "/");
        var advancement = output.advancement();
        this.criteria.forEach(advancement::addCriterion);
        advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(idIn))
                .rewards(AdvancementRewards.Builder.recipe(idIn))
                .requirements(AdvancementRequirements.Strategy.OR);
        AdvancementHolder advancementHolder = advancement.build(advancementId);
        output.accept(idIn, recipe, advancementHolder);
    }
	
/*	private void validate(ResourceLocation idIn)
	{
		if(advancementBuilder.getCriteria().isEmpty())
			throw new IllegalStateException("Cannot obtain recipe " + idIn);
	}*/

    @FunctionalInterface
    public interface RecipeFactory<T extends ITagCookingRecipe> {
        T create(String groupIn, CookingBookCategory categoryIn, Ingredient inputIngredientIn, TagKey<Item> resultTagIn, float experienceIn, int cookTimeIn);
    }
}
