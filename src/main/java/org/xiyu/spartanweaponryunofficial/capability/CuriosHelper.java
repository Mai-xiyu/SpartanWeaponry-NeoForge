package org.xiyu.spartanweaponryunofficial.capability;

// TODO: Curios API not available for 26.1 yet - stub out
import net.neoforged.fml.ModList;

public final class CuriosHelper {
    public static final boolean LOADED = false; // ModList.get().isLoaded("curios");

    /* Curios integration commented out until Curios is available for 26.1
    import net.minecraft.world.item.Item;
    import net.neoforged.bus.api.SubscribeEvent;
    import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
    import org.xiyu.spartanweaponryunofficial.client.model.CurioRenderer;
    import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
    import top.theillusivec4.curios.api.CuriosCapability;

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
    */
}
