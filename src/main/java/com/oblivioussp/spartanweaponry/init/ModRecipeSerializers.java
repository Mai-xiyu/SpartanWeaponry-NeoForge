package com.oblivioussp.spartanweaponry.init;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.crafting.condition.TypeDisabledCondition;
import com.oblivioussp.spartanweaponry.item.crafting.ApplyOilRecipe;
import com.oblivioussp.spartanweaponry.item.crafting.QuiverUpgradeRecipe;
import com.oblivioussp.spartanweaponry.item.crafting.TagBlastingRecipe;
import com.oblivioussp.spartanweaponry.item.crafting.TagCookingRecipeSerializer;
import com.oblivioussp.spartanweaponry.item.crafting.TagSmeltingRecipe;
import com.oblivioussp.spartanweaponry.item.crafting.TippedProjectileBaseRecipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModSpartanWeaponry.ID);
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, ModSpartanWeaponry.ID);
	
	public static final DeferredHolder<RecipeSerializer<?>, TippedProjectileBaseRecipe.Serializer> TIPPED_PROJECTILE_BASE = REGISTRY.register("tipped_projectile", () -> new TippedProjectileBaseRecipe.Serializer());
	public static final DeferredHolder<RecipeSerializer<?>, QuiverUpgradeRecipe.Serializer> QUIVER_UPGRADE_SMITHING = REGISTRY.register("quiver_upgrade_smithing", () -> new QuiverUpgradeRecipe.Serializer());
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ApplyOilRecipe>> APPLY_OIL = REGISTRY.register("apply_oil", () -> new SimpleCraftingRecipeSerializer<>(ApplyOilRecipe::new));
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TagSmeltingRecipe>> TAGGED_SMELTING = REGISTRY.register("tag_smelting", () -> new TagCookingRecipeSerializer<>(TagSmeltingRecipe::new, 200));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TagBlastingRecipe>> TAGGED_BLASTING = REGISTRY.register("tag_blasting", () -> new TagCookingRecipeSerializer<>(TagBlastingRecipe::new, 100));
	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<TypeDisabledCondition>> TYPE_DISABLED_CONDITION = CONDITION_SERIALIZERS.register("type_disabled", () -> TypeDisabledCondition.CODEC);
	
}
