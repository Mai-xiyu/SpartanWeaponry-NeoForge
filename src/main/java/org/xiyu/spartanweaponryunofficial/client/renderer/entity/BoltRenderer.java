package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;

// TODO: Custom bolt rendering with vertex() was removed in MC 26.1.
// The old render() method used immediate-mode vertex submission which is no longer available.
// Currently uses default ArrowRenderer rendering. Needs reimplementation with the new SubmitNodeCollector system.
public class BoltRenderer<T extends BoltEntity> extends ArrowRenderer<T, SWArrowRenderState> {
    public BoltRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    public @NotNull SWArrowRenderState createRenderState() {
        return new SWArrowRenderState();
    }

    @Override
    public void extractRenderState(T entity, SWArrowRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.texture = entity.getTexture();
    }

    @Override
    protected @NotNull Identifier getTextureLocation(SWArrowRenderState state) {
        return state.texture;
    }
}