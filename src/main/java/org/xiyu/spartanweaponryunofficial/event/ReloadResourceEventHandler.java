package org.xiyu.spartanweaponryunofficial.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.event.TagsUpdatedEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.ReloadableHandler;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.client.OilCoatingColours;
import org.xiyu.spartanweaponryunofficial.init.ModOilRecipes;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

import java.util.List;

@Mod.EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadResourceEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUpdateTags(TagsUpdatedEvent ev) {
        List<WeaponMaterial> materialReloadList = ReloadableHandler.getMaterialReloadList();
        List<IReloadable> itemReloadList = ReloadableHandler.getItemReloadList();

        Log.debug("Initaliasing reloadables for " + materialReloadList.size() + " materials, " + WeaponArchetype.ALL_ARCHETYPES.size() + " archetypes and " + itemReloadList.size() + " items");
        long start = System.nanoTime();
        // Enforce an order of materials being reloaded first to ensure that items can fetch the appropriate traits from their materials
        // to prevent NullPointerExceptions!
        if (FMLEnvironment.dist == Dist.CLIENT)
            OilCoatingColours.reload();
        materialReloadList.forEach(WeaponMaterial::reload);
        WeaponArchetype.ALL_ARCHETYPES.forEach(WeaponArchetype::reload);
        itemReloadList.forEach(IReloadable::reload);
        long end = System.nanoTime();
        double milliseconds = (end - start) / 1000000.0d;
        ModOilRecipes.loadOilMixes();
        Log.info("Finished initialising Weapon Traits & Attributes! Took " + milliseconds + "ms");
    }
}
