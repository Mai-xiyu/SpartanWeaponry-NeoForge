package org.xiyu.spartanweaponryunofficial.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public interface ICrosshairOverlay {
    void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, ItemStack stack);
}
