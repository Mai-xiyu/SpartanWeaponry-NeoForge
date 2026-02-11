package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IHudLoadState {
    boolean isLoaded(ItemStack stack);

    float getLoadProgress(ItemStack stack, LivingEntity entity);
}
