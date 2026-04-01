package org.xiyu.spartanweaponryunofficial.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.data.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID)
public class DataGenEventHandler {
    @SubscribeEvent
    public static void onDataGatherClient(GatherDataEvent.Client ev) {
        DataGenerator gen = ev.getGenerator();
        PackOutput output = gen.getPackOutput();
        gen.addProvider(true, new ModBlockModelProvider(output));
        gen.addProvider(true, new ModSoundDefinitionsProvider(output));
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onDataGatherServer(GatherDataEvent.Server ev) {
/*		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Find files to convert
		File langDir = new File("../src/main/resources/assets/spartan_weaponry_unofficial/lang");
		if(!langDir.exists())
			throw new IllegalArgumentException("Cannot find lang directory!");

		// Exclude the english language file and any file that isn't a Json file
		String[] langList = langDir.list((file, name) -> name.endsWith(".json") && !name.contains("en_us.json"));
		Log.info("Found " + langList.length +  " lang files: " + String.join(", ", langList));
		
		// Load the conversion names file
		File conversionFile = new File("conversion_names.json");
		if(!conversionFile.exists())
			throw new IllegalArgumentException("Cannot find" + conversionFile.getName() + "!");
		
		Map<String, String> conversions = null;
		try 
		{
			Reader conversionreader = Files.newBufferedReader(conversionFile.toPath(), StandardCharsets.UTF_8);
			JsonObject json = JsonParser.parseReader(conversionreader).getAsJsonObject();
			conversions = json.asMap().entrySet().stream().collect(Collectors.toUnmodifiableMap((value) -> value.getKey(), (value) -> value.getValue().getAsString()));
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		if(conversions == null)
			throw new IllegalStateException("Couldn't load " + conversionFile.getName() + "!");
		
		File convertedLangDir = new File("lang");
		if(!convertedLangDir.exists())
			convertedLangDir.mkdirs();
		
		// Convert every language file except for the english one
		for(String fileName : langList)
		{
			File inputFile = new File(langDir, fileName);
			if(!inputFile.exists())
			{
				Log.warn("Skipped missing file: " + inputFile + "!");
				continue;
			}
			
			File outputFile = new File(convertedLangDir, fileName);
			try
			{
				Reader inputReader = Files.newBufferedReader(inputFile.toPath(), StandardCharsets.UTF_8);
				JsonObject json = JsonParser.parseReader(inputReader).getAsJsonObject();
				JsonObject newJson = new JsonObject();
				int replaced = 0;
				// Perform the conversion here
				for(String key : json.keySet())
				{
					String value = json.get(key).getAsString();
					
					if(conversions.containsKey(key))
					{
						newJson.addProperty(conversions.get(key), value);
						replaced++;
					}
					else
						newJson.addProperty(key, value);
				}
				
				JsonWriter jsonWriter = gson.newJsonWriter(new FileWriter(outputFile));
				jsonWriter.setIndent("    ");
				gson.toJson(newJson, jsonWriter);
				jsonWriter.close();
				Log.info("Replaced " + replaced + " values in file " + fileName + "!");
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		Log.info("Finished converting language JSON files!");*/

        DataGenerator gen = ev.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = ev.getLookupProvider();
        ModDatapackRegistriesProvider mdpProvider = new ModDatapackRegistriesProvider(output, lookupProvider);
        gen.addProvider(true, mdpProvider);
        // ModItemModelProvider currently doesn't implement DataProvider in the 26.1 migration branch.
        // Skip registration for now to keep datagen compile-safe.
        gen.addProvider(true, new ModBlockTagsProvider(output, lookupProvider));
        gen.addProvider(true, new ModItemTagsProvider(output, lookupProvider));
        gen.addProvider(true, new ModEntityTypeTagsProvider(output, lookupProvider));
        gen.addProvider(true, new ModDamageTypeTagsProvider(output, mdpProvider.getRegistryProvider()));
        gen.addProvider(true, new ModWeaponTraitTagsProvider(output, lookupProvider));
        gen.addProvider(true, new ModAdvancementProvider(output, lookupProvider));
        gen.addProvider(true, new ModRecipeProvider(output, lookupProvider));
        gen.addProvider(true, new ModLootTablesProvider(output, Set.of(), lookupProvider));
//		gen.addProvider(true, new ModGlobalLootModifierProvider(gen));
    }
}
