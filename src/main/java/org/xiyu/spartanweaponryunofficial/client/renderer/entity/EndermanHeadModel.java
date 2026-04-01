package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class EndermanHeadModel extends SkullModelBase {
    private static final String PART_HEAD = "head";

    private final ModelPart head;

    public EndermanHeadModel(ModelPart modelRoot) {
        super(modelRoot);
        this.head = modelRoot.getChild(PART_HEAD);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDef = new MeshDefinition();
        PartDefinition partDef = meshDef.getRoot();

        partDef.addOrReplaceChild(PART_HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);

        return LayerDefinition.create(meshDef, 32, 32);
    }

    @Override
    public void setupAnim(State state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float) Math.PI / 180.0f);
        this.head.xRot = state.xRot * ((float) Math.PI / 180.0f);
    }
}
