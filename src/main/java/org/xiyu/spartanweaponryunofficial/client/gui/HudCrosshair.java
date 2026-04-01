package org.xiyu.spartanweaponryunofficial.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.compat.shouldersurfing.ShoulderSurfingCompat;
import org.xiyu.spartanweaponryunofficial.item.IHudCrosshair;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;

public class HudCrosshair {
    public static final Identifier CROSSHAIR_TEXTURES = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/crosshairs.png");
    public static final Identifier ICONS_LOCATION = Identifier.parse("textures/gui/icons.png");
    protected static boolean isVanillaCrosshairDisabled = false;

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
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
