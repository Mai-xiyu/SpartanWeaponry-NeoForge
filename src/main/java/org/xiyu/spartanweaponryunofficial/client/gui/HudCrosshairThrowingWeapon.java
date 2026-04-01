package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class HudCrosshairThrowingWeapon {
    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, ItemStack equippedStack) {
        RenderSystem.assertOnRenderThread();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        if ((!ClientConfig.INSTANCE.disableNewCrosshairsThrowingWeapon.get() || ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) &&
                equippedStack.getItem() instanceof ThrowingWeaponItem throwingWeapon)    // Assert that the equipped stack is a Throwing Weapon; otherwise abort the rendering
        {
            int offset = ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get() ? 20 : 10;
            if (player.isUsingItem()) {
                float percentage = Mth.clamp((player.getTicksUsingItem() + partialTicks) / throwingWeapon.getMaxChargeTicks(equippedStack, mc.level), 0.0f, 1.0f);
                offset = (int) (offset * (1.0f - percentage));
            }

            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();

            if (ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) {
                int crossOriginX = (mc.getWindow().getGuiScaledWidth() - 15) / 2;
                int crossOriginY = (mc.getWindow().getGuiScaledHeight() - 15) / 2;

                offset = Mth.floor(Math.sqrt((offset * offset) / 2.0));
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 - offset, 11f, 12f, 4, 4, 256, 256);            // Top-Left Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 - offset, 18f, 12f, 4, 4, 256, 256);        // Top-Right Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 + 7 + offset, 11f, 19f, 4, 4, 256, 256);        // Bottom-Left Part
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 + 7 + offset, 18f, 19f, 4, 4, 256, 256);    // Bottom-Right Part
            } else {
                int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 9) / 2;
                int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 5) / 2;

                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY - 2, 12f, 1f, 9, 5, 256, 256);
                guiGraphics.blit(RenderPipelines.CROSSHAIR, HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY - 2 - 3 - offset, 12f, 1f, 9, 5, 256, 256);
            }

            // Render the attack indicator if applicable
            if (mc.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR && (!ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get())) {
                float f = player.getAttackStrengthScale(0.0F);
                boolean flag = false;

                if (mc.crosshairPickEntity instanceof LivingEntity living && f >= 1.0F) {
                    flag = mc.player.getCurrentItemAttackStrengthDelay() > 5.0F;
                    flag = flag & living.isAlive();
                }

                int i = screenHeight - 7 + 16;
                int j = screenWidth - 8;

                if (flag) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, net.minecraft.resources.Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_full"), j, i, 16, 16);
                } else if (f < 1.0F) {
                    int k = (int) (f * 17.0F);
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, net.minecraft.resources.Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background"), j, i, 16, 4);
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, net.minecraft.resources.Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress"), 16, 4, 0, 0, j, i, k, 4);
                }
            }

            if (ItemStackDataHelper.getTag(equippedStack).contains(ThrowingWeaponItem.NBT_AMMO_USED)) {
                int maxAmmo = throwingWeapon.getMaxAmmo(equippedStack, player.level());
                int ammo = maxAmmo - ItemStackDataHelper.getTag(equippedStack).getIntOr(ThrowingWeaponItem.NBT_AMMO_USED, 0);

                String text = String.format("%d/%d", ammo, maxAmmo);

                org.joml.Matrix3x2fStack guiPose = guiGraphics.pose();
                guiPose.pushMatrix();
                guiPose.identity();
                guiGraphics.text(mc.font, text, screenWidth / 2 - (mc.font.width(text) / 2), screenHeight / 2 + 20, 0xFFFFFFFF);
                guiPose.popMatrix();
            }

            modelViewStack.popMatrix();
        }
    }
}