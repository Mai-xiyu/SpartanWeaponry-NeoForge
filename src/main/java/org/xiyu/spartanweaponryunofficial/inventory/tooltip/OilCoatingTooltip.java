package org.xiyu.spartanweaponryunofficial.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class OilCoatingTooltip implements TooltipComponent {
    private final ItemStack oilStack;
    private final int usesLeft;
    private final int maxUses;

    public OilCoatingTooltip(ItemStack oilStackIn, int usesLeftIn, int maxUsesIn) {
        this.oilStack = oilStackIn;
        this.usesLeft = usesLeftIn;
        this.maxUses = maxUsesIn;
    }

    public ItemStack getOilStack() {
        return this.oilStack;
    }

    public int getUsesLeft() {
        return this.usesLeft;
    }

    public int getMaxUses() {
        return this.maxUses;
    }
}
