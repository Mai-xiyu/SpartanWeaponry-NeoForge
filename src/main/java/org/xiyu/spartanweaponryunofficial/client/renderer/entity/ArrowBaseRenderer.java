package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowBaseEntity;

public class ArrowBaseRenderer<T extends ArrowBaseEntity> extends ArrowRenderer<T> {
    public ArrowBaseRenderer(EntityRendererProvider.Context rendererProvider) {
        super(rendererProvider);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(T entity) {
        return entity.getTexture();
    }

}
