package org.xiyu.spartanweaponryunofficial.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4fStack;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;

import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HudCrosshairThrowingWeapon
{
	public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, ItemStack equippedStack)
	{
		RenderSystem.assertOnRenderThread();
		
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();
		
		if((!ClientConfig.INSTANCE.disableNewCrosshairsThrowingWeapon.get() || ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()) &&
				equippedStack.getItem() instanceof ThrowingWeaponItem throwingWeapon)	// Assert that the equipped stack is a Throwing Weapon; otherwise abort the rendering
		{
			int offset = ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get() ? 20 : 10;;
			if(player.isUsingItem())
			{
				float percentage = Mth.clamp((player.getTicksUsingItem() + partialTicks) / throwingWeapon.getMaxChargeTicks(equippedStack, mc.level), 0.0f, 1.0f);
				offset *= (1.0f - percentage);
			}
			
			Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.pushMatrix();
			
			RenderSystem.blendFuncSeparate(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
			RenderSystem.enableBlend();
	
	        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//	        RenderSystem.setShaderTexture(0, HudCrosshair.CROSSHAIR_TEXTURES);
			
			if(ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get())
			{
				int crossOriginX = (mc.getWindow().getGuiScaledWidth() - 15) / 2;
				int crossOriginY = (mc.getWindow().getGuiScaledHeight() - 15) / 2;
				
				offset = Mth.floor(Math.sqrt((offset * offset) / 2.0));
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 - offset, 11, 12, 4, 4);			// Top-Left Part
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 - offset, 18, 12, 4, 4);		// Top-Right Part
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 - offset, crossOriginY + 2 + 7 + offset, 11, 19, 4, 4);		// Bottom-Left Part
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, crossOriginX + 2 + 7 + offset, crossOriginY + 2 + 7 + offset, 18, 19, 4, 4);	// Bottom-Right Part
			}
			else
			{
				int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 9) / 2;
				int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 5) / 2;
				
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY - 2, 12, 1, 9, 5);
				guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY - 2 - 3 - offset, 12, 1, 9, 5);
			}
	        
	        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//	        RenderSystem.setShaderTexture(0, HudCrosshair.ICONS_LOCATION);
	        
	        // Render the attack indicator if applicable
	        if (mc.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR && (!ClientConfig.INSTANCE.forceCompatibilityCrosshairs.get()))
	        {
	            float f = player.getAttackStrengthScale(0.0F);
	            boolean flag = false;
	
	            if (mc.crosshairPickEntity != null && mc.crosshairPickEntity instanceof LivingEntity living && f >= 1.0F)
	            {
	                flag = mc.player.getCurrentItemAttackStrengthDelay() > 5.0F;
	                flag = flag & living.isAlive();
	            }
	
	            int i = screenHeight - 7 + 16;
	            int j = screenWidth - 8;
	
	            if (flag)
	            {
	            	guiGraphics.blit(HudCrosshair.ICONS_LOCATION, j, i, 68, 94, 16, 16);
	            }
	            else if (f < 1.0F)
	            {
	                int k = (int)(f * 17.0F);
	                guiGraphics.blit(HudCrosshair.ICONS_LOCATION, j, i, 36, 94, 16, 4);
	                guiGraphics.blit(HudCrosshair.ICONS_LOCATION, j, i, 52, 94, k, 4);
	            }
	        }

	        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	        
	        if(ItemStackDataHelper.getTag(equippedStack).contains(ThrowingWeaponItem.NBT_AMMO_USED))
	        {
				int maxAmmo = throwingWeapon.getMaxAmmo(equippedStack, player.level());
	        	int ammo = maxAmmo - ItemStackDataHelper.getTag(equippedStack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
	        	
	        	String text = String.format("%d/%d", ammo, maxAmmo);
				
//				MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

				int textX = screenWidth / 2;
				int textY = screenHeight / 2 + 20;
//				mc.font.drawInBatch(text, textX - (mc.font.width(text) / 2), textY, 0xFFFFFFFF,
//						true, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 0xF000F0);
				PoseStack guiPose = guiGraphics.pose();
				guiPose.pushPose();
				guiPose.setIdentity();
				guiGraphics.drawString(mc.font, text, textX - (mc.font.width(text) / 2), textY, 0xFFFFFFFF);
				guiPose.popPose();

//				buffer.endBatch();
	        }
	        
			modelViewStack.popMatrix();
		}
	}

}
