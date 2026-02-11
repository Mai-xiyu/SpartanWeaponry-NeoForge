package org.xiyu.spartanweaponryunofficial.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Projectile.class)
public class ProjectileMixin extends EntityMixin {
    @Shadow
    public Entity getOwner() {
        throw new IllegalStateException("Mixin failed to shadow the \"ProjectileMixin.getOwner()\" method!");
    }
}
