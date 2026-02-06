package org.xiyu.spartanweaponryunofficial;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.trait.MeleeBlockWeaponTrait;
import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.init.*;
import org.xiyu.spartanweaponryunofficial.network.NetworkHandler;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.InternalAPIMethodHandler;
import org.xiyu.spartanweaponryunofficial.util.Log;

@Mod(value = ModSpartanWeaponry.ID)
public class ModSpartanWeaponry {
    // Mod information
    public static final String ID = "spartan_weaponry_unofficial";
    public static final String NAME = "Spartan Weaponr unofficial";

    public ModSpartanWeaponry(ModContainer modContainer, IEventBus modBus) {
        Log.info("Constructing Mod: " + NAME);
        Log.info("Initialising API! Version: " + SpartanWeaponryAPI.API_VERSION);
        SpartanWeaponryAPI.init(new InternalAPIMethodHandler());

        modBus.addListener(this::onSetup);
        modBus.addListener(this::onClientSetup);
        modBus.addListener(NetworkHandler::registerPayloads);

        // Registering Deferred Registries
        ModBlocks.REGISTRY.register(modBus);
        ModItems.REGISTRY.register(modBus);
        ModCreativeTabs.REGISTRY.register(modBus);
        ModEntities.REGISTRY.register(modBus);
        ModBlockEntities.REGISTRY.register(modBus);
        ModRecipeSerializers.REGISTRY.register(modBus);
        ModRecipeSerializers.CONDITION_SERIALIZERS.register(modBus);
        ModMenus.REGISTRY.register(modBus);
        ModSounds.REGISTRY.register(modBus);
        ModParticles.REGISTRY.register(modBus);
        ModLootModifiers.REGISTRY.register(modBus);
        ModLootModifiers.LOOT_CONDITION_REGISTRY.register(modBus);
        ModCriteriaTriggers.REGISTRY.register(modBus);
        WeaponTraits.REGISTRY.makeRegistry(registryBuilder -> {
        });
        WeaponTraits.REGISTRY.register(modBus);
        ModMobEffects.REGISTRY.register(modBus);
        OilEffects.REGISTRY.makeRegistry(registryBuilder -> registryBuilder.defaultKey(ResourceLocation.fromNamespaceAndPath(ID, "none")));
        OilEffects.REGISTRY.register(modBus);

        modBus.addListener(ModCapabilities::registerCapabilities);
        NeoForge.EVENT_BUS.addListener(MeleeBlockWeaponTrait::onBlockEvent);
        NeoForge.EVENT_BUS.addListener(ModCommands::registerCommands);
        NeoForge.EVENT_BUS.addListener(ModOilRecipes::initOilRecipes);
        if (CuriosHelper.LOADED)
            modBus.addListener(CuriosHelper.Client::registerReloadListener);

        // Place Config registration here...
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC);
        // Register extension points
//        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ConfigScreen::new);
        // NeoForge 1.21: Removed register(this) as it requires @SubscribeEvent methods
    }

    private void onSetup(FMLCommonSetupEvent ev) {
        Log.info("Setting up " + NAME + "!");
        // NeoForge 1.21: LootConditions and CriteriaTriggers now registered via DeferredRegister
        ev.enqueueWork(ModCommands::registerArgumentSerializers);
    }

    private void onClientSetup(FMLClientSetupEvent ev) {
        Log.info("Setting up Client for " + NAME + "!");
        ev.enqueueWork(() ->
        {
            ClientHelper.registerCurioRenders();
            ClientHelper.registerSkullTextures();
            // registerScreens is now handled by @SubscribeEvent on RegisterMenuScreensEvent
        });
    }
}
