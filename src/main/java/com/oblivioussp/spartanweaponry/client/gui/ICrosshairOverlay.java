package com.oblivioussp.spartanweaponry.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface ICrosshairOverlay 
{
	void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, ItemStack stack);
}
