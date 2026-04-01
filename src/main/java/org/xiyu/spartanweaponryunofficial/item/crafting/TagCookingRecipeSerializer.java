package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Factory that creates RecipeSerializer instances for tag-based cooking recipes.
 * In MC 26.1, RecipeSerializer is a final Record, so this class serves as a factory
 * rather than implementing RecipeSerializer directly.
 */
public class TagCookingRecipeSerializer {

    public static <T extends AbstractCookingRecipe & ITagCookingRecipe> RecipeSerializer<T> create(
            RecipeFactory<T> factory, int defaultCookingTime) {
        MapCodec<T> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(AbstractCookingRecipe::group),
                CookingBookCategory.CODEC.optionalFieldOf("category", CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.input()),
                TagKey.codec(Registries.ITEM).fieldOf("result_tag").forGetter(ITagCookingRecipe::getResultTag),
                Codec.FLOAT.optionalFieldOf("experience", 0.0f).forGetter(AbstractCookingRecipe::experience),
                Codec.INT.optionalFieldOf("cookingtime", defaultCookingTime).forGetter(AbstractCookingRecipe::cookingTime)
        ).apply(instance, factory::create));
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());
        return new RecipeSerializer<>(codec, streamCodec);
    }

    @FunctionalInterface
    public interface RecipeFactory<T> {
        T create(String groupIn, CookingBookCategory categoryIn, Ingredient inputIngredientIn,
                 TagKey<Item> resultTagIn, float experienceIn, int cookTimeIn);
    }
}