package org.xiyu.spartanweaponryunofficial.client.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ToggleImageButton extends Button {
    protected final Identifier textureLocation;
    protected final int texStartU, texStartV;
    protected final int toggleDiffU, hoverDiffV;
    protected final int textureWidth, textureHeight;
    private boolean toggleState;

    public ToggleImageButton(boolean isToggled, int xPos, int yPos, int width, int height, int texU, int texV,
                             int texToggleDiffU, int texHoverDiffV, Identifier textureLoc, int texWidth, int texHeight, OnPress onPress,
                             Component component) {
        super(xPos, yPos, width, height, component, onPress, DEFAULT_NARRATION);
        this.texStartU = texU;
        this.texStartV = texV;
        this.toggleDiffU = texToggleDiffU;
        this.hoverDiffV = texHoverDiffV;
        this.textureLocation = textureLoc;
        this.textureWidth = texWidth;
        this.textureHeight = texHeight;
        this.toggleState = isToggled;
    }

    @Override
    public void onPress(@NotNull InputWithModifiers input) {
        this.toggleState = !this.toggleState;
        super.onPress(input);
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int renderX, int renderY, float partialTicks) {
        extractDefaultSprite(guiGraphics);
        float u = this.texStartU;
        float v = this.texStartV;

        if (this.toggleState)
            u += this.toggleDiffU;
        if (this.isHovered())
            v += this.hoverDiffV;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.textureLocation, this.getX(), this.getY(), u, v, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
