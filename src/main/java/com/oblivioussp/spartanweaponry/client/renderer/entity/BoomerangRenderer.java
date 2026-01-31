package com.oblivioussp.spartanweaponry.client.renderer.entity;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oblivioussp.spartanweaponry.entity.projectile.BoomerangEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

public class BoomerangRenderer extends ThrowingWeaponRenderer<BoomerangEntity> 
{
	public BoomerangRenderer(EntityRendererProvider.Context rendererProvider) 
	{
		super(rendererProvider);
	}
	
	@Override
	protected void doRenderTransformations(BoomerangEntity entity, float partialTicks, PoseStack matrixStack) 
	{
		float rotationInAir = entity.getTicksInAir() != 0 && (!entity.isUnderWater()) ? (entity.getTicksInAir() + partialTicks)  * 40.0f % 360.0f : 0.0f;
		if(entity.getTicksInAir() != 0)
			entity.setNoGravity(true);
		
		float partTicks = rotationInAir == 0.0f ? 0.0f : partialTicks;
		
		matrixStack.scale(2.0f, 2.0f, 2.0f);
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partTicks, entity.yRotO, entity.getYRot()) - 90.0f));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partTicks, entity.xRotO, entity.getXRot()) - 135.0f));
		Vector3f rotation = new Vector3f(1.0f, 1.0f, 0.0f);
		rotation.normalize();
		matrixStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI, rotation.x, rotation.y, rotation.z));			// NOTE: PI = 180 degrees
		rotation = new Vector3f(1.0f, -1.0f, 0.0f);
		rotation.normalize();
		matrixStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI / 2.0f, rotation.x, rotation.y, rotation.z));		// NOTE: PI / 2 = 90 degrees
		matrixStack.mulPose(Axis.ZP.rotationDegrees(rotationInAir));
		matrixStack.translate(0.075f, 0.25f, 0.0f);
	}
}
