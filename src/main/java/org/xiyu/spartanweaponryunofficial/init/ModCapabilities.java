package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.capability.*;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

/**
 * Capabilities for Forge 1.21.1
 * Note: Forge 1.21.1 has changed capability system significantly
 * Using simplified direct access pattern instead
 */
public class ModCapabilities {
    
    /**
     * Get oil handler for an item stack
     * In Forge 1.21.1, using simplified NBT-based approach
     */
    public static IOilHandler getOilHandler(ItemStack stack) {
        if (stack.isEmpty()) return null;
        // Return a new OilHandler that operates on the stack's NBT
        return new OilHandler(stack);
    }
    
    /**
     * Get quiver item handler for a quiver stack
     */
    public static IQuiverItemHandler getQuiverHandler(ItemStack stack) {
        if (stack.isEmpty()) return null;
        if (!(stack.getItem() instanceof QuiverBaseItem quiverItem)) return null;
        return new QuiverItemStackHandler(stack, quiverItem.getAmmoSlots());
    }
}
