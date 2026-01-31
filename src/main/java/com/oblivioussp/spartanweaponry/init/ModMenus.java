package com.oblivioussp.spartanweaponry.init;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.inventory.QuiverArrowMenu;
import com.oblivioussp.spartanweaponry.inventory.QuiverBoltMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus 
{
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ModSpartanWeaponry.ID);
	
	public static final DeferredHolder<MenuType<?>, MenuType<QuiverArrowMenu>> QUIVER_ARROW = REGISTRY.register("quiver_arrow", () -> IMenuTypeExtension.create(QuiverArrowMenu::createFromNetwork));
	public static final DeferredHolder<MenuType<?>, MenuType<QuiverBoltMenu>> QUIVER_BOLT = REGISTRY.register("quiver_bolt", () -> IMenuTypeExtension.create(QuiverBoltMenu::createFromNetwork));
}
