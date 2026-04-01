package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;

public class ThrowingWeaponRenderer<T extends ThrowingWeaponEntity> extends EntityRenderer<T, SWThrowingWeaponRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ThrowingWeaponRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
        this.itemModelResolver = rendererProvider.getItemModelResolver();
    }

    @Override
    public @NotNull SWThrowingWeaponRenderState createRenderState() {
        return new SWThrowingWeaponRenderState();
    }

    @Override
    public void extractRenderState(T entity, SWThrowingWeaponRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = entity.getYRot();
        state.xRot = entity.getXRot();
        state.yRotO = entity.yRotO;
        state.xRotO = entity.xRotO;
        state.partialTick = partialTick;
        ItemStack weapon = entity.getWeaponItem();
        state.hasWeapon = !weapon.isEmpty();
        if (state.hasWeapon) {
            this.itemModelResolver.updateForTopItem(state.weaponItem, weapon, ItemDisplayContext.GROUND, entity.level(), entity, entity.getId());
        }
    }

    @Override
    public void submit(@NotNull SWThrowingWeaponRenderState state, PoseStack matrixStackIn,
                       @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraState) {
        matrixStackIn.pushPose();
        this.doRenderTransformations(state, matrixStackIn);

        Vector3f nextRotateAxis = new Vector3f(1.0f, 1.0f, 0.0f);
        nextRotateAxis.normalize();
        matrixStackIn.mulPose(new Quaternionf().setAngleAxis(Mth.PI, nextRotateAxis.x, nextRotateAxis.y, nextRotateAxis.z));
        matrixStackIn.translate(-0.10d, -0.20d, 0.0d);

        if (state.hasWeapon) {
            state.weaponItem.submit(matrixStackIn, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        }
        matrixStackIn.popPose();
        super.submit(state, matrixStackIn, collector, cameraState);
    }

    protected void doRenderTransformations(SWThrowingWeaponRenderState state, PoseStack matrixStack) {
        float partialTicks = state.partialTick;
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, state.yRotO, state.yRot) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, state.xRotO, state.xRot) - 45.0f));
    }
}