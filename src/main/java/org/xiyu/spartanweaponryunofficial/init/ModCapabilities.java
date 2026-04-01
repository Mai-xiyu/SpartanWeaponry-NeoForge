package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.*;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

import java.util.List;

public class ModCapabilities {
    public static final ItemCapability<IOilHandler, Void> OIL_CAPABILITY = ItemCapability.createVoid(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "oil"), IOilHandler.class);
    public static final ItemCapability<IQuiverItemHandler, Void> QUIVER_ITEM_CAPABILITY = ItemCapability.createVoid(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_item"), IQuiverItemHandler.class);

    public static void registerCapabilities(RegisterCapabilitiesEvent ev) {
        List<Item> oilableItems = BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.wrapAsHolder(item).is(ModItemTags.OILABLE_WEAPONS))
                .toList();
        if (!oilableItems.isEmpty())
            ev.registerItem(OIL_CAPABILITY, (stack, context) -> new OilHandler(stack), oilableItems.toArray(Item[]::new));

        List<Item> quiverItems = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof QuiverBaseItem).toList();
        if (!quiverItems.isEmpty()) {
            ev.registerItem(QUIVER_ITEM_CAPABILITY, (stack, context) -> new QuiverItemStackHandler(stack, ((QuiverBaseItem) stack.getItem()).getAmmoSlots()), quiverItems.toArray(Item[]::new));

            // Curios capability bridge is temporarily disabled in 26.1 migration.
        }
    }
}
