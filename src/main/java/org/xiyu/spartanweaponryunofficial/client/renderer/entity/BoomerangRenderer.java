package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoomerangEntity;

public class BoomerangRenderer extends ThrowingWeaponRenderer<BoomerangEntity> {
    public BoomerangRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    public void extractRenderState(BoomerangEntity entity, SWThrowingWeaponRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.ticksInAir = entity.getTicksInAir();
        state.isUnderWater = entity.isUnderWater();
        state.partialTick = partialTick;
    }

    @Override
    protected void doRenderTransformations(SWThrowingWeaponRenderState state, PoseStack matrixStack) {
        float rotationInAir = state.ticksInAir != 0 && !state.isUnderWater ? (state.ticksInAir + state.partialTick) * 40.0f % 360.0f : 0.0f;

        float partTicks = rotationInAir == 0.0f ? 0.0f : state.partialTick;

        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partTicks, state.yRotO, state.yRot) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partTicks, state.xRotO, state.xRot) - 135.0f));
        Vector3f rotation = new Vector3f(1.0f, 1.0f, 0.0f);
        rotation.normalize();
        matrixStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI, rotation.x, rotation.y, rotation.z));            // NOTE: PI = 180 degrees
        rotation = new Vector3f(1.0f, -1.0f, 0.0f);
        rotation.normalize();
        matrixStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI / 2.0f, rotation.x, rotation.y, rotation.z));        // NOTE: PI / 2 = 90 degrees
        matrixStack.mulPose(Axis.ZP.rotationDegrees(rotationInAir));
        matrixStack.translate(0.075f, 0.25f, 0.0f);
    }
}
