package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.xiyu.spartanweaponryunofficial.entity.projectile.JavelinEntity;

public class JavelinRenderer<T extends JavelinEntity> extends ThrowingWeaponRenderer<T> {
    private float scale = 1.5f;

    public JavelinRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    protected void doRenderTransformations(SWThrowingWeaponRenderState state, PoseStack matrixStack) {
        this.scale = 1.5f;
        matrixStack.scale(this.scale, this.scale, this.scale);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(state.partialTick, state.yRotO, state.yRot) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(state.partialTick, state.xRotO, state.xRot) - 45.0f));

        matrixStack.translate(-0.45f, -0.35f, 0.0f);
    }
}
