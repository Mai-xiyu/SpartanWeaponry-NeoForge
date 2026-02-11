package org.xiyu.spartanweaponryunofficial.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.crafting.condition.TypeDisabledCondition;
import org.xiyu.spartanweaponryunofficial.item.crafting.*;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModSpartanWeaponry.ID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.Keys.CONDITION_SERIALIZERS, ModSpartanWeaponry.ID);

    public static final RegistryObject<TippedProjectileBaseRecipe.Serializer> TIPPED_PROJECTILE_BASE = REGISTRY.register("tipped_projectile", TippedProjectileBaseRecipe.Serializer::new);
    public static final RegistryObject<QuiverUpgradeRecipe.Serializer> QUIVER_UPGRADE_SMITHING = REGISTRY.register("quiver_upgrade_smithing", QuiverUpgradeRecipe.Serializer::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ApplyOilRecipe>> APPLY_OIL = REGISTRY.register("apply_oil", () -> new SimpleCraftingRecipeSerializer<>(ApplyOilRecipe::new));

    public static final RegistryObject<RecipeSerializer<TagSmeltingRecipe>> TAGGED_SMELTING = REGISTRY.register("tag_smelting", () -> new TagCookingRecipeSerializer<>(TagSmeltingRecipe::new, 200));
    public static final RegistryObject<RecipeSerializer<TagBlastingRecipe>> TAGGED_BLASTING = REGISTRY.register("tag_blasting", () -> new TagCookingRecipeSerializer<>(TagBlastingRecipe::new, 100));
    public static final RegistryObject<MapCodec<TypeDisabledCondition>> TYPE_DISABLED_CONDITION = CONDITION_SERIALIZERS.register("type_disabled", () -> TypeDisabledCondition.CODEC);

}
