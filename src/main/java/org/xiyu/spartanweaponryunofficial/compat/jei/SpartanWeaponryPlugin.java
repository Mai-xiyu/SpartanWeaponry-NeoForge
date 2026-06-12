package org.xiyu.spartanweaponryunofficial.compat.jei;

import java.util.Arrays;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.Log;

@JeiPlugin
public class SpartanWeaponryPlugin implements IModPlugin {
    private final ResourceLocation PLUGIN_UID =
            ResourceLocation.tryBuild("spartan_weaponry_unofficial", "jei_plugin");

    public @NotNull ResourceLocation getPluginUid() {
        return this.PLUGIN_UID;
    }

    public void registerItemSubtypes(@NotNull ISubtypeRegistration subtypeRegistry) {
        if (ModList.get().isLoaded("emi")) return;
        Log.info("JEI Plugin is Registering subtypes");

        for (Item tippedProjectile :
                new Item[] {
                    ModItems.TIPPED_WOODEN_ARROW.get(),
                    ModItems.TIPPED_COPPER_ARROW.get(),
                    ModItems.TIPPED_IRON_ARROW.get(),
                    ModItems.TIPPED_DIAMOND_ARROW.get(),
                    ModItems.TIPPED_NETHERITE_ARROW.get(),
                    ModItems.TIPPED_BOLT.get(),
                    ModItems.TIPPED_COPPER_BOLT.get(),
                    ModItems.TIPPED_DIAMOND_BOLT.get(),
                    ModItems.TIPPED_NETHERITE_BOLT.get()
                }) {
            subtypeRegistry.registerSubtypeInterpreter(
                    tippedProjectile, TippedProjectileSubtypeInterpreter.INSTANCE);
        }
        subtypeRegistry.registerSubtypeInterpreter(
                ModItems.WEAPON_OIL.get(), WeaponOilSubtypeInterpreter.INSTANCE);
    }

