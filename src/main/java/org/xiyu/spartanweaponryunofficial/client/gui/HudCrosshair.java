package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.ModList;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.compat.shouldersurfing.ShoulderSurfingCompat;
import org.xiyu.spartanweaponryunofficial.item.IHudCrosshair;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;

public class HudCrosshair {
    public static final ResourceLocation CROSSHAIR_TEXTURES = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/gui/crosshairs.png");
    public static final ResourceLocation ICONS_LOCATION = ResourceLocation.parse("textures/gui/icons.png");
    protected static boolean isVanillaCrosshairDisabled = false;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Options options = mc.options;
        LocalPlayer player = mc.player;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);

        ItemStack equipStack = ItemStack.EMPTY;
        if (player.getMainHandItem().getItem() instanceof IHudCrosshair)
            equipStack = player.getMainHandItem();
        else if (player.getUseItem().getItem() instanceof IHudCrosshair)
            equipStack = player.getUseItem();

        if (equipStack.isEmpty()) {
            if (isVanillaCrosshairDisabled)
                isVanillaCrosshairDisabled = false;
/*			if(!OverlayRegistry.getEntry(ForgeIngameGui.CROSSHAIR_ELEMENT).isEnabled())
				OverlayRegistry.enableOverlay(ForgeIngameGui.CROSSHAIR_ELEMENT, true);*/
        } else {
            IHudCrosshair crosshairItem = ((IHudCrosshair) equipStack.getItem());

            if (crosshairItem.getCrosshairHudElement() != null) {
//				if(OverlayRegistry.getEntry(ForgeIngameGui.CROSSHAIR_ELEMENT).isEnabled())
//					OverlayRegistry.enableOverlay(ForgeIngameGui.CROSSHAIR_ELEMENT, false);
                if (!isVanillaCrosshairDisabled)
                    isVanillaCrosshairDisabled = true;

                if ((options.getCameraType().isFirstPerson() || ModList.get().isLoaded("leawind_third_person") || (!ClientConfig.INSTANCE.disableShoulderSurfingIntegration.get() && ModList.get().isLoaded("shouldersurfing") && ShoulderSurfingCompat.isShoulderSurfing()))
                        && (mc.gameMode.getPlayerMode() != GameType.SPECTATOR || canRenderCrosshairForSpectator(mc))) {
                    // Do the debug rendering for crosshairs even with the custom crosshairs enabled
                    if (mc.getDebugOverlay().showDebugScreen() && !options.hideGui && !player.isReducedDebugInfo() && !options.reducedDebugInfo().get()) {
                        Camera camera = mc.gameRenderer.getMainCamera();
                        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
                        modelViewStack.pushMatrix();
                        modelViewStack.translate((float) (screenWidth / 2), (float) (screenHeight / 2), 0.0f);
                        modelViewStack.rotate(Axis.XN.rotationDegrees(camera.getXRot()));
                        modelViewStack.rotate(Axis.YP.rotationDegrees(camera.getYRot()));
                        modelViewStack.scale(-1.0F, -1.0F, -1.0F);
                        RenderSystem.renderCrosshair(10);
                        modelViewStack.popMatrix();
                    } else
                        crosshairItem.getCrosshairHudElement().render(guiGraphics, deltaTracker, equipStack);
                }
            }
        }
    }

    public static boolean isVanillaCrosshairDisabled() {
        return isVanillaCrosshairDisabled;
    }

    private static boolean canRenderCrosshairForSpectator(Minecraft mc) {
        HitResult hitResult = mc.hitResult;
        if (hitResult == null)
            return false;
        else if (hitResult.getType() == HitResult.Type.ENTITY)
            return ((EntityHitResult) hitResult).getEntity() instanceof MenuProvider;
        else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) hitResult).getBlockPos();
            Level level = mc.level;
            return level.getBlockState(blockpos).getMenuProvider(level, blockpos) != null;
        } else
            return false;
    }
}
