package org.xiyu.spartanweaponryunofficial.mixin;

import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.xiyu.spartanweaponryunofficial.client.model.OilCoatedItemModel;

/**
 * Keeps the non-standard "coating" texture available for the weapon oil custom model loader while
 * preserving vanilla's generated item layer handling for "layer0" through "layer4". TODO:
 * Client-only compatibility risk; keep this scoped to oil-coated custom geometry unless NeoForge
 * exposes a texture-preserving model generation extension point.
 *
 * @author ObliviousSpartan
 */
@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    @Inject(
            at = @At(value = "HEAD"),
            method =
                    "generateBlockModel(Ljava/util/function/Function;Lnet/minecraft/client/renderer/block/model/BlockModel;)Lnet/minecraft/client/renderer/block/model/BlockModel;",
            cancellable = true)
    public void generateBlockModel(
            Function<Material, TextureAtlasSprite> spriteGetter,
            BlockModel baseModel,
            CallbackInfoReturnable<BlockModel> callback) {
        //        Log.debug("Intercepted generateBlockModel(...) method!");
        if (baseModel.customData.hasCustomGeometry()
                && baseModel.customData.getCustomGeometry().getClass() == OilCoatedItemModel.class
                && baseModel.hasTexture("coating")) {
            //            Log.debug("Intercepted compatible model: " + baseModel.name);
            List<BlockElement> blockElements = new ArrayList<>();

            for (int i = 0; i < ItemModelGenerator.LAYERS.size(); i++) {
                String value = ItemModelGenerator.LAYERS.get(i);
                if (!baseModel.hasTexture(value)) break;
                Material material = baseModel.getMaterial(value);
                TextureAtlasSprite sprite = spriteGetter.apply(material);
                blockElements.addAll(
                        ClientHooks.fixItemModelSeams(
                                this.processFrames(i, value, sprite.contents()), sprite));
            }

            Map<String, Either<Material, String>> textureMap = new HashMap<>(baseModel.textureMap);
            textureMap.put(
                    "particle",
                    baseModel.hasTexture("particle")
                            ? Either.left(baseModel.getMaterial("particle"))
                            : textureMap.get("layer0"));
            BlockModel resultModel =
                    new BlockModel(
                            null,
                            blockElements,
                            textureMap,
                            false,
                            baseModel.getGuiLight(),
                            baseModel.getTransforms(),
                            baseModel.getOverrides());
            resultModel.customData.copyFrom(baseModel.customData);
            resultModel.customData.setGui3d(false);
            //            Log.debug("Verifying coating texture for model: " + baseModel.name + " - "
            // +
            // (textureMap.containsKey("coating") ? "Success!" : "Failed..."));
            callback.setReturnValue(resultModel);
        }
    }

    @Shadow
    public List<BlockElement> processFrames(int index, String name, SpriteContents sprite) {
        throw new IllegalStateException(
                "Mixin failed to shadow the \"ItemModelGenerator.processFrames(...)\" method!");
    }
}
