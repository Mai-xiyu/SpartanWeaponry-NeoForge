package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.xiyu.spartanweaponryunofficial.data.loot.ModBlockLoot;
import org.xiyu.spartanweaponryunofficial.data.loot.ModChestLoot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTablesProvider extends LootTableProvider {
//	List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, LootContextParamSet>> lootTables = ImmutableList.of(Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK), Pair.of(ModChestLootTables::new, LootContextParamSets.CHEST));

    public ModLootTablesProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, requiredTables, List.of(new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModChestLoot::new, LootContextParamSets.CHEST)), lookupProvider);
    }
}
