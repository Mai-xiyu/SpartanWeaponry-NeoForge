package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import org.xiyu.spartanweaponryunofficial.client.model.CurioRenderer;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

import java.util.List;

/**
 * Curios integration helper - disabled for Forge 1.21.1 as Curios doesn't support this version.
 */
public final class CuriosHelper {
    // Curios is not available for Forge 1.21.1
    public static final boolean LOADED = false; // ModList.get().isLoaded("curios");

    public static final class Common {
        public static void registerCapabilities(RegisterCapabilitiesEvent ev, List<Item> quiverItems) {
            // Disabled - Curios not available for Forge 1.21.1
        }
    }

    public static final class Client {
        @SubscribeEvent
        public static void registerReloadListener(RegisterClientReloadListenersEvent ev) {
            // Disabled - Curios not available for Forge 1.21.1
            // ev.registerReloadListener(CurioRenderer.INSTANCE);
        }
    }
}
