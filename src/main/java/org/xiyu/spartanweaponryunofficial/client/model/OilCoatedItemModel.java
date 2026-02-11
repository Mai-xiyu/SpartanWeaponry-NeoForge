package org.xiyu.spartanweaponryunofficial.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Copy of Forge's {@linkplain ItemLayerModel} with the addition of a coating layer for use with items that can be oiled
 * Simplified for Forge 1.21.1 - ExtraFaceData removed
 *
 * @author ObliviousSpartan
 */
public class OilCoatedItemModel implements IUnbakedGeometry<OilCoatedItemModel> {
    protected ImmutableList<Material> textures;
    protected Material coatingTexture;
    protected final Int2ObjectMap<ResourceLocation> renderTypeNames;

    public OilCoatedItemModel(@Nullable ImmutableList<Material> texturesIn, @Nullable Material coatingTextureIn,
                              Int2ObjectMap<ResourceLocation> renderTypeNamesIn) {
        this.textures = texturesIn;
        this.coatingTexture = coatingTextureIn;
        this.renderTypeNames = renderTypeNamesIn;
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker,
                                    @NotNull Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ModelState modelState, @NotNull ItemOverrides overrides) {
        if (this.textures == null) {
            ImmutableList.Builder<Material> layerTextureBuilder = ImmutableList.builder();
            for (int i = 0; context.hasMaterial("layer" + i); i++)
                layerTextureBuilder.add(context.getMaterial("layer" + i));

            if (context.hasMaterial("coating")) {
                this.coatingTexture = context.getMaterial("coating");
            }
            this.textures = layerTextureBuilder.build();
        }

        if (this.textures.isEmpty())
            throw new IllegalStateException("Couldn't resolve Textures for model: " + context.getModelName());
        // Coating texture is optional - only warn in debug mode, not log an error
        // if(coatingTexture == null)
        // 	Log.warn("Couldn't resolve Coating textures for model: " + context.getModelName());

        TextureAtlasSprite particleSprite = spriteGetter.apply(context.hasMaterial("particle") ? context.getMaterial("particle") : this.textures.getFirst());

        // Apply root transformation to the model state if not default
        Transformation transform = context.getRootTransform();
        if (!transform.isIdentity())
            modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, transform);

        RenderTypeGroup normalRenderTypes = new RenderTypeGroup(RenderType.cutout(), RenderType.cutout());
        RenderTypeGroup coatingRenderTypes = new RenderTypeGroup(RenderType.translucent(), RenderType.translucent());
        OilCoatingItemBakedModel.Builder builder = OilCoatingItemBakedModel.makeBuilder(context, particleSprite, overrides, context.getTransforms());

        for (int i = 0; i < this.textures.size(); i++) {
            TextureAtlasSprite sprite = spriteGetter.apply(this.textures.get(i));
            List<BlockElement> unbakedElements = UnbakedGeometryHelper.createUnbakedItemElements(i, sprite.contents());
            List<BakedQuad> bakedQuads = UnbakedGeometryHelper.bakeElements(unbakedElements, mat -> sprite, modelState);
            ResourceLocation renderTypeName = this.renderTypeNames.get(i);
            RenderTypeGroup renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : normalRenderTypes;
            builder.addQuads(renderTypes, bakedQuads);
        }

        // Bake the coating quads
        if (this.coatingTexture != null) {
            final int coatingLayer = 100;
            TextureAtlasSprite sprite = spriteGetter.apply(this.coatingTexture);
            List<BlockElement> unbakedElements = UnbakedGeometryHelper.createUnbakedItemElements(coatingLayer, sprite.contents());
            List<BakedQuad> bakedQuads = UnbakedGeometryHelper.bakeElements(unbakedElements, mat -> sprite, modelState);
            ResourceLocation renderTypeName = this.renderTypeNames.get(coatingLayer);
            RenderTypeGroup renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : coatingRenderTypes;
            builder.addCoatedQuads(renderTypes, bakedQuads);
        }

        return builder.build();
    }

    public static class Loader implements IGeometryLoader<OilCoatedItemModel> {
        public static final Loader INSTANCE = new Loader();

        @Override
        public @NotNull OilCoatedItemModel read(JsonObject jsonObject, @NotNull JsonDeserializationContext deserializationContext)
                throws JsonParseException {
            Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames = new Int2ObjectOpenHashMap<>();
            if (jsonObject.has("render_types")) {
                JsonObject renderTypes = jsonObject.getAsJsonObject("render_types");
                for (Map.Entry<String, JsonElement> entry : renderTypes.entrySet()) {
                    ResourceLocation renderType = ResourceLocation.parse(entry.getKey());
                    for (JsonElement layer : entry.getValue().getAsJsonArray()) {
                        if (renderTypeNames.put(layer.getAsInt(), renderType) != null)
                            throw new JsonParseException("Duplicate render type for layer " + layer);
                    }
                }
            }

            // Simplified for Forge 1.21.1 - ExtraFaceData/emissive layers removed
            return new OilCoatedItemModel(null, null, renderTypeNames);
        }
    }
}
