package org.xiyu.spartanweaponryunofficial.client.gui;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.client.gui.AlignmentHelper.Alignment;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

public class HudOilUses
{
	protected static final ResourceLocation WIDGETS = ResourceLocation.parse("textures/gui/widgets.png");
	
	public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
	{
		RenderSystem.assertOnRenderThread();
		
		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;
		LocalPlayer player = mc.player;
		
		ItemStack weaponStack = ItemStack.EMPTY;
		ItemStack oilStack = ItemStack.EMPTY;
		int usesCount = 0;
		Alignment align = ClientConfig.INSTANCE.oilUsesHudAlignment.get();
		String usesStr = "";
		int offsetX = 0;
		int offsetY = 0;
		
		weaponStack = player.getMainHandItem();
		
		IOilHandler oilHandler = weaponStack.getCapability(ModCapabilities.OIL_CAPABILITY);
		if(oilHandler == null)
			return;
		if(!oilHandler.isOiled())
			return;
		
		Optional<Potion> potionOpt = oilHandler.getPotion();
		oilStack = potionOpt.isPresent() ? OilHelper.makePotionOilStack(potionOpt.get()) : OilHelper.makeOilStack(oilHandler.getEffect().get());
		usesCount = oilHandler.getUsesLeft();
		
		usesStr = String.format("%d/%d", usesCount, oilHandler.getEffect().get().getMaxUses());
		offsetX = AlignmentHelper.getAlignedX(align, ClientConfig.INSTANCE.oilUsesHudOffsetX.get(), 22);
		offsetY = AlignmentHelper.getAlignedY(align, ClientConfig.INSTANCE.oilUsesHudOffsetY.get(), 22);

		Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushMatrix();
        modelViewStack.translate(0.0f, 0.0f, 200.0f);
//        MultiBufferSource.BufferSource renderBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);
        RenderSystem.enableBlend();
        
        guiGraphics.renderFakeItem(oilStack, offsetX - 17, offsetY);
        
		com.mojang.blaze3d.vertex.PoseStack guiPose = guiGraphics.pose();
		guiPose.pushPose();
		guiPose.setIdentity();
        guiGraphics.drawString(font, usesStr, offsetX , offsetY + 6, 0xFFFFFF);
		guiPose.popPose();
//		font.drawInBatch(usesStr, offsetX , offsetY + 6, 0xFFFFFF, true, poseStack.last().pose(), renderBuffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);
		
//		renderBuffer.endBatch();
		modelViewStack.popMatrix();
	}
}
