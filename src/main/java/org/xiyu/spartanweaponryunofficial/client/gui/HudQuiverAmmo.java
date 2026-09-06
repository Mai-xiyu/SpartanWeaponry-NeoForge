package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.client.KeyBinds;
import org.xiyu.spartanweaponryunofficial.client.gui.AlignmentHelper.Alignment;
import org.xiyu.spartanweaponryunofficial.client.gui.AlignmentHelper.VerticalAlignment;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;

public class HudQuiverAmmo {
    private static final ResourceLocation SLOT_BACKGROUND =
            ResourceLocation.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final int BACKGROUND_WIDTH = 29;
    private static final int BACKGROUND_HEIGHT = 24;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        RenderSystem.assertOnRenderThread();

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        LocalPlayer player = mc.player;

        ItemStack quiverStack = ItemStack.EMPTY;
        int ammoCount = 0;
        Alignment align = ClientConfig.INSTANCE.quiverHudAlignment.get();
        String ammoStr;
        int offsetX;
        int offsetY;

        // Check and see if the weapon equipped has an appropriate quiver first  [first pass]
        for (IQuiverInfo info : QuiverHelper.info) {
            if (info.isWeapon(player.getMainHandItem())) {
                quiverStack = QuiverHelper.findFirstOfType(player, info);
                break;
            }
        }

        // Now check and find the first available quiver if none was found in the first pass [second
        // pass]
        if (quiverStack.isEmpty()) {
            quiverStack = QuiverHelper.findFirstQuiver(player);
        }

        if (quiverStack.isEmpty()) return;

        ListTag list =
                ItemStackDataHelper.getTag(quiverStack)
                        .getCompound(QuiverBaseItem.NBT_AMMO)
                        .getList("Items", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            ItemStack ammoStack =
                    ItemStack.parseOptional(mc.level.registryAccess(), list.getCompound(i));
            if (!ammoStack.isEmpty() && ammoStack.getCount() != 0) {
                ammoCount += ammoStack.getCount();
            }
        }

        ammoStr = Integer.toString(ammoCount);
        offsetX =
                AlignmentHelper.getAlignedX(
                        align, ClientConfig.INSTANCE.quiverHudOffsetX.get(), BACKGROUND_WIDTH);
        offsetY =
                AlignmentHelper.getAlignedY(
                                align,
                                ClientConfig.INSTANCE.quiverHudOffsetY.get(),
                                BACKGROUND_HEIGHT)
                        + 1;

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        //        MultiBufferSource.BufferSource renderBuffer =
        // MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(
                SLOT_BACKGROUND, offsetX, offsetY, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        RenderSystem.disableBlend();
        guiGraphics.renderFakeItem(quiverStack, offsetX + 3, offsetY + 4);
        poseStack.translate(0.0f, 0.0f, 200.0f);
        guiGraphics.drawString(
                font,
                ammoStr,
                offsetX + 20 - font.width(ammoStr),
                offsetY + 13,
                ammoCount == 0 ? 0xFF6060 : 0xFFC000,
                true);
        //        font.drawInBatch(ammoStr, offsetX + 20 - font.width(ammoStr), offsetY + 13,
        // ammoCount ==
        // 0 ? 0xFF6060 : 0xFFC000, true, poseStack.last().pose(), renderBuffer,
        // Font.DisplayMode.NORMAL, 0, 0xF000F0);

        // Draw the key (in text form) required to open this quiver
        if (!KeyBinds.KEY_ACCESS_QUIVER.isUnbound()) {
            String inventoryKey =
                    "["
                            + KeyBinds.KEY_ACCESS_QUIVER
                                    .getTranslatedKeyMessage()
                                    .getString()
                                    .toUpperCase()
                            + "]";
            int keyTextYOffset =
                    align.getVertical() == VerticalAlignment.TOP ? BACKGROUND_HEIGHT : -8;
            guiGraphics.drawString(
                    font,
                    inventoryKey,
                    offsetX + 11 - ((float) font.width(inventoryKey) / 2.0f),
                    offsetY + keyTextYOffset,
                    0xFFFFFF,
                    true);
            //            font.drawInBatch(inventoryKey, offsetX + 11 -
            // ((float)font.width(inventoryKey) /
            // 2.0f), offsetY + keyTextYOffset, 0xFFFFFF, true, poseStack.last().pose(),
            // renderBuffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
        //        renderBuffer.endBatch();
        poseStack.popPose();
    }
}
