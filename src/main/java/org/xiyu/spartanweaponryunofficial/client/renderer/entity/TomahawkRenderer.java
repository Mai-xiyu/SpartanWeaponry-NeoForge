package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.xiyu.spartanweaponryunofficial.entity.projectile.TomahawkEntity;

public class TomahawkRenderer<T extends TomahawkEntity> extends ThrowingWeaponRenderer<T> {
    float previousRotation = 0.0f;

    public TomahawkRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    protected void doRenderTransformations(SWThrowingWeaponRenderState state, PoseStack matrixStack) {
        int ticksInAir = state.ticksInAir;
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(state.partialTick, state.yRotO, state.yRot) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(state.partialTick, state.xRotO, state.xRot) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        matrixStack.translate(-0.05d, 0.05d, 0.0d);

        if (ticksInAir != 0) {
            float rotation = ((float) ticksInAir + state.partialTick) * 30.0f % 360.0f;
            matrixStack.mulPose(Axis.ZN.rotationDegrees(rotation));
        }
    }
}
