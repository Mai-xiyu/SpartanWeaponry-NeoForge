package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBoltMenu;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY =
            DeferredRegister.create(Registries.MENU, ModSpartanWeaponry.ID);

    public static final DeferredHolder<MenuType<?>, MenuType<QuiverArrowMenu>> QUIVER_ARROW =
            REGISTRY.register(
                    "quiver_arrow",
                    () -> IMenuTypeExtension.create(QuiverArrowMenu::createFromNetwork));
    public static final DeferredHolder<MenuType<?>, MenuType<QuiverBoltMenu>> QUIVER_BOLT =
            REGISTRY.register(
                    "quiver_bolt",
                    () -> IMenuTypeExtension.create(QuiverBoltMenu::createFromNetwork));
}
