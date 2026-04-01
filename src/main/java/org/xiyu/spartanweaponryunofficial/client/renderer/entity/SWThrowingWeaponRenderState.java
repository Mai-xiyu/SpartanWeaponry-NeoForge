package org.xiyu.spartanweaponryunofficial.client.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SWThrowingWeaponRenderState extends EntityRenderState {
    public float yRot;
    public float xRot;
    public float yRotO;
    public float xRotO;
    public final ItemStackRenderState weaponItem = new ItemStackRenderState();
    public boolean hasWeapon;
    // Boomerang-specific fields
    public int ticksInAir;
    public boolean isUnderWater;
    public float partialTick;
}