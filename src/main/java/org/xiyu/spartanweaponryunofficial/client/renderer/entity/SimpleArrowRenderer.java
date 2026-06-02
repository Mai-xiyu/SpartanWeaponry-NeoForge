package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowEntitySW;

public class SimpleArrowRenderer<T extends ArrowEntitySW> extends ArrowRenderer<T> {
    public final ResourceLocation texture;

    public SimpleArrowRenderer(
            EntityRendererProvider.Context rendererProvider, ResourceLocation textureLocation) {
        super(rendererProvider);
        this.texture = textureLocation;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return this.texture;
    }
}
