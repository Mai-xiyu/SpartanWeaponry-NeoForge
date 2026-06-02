package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;

public class BoltRenderer<T extends BoltEntity> extends ArrowRenderer<T> {
    public BoltRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(T entity) {
        return entity.getTexture();
    }

    @Override
    public void render(
            T entityIn,
            float entityYaw,
            float partialTicks,
            PoseStack matrixStackIn,
            @NotNull MultiBufferSource bufferIn,
            int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(
                Axis.YP.rotationDegrees(
                        Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(
                Axis.ZP.rotationDegrees(
                        Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));

        // Move the Bolt forward when rendering.
        matrixStackIn.translate(0.2f, 0.0f, 0.0f);

        float f9 = (float) entityIn.shakeTime - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f10));
        }

        matrixStackIn.mulPose(Axis.XP.rotationDegrees(45.0F));
        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer consumer =
                bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        PoseStack.Pose pose = matrixStackIn.last();
        this.vertex(pose, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        this.vertex(pose, consumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);

        for (int j = 0; j < 4; ++j) {
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose = matrixStackIn.last();
            this.vertex(pose, consumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            this.vertex(pose, consumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            this.vertex(pose, consumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            this.vertex(pose, consumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }

        matrixStackIn.popPose();
    }
}
