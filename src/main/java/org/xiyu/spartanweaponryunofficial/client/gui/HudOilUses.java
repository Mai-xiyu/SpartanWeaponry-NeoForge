package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.client.gui.AlignmentHelper.Alignment;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import java.util.Optional;

public class HudOilUses {
    protected static final Identifier WIDGETS = Identifier.parse("textures/gui/widgets.png");

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        RenderSystem.assertOnRenderThread();

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        LocalPlayer player = mc.player;

        ItemStack weaponStack;
        ItemStack oilStack;
        int usesCount;
        Alignment align = ClientConfig.INSTANCE.oilUsesHudAlignment.get();
        String usesStr;
        int offsetX;
        int offsetY;

        weaponStack = player.getMainHandItem();

        IOilHandler oilHandler = weaponStack.getCapability(ModCapabilities.OIL_CAPABILITY);
        if (oilHandler == null)
            return;
        if (!oilHandler.isOiled() || oilHandler.getEffect().isEmpty())
            return;

        Optional<Potion> potionOpt = oilHandler.getPotion();
        oilStack = potionOpt.map(OilHelper::makePotionOilStack).orElseGet(() -> OilHelper.makeOilStack(oilHandler.getEffect().get()));
        usesCount = oilHandler.getUsesLeft();

        usesStr = String.format("%d/%d", usesCount, oilHandler.getEffect().get().getMaxUses());
        offsetX = AlignmentHelper.getAlignedX(align, ClientConfig.INSTANCE.oilUsesHudOffsetX.get(), 22);
        offsetY = AlignmentHelper.getAlignedY(align, ClientConfig.INSTANCE.oilUsesHudOffsetY.get(), 22);

        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0f, 0.0f, 200.0f);
//        MultiBufferSource.BufferSource renderBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        guiGraphics.fakeItem(oilStack, offsetX - 17, offsetY);

        org.joml.Matrix3x2fStack guiPose = guiGraphics.pose();
        guiPose.pushMatrix();
        guiPose.identity();
        guiGraphics.text(font, usesStr, offsetX, offsetY + 6, 0xFFFFFF);
        guiPose.popMatrix();
//		font.drawInBatch(usesStr, offsetX , offsetY + 6, 0xFFFFFF, true, poseStack.last().pose(), renderBuffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);

//		renderBuffer.endBatch();
        modelViewStack.popMatrix();
    }
}
