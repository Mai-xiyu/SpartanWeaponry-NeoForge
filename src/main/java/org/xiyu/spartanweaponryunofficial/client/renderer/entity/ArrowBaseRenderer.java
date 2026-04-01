package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowBaseEntity;

public class ArrowBaseRenderer<T extends ArrowBaseEntity> extends ArrowRenderer<T, SWArrowRenderState> {
    public ArrowBaseRenderer(EntityRendererProvider.Context rendererProvider) {
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