package org.xiyu.spartanweaponryunofficial.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.client.OilCoatingColours;
import org.xiyu.spartanweaponryunofficial.init.ModOilRecipes;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.GAME)
public class ReloadResourceEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUpdateTags(TagsUpdatedEvent ev) {
        Log.debug(
                "Initaliasing reloadables for "
                        + ReloadableHandler.getMaterialReloadList().size()
                        + " materials, "
                        + WeaponArchetype.ALL_ARCHETYPES.size()
                        + " archetypes and "
                        + ReloadableHandler.getItemReloadList().size()
                        + " items");
        long start = System.nanoTime();
        // Enforce an order of materials being reloaded first to ensure that items can fetch the
        // appropriate traits from their materials
        // to prevent NullPointerExceptions!
        if (FMLEnvironment.dist == Dist.CLIENT) OilCoatingColours.reload();
        ReloadableHandler.reloadAll();
        ReloadableHandler.markTagsLoaded();
        long end = System.nanoTime();
        double milliseconds = (end - start) / 1000000.0d;
        ModOilRecipes.loadOilMixes();
        Log.info("Finished initialising Weapon Traits & Attributes! Took " + milliseconds + "ms");
    }
}
