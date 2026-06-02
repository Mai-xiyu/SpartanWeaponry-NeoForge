package org.xiyu.spartanweaponryunofficial.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.item.IHudLoadState;

public class HudLoadState {
    protected static final int COLOUR_LOADING = 0x60FFA000; // Orange
    protected static final int COLOUR_LOAD_READY = 0x6060FFFF; // Teal-ish
    protected static final int COLOUR_LOADED = 0x6040C040; // Green

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);

        if (!mc.options.hideGui) {
            LocalPlayer player = mc.player;
            int loadSlot = -1;
            ItemStack loadStack = ItemStack.EMPTY;
            IHudLoadState loadItem = null;
            boolean isOffhand = false;

            // Get the item that this Hud element is being applied to
            if (player.getMainHandItem().getItem() instanceof IHudLoadState) {
                loadStack = player.getMainHandItem();
                loadItem = (IHudLoadState) loadStack.getItem();
                isOffhand = false;
            } else if (player.getOffhandItem().getItem() instanceof IHudLoadState) {
                loadStack = player.getOffhandItem();
                loadItem = (IHudLoadState) loadStack.getItem();
                isOffhand = true;
            }

            if (loadItem != null
                    && (!loadStack.isEmpty() && ItemStack.isSameItem(player.getUseItem(), loadStack)
                            || loadItem.isLoaded(loadStack))) {
                if (!isOffhand) {
                    // An optimization/bug-fix; the current main-hand item slot in the hotbar is
                    // already stored. Let's use that!
                    loadSlot = player.getInventory().selected;
                }

                int posX =
                        isOffhand
                                ? mc.getWindow().getGuiScaledWidth() / 2 - 117
                                : mc.getWindow().getGuiScaledWidth() / 2 - 88 + (loadSlot * 20);
                int posY = mc.getWindow().getGuiScaledHeight() - 19;
                float loadProgress = loadItem.getLoadProgress(loadStack, player);
                boolean isLoaded = loadItem.isLoaded(loadStack);
                int colour =
                        loadProgress == 1.0f
                                ? COLOUR_LOAD_READY
                                : isLoaded ? COLOUR_LOADED : COLOUR_LOADING;

                if (!isLoaded)
                    posY =
                            mc.getWindow().getGuiScaledHeight()
                                    - 3
                                    - Mth.clamp(
                                            Mth.floor((16 * loadProgress) + partialTicks), 0, 16);

                if (loadSlot != -1 || isOffhand)
                    guiGraphics.fill(
                            RenderType.guiOverlay(),
                            posX,
                            posY,
                            posX + 16,
                            (mc.getWindow().getGuiScaledHeight() - 19) + 16,
                            colour);
            }
        }
    }
}
