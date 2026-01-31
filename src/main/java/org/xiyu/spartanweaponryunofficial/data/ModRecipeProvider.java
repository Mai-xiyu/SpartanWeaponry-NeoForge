package org.xiyu.spartanweaponryunofficial.data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.crafting.condition.TypeDisabledCondition;
import org.xiyu.spartanweaponryunofficial.api.data.recipe.ConditionalShapedRecipeBuilder;
import org.xiyu.spartanweaponryunofficial.api.data.recipe.ConditionalShapelessRecipeBuilder;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.data.recipe.TagCookingRecipeBuilder;
import org.xiyu.spartanweaponryunofficial.data.recipe.TippedProjectileRecipeBuilder;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;
import org.xiyu.spartanweaponryunofficial.item.SwordBaseItem;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.item.crafting.ApplyOilRecipe;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModRecipeProvider extends RecipeProvider
{
	public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) 
	{
		super(output, lookupProvider);
	}
	
	@Override
	protected void buildRecipes(RecipeOutput recipeFunc) 
	{
		TagKey<Item> woodLog = ItemTags.create(ResourceLocation.parse("minecraft:logs"));
		TagKey<Item> planks = ItemTags.create(ResourceLocation.parse("minecraft:planks"));
		TagKey<Item> arrows = ItemTags.create(ResourceLocation.parse("minecraft:arrows"));
		
		TagKey<Item> stick = ItemTags.create(ResourceLocation.parse("c:rods/wooden"));
		TagKey<Item> string = ItemTags.create(ResourceLocation.parse("c:strings"));
		TagKey<Item> leather = ItemTags.create(ResourceLocation.parse("c:leathers"));
		TagKey<Item> gunpowder = ItemTags.create(ResourceLocation.parse("c:gunpowders"));
		TagKey<Item> feathers = ItemTags.create(ResourceLocation.parse("c:feathers"));
		TagKey<Item> slimeballs = ItemTags.create(ResourceLocation.parse("c:slimeballs"));
		
		TagKey<Item> bolts = ItemTags.create(ResourceLocation.parse(ModSpartanWeaponry.ID + ":bolts"));
		
		// Materials
		TagKey<Item> stone = ItemTags.create(ResourceLocation.parse(WeaponMaterial.STONE.getRepairTagName()));
		TagKey<Item> copper = ItemTags.create(ResourceLocation.parse(WeaponMaterial.COPPER.getRepairTagName()));
		TagKey<Item> iron = ItemTags.create(ResourceLocation.parse(WeaponMaterial.IRON.getRepairTagName()));
		TagKey<Item> gold = ItemTags.create(ResourceLocation.parse(WeaponMaterial.GOLD.getRepairTagName()));
		TagKey<Item> diamond = ItemTags.create(ResourceLocation.parse(WeaponMaterial.DIAMOND.getRepairTagName()));
		TagKey<Item> netherite = ItemTags.create(ResourceLocation.parse(WeaponMaterial.NETHERITE.getRepairTagName()));
		
		TagKey<Item> tin = ItemTags.create(ResourceLocation.parse(WeaponMaterial.TIN.getRepairTagName()));
		TagKey<Item> bronze = ItemTags.create(ResourceLocation.parse(WeaponMaterial.BRONZE.getRepairTagName()));
		TagKey<Item> steel = ItemTags.create(ResourceLocation.parse(WeaponMaterial.STEEL.getRepairTagName()));
		TagKey<Item> silver = ItemTags.create(ResourceLocation.parse(WeaponMaterial.SILVER.getRepairTagName()));
		TagKey<Item> electrum = ItemTags.create(ResourceLocation.parse(WeaponMaterial.ELECTRUM.getRepairTagName()));
		TagKey<Item> lead = ItemTags.create(ResourceLocation.parse(WeaponMaterial.LEAD.getRepairTagName()));
		TagKey<Item> nickel = ItemTags.create(ResourceLocation.parse(WeaponMaterial.NICKEL.getRepairTagName()));
		TagKey<Item> invar = ItemTags.create(ResourceLocation.parse(WeaponMaterial.INVAR.getRepairTagName()));
		TagKey<Item> constantan = ItemTags.create(ResourceLocation.parse(WeaponMaterial.CONSTANTAN.getRepairTagName()));
		TagKey<Item> platinum = ItemTags.create(ResourceLocation.parse(WeaponMaterial.PLATINUM.getRepairTagName()));
		TagKey<Item> aluminum = ItemTags.create(ResourceLocation.parse(WeaponMaterial.ALUMINUM.getRepairTagName()));

		TagKey<Item> copperNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/copper"));
		TagKey<Item> ironNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/iron"));
		TagKey<Item> goldNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/gold"));
		TagKey<Item> tinNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/tin"));
		TagKey<Item> bronzeNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/bronze"));
		TagKey<Item> steelNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/steel"));
		TagKey<Item> silverNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/silver"));
		TagKey<Item> electrumNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/electrum"));
		TagKey<Item> leadNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/lead"));
		TagKey<Item> nickelNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/nickel"));
		TagKey<Item> invarNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/invar"));
		TagKey<Item> constantanNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/constantan"));
		TagKey<Item> platinumNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/platinum"));
		TagKey<Item> aluminumNugget = ItemTags.create(ResourceLocation.parse("c:nuggets/aluminum"));
		
		// Handles
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.SIMPLE_HANDLE.get()).requires(stick).requires(ModItemTags.GRASS).unlockedBy("has_stick", hasItem(stick)).save(recipeFunc);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.HANDLE.get()).requires(stick).requires(string).group("spartanweaponryunofficial:handle").unlockedBy("has_string", hasItem(string)).save(recipeFunc, ModSpartanWeaponry.ID + ":handle_from_string");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.HANDLE.get(), 4).requires(stick).requires(stick).requires(stick).requires(stick).requires(ItemTags.WOOL).group("spartanweaponryunofficial:handle").unlockedBy("has_stick", hasItem(stick)).save(recipeFunc, ModSpartanWeaponry.ID + ":handle_from_wool");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.HANDLE.get(), 4).requires(stick).requires(stick).requires(stick).requires(stick).requires(leather).group("spartanweaponryunofficial:handle").unlockedBy("has_stick", hasItem(stick)).save(recipeFunc, ModSpartanWeaponry.ID + ":handle_from_leather");
		// Poles
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SIMPLE_POLE.get()).define('#', ModItems.SIMPLE_HANDLE.get()).define('/', stick).pattern("/").pattern("#").pattern("/").unlockedBy("has_handle", hasItem(ModItems.SIMPLE_HANDLE.get())).save(recipeFunc);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.POLE.get()).define('|', stick).define('#', string).pattern("| ").pattern("|#").pattern("| ").group("spartanweaponryunofficial:pole").unlockedBy("has_stick", hasItem(stick)).save(recipeFunc, ModSpartanWeaponry.ID + ":pole_from_string");
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.POLE.get(), 4).define('|', stick).define('#', ItemTags.WOOL).pattern("|||").pattern("|||").pattern("||#").group("spartanweaponryunofficial:pole").unlockedBy("has_stick", hasItem(stick)).save(recipeFunc, ModSpartanWeaponry.ID + ":pole_from_wool");
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.POLE.get(), 4).define('|', stick).define('#', leather).pattern("|||").pattern("|||").pattern("||#").group("spartanweaponryunofficial:pole").unlockedBy("has_stick", hasItem(stick)).save(recipeFunc, ModSpartanWeaponry.ID + ":pole_from_leather");
	
		ConditionalShapedRecipeBuilder.shaped(ModItems.EXPLOSIVE_CHARGE.get(), 4).define('#', gunpowder).define('-', ironNugget).pattern("###").pattern("---").pattern("###").group("spartanweaponryunofficial:explosive").unlockedBy("has_gunpowder", hasItem(gunpowder)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.EXPLOSIVES))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.GREASE_BALL.get()).define('#', ModItemTags.RAW_MEAT).define('O', slimeballs).pattern(" # ").pattern("#O#").pattern(" # ").group("spartanweaponryunofficial:grease_ball").unlockedBy("has_meat", hasItem(ModItemTags.RAW_MEAT)).unlockedBy("has_slimeball", hasItem(slimeballs)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.OIL))).save(recipeFunc);
		
		RecipeData dataWood = new RecipeData(ItemTags.PLANKS, "wood", "has_wood");
		RecipeData dataStone = new RecipeData(stone, "stone", "has_cobblestone");
		RecipeData dataLeather = new RecipeData(leather, "leather", "has_leather");
		RecipeData dataCopper = new RecipeData(copper, copperNugget, "copper", "has_copper_ingot", "copper");
		RecipeData dataIron = new RecipeData(iron, ironNugget, "iron", "has_iron_ingot");
		RecipeData dataGold = new RecipeData(gold, goldNugget, "gold", "has_gold_ingot");
		RecipeData dataDiamond = new RecipeData(diamond, "diamond", "has_diamond");
		RecipeData dataNetherite = new RecipeData(netherite, "netherite", "has_netherite_ingot");
		
		RecipeData dataTin = new RecipeData(tin, tinNugget, "tin", "has_tin_ingot", "tin");
		RecipeData dataBronze = new RecipeData(bronze, bronzeNugget, "bronze", "has_bronze_ingot", "bronze");
		RecipeData dataSteel = new RecipeData(steel, steelNugget, "steel", "has_steel_ingot", "steel");
		RecipeData dataSilver = new RecipeData(silver, silverNugget, "silver", "has_silver_ingot", "silver");
		RecipeData dataElectrum = new RecipeData(electrum, electrumNugget, "electrum", "has_electrum_ingot", "electrum");
		RecipeData dataLead = new RecipeData(lead, leadNugget, "lead", "has_lead_ingot", "lead");
		RecipeData dataNickel = new RecipeData(nickel, nickelNugget, "nickel", "has_nickel_ingot", "nickel");
		RecipeData dataInvar = new RecipeData(invar, invarNugget, "invar", "has_invar_ingot", "invar");
		RecipeData dataConstantan = new RecipeData(constantan, constantanNugget, "constantan", "has_constantan_ingot", "constantan");
		RecipeData dataPlatinum = new RecipeData(platinum, platinumNugget, "platinum", "has_platinum_ingot", "platinum");
		RecipeData dataAluminum = new RecipeData(aluminum, aluminumNugget, "aluminum", "has_aluminum_ingot", "aluminum");
		
		ImmutableList<SwordBaseItem> daggers = ModItems.DAGGERS.getAsList();
		ImmutableList<SwordBaseItem> parryingDaggers = ModItems.PARRYING_DAGGERS.getAsList();
		ImmutableList<SwordBaseItem> longswords = ModItems.LONGSWORDS.getAsList();
		ImmutableList<SwordBaseItem> katanas = ModItems.KATANAS.getAsList();
		ImmutableList<SwordBaseItem> sabers = ModItems.SABERS.getAsList();
		ImmutableList<SwordBaseItem> rapiers = ModItems.RAPIERS.getAsList();
		ImmutableList<SwordBaseItem> greatswords = ModItems.GREATSWORDS.getAsList();
		ImmutableList<SwordBaseItem> battleHammers = ModItems.BATTLE_HAMMERS.getAsList();
		ImmutableList<SwordBaseItem> warhammers = ModItems.WARHAMMERS.getAsList();
		ImmutableList<SwordBaseItem> spears = ModItems.SPEARS.getAsList();
		ImmutableList<SwordBaseItem> halberds = ModItems.HALBERDS.getAsList();
		ImmutableList<SwordBaseItem> pikes = ModItems.PIKES.getAsList();
		ImmutableList<SwordBaseItem> lances = ModItems.LANCES.getAsList();
		ImmutableList<Item> longbows = ModItems.LONGBOWS.getAsList();
		ImmutableList<Item> heavyCrossbows = ModItems.HEAVY_CROSSBOWS.getAsList();
		ImmutableList<ThrowingWeaponItem> throwingKnives = ModItems.THROWING_KNIVES.getAsList();
		ImmutableList<ThrowingWeaponItem> tomahawks = ModItems.TOMAHAWKS.getAsList();
		ImmutableList<ThrowingWeaponItem> javelins = ModItems.JAVELINS.getAsList();
		ImmutableList<ThrowingWeaponItem> boomerangs = ModItems.BOOMERANGS.getAsList();
		ImmutableList<SwordBaseItem> battleaxes = ModItems.BATTLEAXES.getAsList();
		ImmutableList<SwordBaseItem> flangedMaces = ModItems.FLANGED_MACES.getAsList();
		ImmutableList<SwordBaseItem> glaives = ModItems.GLAIVES.getAsList();
		ImmutableList<SwordBaseItem> quarterstaves = ModItems.QUARTERSTAVES.getAsList();
		ImmutableList<SwordBaseItem> scythes = ModItems.SCYTHES.getAsList();
		
		ImmutableList<RecipeData> dataList = ImmutableList.of(dataWood, dataStone, dataCopper, dataIron, dataGold, dataDiamond, dataNetherite, 
				dataTin, dataBronze, dataSteel, dataSilver, dataElectrum, dataLead, dataNickel, dataInvar, dataConstantan, dataPlatinum, dataAluminum);
		for(int i = 0; i < dataList.size(); i++)
		{
			RecipeData data = dataList.get(i);
			if(data.getMaterialTag() == netherite)
			{
				smithingRecipe(recipeFunc, ModItems.DAGGERS.diamond.get(), daggers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.PARRYING_DAGGERS.diamond.get(), parryingDaggers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.LONGSWORDS.diamond.get(), longswords.get(i), data);
				smithingRecipe(recipeFunc, ModItems.KATANAS.diamond.get(), katanas.get(i), data);
				smithingRecipe(recipeFunc, ModItems.SABERS.diamond.get(), sabers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.RAPIERS.diamond.get(), rapiers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.GREATSWORDS.diamond.get(), greatswords.get(i), data);
				smithingRecipe(recipeFunc, ModItems.BATTLE_HAMMERS.diamond.get(), battleHammers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.WARHAMMERS.diamond.get(), warhammers.get(i), data);
				smithingRecipe(recipeFunc, ModItems.SPEARS.diamond.get(), spears.get(i), data);
				smithingRecipe(recipeFunc, ModItems.HALBERDS.diamond.get(), halberds.get(i), data);
				smithingRecipe(recipeFunc, ModItems.PIKES.diamond.get(), pikes.get(i), data);
				smithingRecipe(recipeFunc, ModItems.LANCES.diamond.get(), lances.get(i), data);
				smithingRecipe(recipeFunc, ModItems.LONGBOWS.diamond.get(), longbows.get(i), data);
				smithingRecipe(recipeFunc, ModItems.HEAVY_CROSSBOWS.diamond.get(), heavyCrossbows.get(i), data);
				smithingRecipe(recipeFunc, ModItems.THROWING_KNIVES.diamond.get(), throwingKnives.get(i), data);
				smithingRecipe(recipeFunc, ModItems.TOMAHAWKS.diamond.get(), tomahawks.get(i), data);
				smithingRecipe(recipeFunc, ModItems.JAVELINS.diamond.get(), javelins.get(i), data);
				smithingRecipe(recipeFunc, ModItems.BOOMERANGS.diamond.get(), boomerangs.get(i), data);
				smithingRecipe(recipeFunc, ModItems.BATTLEAXES.diamond.get(), battleaxes.get(i), data);
				smithingRecipe(recipeFunc, ModItems.FLANGED_MACES.diamond.get(), flangedMaces.get(i), data);
				smithingRecipe(recipeFunc, ModItems.GLAIVES.diamond.get(), glaives.get(i), data);
				smithingRecipe(recipeFunc, ModItems.QUARTERSTAVES.diamond.get(), quarterstaves.get(i), data);
				smithingRecipe(recipeFunc, ModItems.SCYTHES.diamond.get(), scythes.get(i), data);
			}
			else
			{
				recipeDagger(recipeFunc, daggers.get(i), data);
				recipeParryingDagger(recipeFunc, parryingDaggers.get(i), data);
				recipeLongsword(recipeFunc, longswords.get(i), data);
				recipeKatana(recipeFunc, katanas.get(i), data);
				recipeSaber(recipeFunc, sabers.get(i), data);
				recipeRapier(recipeFunc, rapiers.get(i), data);
				recipeGreatsword(recipeFunc, greatswords.get(i), data);
				recipeBattleHammer(recipeFunc, battleHammers.get(i), data);
				recipeWarhammer(recipeFunc, warhammers.get(i), data);
				recipeSpear(recipeFunc, spears.get(i), data);
				recipeHalberd(recipeFunc, halberds.get(i), data);
				recipePike(recipeFunc, pikes.get(i), data);
				recipeLance(recipeFunc, lances.get(i), data);
				if(data.getMaterialTag() == stone)
				{
					recipeLongbow(recipeFunc, stick, string, longbows.get(i), dataLeather);
					recipeHeavyCrossbow(recipeFunc, planks, heavyCrossbows.get(i), dataLeather);
				}
				else
				{
					recipeLongbow(recipeFunc, stick, string, longbows.get(i), data);
					recipeHeavyCrossbow(recipeFunc, planks, heavyCrossbows.get(i), data);
				}
				recipeThrowingKnife(recipeFunc, throwingKnives.get(i), data);
				recipeTomahawk(recipeFunc, tomahawks.get(i), data);
				recipeJavelin(recipeFunc, javelins.get(i), data);
				recipeBoomerang(recipeFunc, planks, boomerangs.get(i), data);
				recipeBattleaxe(recipeFunc, stick, battleaxes.get(i), data);
				recipeFlangedMace(recipeFunc, stick, flangedMaces.get(i), data);
				recipeGlaive(recipeFunc, glaives.get(i), data);
				recipeQuarterstaff(recipeFunc, quarterstaves.get(i), data);
				recipeScythe(recipeFunc, scythes.get(i), data);
				
			}
			// Create nugget smelting recipes if necessary
			if(data.hasNuggetTag())
			{
				ImmutableMap.Builder<String, Item> mapBuilder = new ImmutableMap.Builder<>();
				mapBuilder.put(TypeDisabledCondition.DAGGER, daggers.get(i)).
						put(TypeDisabledCondition.PARRYING_DAGGER, parryingDaggers.get(i)).
						put(TypeDisabledCondition.LONGSWORD, longswords.get(i)).
						put(TypeDisabledCondition.KATANA, katanas.get(i)).
						put(TypeDisabledCondition.SABER, sabers.get(i)).
						put(TypeDisabledCondition.RAPIER, rapiers.get(i)).
						put(TypeDisabledCondition.GREATSWORD, greatswords.get(i)).
						put(TypeDisabledCondition.BATTLE_HAMMER, battleHammers.get(i)).
						put(TypeDisabledCondition.WARHAMMER, warhammers.get(i)).
						put(TypeDisabledCondition.SPEAR, spears.get(i)).
						put(TypeDisabledCondition.HALBERD, halberds.get(i)).
						put(TypeDisabledCondition.PIKE, pikes.get(i)).
						put(TypeDisabledCondition.LANCE, lances.get(i)).
						put(TypeDisabledCondition.LONGBOW, longbows.get(i)).
						put(TypeDisabledCondition.HEAVY_CROSSBOW, heavyCrossbows.get(i)).
						put(TypeDisabledCondition.THROWING_KNIFE, throwingKnives.get(i)).
						put(TypeDisabledCondition.TOMAHAWK, tomahawks.get(i)).
						put(TypeDisabledCondition.JAVELIN, javelins.get(i)).
						put(TypeDisabledCondition.BOOMERANG, boomerangs.get(i)).
						put(TypeDisabledCondition.BATTLEAXE, battleaxes.get(i)).
						put(TypeDisabledCondition.FLANGED_MACE, flangedMaces.get(i)).
						put(TypeDisabledCondition.GLAIVE, glaives.get(i)).
						put(TypeDisabledCondition.QUARTERSTAFF, quarterstaves.get(i)).
						put(TypeDisabledCondition.SCYTHE, scythes.get(i));
				ImmutableMap<String, Item> ingredientMap = mapBuilder.build();
				String materialName = data.getMaterialName();
				
				TagCookingRecipeBuilder smeltingRecipe = TagCookingRecipeBuilder.smelting(ingredientMap, RecipeCategory.MISC, data.getNuggetTag(), 0.1f, 200);
				if(data.isModdedMaterial())
					smeltingRecipe.addDisabledTypes(data.getDisableType());
				smeltingRecipe.save(recipeFunc, ResourceLocation.tryBuild(ModSpartanWeaponry.ID, materialName + "_nugget_from_smelting_" + materialName + "_weapons"));
				
				TagCookingRecipeBuilder blastingRecipe = TagCookingRecipeBuilder.blasting(ingredientMap, RecipeCategory.MISC, data.getNuggetTag(), 0.1f, 100);
				if(data.isModdedMaterial())
					blastingRecipe.addDisabledTypes(data.getDisableType());
				blastingRecipe.save(recipeFunc, ResourceLocation.tryBuild(ModSpartanWeaponry.ID, materialName + "_nugget_from_blasting_" + materialName + "_weapons"));
			}
		}
		
