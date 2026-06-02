package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.model.OilCoatedItemModel;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModelLoaders {
    @SubscribeEvent
    public static void register(ModelEvent.RegisterGeometryLoaders ev) {
        ev.register(
                ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "oil_coated_item"),
                OilCoatedItemModel.Loader.INSTANCE);
    }
}

/*import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.model.OilCoatedItemModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModModelLoaders
{
    public static void register()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.<ModelRegistryEvent>addListener(ev -> ModelLoaderRegistry.registerLoader(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "oil_coated_item"), OilCoatedItemModel.Loader.INSTANCE));
    }
}*/
