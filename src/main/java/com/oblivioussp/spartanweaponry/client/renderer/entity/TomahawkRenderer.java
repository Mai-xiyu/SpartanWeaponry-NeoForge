package com.oblivioussp.spartanweaponry.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oblivioussp.spartanweaponry.entity.projectile.TomahawkEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

public class TomahawkRenderer<T extends TomahawkEntity> extends ThrowingWeaponRenderer<T> 
{
	float previousRotation = 0.0f;
	
	public TomahawkRenderer(EntityRendererProvider.Context rendererProvider)
	{
		super(rendererProvider);
	}

	@Override
	protected void doRenderTransformations(T entity, float partialTicks, PoseStack matrixStack)
	{
		int ticksInAir = entity.getTicksInAir();
		matrixStack.scale(2.0f, 2.0f, 2.0f);
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0f));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 90.0f));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
		matrixStack.translate(-0.05d, 0.05d, 0.0d);
		
		if(ticksInAir != 0)
		{
			float rotation = ((float)ticksInAir + partialTicks) * 30.0f % 360.0f;
			matrixStack.mulPose(Axis.ZN.rotationDegrees(rotation));
		}
	}
}
