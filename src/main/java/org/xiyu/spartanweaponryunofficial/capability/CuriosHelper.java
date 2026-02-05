package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import org.xiyu.spartanweaponryunofficial.client.model.CurioRenderer;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.List;

public final class CuriosHelper {
    public static final boolean LOADED = ModList.get().isLoaded("curios");

    public static final class Common {
        public static void registerCapabilities(RegisterCapabilitiesEvent ev, List<Item> quiverItems) {
            ev.registerItem(CuriosCapability.ITEM, (stack, context) -> new CurioHandler((QuiverBaseItem) stack.getItem(), stack), quiverItems.toArray(Item[]::new));
        }
    }

    public static final class Client {
        @SubscribeEvent
        public static void registerReloadListener(RegisterClientReloadListenersEvent ev) {
            ev.registerReloadListener(CurioRenderer.INSTANCE);
        }
    }
}
