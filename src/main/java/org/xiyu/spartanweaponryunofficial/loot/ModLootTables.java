package org.xiyu.spartanweaponryunofficial.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

public class ModLootTables {
    public static final ResourceKey<LootTable> INJECT_VILLAGE_WEAPONSMITH = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/village_weaponsmith"));
    public static final ResourceKey<LootTable> INJECT_VILLAGE_FLETCHER = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/village_fletcher"));
    public static final ResourceKey<LootTable> INJECT_END_CITY_TREASURE = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "inject/end_city_treasure"));
}
