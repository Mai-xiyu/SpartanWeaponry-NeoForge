package org.xiyu.spartanweaponryunofficial.init;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds
{
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, ModSpartanWeaponry.ID);
	
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWN_WEAPON_THROW = REGISTRY.register("throwing_weapon_throw", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_weapon_throw")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWN_WEAPON_HIT_MOB = REGISTRY.register("throwing_weapon_hit_mob", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_weapon_hit_mob")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWN_WEAPON_HIT_GROUND = REGISTRY.register("dagger_hit_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_weapon_hit_ground")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWING_KNIFE_THROW = REGISTRY.register("throwing_knife_throw", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_knife_throw")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWING_KNIFE_HIT_MOB = REGISTRY.register("throwing_knife_hit_mob", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_knife_hit_mob")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWING_KNIFE_HIT_GROUND = REGISTRY.register("throwing_knife_hit_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_knife_hit_ground")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TOMAHAWK_THROW = REGISTRY.register("tomahawk_throw", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "tomahawk_throw")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TOMAHAWK_HIT_MOB = REGISTRY.register("tomahawk_hit_mob", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "tomahawk_hit_mob")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TOMAHAWK_HIT_GROUND = REGISTRY.register("tomahawk_hit_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "tomahawk_hit_ground")));
	public static final DeferredHolder<SoundEvent, SoundEvent> JAVELIN_THROW = REGISTRY.register("javelin_throw", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "javelin_throw")));
	public static final DeferredHolder<SoundEvent, SoundEvent> JAVELIN_HIT_MOB = REGISTRY.register("javelin_hit_mob", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "javelin_hit_mob")));
	public static final DeferredHolder<SoundEvent, SoundEvent> JAVELIN_HIT_GROUND = REGISTRY.register("javelin_hit_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "javelin_hit_ground")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BOOMERANG_THROW = REGISTRY.register("boomerang_throw", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "boomerang_throw")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BOOMERANG_FLY = REGISTRY.register("boomerang_fly", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "boomerang_fly")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BOOMERANG_HIT_MOB = REGISTRY.register("boomerang_hit_mob", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "boomerang_hit_mob")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BOOMERANG_BOUNCE = REGISTRY.register("boomerang_bounce", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "boomerang_bounce")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BOOMERANG_HIT_GROUND = REGISTRY.register("boomerang_hit_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "boomerang_hit_ground")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THROWING_WEAPON_LOYALTY_RETURN = REGISTRY.register("throwing_weapon_loyalty_return", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "throwing_weapon_loyalty_return")));

	public static final DeferredHolder<SoundEvent, SoundEvent> OIL_APPLIED = REGISTRY.register("oil_applied", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "oil_applied")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HAMMER_SLAMS_INTO_GROUND = REGISTRY.register("hammer_slams_into_ground", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "hammer_slams_into_ground")));

}