    public void registerRecipes(IRecipeRegistration reg) {
        // Does EMI already cover this?
        //        if(ModList.get().isLoaded("emi")) return;

        reg.addRecipes(
                RecipeTypes.CRAFTING,
                TippedProjectileRecipeMaker.getRecipes(
                        ModItems.BOLT.get(), ModItems.TIPPED_BOLT.get()));

        if (!Config.INSTANCE.disableNewArrowRecipes.get()) {
            reg.addRecipes(
                    RecipeTypes.CRAFTING,
                    TippedProjectileRecipeMaker.getRecipes(
                            ModItems.WOODEN_ARROW.get(), ModItems.TIPPED_WOODEN_ARROW.get()));
            reg.addRecipes(
                    RecipeTypes.CRAFTING,
                    TippedProjectileRecipeMaker.getRecipes(
                            ModItems.IRON_ARROW.get(), ModItems.TIPPED_IRON_ARROW.get()));
        }
        if (!Config.INSTANCE.disableCopperAmmoRecipes.get()) {
            reg.addRecipes(
                    RecipeTypes.CRAFTING,
                    TippedProjectileRecipeMaker.getRecipes(
                            ModItems.COPPER_BOLT.get(), ModItems.TIPPED_COPPER_BOLT.get()));
            if (!Config.INSTANCE.disableNewArrowRecipes.get())
                reg.addRecipes(
                        RecipeTypes.CRAFTING,
                        TippedProjectileRecipeMaker.getRecipes(
                                ModItems.COPPER_ARROW.get(), ModItems.TIPPED_COPPER_ARROW.get()));
        }
        if (!Config.INSTANCE.disableDiamondAmmoRecipes.get()) {
            reg.addRecipes(
                    RecipeTypes.CRAFTING,
                    TippedProjectileRecipeMaker.getRecipes(
                            ModItems.DIAMOND_BOLT.get(), ModItems.TIPPED_DIAMOND_BOLT.get()));
            if (!Config.INSTANCE.disableNewArrowRecipes.get())
                reg.addRecipes(
                        RecipeTypes.CRAFTING,
                        TippedProjectileRecipeMaker.getRecipes(
                                ModItems.DIAMOND_ARROW.get(), ModItems.TIPPED_DIAMOND_ARROW.get()));
        }
        if (!Config.INSTANCE.disableNetheriteAmmoRecipes.get()) {
            reg.addRecipes(
                    RecipeTypes.CRAFTING,
                    TippedProjectileRecipeMaker.getRecipes(
                            ModItems.NETHERITE_BOLT.get(), ModItems.TIPPED_NETHERITE_BOLT.get()));
            if (!Config.INSTANCE.disableNewArrowRecipes.get())
                reg.addRecipes(
                        RecipeTypes.CRAFTING,
                        TippedProjectileRecipeMaker.getRecipes(
                                ModItems.NETHERITE_ARROW.get(),
                                ModItems.TIPPED_NETHERITE_ARROW.get()));
        }

        //        if(!ModList.get().isLoaded("emi"))
        reg.addRecipes(
                RecipeTypes.BREWING,
                OilBrewingRecipeMaker.getRecipes(reg.getVanillaRecipeFactory()));
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        if (Config.INSTANCE.forceShowDisabledItems
                .get()) // Skip disabling items if this config option is enabled
        return;
        if (Config.INSTANCE.daggers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.DAGGERS);
        if (Config.INSTANCE.parryingDaggers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.PARRYING_DAGGERS);
        if (Config.INSTANCE.longswords.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.LONGSWORDS);
        if (Config.INSTANCE.katanas.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.KATANAS);
        if (Config.INSTANCE.sabers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.SABERS);
        if (Config.INSTANCE.rapiers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.RAPIERS);
        if (Config.INSTANCE.greatswords.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.GREATSWORDS);
        if (Config.INSTANCE.clubs.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.CLUBS);
        if (Config.INSTANCE.cestus.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.CESTUSAE);
        if (Config.INSTANCE.battleHammers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.BATTLE_HAMMERS);
        if (Config.INSTANCE.warhammers.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.WARHAMMERS);
        if (Config.INSTANCE.spears.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.SPEARS);
        if (Config.INSTANCE.halberds.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.HALBERDS);
        if (Config.INSTANCE.pikes.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.PIKES);
        if (Config.INSTANCE.lances.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.LANCES);
        if (Config.INSTANCE.longbows.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.LONGBOWS);
        if (Config.INSTANCE.heavyCrossbows.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.HEAVY_CROSSBOWS);
        if (Config.INSTANCE.throwingKnives.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.THROWING_KNIVES);
        if (Config.INSTANCE.tomahawks.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.TOMAHAWKS);
        if (Config.INSTANCE.javelins.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.JAVELINS);
        if (Config.INSTANCE.boomerangs.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.BOOMERANGS);
        if (Config.INSTANCE.battleaxes.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.BATTLEAXES);
        if (Config.INSTANCE.flangedMaces.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.FLANGED_MACES);
        if (Config.INSTANCE.glaives.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.GLAIVES);
        if (Config.INSTANCE.quarterstaves.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.QUARTERSTAVES);
        if (Config.INSTANCE.scythes.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.SCYTHES);

        if (Config.INSTANCE.copper.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.COPPER_WEAPONS);
        if (Config.INSTANCE.tin.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.TIN_WEAPONS);
        if (Config.INSTANCE.bronze.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.BRONZE_WEAPONS);
        if (Config.INSTANCE.steel.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.STEEL_WEAPONS);
        if (Config.INSTANCE.silver.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.SILVER_WEAPONS);
        if (Config.INSTANCE.electrum.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.ELECTRUM_WEAPONS);
        if (Config.INSTANCE.lead.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.LEAD_WEAPONS);
        if (Config.INSTANCE.nickel.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.NICKEL_WEAPONS);
        if (Config.INSTANCE.invar.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.INVAR_WEAPONS);
        if (Config.INSTANCE.constantan.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.CONSTANTAN_WEAPONS);
        if (Config.INSTANCE.platinum.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.PLATINUM_WEAPONS);
        if (Config.INSTANCE.aluminum.disableRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.ALUMINUM_WEAPONS);

        if (Config.INSTANCE.disableNewArrowRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.ARROWS);
        if (Config.INSTANCE.disableDiamondAmmoRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.DIAMOND_PROJECTILES);
        if (Config.INSTANCE.disableQuiverRecipes.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.QUIVERS);
        if (Config.INSTANCE.disableRecipesExplosives.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.EXPLOSIVES);

        if (Config.INSTANCE.disableNewHeadDrops.get())
            this.removeItemTagFromJEI(jeiRuntime, ModItemTags.HEADS);
    }

    private void removeItemTagFromJEI(IJeiRuntime jeiRuntime, TagKey<Item> tag) {
        jeiRuntime
                .getIngredientManager()
                .removeIngredientsAtRuntime(
                        VanillaTypes.ITEM_STACK, Arrays.asList(Ingredient.of(tag).getItems()));
    }
}
