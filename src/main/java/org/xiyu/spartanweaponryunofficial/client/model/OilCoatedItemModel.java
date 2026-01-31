package org.xiyu.spartanweaponryunofficial.client.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import org.xiyu.spartanweaponryunofficial.util.Log;

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
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.ItemLayerModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;

/** Copy of Forge's {@linkplain ItemLayerModel} with the addition of a coating layer for use with items that can be oiled
 * 
 * @author ObliviousSpartan
 *
 */
public class OilCoatedItemModel implements IUnbakedGeometry<OilCoatedItemModel>
{
	protected ImmutableList<Material> textures;
	protected Material coatingTexture;
	protected final Int2ObjectMap<ExtraFaceData> faceData;
	protected final Int2ObjectMap<ResourceLocation> renderTypeNames;

	public OilCoatedItemModel(@Nullable ImmutableList<Material> texturesIn, @Nullable Material coatingTextureIn, Int2ObjectMap<ExtraFaceData> faceDataIn,
			Int2ObjectMap<ResourceLocation> renderTypeNamesIn) 
	{
		textures = texturesIn;
		coatingTexture = coatingTextureIn;
		faceData = faceDataIn;
		renderTypeNames = renderTypeNamesIn;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
			Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) 
	{
		if(textures == null)
		{
			ImmutableList.Builder<Material> layerTextureBuilder = ImmutableList.builder();
			for(int i = 0; context.hasMaterial("layer" + i); i++)
				layerTextureBuilder.add(context.getMaterial("layer" + i));
	
			if(context.hasMaterial("coating"))
			{
				coatingTexture = context.getMaterial("coating");
			}
			textures = layerTextureBuilder.build();
		}
		
		if(textures.size() <= 0)
			throw new IllegalStateException("Couldn't resolve Textures for model: " + context.getModelName());
		// Coating texture is optional - only warn in debug mode, not log an error
		// if(coatingTexture == null)
		// 	Log.warn("Couldn't resolve Coating textures for model: " + context.getModelName());
		
		TextureAtlasSprite particleSprite = spriteGetter.apply(context.hasMaterial("particle") ? context.getMaterial("particle") : textures.get(0));
		
		// Apply root transformation to the model state if not default
		Transformation transform = context.getRootTransform();
		if(!transform.isIdentity())
			modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, transform);
		
		RenderTypeGroup normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), RenderType.translucent());
		OilCoatingItemBakedModel.Builder builder = OilCoatingItemBakedModel.makeBuilder(context, particleSprite, overrides, context.getTransforms());
		
		for(int i = 0; i < textures.size(); i++)
		{
			TextureAtlasSprite sprite = spriteGetter.apply(textures.get(i));
			List<BlockElement> unbakedElements = UnbakedGeometryHelper.createUnbakedItemElements(i, sprite, faceData.get(i));
			List<BakedQuad> bakedQuads = UnbakedGeometryHelper.bakeElements(unbakedElements, mat -> sprite, modelState);
			ResourceLocation renderTypeName = renderTypeNames.get(i);
			RenderTypeGroup renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : normalRenderTypes;
			builder.addQuads(renderTypes, bakedQuads);
		}
		
		// Bake the coating quads
		if(coatingTexture != null)
		{
			final int coatingLayer = 100;
			TextureAtlasSprite sprite = spriteGetter.apply(coatingTexture);
			List<BlockElement> unbakedElements = UnbakedGeometryHelper.createUnbakedItemElements(coatingLayer, sprite, faceData.get(coatingLayer));
			List<BakedQuad> bakedQuads = UnbakedGeometryHelper.bakeElements(unbakedElements, mat -> sprite, modelState);
			ResourceLocation renderTypeName = renderTypeNames.get(coatingLayer);
			RenderTypeGroup renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : normalRenderTypes;
			builder.addCoatedQuads(renderTypes, bakedQuads);
		}
		
		return builder.build();
	}
	
	public static class Loader implements IGeometryLoader<OilCoatedItemModel>
	{
		public static final Loader INSTANCE = new Loader();

		@Override
		public OilCoatedItemModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
				throws JsonParseException 
		{
			Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames = new Int2ObjectOpenHashMap<ResourceLocation>();
			if(jsonObject.has("render_types"))
			{
				JsonObject renderTypes = jsonObject.getAsJsonObject("render_types");
				for(Map.Entry<String, JsonElement> entry : renderTypes.entrySet())
				{
					ResourceLocation renderType = ResourceLocation.parse(entry.getKey());
					for(JsonElement layer : entry.getValue().getAsJsonArray())
					{
						if(renderTypeNames.put(layer.getAsInt(), renderType) != null)
							throw new JsonParseException("Duplicate render type for layer " + layer);
					}
				}
			}
			
			Int2ObjectArrayMap<ExtraFaceData> emissiveLayers = new Int2ObjectArrayMap<>();
			if(jsonObject.has("forge_data"))
			{
				JsonObject jsonForgeData = jsonObject.getAsJsonObject("forge_data");
				
				if(jsonForgeData.has("layers"))
				{
					JsonObject jsonEmissiveLayers = jsonForgeData.getAsJsonObject("layers");
					for(Map.Entry<String, JsonElement> entry : jsonEmissiveLayers.entrySet())
					{
						int layer = Integer.parseInt(entry.getKey());
						ExtraFaceData faceData = ExtraFaceData.read(entry.getValue(), ExtraFaceData.DEFAULT);
						emissiveLayers.put(layer, faceData);
					}
				}
			}
			
			return new OilCoatedItemModel(null, null, emissiveLayers, renderTypeNames);
		}
	}
}
