package org.xiyu.spartanweaponryunofficial.event;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.data.*;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenEventHandler {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onDataGather(GatherDataEvent ev) {
        DataGenerator gen = ev.getGenerator();
        ExistingFileHelper existingFileHelper = ev.getExistingFileHelper();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = ev.getLookupProvider();
        ModDatapackRegistriesProvider mdpProvider =
                new ModDatapackRegistriesProvider(output, lookupProvider);
        gen.addProvider(true, mdpProvider);
        gen.addProvider(true, new ModBlockModelProvider(output, existingFileHelper));
        gen.addProvider(true, new ModItemModelProvider(output, existingFileHelper));
        gen.addProvider(true, new ModSoundDefinitionsProvider(output, existingFileHelper));
        ModBlockTagsProvider blockTagsProvider =
                new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
        gen.addProvider(true, blockTagsProvider);
        gen.addProvider(
                true,
                new ModItemTagsProvider(
                        output,
                        lookupProvider,
                        blockTagsProvider.contentsGetter(),
                        existingFileHelper));
        gen.addProvider(
                true, new ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper));
        gen.addProvider(
                true,
                new ModDamageTypeTagsProvider(
                        output, mdpProvider.getRegistryProvider(), existingFileHelper));
        gen.addProvider(
                true, new ModWeaponTraitTagsProvider(output, lookupProvider, existingFileHelper));
        gen.addProvider(
                true, new ModEnchantmentTagsProvider(output, lookupProvider, existingFileHelper));
        gen.addProvider(
                true, new ModAdvancementProvider(output, lookupProvider, existingFileHelper));
        gen.addProvider(true, new ModRecipeProvider(output, lookupProvider));
        gen.addProvider(true, new ModLootTablesProvider(output, Set.of(), lookupProvider));
        //        gen.addProvider(true, new ModGlobalLootModifierProvider(gen));
    }
}
