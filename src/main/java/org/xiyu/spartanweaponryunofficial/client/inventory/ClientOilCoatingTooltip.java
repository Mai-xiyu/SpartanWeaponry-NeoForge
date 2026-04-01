package org.xiyu.spartanweaponryunofficial.client.inventory;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.OilCoatingTooltip;

public class ClientOilCoatingTooltip implements ClientTooltipComponent {
    private final ItemStack oilStack;
    private final String text;

    public ClientOilCoatingTooltip(OilCoatingTooltip tooltipIn) {
        this.oilStack = tooltipIn.getOilStack();
        this.text = String.format("%d/%d", tooltipIn.getUsesLeft(), tooltipIn.getMaxUses());
    }

    @Override
    public int getHeight(@NotNull Font fontIn) {
        return 20;
    }

    @Override
    public int getWidth(@NotNull Font fontIn) {
        return 20 + 2 + fontIn.width(this.oilStack.getHoverName());
    }

    @Override
    public void extractImage(@NotNull Font fontIn, int posXIn, int posYIn, int widthIn, int heightIn, @NotNull GuiGraphicsExtractor guiGraphics) {
        guiGraphics.item(this.oilStack, posXIn, posYIn + 1);
    }

    @Override
    public void extractText(@NotNull GuiGraphicsExtractor guiGraphics, @NotNull Font fontIn, int posXIn, int posYIn) {
        guiGraphics.text(fontIn, this.oilStack.getHoverName(), posXIn + 20, posYIn, 0xFFFFFFFF, true);
        guiGraphics.text(fontIn, this.text, posXIn + 20, posYIn + 10, ChatFormatting.GOLD.getColor(), true);
    }
}
