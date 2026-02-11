package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBoltMenu;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ModSpartanWeaponry.ID);

    public static final RegistryObject<MenuType<QuiverArrowMenu>> QUIVER_ARROW = REGISTRY.register("quiver_arrow", () -> IForgeMenuType.create(QuiverArrowMenu::createFromNetwork));
    public static final RegistryObject<MenuType<QuiverBoltMenu>> QUIVER_BOLT = REGISTRY.register("quiver_bolt", () -> IForgeMenuType.create(QuiverBoltMenu::createFromNetwork));
}
