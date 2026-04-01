package org.xiyu.spartanweaponryunofficial.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public abstract class QuiverModelBase extends Model<EntityRenderState> {
    protected int arrowsToRender = 0;

    public QuiverModelBase(ModelPart rootModel) {
        super(rootModel, RenderTypes::entityCutout);
    }

//    public abstract void rotate(HumanoidModel<LivingEntity> model);

    public void setArrowsToRender(int arrowsToRender) {
        this.arrowsToRender = arrowsToRender;
    }

    protected abstract void renderArrows(int arrows, PoseStack mStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color);
}
