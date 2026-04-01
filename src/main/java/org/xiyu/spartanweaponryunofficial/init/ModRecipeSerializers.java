package org.xiyu.spartanweaponryunofficial.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.crafting.condition.TypeDisabledCondition;
import org.xiyu.spartanweaponryunofficial.item.crafting.*;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModSpartanWeaponry.ID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, ModSpartanWeaponry.ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TippedProjectileBaseRecipe>> TIPPED_PROJECTILE_BASE = REGISTRY.register("tipped_projectile", () -> new RecipeSerializer<>(TippedProjectileBaseRecipe.CODEC, TippedProjectileBaseRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<QuiverUpgradeRecipe>> QUIVER_UPGRADE_SMITHING = REGISTRY.register("quiver_upgrade_smithing", () -> new RecipeSerializer<>(QuiverUpgradeRecipe.CODEC, QuiverUpgradeRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ApplyOilRecipe>> APPLY_OIL = REGISTRY.register("apply_oil", () -> new RecipeSerializer<>(ApplyOilRecipe.CODEC, ApplyOilRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TagSmeltingRecipe>> TAGGED_SMELTING = REGISTRY.register("tag_smelting", () -> TagCookingRecipeSerializer.create(TagSmeltingRecipe::new, 200));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TagBlastingRecipe>> TAGGED_BLASTING = REGISTRY.register("tag_blasting", () -> TagCookingRecipeSerializer.create(TagBlastingRecipe::new, 100));
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<TypeDisabledCondition>> TYPE_DISABLED_CONDITION = CONDITION_SERIALIZERS.register("type_disabled", () -> TypeDisabledCondition.CODEC);

}