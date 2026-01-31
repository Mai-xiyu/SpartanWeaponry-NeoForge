package com.oblivioussp.spartanweaponry.init;

import com.mojang.serialization.MapCodec;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.loot.ConfigLootCondition;
import com.oblivioussp.spartanweaponry.loot.DecapitateLootModifier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers 
{
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ModSpartanWeaponry.ID);
	
	// Loot Modifiers
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<DecapitateLootModifier>> DECAPITATE = REGISTRY.register("decapitate", () -> DecapitateLootModifier.DECAPITATE_CODEC);

	// Loot Conditions
	public static LootItemConditionType CONFIG_ENABLED;
	
	public static void registerLootConditions()
	{
		CONFIG_ENABLED = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "new_heads_enabled"), new LootItemConditionType(ConfigLootCondition.CODEC));
	}
}
