package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class HudCrosshairHeavyCrossbow {
    public static void render(
            GuiGraphics guiGraphics, DeltaTracker deltaTracker, ItemStack equippedStack) {
        RenderSystem.assertOnRenderThread();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        if ((!ClientConfig.INSTANCE.disableNewCrosshairsCrossbow.get()
                        || ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get())
                && equippedStack.getItem()
                        instanceof
                        HeavyCrossbowItem
                        crossbowItem) // Assert that the equipped stack is a Heavy Crossbow;
        // otherwise abort the rendering
        {
            //            gui.setBlitOffset(-90);

            // Fixed the crosshair size to account for the actual aim area, while retaining scaling.
            int offset = Mth.floor(mc.getWindow().getGuiScaledHeight() / 10.0);
            if (!equippedStack.isEmpty()
                    && ItemStackDataHelper.getTag(equippedStack)
                            .getBoolean(HeavyCrossbowItem.NBT_CHARGED)
                    && player.getTicksUsingItem() != 0) {
                float percentage =
                        Mth.clamp(
                                (player.getTicksUsingItem() + partialTicks)
                                        / crossbowItem.getAimTicks(equippedStack, player.level()),
                                0.0f,
                                1.0f);
                offset *= (int) (1.0f - percentage);
            }

            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();

            RenderSystem.blendFuncSeparate(
                    SourceFactor.ONE_MINUS_DST_COLOR,
                    DestFactor.ONE_MINUS_SRC_COLOR,
                    SourceFactor.ONE,
                    DestFactor.ZERO);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            //            RenderSystem.setShaderTexture(0, );
            //            gui.setupOverlayRenderState(true, false);

            if (ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) {
                int crossOriginX = (mc.getWindow().getGuiScaledWidth() - 15) / 2;
                int crossOriginY = (mc.getWindow().getGuiScaledHeight() - 15) / 2;

                offset = Mth.floor(Math.sqrt((offset * offset) / 2.0));
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        crossOriginX + 2 - offset,
                        crossOriginY + 2 - offset,
                        11,
                        12,
                        4,
                        4); // Top-Left Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        crossOriginX + 2 + 7 + offset,
                        crossOriginY + 2 - offset,
                        18,
                        12,
                        4,
                        4); // Top-Right Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        crossOriginX + 2 - offset,
                        crossOriginY + 2 + 7 + offset,
                        11,
                        19,
                        4,
                        4); // Bottom-Left Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        crossOriginX + 2 + 7 + offset,
                        crossOriginY + 2 + 7 + offset,
                        18,
                        19,
                        4,
                        4); // Bottom-Right Part
            } else {
                int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 3) / 2;
                int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 3) / 2;

                // offset = 0;
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        centreOriginX,
                        centreOriginY,
                        4,
                        4,
                        3,
                        3); // Center Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        centreOriginX + 1,
                        centreOriginY - 4 - offset,
                        5,
                        0,
                        1,
                        4); // Top Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        centreOriginX + 1,
                        centreOriginY + 3 + offset,
                        5,
                        7,
                        1,
                        4); // Bottom Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        centreOriginX - 4 - offset,
                        centreOriginY + 1,
                        0,
                        5,
                        4,
                        1); // Left Part
                guiGraphics.blit(
                        HudCrosshair.CROSSHAIR_TEXTURES,
                        centreOriginX + 3 + offset,
                        centreOriginY + 1,
                        0,
                        5,
                        4,
                        1); // Right Part
            }

            RenderSystem.blendFuncSeparate(
                    SourceFactor.SRC_ALPHA,
                    DestFactor.ONE_MINUS_SRC_ALPHA,
                    SourceFactor.ONE,
                    DestFactor.ZERO);
            modelViewStack.popMatrix();
        }
    }
}
