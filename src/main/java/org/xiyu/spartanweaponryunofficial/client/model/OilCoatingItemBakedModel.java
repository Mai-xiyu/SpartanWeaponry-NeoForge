package org.xiyu.spartanweaponryunofficial.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.CompositeModel;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.WeaponOilConfig;

public class OilCoatingItemBakedModel extends CompositeModel.Baked {
    private final ImmutableList<BakedModel> coatedLayerModels;

    public OilCoatingItemBakedModel(
            boolean isGui3d,
            boolean isSideLit,
            boolean isAmbientOcclusion,
            TextureAtlasSprite particle,
            ItemTransforms transforms,
            ItemOverrides overrides,
            ImmutableMap<String, BakedModel> children,
            ImmutableList<BakedModel> itemPasses,
            ImmutableList<BakedModel> coatedLayerModelsIn) {
        super(
                isGui3d,
                isSideLit,
                isAmbientOcclusion,
                particle,
                transforms,
                overrides,
                children,
                itemPasses);
        this.coatedLayerModels = coatedLayerModelsIn;
    }

    @Override
    public @NotNull List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        IOilHandler handler = itemStack.getCapability(ModCapabilities.OIL_CAPABILITY);
        return WeaponOilConfig.isEnabled()
                        && handler != null
                        && handler.isOiled()
                        && !this.coatedLayerModels.isEmpty()
                ? this.coatedLayerModels
                : super.getRenderPasses(itemStack, fabulous);
    }

    public static Builder makeBuilder(
            IGeometryBakingContext contextIn,
            TextureAtlasSprite particleIn,
            ItemOverrides overridesIn,
            ItemTransforms transformsIn) {
        return new Builder(
                contextIn.getModelName(),
                contextIn.useAmbientOcclusion(),
                contextIn.isGui3d(),
                contextIn.useBlockLight(),
                particleIn,
                overridesIn,
                transformsIn);
    }

    public static class Builder {
        private final String name;
        private final boolean isAmbientOcclusion, isGui3d, isSideLit;
        private final List<BakedModel> children = new ArrayList<>();
        private final List<BakedQuad> quads = new ArrayList<>();
        private final List<BakedModel> coatedModel = new ArrayList<>();
        private final ItemOverrides overrides;
        private final ItemTransforms transforms;
        private final TextureAtlasSprite particle;
        private RenderTypeGroup lastRenderType = RenderTypeGroup.EMPTY;

        private Builder(
                String nameIn,
                boolean isAmbientOcclusionIn,
                boolean isGui3dIn,
                boolean isSideLitIn,
                TextureAtlasSprite particleIn,
                ItemOverrides overridesIn,
                ItemTransforms transformsIn) {
            this.name = nameIn;
            this.isAmbientOcclusion = isAmbientOcclusionIn;
            this.isGui3d = isGui3dIn;
            this.isSideLit = isSideLitIn;
            this.overrides = overridesIn;
            this.transforms = transformsIn;
            this.particle = particleIn;
        }

        private void addChildrenLayer(RenderTypeGroup renderTypeIn, List<BakedQuad> quadsIn) {
            IModelBuilder<?> modelBuilder =
                    IModelBuilder.of(
                            this.isAmbientOcclusion,
                            this.isSideLit,
                            this.isGui3d,
                            this.transforms,
                            this.overrides,
                            this.particle,
                            renderTypeIn);
            quadsIn.forEach(modelBuilder::addUnculledFace);
            this.children.add(modelBuilder.build());
        }

        private void addCoatedLayer(RenderTypeGroup renderTypeIn, List<BakedQuad> quadsIn) {
            IModelBuilder<?> modelBuilder =
                    IModelBuilder.of(
                            this.isAmbientOcclusion,
                            this.isSideLit,
                            this.isGui3d,
                            this.transforms,
                            this.overrides,
                            this.particle,
                            renderTypeIn);
            quadsIn.forEach(modelBuilder::addUnculledFace);
            this.coatedModel.add(modelBuilder.build());
        }

        private void flushChildrenQuads(RenderTypeGroup renderTypeIn) {
            if (!Objects.equals(renderTypeIn, this.lastRenderType)) {
                if (!this.quads.isEmpty()) {
                    this.addChildrenLayer(this.lastRenderType, this.quads);
                    this.quads.clear();
                }
                this.lastRenderType = renderTypeIn;
            }
        }

        public Builder addQuads(RenderTypeGroup renderTypeIn, List<BakedQuad> quadsIn) {
            this.flushChildrenQuads(renderTypeIn);
            this.quads.addAll(quadsIn);
            return this;
        }

        public Builder addCoatedQuads(RenderTypeGroup renderTypeIn, List<BakedQuad> quadsIn) {
            if (!this.coatedModel.isEmpty())
                Log.error(
                        "Failed to add coating quads for model '"
                                + this.name
                                + "'; Coating quads have already been added!");
            else {
                if (!this.quads.isEmpty()) {
                    this.addChildrenLayer(this.lastRenderType, this.quads);
                    this.quads.clear();
                }
                this.addCoatedLayer(renderTypeIn, quadsIn);
                this.lastRenderType = renderTypeIn;
            }
            return this;
        }

        public BakedModel build() {
            if (this.coatedModel.isEmpty() && !this.quads.isEmpty())
                this.addChildrenLayer(this.lastRenderType, this.quads);

            ImmutableMap.Builder<String, BakedModel> childrenBuilder = ImmutableMap.builder();
            ImmutableList.Builder<BakedModel> itemPassesBuilder = ImmutableList.builder();
            int i = 0;
            for (BakedModel model : this.children) {
                childrenBuilder.put("model_" + (i++), model);
                itemPassesBuilder.add(model);
            }

            ImmutableList<BakedModel> itemPasses = itemPassesBuilder.build();
            ImmutableList<BakedModel> coatedLayerModels = ImmutableList.of();
            if (!this.coatedModel.isEmpty()) {
                ImmutableList.Builder<BakedModel> coatedModelBuilder = ImmutableList.builder();
                coatedModelBuilder.addAll(itemPasses);
                coatedModelBuilder.addAll(this.coatedModel);
                coatedLayerModels = coatedModelBuilder.build();
            }

            return new OilCoatingItemBakedModel(
                    this.isGui3d,
                    this.isSideLit,
                    this.isAmbientOcclusion,
                    this.particle,
                    this.transforms,
                    this.overrides,
                    childrenBuilder.build(),
                    itemPasses,
                    coatedLayerModels);
        }
    }
}
