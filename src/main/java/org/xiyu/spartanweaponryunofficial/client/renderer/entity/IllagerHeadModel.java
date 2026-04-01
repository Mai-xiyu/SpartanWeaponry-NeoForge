package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class IllagerHeadModel extends SkullModelBase {
    private static final String PART_HEAD = "head";
    private static final String PART_NOSE = "nose";

    private final ModelPart head;

    public IllagerHeadModel(ModelPart modelRoot) {
        super(modelRoot);
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
    public void setupAnim(State state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float) Math.PI / 180.0f);
        this.head.xRot = state.xRot * ((float) Math.PI / 180.0f);
    }
}
