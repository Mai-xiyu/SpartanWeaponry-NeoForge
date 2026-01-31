package com.oblivioussp.spartanweaponry.loot;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables 
{
	public static final ResourceKey<LootTable> INJECT_VILLAGE_WEAPONSMITH = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/" + BuiltInLootTables.VILLAGE_WEAPONSMITH.location().getPath()));
	public static final ResourceKey<LootTable> INJECT_VILLAGE_FLETCHER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/" + BuiltInLootTables.VILLAGE_FLETCHER.location().getPath()));
	public static final ResourceKey<LootTable> INJECT_END_CITY_TREASURE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/" + BuiltInLootTables.END_CITY_TREASURE.location().getPath()));
}
