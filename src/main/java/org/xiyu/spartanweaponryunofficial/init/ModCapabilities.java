package org.xiyu.spartanweaponryunofficial.init;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.CurioHandler;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.capability.OilHandler;
import org.xiyu.spartanweaponryunofficial.capability.QuiverItemStackHandler;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;

public class ModCapabilities
{
	public static final ItemCapability<IOilHandler, Void> OIL_CAPABILITY = ItemCapability.createVoid(
			ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "oil"), IOilHandler.class);
	public static final ItemCapability<IQuiverItemHandler, Void> QUIVER_ITEM_CAPABILITY = ItemCapability.createVoid(
			ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "quiver_item"), IQuiverItemHandler.class);

	public static void registerCapabilities(RegisterCapabilitiesEvent ev)
	{
		List<Item> oilableItems = BuiltInRegistries.ITEM.stream()
				.filter(item -> item.builtInRegistryHolder().is(ModItemTags.OILABLE_WEAPONS))
				.toList();
		if(!oilableItems.isEmpty())
		{
			ev.registerItem(OIL_CAPABILITY, (stack, context) -> new OilHandler(stack), oilableItems.toArray(Item[]::new));
		}

		List<Item> quiverItems = BuiltInRegistries.ITEM.stream()
				.filter(item -> item instanceof QuiverBaseItem)
				.toList();
		if(!quiverItems.isEmpty())
		{
			ev.registerItem(QUIVER_ITEM_CAPABILITY,
					(stack, context) -> new QuiverItemStackHandler(stack, ((QuiverBaseItem)stack.getItem()).getAmmoSlots()),
					quiverItems.toArray(Item[]::new));

			ev.registerItem(CuriosCapability.ITEM,
					(stack, context) -> new CurioHandler((QuiverBaseItem)stack.getItem(), stack),
					quiverItems.toArray(Item[]::new));
		}
	}
}
