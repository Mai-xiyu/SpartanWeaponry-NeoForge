package org.xiyu.spartanweaponryunofficial.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * ModelQuiver - ObliviousSpartan Created using Tabula 8.0.0; Also edited manually by
 * ObliviousSpartan To allow arrows to be rendered if there is sufficient arrows in the quiver Also
 * edited to work in Minecraft 1.18.x
 */
@OnlyIn(Dist.CLIENT)
public class LargeArrowQuiverModel extends QuiverModelBase {
    protected static final String PART_QUIVER = "quiver";
    protected static final String PART_STRAP_FRONT = "strap_front";
    protected static final String PART_STRAP_TOP = "strap_top";
    protected static final String PART_STRAP_BACK = "strap_back";
    protected static final String PART_STRAP_BOTTOM = "strap_bottom";
    protected static final String PART_ARROW_1_PART_1 = "arrow_1_1";
    protected static final String PART_ARROW_1_PART_2 = "arrow_1_2";
    protected static final String PART_ARROW_2_PART_1 = "arrow_2_1";
    protected static final String PART_ARROW_2_PART_2 = "arrow_2_2";
    protected static final String PART_ARROW_3_PART_1 = "arrow_3_1";
    protected static final String PART_ARROW_3_PART_2 = "arrow_3_2";
    protected static final String PART_ARROW_4_PART_1 = "arrow_4_1";
    protected static final String PART_ARROW_4_PART_2 = "arrow_4_2";
    protected static final String PART_ARROW_5_PART_1 = "arrow_5_1";
    protected static final String PART_ARROW_5_PART_2 = "arrow_5_2";

    protected ModelPart quiver;
    protected ModelPart strapFront;
    protected ModelPart strapTop;
    protected ModelPart strapBack;
    protected ModelPart strapBottom;

    protected ModelPart arrow1Part1;
    protected ModelPart arrow1Part2;
    protected ModelPart arrow2Part1;
    protected ModelPart arrow2Part2;
    protected ModelPart arrow3Part1;
    protected ModelPart arrow3Part2;
    protected ModelPart arrow4Part1;
    protected ModelPart arrow4Part2;
    protected ModelPart arrow5Part1;
    protected ModelPart arrow5Part2;

    public LargeArrowQuiverModel(ModelPart rootModel) {
        super(rootModel);
        this.quiver = this.root.getChild(PART_QUIVER);
        this.strapFront = this.root.getChild(PART_STRAP_FRONT);
        this.strapTop = this.root.getChild(PART_STRAP_TOP);
        this.strapBack = this.root.getChild(PART_STRAP_BACK);
        this.strapBottom = this.root.getChild(PART_STRAP_BOTTOM);
        this.arrow1Part1 = this.root.getChild(PART_ARROW_1_PART_1);
        this.arrow1Part2 = this.root.getChild(PART_ARROW_1_PART_2);
        this.arrow2Part1 = this.root.getChild(PART_ARROW_2_PART_1);
        this.arrow2Part2 = this.root.getChild(PART_ARROW_2_PART_2);
        this.arrow3Part1 = this.root.getChild(PART_ARROW_3_PART_1);
        this.arrow3Part2 = this.root.getChild(PART_ARROW_3_PART_2);
        this.arrow4Part1 = this.root.getChild(PART_ARROW_4_PART_1);
        this.arrow4Part2 = this.root.getChild(PART_ARROW_4_PART_2);
        this.arrow5Part1 = this.root.getChild(PART_ARROW_5_PART_1);
        this.arrow5Part2 = this.root.getChild(PART_ARROW_5_PART_2);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild(
                PART_QUIVER,
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -4.0f, 3.0f, 6.0f, 8.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 4.5f, 0.0f, 0.0f, 0.0f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_STRAP_FRONT,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-6.0f, -1.0f, -3.5f, 12.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 4.5f, 0.0f, 0.0f, 0.0f, -0.8726646259971648f));
        part.addOrReplaceChild(
                PART_STRAP_TOP,
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-3.5f, -1.0f, 6.0f, 7.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(
                        0.0f, 4.5f, 0.0f, 0.0f, 1.5707963267948966f, -0.8726646259971648f));
        part.addOrReplaceChild(
                PART_STRAP_BACK,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-6.0f, -1.0f, 2.5f, 12.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 4.5f, 0.0f, 0.0f, 0.0f, -0.8726646259971648f));
        part.addOrReplaceChild(
                PART_STRAP_BOTTOM,
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-3.5f, -1.0f, -7.0f, 7.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(
                        0.0f, 4.5f, 0.0f, 0.0f, 1.5707963267948966f, -0.8726646259971648f));

        part.addOrReplaceChild(
                PART_ARROW_1_PART_1,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-8.6f, -5.6f, 1.1f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, 0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_1_PART_2,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-0.4f, -5.6f, 7.1f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, -0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_2_PART_1,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-7.4f, -5.6f, 2.4f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, 0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_2_PART_2,
                CubeListBuilder.create().texOffs(26, 0).addBox(0.9f, -5.6f, 5.9f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, -0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_3_PART_1,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-6.8f, -5.6f, 0.6f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, 0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_3_PART_2,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-0.9f, -5.6f, 5.3f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, -0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_4_PART_1,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-5.5f, -5.6f, 1.8f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, 0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_4_PART_2,
                CubeListBuilder.create().texOffs(26, 0).addBox(0.3f, -5.6f, 4.0f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, -0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_5_PART_1,
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-6.1f, -5.6f, 3.6f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, 0.7853981633974483f, -0.5235987755982988f));
        part.addOrReplaceChild(
                PART_ARROW_5_PART_2,
                CubeListBuilder.create().texOffs(26, 0).addBox(2.1f, -5.6f, 4.6f, 3.0f, 6.0f, 0.0f),
                PartPose.offsetAndRotation(
                        0.0f, -0.5f, 0.0f, 0.0f, -0.7853981633974483f, -0.5235987755982988f));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack mStack,
            @NotNull VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        ImmutableList.of(
                        this.quiver,
                        this.strapFront,
                        this.strapTop,
                        this.strapBack,
                        this.strapBottom)
                .forEach((part) -> part.render(mStack, buffer, packedLight, packedOverlay, color));
    }

    @Override
    protected void renderArrows(
            int arrows,
            PoseStack mStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int color) {
        if (arrows >= 1) {
            ImmutableList.of(this.arrow1Part1, this.arrow1Part2)
                    .forEach(
                            (part) ->
                                    part.render(mStack, buffer, packedLight, packedOverlay, color));
        }
        if (arrows >= 2) {
            ImmutableList.of(this.arrow2Part1, this.arrow2Part2)
                    .forEach(
                            (part) ->
                                    part.render(mStack, buffer, packedLight, packedOverlay, color));
        }
        if (arrows >= 3) {
            ImmutableList.of(this.arrow3Part1, this.arrow3Part2)
                    .forEach(
                            (part) ->
                                    part.render(mStack, buffer, packedLight, packedOverlay, color));
        }
        if (arrows >= 4) {
            ImmutableList.of(this.arrow4Part1, this.arrow4Part2)
                    .forEach(
                            (part) ->
                                    part.render(mStack, buffer, packedLight, packedOverlay, color));
        }
        if (arrows >= 5) {
            ImmutableList.of(this.arrow5Part1, this.arrow5Part2)
                    .forEach(
                            (part) ->
                                    part.render(mStack, buffer, packedLight, packedOverlay, color));
        }
    }
}