//		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.DAGGERS.iron.get()), RecipeCategory.MISC, Ingredient.of(ironNugget).getItems()[0].getItem(), 0.1f, 200).save(recipeFunc, ModSpartanWeaponry.ID + ":" + ForgeRegistries.ITEMS.getKey(Items.IRON_NUGGET).getPath() + "_from_smelting");
		
		ConditionalShapedRecipeBuilder.shaped(ModItems.WOODEN_CLUB.get()).define('#', woodLog).pattern(" #").pattern("# ").group(ModSpartanWeaponry.ID + ":club").unlockedBy("has_wood_log", hasItem(woodLog)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.CLUB))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.STUDDED_CLUB.get()).define('#', iron).define('C', ModItems.WOODEN_CLUB.get()).pattern("C#").group(ModSpartanWeaponry.ID + ":club").unlockedBy("has_club", hasItem(ModItems.WOODEN_CLUB.get())).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.CLUB))).save(recipeFunc);
		
		ConditionalShapedRecipeBuilder.shaped(ModItems.CESTUS.get()).define('l', leather).define('o', ItemTags.WOOL).pattern("lo").group(ModSpartanWeaponry.ID + ":cestus").unlockedBy("has_leather", hasItem(leather)).unlockedBy("has_wool", hasItem(ItemTags.WOOL)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.CESTUS))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.STUDDED_CESTUS.get()).define('#', iron).define('C', ModItems.CESTUS.get()).pattern("C#").group(ModSpartanWeaponry.ID + ":cestus").unlockedBy("has_cestus", hasItem(ModItems.CESTUS.get())).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.CESTUS))).save(recipeFunc);
	
		recipeArrow(recipeFunc, planks, stick, feathers, ModItems.WOODEN_ARROW.get());
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_WOODEN_ARROW.get()).input(ModItems.WOODEN_ARROW.get()).save(recipeFunc);
		recipeArrow(recipeFunc, copper, stick, feathers, ModItems.COPPER_ARROW.get(), TypeDisabledCondition.COPPER_AMMO);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_COPPER_ARROW.get()).input(ModItems.COPPER_ARROW.get()).save(recipeFunc);
		recipeArrow(recipeFunc, iron, stick, feathers, ModItems.IRON_ARROW.get());
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_IRON_ARROW.get()).input(ModItems.IRON_ARROW.get()).save(recipeFunc);
		recipeArrow(recipeFunc, diamond, stick, feathers, ModItems.DIAMOND_ARROW.get(), TypeDisabledCondition.DIAMOND_AMMO);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_DIAMOND_ARROW.get()).input(ModItems.DIAMOND_ARROW.get()).save(recipeFunc);
		ConditionalShapelessRecipeBuilder.shapeless(ModItems.NETHERITE_ARROW.get(), 8).requires(netherite).requires(ModItems.DIAMOND_ARROW.get(), 8).unlockedBy("has_netherite_ingot", hasItem(netherite)).unlockedBy("has_bow", hasItem(Items.BOW)).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.ARROWS, TypeDisabledCondition.NETHERITE_AMMO))).save(recipeFunc);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_NETHERITE_ARROW.get()).input(ModItems.NETHERITE_ARROW.get()).save(recipeFunc);
		ConditionalShapelessRecipeBuilder.shapeless(ModItems.EXPLOSIVE_ARROW.get()).requires(Items.ARROW).requires(ModItems.EXPLOSIVE_CHARGE.get()).unlockedBy("has_explosive_charge", hasItem(ModItems.EXPLOSIVE_CHARGE.get())).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.ARROWS, TypeDisabledCondition.EXPLOSIVES))).save(recipeFunc);
	
		recipeBolt(recipeFunc, iron, ironNugget, feathers, ModItems.BOLT.get(), ModItemTags.HEAVY_CROSSBOWS);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_BOLT.get()).input(ModItems.BOLT.get()).save(recipeFunc);
		ConditionalShapelessRecipeBuilder.shapeless(ModItems.SPECTRAL_BOLT.get()).requires(ModItems.BOLT.get()).requires(Items.GLOWSTONE_DUST, 2).unlockedBy("has_glowstone_dust", hasItem(Items.GLOWSTONE_DUST)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.BOLTS))).save(recipeFunc);
		recipeBolt(recipeFunc, copper, ironNugget, feathers, ModItems.COPPER_BOLT.get(), ModItemTags.HEAVY_CROSSBOWS);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_COPPER_BOLT.get()).input(ModItems.COPPER_BOLT.get()).save(recipeFunc);
		recipeBolt(recipeFunc, diamond, ironNugget, feathers, ModItems.DIAMOND_BOLT.get(), ModItemTags.HEAVY_CROSSBOWS, TypeDisabledCondition.DIAMOND_AMMO);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_DIAMOND_BOLT.get()).input(ModItems.DIAMOND_BOLT.get()).save(recipeFunc);
		ConditionalShapelessRecipeBuilder.shapeless(ModItems.NETHERITE_BOLT.get(), 8).requires(netherite).requires(ModItems.DIAMOND_BOLT.get(), 8).unlockedBy("has_netherite_ingot", hasItem(netherite)).unlockedBy("has_heavy_crossbow", hasItem(ModItemTags.HEAVY_CROSSBOWS)).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.BOLTS, TypeDisabledCondition.NETHERITE_AMMO))).save(recipeFunc);
		TippedProjectileRecipeBuilder.tipped(ModItems.TIPPED_NETHERITE_BOLT.get()).input(ModItems.NETHERITE_BOLT.get()).save(recipeFunc);
		
		ConditionalShapedRecipeBuilder.shaped(ModItems.SMALL_ARROW_QUIVER.get()).define('L', leather).define('~', string).define('^', arrows).define('#', iron).pattern("L~L").pattern("L^L").pattern("###").unlockedBy("has_arrow", hasItem(arrows)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.SMALL_ARROW_QUIVER.get(), ModItems.MEDIUM_QUIVER_BRACE.get(), ModItems.MEDIUM_ARROW_QUIVER.get(), "has_medium_quiver_brace");
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.MEDIUM_ARROW_QUIVER.get(), ModItems.LARGE_QUIVER_BRACE.get(), ModItems.LARGE_ARROW_QUIVER.get(), "has_large_quiver_brace");
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.LARGE_ARROW_QUIVER.get(), ModItems.HUGE_QUIVER_BRACE.get(), ModItems.HUGE_ARROW_QUIVER.get(), "has_huge_quiver_brace");
//		quiverSmithingRecipe(recipeFunc, ModItems.SMALL_ARROW_QUIVER.get(), ModItems.MEDIUM_QUIVER_UPGRADE_KIT.get(), ModItems.MEDIUM_ARROW_QUIVER.get(), "has_medium_quiver_upgrade_kit");
//		quiverSmithingRecipe(recipeFunc, ModItems.MEDIUM_ARROW_QUIVER.get(), ModItems.LARGE_QUIVER_UPGRADE_KIT.get(), ModItems.LARGE_ARROW_QUIVER.get(), "has_large_quiver_upgrade_kit");
//		quiverSmithingRecipe(recipeFunc, ModItems.LARGE_ARROW_QUIVER.get(), ModItems.HUGE_QUIVER_UPGRADE_KIT.get(), ModItems.HUGE_ARROW_QUIVER.get(), "has_huge_quiver_upgrade_kit");
		ConditionalShapedRecipeBuilder.shaped(ModItems.SMALL_BOLT_QUIVER.get()).define('L', leather).define('~', string).define('^', bolts).define('#', iron).pattern("L~L").pattern("L^L").pattern("###").unlockedBy("has_bolt", hasItem(bolts)).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.QUIVER, TypeDisabledCondition.BOLTS))).save(recipeFunc);
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.SMALL_BOLT_QUIVER.get(), ModItems.MEDIUM_QUIVER_BRACE.get(), ModItems.MEDIUM_BOLT_QUIVER.get(), "has_medium_quiver_brace");
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.MEDIUM_BOLT_QUIVER.get(), ModItems.LARGE_QUIVER_BRACE.get(), ModItems.LARGE_BOLT_QUIVER.get(), "has_large_quiver_brace");
		quiverSmithingRecipe(recipeFunc, ModItems.QUIVER_COMPARTMENT.get(), ModItems.LARGE_BOLT_QUIVER.get(), ModItems.HUGE_QUIVER_BRACE.get(), ModItems.HUGE_BOLT_QUIVER.get(), "has_huge_quiver_brace");
