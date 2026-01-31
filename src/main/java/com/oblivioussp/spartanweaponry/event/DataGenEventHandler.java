package com.oblivioussp.spartanweaponry.event;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.data.ModAdvancementProvider;
import com.oblivioussp.spartanweaponry.data.ModBlockModelProvider;
import com.oblivioussp.spartanweaponry.data.ModBlockTagsProvider;
import com.oblivioussp.spartanweaponry.data.ModDamageTypeTagsProvider;
import com.oblivioussp.spartanweaponry.data.ModDatapackRegistriesProvider;
import com.oblivioussp.spartanweaponry.data.ModEntityTypeTagsProvider;
import com.oblivioussp.spartanweaponry.data.ModItemModelProvider;
import com.oblivioussp.spartanweaponry.data.ModItemTagsProvider;
import com.oblivioussp.spartanweaponry.data.ModLootTablesProvider;
import com.oblivioussp.spartanweaponry.data.ModRecipeProvider;
import com.oblivioussp.spartanweaponry.data.ModSoundDefinitionsProvider;
import com.oblivioussp.spartanweaponry.data.ModWeaponTraitTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenEventHandler 
{
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onDataGather(GatherDataEvent ev)
	{
/*		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Find files to convert
		File langDir = new File("../src/main/resources/assets/spartanweaponry/lang");
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
		ExistingFileHelper existingFileHelper = ev.getExistingFileHelper();
		PackOutput output = gen.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = ev.getLookupProvider();
		ModDatapackRegistriesProvider mdpProvider = new ModDatapackRegistriesProvider(output, lookupProvider);
		gen.addProvider(true, mdpProvider);
		gen.addProvider(true, new ModBlockModelProvider(output, existingFileHelper));
		gen.addProvider(true, new ModItemModelProvider(output, existingFileHelper));
		gen.addProvider(true, new ModSoundDefinitionsProvider(output, existingFileHelper));
		ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
		gen.addProvider(true, blockTagsProvider);
		gen.addProvider(true, new ModItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
		gen.addProvider(true, new ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper));
		gen.addProvider(true, new ModDamageTypeTagsProvider(output, mdpProvider.getRegistryProvider(), existingFileHelper));
		gen.addProvider(true, new ModWeaponTraitTagsProvider(output, lookupProvider, existingFileHelper));
		gen.addProvider(true, new ModAdvancementProvider(output, lookupProvider, existingFileHelper));
		gen.addProvider(true, new ModRecipeProvider(output, lookupProvider));
		gen.addProvider(true, new ModLootTablesProvider(output, Set.of(), lookupProvider));
//		gen.addProvider(true, new ModGlobalLootModifierProvider(gen));
	}
}
