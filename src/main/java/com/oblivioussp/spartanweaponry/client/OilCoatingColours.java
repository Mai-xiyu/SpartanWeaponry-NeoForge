package com.oblivioussp.spartanweaponry.client;

import com.oblivioussp.spartanweaponry.api.tags.ModItemTags;
import com.oblivioussp.spartanweaponry.capability.IOilHandler;
import com.oblivioussp.spartanweaponry.init.ModCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class OilCoatingColours
{
	public static final ItemColor OIL_COATED_WEAPON = (stack, idx) -> 
	{
		if(idx != 100) return 0xFFFFFFFF;
		IOilHandler oilHandler = stack.getCapability(ModCapabilities.OIL_CAPABILITY);
		if(oilHandler != null)
			return oilHandler.isOiled() ? oilHandler.getEffect().get().getColor(stack) : 0x00000000;
		return 0;
	};

	@SuppressWarnings("deprecation")
	public static void reload() 
	{
		if(FMLEnvironment.dist == Dist.CLIENT)
		{
			BuiltInRegistries.ITEM.stream()
					.filter(item -> item.builtInRegistryHolder().is(ModItemTags.OILABLE_WEAPONS))
					.forEach(item -> Minecraft.getInstance().getItemColors().register(OIL_COATED_WEAPON, item));
		}
	}

}