//		quiverSmithingRecipe(recipeFunc, ModItems.SMALL_BOLT_QUIVER.get(), ModItems.MEDIUM_QUIVER_UPGRADE_KIT.get(), ModItems.MEDIUM_BOLT_QUIVER.get(), "has_medium_quiver_upgrade_kit");
//		quiverSmithingRecipe(recipeFunc, ModItems.MEDIUM_BOLT_QUIVER.get(), ModItems.LARGE_QUIVER_UPGRADE_KIT.get(), ModItems.LARGE_BOLT_QUIVER.get(), "has_large_quiver_upgrade_kit");
//		quiverSmithingRecipe(recipeFunc, ModItems.LARGE_BOLT_QUIVER.get(), ModItems.HUGE_QUIVER_UPGRADE_KIT.get(), ModItems.HUGE_BOLT_QUIVER.get(), "has_huge_quiver_upgrade_kit");
		ConditionalShapedRecipeBuilder.shaped(ModItems.QUIVER_COMPARTMENT.get()).define('#', leather).define('~', string).pattern("#~#").unlockedBy("has_leather", hasItem(leather)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.MEDIUM_QUIVER_BRACE.get()).define('#', gold).define('~', string).pattern("#~#").pattern(" # ").unlockedBy("has_gold_ingot", hasItem(gold)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.LARGE_QUIVER_BRACE.get()).define('#', diamond).define('~', string).pattern("#~#").pattern(" # ").unlockedBy("has_diamond", hasItem(diamond)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
		ConditionalShapedRecipeBuilder.shaped(ModItems.HUGE_QUIVER_BRACE.get()).define('#', netherite).define('~', string).pattern("~").pattern("#").unlockedBy("has_diamond", hasItem(diamond)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
//		ConditionalShapedRecipeBuilder.shaped(ModItems.MEDIUM_QUIVER_UPGRADE_KIT.get()).define('L', leather).define('#', gold).pattern("L L").pattern("###").unlockedBy("has_gold_ingot", hasItem(gold)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
//		ConditionalShapedRecipeBuilder.shaped(ModItems.LARGE_QUIVER_UPGRADE_KIT.get()).define('L', leather).define('#', diamond).pattern("L L").pattern("###").unlockedBy("has_diamond", hasItem(diamond)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
//		ConditionalShapedRecipeBuilder.shaped(ModItems.HUGE_QUIVER_UPGRADE_KIT.get()).define('L', leather).define('#', netherite).pattern("L L").pattern(" # ").unlockedBy("has_netherite_ingot", hasItem(netherite)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.QUIVER))).save(recipeFunc);
		
		ConditionalShapedRecipeBuilder.shaped(ModItems.DYNAMITE.get(), 2).define('~', string).define('#', ModItems.EXPLOSIVE_CHARGE.get()).pattern("  ~").pattern(" # ").pattern("#  ").unlockedBy("has_explosive_charge", hasItem(ModItems.EXPLOSIVE_CHARGE.get())).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.EXPLOSIVES))).save(recipeFunc);
		
		// Mob heads recipes
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLAZE_POWDER, 4).requires(Ingredient.of(ModItems.BLAZE_HEAD.get())).unlockedBy("has_blaze_head", hasItem(ModItems.BLAZE_HEAD.get())).save(recipeFunc, ModSpartanWeaponry.ID + ":" + BuiltInRegistries.ITEM.getKey(Items.BLAZE_POWDER).getPath() + "_from_blaze_head");
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL, 2).requires(Ingredient.of(ModItems.ENDERMAN_HEAD.get())).unlockedBy("has_enderman_head", hasItem(ModItems.ENDERMAN_HEAD.get())).save(recipeFunc, ModSpartanWeaponry.ID + ":" + BuiltInRegistries.ITEM.getKey(Items.ENDER_PEARL).getPath() + "_from_enderman_head");
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SPIDER_EYE, 4).requires(Ingredient.of(ModItems.SPIDER_HEAD.get(), ModItems.CAVE_SPIDER_HEAD.get())).unlockedBy("has_spider_head", hasItem(ModItems.SPIDER_HEAD.get(), ModItems.CAVE_SPIDER_HEAD.get())).save(recipeFunc, ModSpartanWeaponry.ID + ":" + BuiltInRegistries.ITEM.getKey(Items.SPIDER_EYE).getPath() + "_from_spider_head");
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ROTTEN_FLESH, 4).requires(Ingredient.of(ModItems.ZOMBIFIED_PIGLIN_HEAD.get(), ModItems.HUSK_HEAD.get(), ModItems.DROWNED_HEAD.get())).unlockedBy("has_zombified_piglin_head", hasItem(ModItems.ZOMBIFIED_PIGLIN_HEAD.get(), ModItems.HUSK_HEAD.get(), ModItems.DROWNED_HEAD.get())).save(recipeFunc, ModSpartanWeaponry.ID + ":" + BuiltInRegistries.ITEM.getKey(Items.ROTTEN_FLESH).getPath() + "_from_zombie_variants_head");
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 4).requires(Ingredient.of(ModItems.STRAY_SKULL.get())).unlockedBy("has_stray_skull", hasItem(ModItems.STRAY_SKULL.get())).save(recipeFunc, ModSpartanWeaponry.ID + ":" + BuiltInRegistries.ITEM.getKey(Items.BONE_MEAL).getPath() + "_from_stray_skull");
		
		ItemStack weaponOilBase = OilHelper.makeOilStack(OilEffects.NONE.get());
				ConditionalShapelessRecipeBuilder.shapeless(weaponOilBase, 3).requires(ModItems.GREASE_BALL.get()).requires(Items.GLASS_BOTTLE, 3).unlockedBy("has_greaseball", hasItem(ModItems.GREASE_BALL.get())).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.OIL))).save(recipeFunc, BuiltInRegistries.ITEM.getKey(weaponOilBase.getItem()) + "_base");

		// Apply Oil recipe
		recipeFunc.accept(ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "apply_oil"), new ApplyOilRecipe(CraftingBookCategory.MISC), null);
	}
	
	private void smithingRecipe(RecipeOutput consumer, ItemLike base, ItemLike result, RecipeData data)
	{
				SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(base), Ingredient.of(data.getMaterialTag()), RecipeCategory.COMBAT, result.asItem()).unlocks(data.getCriterion(), hasItem(data.getMaterialTag())).save(consumer, BuiltInRegistries.ITEM.getKey(result.asItem()) + "_smithing");
	}
	
	private void recipeDagger(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.DAGGER) : ImmutableList.of(TypeDisabledCondition.DAGGER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItemTags.HANDLES).pattern("#").pattern("|").group("spartanweaponryunofficial:dagger").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeParryingDagger(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.PARRYING_DAGGER) : ImmutableList.of(TypeDisabledCondition.PARRYING_DAGGER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern(" #").pattern("#|").group("spartanweaponryunofficial:parrying_dagger").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeLongsword(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.LONGSWORD) : ImmutableList.of(TypeDisabledCondition.LONGSWORD, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern(" # ").pattern(" # ").pattern("#|#").group("spartanweaponryunofficial:longsword").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeKatana(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.KATANA) : ImmutableList.of(TypeDisabledCondition.KATANA, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern("  #").pattern(" # ").pattern("|  ").group("spartanweaponryunofficial:katana").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeSaber(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.SABER) : ImmutableList.of(TypeDisabledCondition.SABER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern(" #").pattern(" #").pattern("#|").group("spartanweaponryunofficial:saber").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeRapier(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.RAPIER) : ImmutableList.of(TypeDisabledCondition.RAPIER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern("  #").pattern("## ").pattern("|# ").group("spartanweaponryunofficial:rapier").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeGreatsword(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.GREATSWORD) : ImmutableList.of(TypeDisabledCondition.GREATSWORD, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern(" # ").pattern("###").pattern("#|#").group("spartanweaponryunofficial:greatsword").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeBattleHammer(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.BATTLE_HAMMER) : ImmutableList.of(TypeDisabledCondition.BATTLE_HAMMER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern("###").pattern("###").pattern(" | ").group("spartanweaponryunofficial:battle_hammer").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeWarhammer(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.WARHAMMER) : ImmutableList.of(TypeDisabledCondition.WARHAMMER, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern(" #").pattern("##").pattern(" |").group("spartanweaponryunofficial:warhammer").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeSpear(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.SPEAR) : ImmutableList.of(TypeDisabledCondition.SPEAR, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItemTags.POLES).pattern("#").pattern("/").group("spartanweaponryunofficial:spear").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeHalberd(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.HALBERD) : ImmutableList.of(TypeDisabledCondition.HALBERD, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern(" #").pattern("##").pattern("#/").group("spartanweaponryunofficial:halberd").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipePike(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.PIKE) : ImmutableList.of(TypeDisabledCondition.PIKE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern("#").pattern("/").pattern("/").group("spartanweaponryunofficial:pike").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeLance(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.LANCE) : ImmutableList.of(TypeDisabledCondition.LANCE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).define('/', ModItems.POLE.get()).pattern("  #").pattern("#/ ").pattern("|# ").group("spartanweaponryunofficial:lance").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeLongbow(RecipeOutput consumer, TagKey<Item> stick, TagKey<Item> string, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.LONGBOW) : ImmutableList.of(TypeDisabledCondition.LONGBOW, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).define('/', stick).define('~', string).pattern("|/#").pattern("/ ~").pattern("#~~").group("spartanweaponryunofficial:longbow").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeHeavyCrossbow(RecipeOutput consumer, TagKey<Item> planks, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.HEAVY_CROSSBOW) : ImmutableList.of(TypeDisabledCondition.HEAVY_CROSSBOW, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).define('P', planks).define('D', Items.BOW).define('H', Items.TRIPWIRE_HOOK).pattern("#D#").pattern("PHP").pattern(" | ").group("spartanweaponryunofficial:heavy_crossbow").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeThrowingKnife(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.THROWING_KNIFE) : ImmutableList.of(TypeDisabledCondition.THROWING_KNIFE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItemTags.HANDLES).pattern("|#").group("spartanweaponryunofficial:throwing_knife").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeTomahawk(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.TOMAHAWK) : ImmutableList.of(TypeDisabledCondition.TOMAHAWK, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).pattern("|#").pattern(" #").group("spartanweaponryunofficial:tomahawk").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeJavelin(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.JAVELIN) : ImmutableList.of(TypeDisabledCondition.JAVELIN, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern("/#").group("spartanweaponryunofficial:javelin").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeBoomerang(RecipeOutput consumer, TagKey<Item> planks, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.BOOMERANG) : ImmutableList.of(TypeDisabledCondition.BOOMERANG, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('P', planks).pattern("#PP").pattern("P  ").pattern("P  ").group("spartanweaponryunofficial:boomerang").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeBattleaxe(RecipeOutput consumer, TagKey<Item> stick, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.BATTLEAXE) : ImmutableList.of(TypeDisabledCondition.BATTLEAXE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).define('/', stick).pattern("###").pattern("#/#").pattern(" | ").group("spartanweaponryunofficial:battleaxe").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeFlangedMace(RecipeOutput consumer, TagKey<Item> stick, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.FLANGED_MACE) : ImmutableList.of(TypeDisabledCondition.FLANGED_MACE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('|', ModItems.HANDLE.get()).define('/', stick).pattern(" ##").pattern(" /#").pattern("|  ").group("spartanweaponryunofficial:flanged_mace").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeGlaive(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.GLAIVE) : ImmutableList.of(TypeDisabledCondition.GLAIVE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern(" #").pattern(" #").pattern(" /").group("spartanweaponryunofficial:glaive").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeQuarterstaff(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.QUARTERSTAFF) : ImmutableList.of(TypeDisabledCondition.QUARTERSTAFF, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern("  #").pattern(" / ").pattern("#  ").group("spartanweaponryunofficial:quarterstaff").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeScythe(RecipeOutput consumer, ItemLike result, RecipeData data)
	{
		String itemDisabledType = data.getDisableType();
		List<String> typesDisabled = itemDisabledType == null || itemDisabledType.isEmpty()  ? Collections.singletonList(TypeDisabledCondition.SCYTHE) : ImmutableList.of(TypeDisabledCondition.SCYTHE, itemDisabledType);
		ConditionalShapedRecipeBuilder builder = ConditionalShapedRecipeBuilder.shaped(result).define('#', data.getMaterialTag()).define('/', ModItems.POLE.get()).pattern("## ").pattern("  #").pattern(" / ").group("spartanweaponryunofficial:scythe").condition(new TypeDisabledCondition(typesDisabled)).unlockedBy(data.getCriterion(), hasItem(data.getMaterialTag()));
		if(data.isModdedMaterial())
			builder.condition(new NotCondition(new TagEmptyCondition(data.getMaterialTag().location().toString())));
		builder.save(consumer);
	}
	
	private void recipeArrow(RecipeOutput consumer, TagKey<Item> arrowHead, TagKey<Item> stick, TagKey<Item> feather, ItemLike result, String extraDisableType)
	{
		ConditionalShapedRecipeBuilder.shaped(result, 4).define('#', arrowHead).define('|', stick).define('F', feather).pattern("#").pattern("|").pattern("F").unlockedBy("has_feather", hasItem(feather)).unlockedBy("has_bow", hasItem(Items.BOW)).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.ARROWS, extraDisableType))).save(consumer);
	}
	
	private void recipeArrow(RecipeOutput consumer, TagKey<Item> arrowHead, TagKey<Item> stick, TagKey<Item> feather, ItemLike result)
	{
		ConditionalShapedRecipeBuilder.shaped(result, 4).define('#', arrowHead).define('|', stick).define('F', feather).pattern("#").pattern("|").pattern("F").unlockedBy("has_feather", hasItem(feather)).unlockedBy("has_bow", hasItem(Items.BOW)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.ARROWS))).save(consumer);
	}
	
	private void recipeBolt(RecipeOutput consumer, TagKey<Item> boltHead, TagKey<Item> stick, TagKey<Item> feather, ItemLike result, TagKey<Item> heavyCrossbows, String extraDisableType)
	{
		ConditionalShapedRecipeBuilder.shaped(result, 4).define('#', boltHead).define('|', stick).define('F', feather).pattern("  #").pattern(" | ").pattern("F  ").unlockedBy("has_feather", hasItem(feather)).unlockedBy("has_heavy_crossbow", hasItem(heavyCrossbows)).condition(new TypeDisabledCondition(ImmutableList.of(TypeDisabledCondition.BOLTS, extraDisableType))).save(consumer);
	}
	
	private void recipeBolt(RecipeOutput consumer, TagKey<Item> boltHead, TagKey<Item> stick, TagKey<Item> feather, ItemLike result, TagKey<Item> heavyCrossbows)
	{
		ConditionalShapedRecipeBuilder.shaped(result, 4).define('#', boltHead).define('|', stick).define('F', feather).pattern("  #").pattern(" | ").pattern("F  ").unlockedBy("has_feather", hasItem(feather)).unlockedBy("has_heavy_crossbow", hasItem(heavyCrossbows)).condition(new TypeDisabledCondition(Collections.singletonList(TypeDisabledCondition.BOLTS))).save(consumer);
	}
	
	private void quiverSmithingRecipe(RecipeOutput consumer, ItemLike template, ItemLike base, ItemLike addition, ItemLike result, String criterionName)
	{
		SmithingTransformRecipeBuilder recipe = new SmithingTransformRecipeBuilder(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), RecipeCategory.COMBAT, result.asItem());
		recipe.unlocks(criterionName, hasItem(addition)).save(consumer, BuiltInRegistries.ITEM.getKey(result.asItem()) + "_smithing");
	}
	
	private static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(TagKey<Item> tag)
	{
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag));
	}
	
	private static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike... items)
	{
		return InventoryChangeTrigger.TriggerInstance.hasItems(items);
	}
	
	public static class RecipeData
	{
		private final TagKey<Item> materialTag;
		private final Optional<TagKey<Item>> nuggetTag;
		private final String materialName;
		private final String criterion;
		private final String disableType;
		private final boolean isModdedMaterial;
		
		public RecipeData(TagKey<Item> materialTagIn, @Nullable TagKey<Item> nuggetTagIn, String materialNameIn, String criterionIn, String disableTypeIn)
		{
			materialTag = materialTagIn;
			nuggetTag = Optional.ofNullable(nuggetTagIn);
			materialName = materialNameIn;
			criterion = criterionIn;
			disableType = disableTypeIn;
			isModdedMaterial = !disableTypeIn.isEmpty();
		}
		
		public RecipeData(TagKey<Item> materialTagIn, String materialNameIn, String criterionIn, String disableTypeIn)
		{
			this(materialTagIn, null, materialNameIn, criterionIn, disableTypeIn);
		}
		
		public RecipeData(TagKey<Item> materialTagIn, TagKey<Item> nuggetTagIn, String materialNameIn, String criterionIn)
		{
			this(materialTagIn, nuggetTagIn, materialNameIn, criterionIn, "");
		}
		
		public RecipeData(TagKey<Item> materialTagIn, String materialNameIn, String criterionIn)
		{
			this(materialTagIn, materialNameIn, criterionIn, "");
		}
		
		public TagKey<Item> getMaterialTag() 
		{
			return materialTag;
		}
		
		public boolean hasNuggetTag()
		{
			return nuggetTag.isPresent();
		}
		
		public TagKey<Item> getNuggetTag()
		{
			return nuggetTag.orElseThrow();
		}
		
		public String getMaterialName()
		{
			return materialName;
		}
		
		public String getCriterion() 
		{
			return criterion;
		}
		
		public String getDisableType()
		{
			return disableType;
		}
		
		public boolean isModdedMaterial() 
		{
			return isModdedMaterial;
		}
	}


}
