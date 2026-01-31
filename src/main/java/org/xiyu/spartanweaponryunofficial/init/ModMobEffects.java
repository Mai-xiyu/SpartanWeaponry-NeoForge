package org.xiyu.spartanweaponryunofficial.init;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.effect.BasicMobEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects 
{
	public static DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ModSpartanWeaponry.ID);
	public static DeferredHolder<MobEffect, MobEffect> ENDER_DISRPUTION = REGISTRY.register("ender_disruption", () -> new BasicMobEffect(MobEffectCategory.HARMFUL, 0x408080));
}
