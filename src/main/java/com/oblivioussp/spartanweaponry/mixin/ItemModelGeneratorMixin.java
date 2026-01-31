package com.oblivioussp.spartanweaponry.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Either;
import com.oblivioussp.spartanweaponry.client.model.OilCoatedItemModel;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.neoforged.neoforge.client.model.ItemLayerModel;

/**
 * The entire purpose for this Mixin is to fix a bug in how Forge loads its custom loaded models which causes any non-standard named textures to be filtered out.<br>
 * It's likely a bug or oversight because it also removes any textures named "layer5" and beyond, which Forge's {@link ItemLayerModel} removes said limitations.<br>
 * This is only intended to fix the coating layer working with Weapon Oils so nothing else will work with this.
 * @author ObliviousSpartan
 *
 */
@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin 
{
	@Inject(at = @At(value = "HEAD"), method = "generateBlockModel(Ljava/util/function/Function;Lnet/minecraft/client/renderer/block/model/BlockModel;)Lnet/minecraft/client/renderer/block/model/BlockModel;", cancellable = true)
	public void generateBlockModel(Function<Material, TextureAtlasSprite> spriteGetter, BlockModel baseModel, CallbackInfoReturnable<BlockModel> callback)
	{
//		Log.debug("Intercepted generateBlockModel(...) method!");
		if(baseModel.customData.hasCustomGeometry() && baseModel.customData.getCustomGeometry().getClass() == OilCoatedItemModel.class && baseModel.hasTexture("coating"))
		{
//			Log.debug("Intercepted compatible model: " + baseModel.name);
			List<BlockElement> blockElements = new ArrayList<>();
			
			for(int i = 0; i < ItemModelGenerator.LAYERS.size(); i++)
			{
				String value = ItemModelGenerator.LAYERS.get(i);
				if(baseModel.hasTexture(value))
					break;
				Material material = baseModel.getMaterial(value);
				SpriteContents sprite = spriteGetter.apply(material).contents();
				if(!value.equals("coating"))
				{
					blockElements.addAll(processFrames(i, value, sprite));
				}
			}
			
			Map<String, Either<Material, String>> textureMap = new HashMap<>(baseModel.textureMap);
			textureMap.put("particle", baseModel.hasTexture("particle") ? Either.left(baseModel.getMaterial("particle")) : textureMap.get("layer0"));
			BlockModel resultModel = new BlockModel(null, blockElements, textureMap, false, baseModel.getGuiLight(), baseModel.getTransforms(), baseModel.getOverrides());
			resultModel.customData.copyFrom(baseModel.customData);
			resultModel.customData.setGui3d(false);
//			Log.debug("Verifying coating texture for model: " + baseModel.name + " - " + (textureMap.containsKey("coating") ? "Success!" : "Failed..."));
			callback.setReturnValue(resultModel);
		}
	}
	
	@Shadow
	public List<BlockElement> processFrames(int index, String name, SpriteContents sprite)
	{
		throw new IllegalStateException("Mixin failed to shadow the \"ItemModelGenerator.processFrames(...)\" method!");
	}
}