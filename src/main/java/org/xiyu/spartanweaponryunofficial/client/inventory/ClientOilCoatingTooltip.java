package org.xiyu.spartanweaponryunofficial.client.inventory;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.OilCoatingTooltip;

public class ClientOilCoatingTooltip implements ClientTooltipComponent {
    private final ItemStack oilStack;
    private final String text;

    public ClientOilCoatingTooltip(OilCoatingTooltip tooltipIn) {
        this.oilStack = tooltipIn.getOilStack();
        this.text = String.format("%d/%d", tooltipIn.getUsesLeft(), tooltipIn.getMaxUses());
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font fontIn) {
        return 20 + 2 + fontIn.width(this.oilStack.getHoverName());
    }

    @Override
    public void renderImage(@NotNull Font fontIn, int posXIn, int posYIn, GuiGraphics guiGraphics) {
        guiGraphics.renderItem(this.oilStack, posXIn, posYIn + 1);
    }

    @Override
    public void renderText(
            Font fontIn,
            int posXIn,
            int posYIn,
            @NotNull Matrix4f matrixIn,
            @NotNull BufferSource bufferSourceIn) {
        fontIn.drawInBatch(
                this.oilStack.getHoverName(),
                posXIn + 20,
                posYIn,
                0xFFFFFFFF,
                true,
                matrixIn,
                bufferSourceIn,
                Font.DisplayMode.NORMAL,
                0,
                0xF000F0);
        fontIn.drawInBatch(
                this.text,
                posXIn + 20,
                posYIn + 10,
                ChatFormatting.GOLD.getColor(),
                true,
                matrixIn,
                bufferSourceIn,
                Font.DisplayMode.NORMAL,
                0,
                0xF000F0);
    }
}
