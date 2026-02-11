package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.ModelEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.model.OilCoatedItemModel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModelLoaders {
    @SubscribeEvent
    public static void register(ModelEvent.RegisterGeometryLoaders ev) {
        ev.register("oil_coated_item", OilCoatedItemModel.Loader.INSTANCE);
    }
}
