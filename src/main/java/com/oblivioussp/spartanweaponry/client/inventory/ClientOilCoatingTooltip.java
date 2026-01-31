package com.oblivioussp.spartanweaponry.client.inventory;

import org.joml.Matrix4f;

import com.oblivioussp.spartanweaponry.inventory.tooltip.OilCoatingTooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.world.item.ItemStack;

public class ClientOilCoatingTooltip implements ClientTooltipComponent
{
	private final ItemStack oilStack;
	private final String text;
	
	public ClientOilCoatingTooltip(OilCoatingTooltip tooltipIn)
	{
		oilStack = tooltipIn.getOilStack();
		text = String.format("%d/%d", tooltipIn.getUsesLeft(), tooltipIn.getMaxUses());
	}
	
	@Override
	public int getHeight() 
	{
		return 20;
	}

	@Override
	public int getWidth(Font fontIn) 
	{
		return 20 + 2 + fontIn.width(oilStack.getHoverName());
	}
	
	

	@Override
	public void renderImage(Font fontIn, int posXIn, int posYIn, GuiGraphics guiGraphics)
	{
		guiGraphics.renderItem(oilStack, posXIn, posYIn + 1);
	}
	
	@Override
	public void renderText(Font fontIn, int posXIn, int posYIn, Matrix4f matrixIn, BufferSource bufferSourceIn)
	{
		fontIn.drawInBatch(oilStack.getHoverName(), posXIn + 20, posYIn, 0xFFFFFFFF, true, matrixIn, bufferSourceIn, Font.DisplayMode.NORMAL, 0, 0xF000F0);
		fontIn.drawInBatch(text, posXIn + 20, posYIn + 10, ChatFormatting.GOLD.getColor(), true, matrixIn, bufferSourceIn, Font.DisplayMode.NORMAL, 0, 0xF000F0);
	}
}
