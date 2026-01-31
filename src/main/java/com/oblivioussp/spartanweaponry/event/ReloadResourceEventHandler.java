package com.oblivioussp.spartanweaponry.event;

import java.util.List;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.IReloadable;
import com.oblivioussp.spartanweaponry.api.ReloadableHandler;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.client.OilCoatingColours;
import com.oblivioussp.spartanweaponry.init.ModOilRecipes;
import com.oblivioussp.spartanweaponry.util.Log;
import com.oblivioussp.spartanweaponry.util.WeaponArchetype;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.GAME)
public class ReloadResourceEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onUpdateTags(TagsUpdatedEvent ev)
	{
		List<WeaponMaterial> materialReloadList = ReloadableHandler.getMaterialReloadList();
		List<IReloadable> itemReloadList = ReloadableHandler.getItemReloadList();
		
		Log.debug("Initaliasing reloadables for " + materialReloadList.size() + " materials, " + WeaponArchetype.ALL_ARCHETYPES.size() + " archetypes and " + itemReloadList.size() + " items");
		long start = System.nanoTime();
		// Enforce an order of materials being reloaded first to ensure that items can fetch the appropriate traits from their materials
		// to prevent NullPointerExceptions!
		if(FMLEnvironment.dist == Dist.CLIENT)
			OilCoatingColours.reload();
		materialReloadList.forEach((material) -> material.reload());
		WeaponArchetype.ALL_ARCHETYPES.forEach((archetype) -> archetype.reload());
		itemReloadList.forEach((item) -> item.reload());
		long end = System.nanoTime();
		double milliseconds = (end-start) / 1000000.0d;
		ModOilRecipes.loadOilMixes();
		Log.info("Finished initialising Weapon Traits & Attributes! Took " + milliseconds + "ms");
	}
}
