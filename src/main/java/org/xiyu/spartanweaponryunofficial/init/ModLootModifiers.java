package org.xiyu.spartanweaponryunofficial.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.loot.ConfigLootCondition;
import org.xiyu.spartanweaponryunofficial.loot.DecapitateLootModifier;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ModSpartanWeaponry.ID);

    // NeoForge 1.21: LootItemConditionType must use DeferredRegister, not Registry.register() in setup
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_REGISTRY = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, ModSpartanWeaponry.ID);

    // Loot Modifiers
    public static final RegistryObject<MapCodec<DecapitateLootModifier>> DECAPITATE = REGISTRY.register("decapitate", () -> DecapitateLootModifier.DECAPITATE_CODEC);

    // Loot Conditions - now using DeferredRegister
    public static final Supplier<LootItemConditionType> CONFIG_ENABLED = LOOT_CONDITION_REGISTRY.register("new_heads_enabled", () -> new LootItemConditionType(ConfigLootCondition.CODEC));
}
