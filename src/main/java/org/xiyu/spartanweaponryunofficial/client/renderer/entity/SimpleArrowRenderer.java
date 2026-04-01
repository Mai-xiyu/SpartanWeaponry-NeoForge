package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowEntitySW;

public class SimpleArrowRenderer<T extends ArrowEntitySW> extends ArrowRenderer<T, ArrowRenderState> {
    public final Identifier texture;

    public SimpleArrowRenderer(EntityRendererProvider.Context rendererProvider, Identifier textureLocation) {
        super(rendererProvider);
        this.texture = textureLocation;
    }

    @Override
    public @NotNull ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected @NotNull Identifier getTextureLocation(@NotNull ArrowRenderState state) {
        return this.texture;
    }
}