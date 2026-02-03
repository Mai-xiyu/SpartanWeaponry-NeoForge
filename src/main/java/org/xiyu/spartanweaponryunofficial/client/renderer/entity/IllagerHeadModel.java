package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class IllagerHeadModel extends SkullModelBase {
    private static final String PART_HEAD = "head";
    private static final String PART_NOSE = "nose";

    private final ModelPart root;
    private final ModelPart head;

    public IllagerHeadModel(ModelPart modelRoot) {
        this.root = modelRoot;
        this.head = modelRoot.getChild(PART_HEAD);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDef = new MeshDefinition();
        PartDefinition rootDef = meshDef.getRoot();

        rootDef.addOrReplaceChild(PART_HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f)
                .addBox(PART_NOSE, -1.0f, -3.0f, -6.0f, 2, 4, 2, 24, 0), PartPose.ZERO);

        return LayerDefinition.create(meshDef, 64, 64);
    }

    @Override
    public void setupAnim(float p_170950_, float p_170951_, float p_170952_) {
        this.head.yRot = p_170951_ * ((float) Math.PI / 180.0f);
        this.head.xRot = p_170952_ * ((float) Math.PI / 180.0f);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
