package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class HudCrosshairHeavyCrossbow {
    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, ItemStack equippedStack) {
        RenderSystem.assertOnRenderThread();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        if ((!ClientConfig.INSTANCE.disableNewCrosshairsCrossbow.get() || ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) &&
                equippedStack.getItem() instanceof HeavyCrossbowItem crossbowItem)    // Assert that the equipped stack is a Heavy Crossbow; otherwise abort the rendering
        {
            // Fixed the crosshair size to account for the actual aim area, while retaining scaling.
            int offset = Mth.floor(mc.getWindow().getGuiScaledHeight() / 10.0);
            if (!equippedStack.isEmpty() && ItemStackDataHelper.getTag(equippedStack).getBooleanOr(HeavyCrossbowItem.NBT_CHARGED, false) && player.getTicksUsingItem() != 0) {
                float percentage = Mth.clamp((player.getTicksUsingItem() + partialTicks) / crossbowItem.getAimTicks(equippedStack, player.level()), 0.0f, 1.0f);
                offset *= (int) (1.0f - percentage);
            }

            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();

            if (ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) {
                int crossOriginX = (mc.getWindow().getGuiScaledWidth() - 15) / 2;
                int crossOriginY = (mc.getWindow().getGuiScaledHeight() - 15) / 2;

                offset = Mth.floor(Math.sqrt((offset * offset) / 2.0));
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 - offset, 11f, 12f, 4, 4, 256, 256);    // Top-Left Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 - offset, 18f, 12f, 4, 4, 256, 256);    // Top-Right Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 + 7 + offset, 11f, 19f, 4, 4, 256, 256);    // Bottom-Left Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 + 7 + offset, 18f, 19f, 4, 4, 256, 256);    // Bottom-Right Part
            } else {
                int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 3) / 2;
                int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 3) / 2;

                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY, 4f, 4f, 3, 3, 256, 256);        // Center Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 1, centreOriginY - 4 - offset, 5f, 0f, 1, 4, 256, 256);    // Top Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 1, centreOriginY + 3 + offset, 5f, 7f, 1, 4, 256, 256);    // Bottom Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX - 4 - offset, centreOriginY + 1, 0f, 5f, 4, 1, 256, 256);    // Left Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 3 + offset, centreOriginY + 1, 0f, 5f, 4, 1, 256, 256);    // Right Part
            }

            modelViewStack.popMatrix();
        }
    }
}