package org.xiyu.spartanweaponryunofficial.inventory.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class QuiverTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int prioritySlot;
    private final boolean isBoltQuiver;

    public QuiverTooltip(
            NonNullList<ItemStack> itemsIn, int prioritySlotIn, boolean isBoltQuiverIn) {
        this.items = itemsIn;
        this.prioritySlot = prioritySlotIn;
        this.isBoltQuiver = isBoltQuiverIn;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getPrioritySlot() {
        return this.prioritySlot;
    }

    public boolean isBoltQuiver() {
        return this.isBoltQuiver;
    }
}
