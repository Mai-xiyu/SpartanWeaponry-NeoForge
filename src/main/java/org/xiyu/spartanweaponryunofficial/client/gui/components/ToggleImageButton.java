package org.xiyu.spartanweaponryunofficial.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ToggleImageButton extends Button {
    protected final ResourceLocation textureLocation;
    protected final int texStartU, texStartV;
    protected final int toggleDiffU, hoverDiffV;
    protected final int textureWidth, textureHeight;
    private boolean toggleState;

    public ToggleImageButton(boolean isToggled, int xPos, int yPos, int width, int height, int texU, int texV,
                             int texToggleDiffU, int texHoverDiffV, ResourceLocation textureLoc, int texWidth, int texHeight, OnPress onPress,
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
    public void onPress() {
        this.toggleState = !this.toggleState;
        super.onPress();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int renderX, int renderY, float p_94285_) {
        super.renderWidget(guiGraphics, renderX, renderY, p_94285_);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.textureLocation);
        float u = this.texStartU;
        float v = this.texStartV;

        if (this.toggleState)
            u += this.toggleDiffU;
        if (this.isHovered())
            v += this.hoverDiffV;

        RenderSystem.enableDepthTest();
        guiGraphics.blit(this.textureLocation, this.getX(), this.getY(), u, v, this.width, this.height, this.textureWidth, this.textureHeight);
//		if(isHovered)
//			renderToolTip(guiGraphics, renderX, renderY);
    }
}
