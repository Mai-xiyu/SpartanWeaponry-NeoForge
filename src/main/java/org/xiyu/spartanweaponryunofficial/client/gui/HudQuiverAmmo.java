package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
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
    protected static final Identifier WIDGETS = Identifier.parse("textures/gui/widgets.png");

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
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

        // Now check and find the first available quiver if none was found in the first pass [second pass]
        if (quiverStack.isEmpty()) {
            quiverStack = QuiverHelper.findFirstQuiver(player);
        }

        if (quiverStack.isEmpty())
            return;

        ListTag list = ItemStackDataHelper.getTag(quiverStack).getCompoundOrEmpty(QuiverBaseItem.NBT_AMMO).getListOrEmpty("Items");
        RegistryOps<net.minecraft.nbt.Tag> ops = RegistryOps.create(NbtOps.INSTANCE, mc.level.registryAccess());

        for (int i = 0; i < list.size(); i++) {
            ItemStack ammoStack = ItemStack.OPTIONAL_CODEC.parse(ops, list.getCompoundOrEmpty(i)).result().orElse(ItemStack.EMPTY);
            if (!ammoStack.isEmpty() && ammoStack.getCount() != 0) {
                ammoCount += ammoStack.getCount();
            }
        }

        ammoStr = Integer.toString(ammoCount);
        offsetX = AlignmentHelper.getAlignedX(align, ClientConfig.INSTANCE.quiverHudOffsetX.get(), 22);
        offsetY = AlignmentHelper.getAlignedY(align, ClientConfig.INSTANCE.quiverHudOffsetY.get(), 22);

        org.joml.Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();
//        MultiBufferSource.BufferSource renderBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, offsetX, offsetY, 24f, 23f, 22, 22, 256, 256);
        guiGraphics.fakeItem(quiverStack, offsetX + 3, offsetY + 3);
        guiGraphics.text(font, ammoStr, offsetX + 20 - font.width(ammoStr), offsetY + 13, ammoCount == 0 ? 0xFF6060 : 0xFFC000, true);
//		font.drawInBatch(ammoStr, offsetX + 20 - font.width(ammoStr), offsetY + 13, ammoCount == 0 ? 0xFF6060 : 0xFFC000, true, poseStack.last().pose(), renderBuffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);

        // Draw the key (in text form) required to open this quiver
        if (!KeyBinds.KEY_ACCESS_QUIVER.isUnbound()) {
            String inventoryKey = "[" + KeyBinds.KEY_ACCESS_QUIVER.getTranslatedKeyMessage().getString().toUpperCase() + "]";
            int keyTextYOffset = align.getVertical() == VerticalAlignment.TOP ? 22 : -8;
            guiGraphics.text(font, inventoryKey, (int)(offsetX + 11 - ((float) font.width(inventoryKey) / 2.0f)), offsetY + keyTextYOffset, 0xFFFFFF, true);
//			font.drawInBatch(inventoryKey, offsetX + 11 - ((float)font.width(inventoryKey) / 2.0f), offsetY + keyTextYOffset, 0xFFFFFF, true, poseStack.last().pose(), renderBuffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
//		renderBuffer.endBatch();
        poseStack.popMatrix();
    }
}
