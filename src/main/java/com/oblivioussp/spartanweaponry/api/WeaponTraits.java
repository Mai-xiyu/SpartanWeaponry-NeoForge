package com.oblivioussp.spartanweaponry.api;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.trait.DamageAbsorbWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.DamageBonusWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.HammerSlamWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.HarvesterWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.KnockbackWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.MeleeBlockWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.NauseaWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.QuickStrikeWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.ReachWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.SpeedModifierWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.SweepWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.ThrowableMeleeWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.ThrowingDamageBonusWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.TwoHandedWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.VersatileWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait.TraitQuality;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTraitWithMagnitude;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WeaponTraits 
{
	public static final ResourceKey<Registry<WeaponTrait>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "weapon_traits"));
	public static final DeferredRegister<WeaponTrait> REGISTRY = DeferredRegister.create(REGISTRY_KEY, ModSpartanWeaponry.ID);
	
	// Weapon Trait Types
	public static final String TYPE_THROWABLE = "throwable",
						TYPE_BLOCK_MELEE = "block_melee",
						TYPE_TWO_HANDED = "two_handed",
						TYPE_DAMAGE_BONUS = "extra_damage",
						TYPE_DAMAGE_BONUS_CHEST = "extra_damage_chest",
						TYPE_DAMAGE_BONUS_HELMET = "extra_damage_helmet",
						TYPE_DAMAGE_BONUS_RIDING = "extra_damage_riding",
						TYPE_DAMAGE_BONUS_THROWN = "extra_damage_thrown",
						TYPE_DAMAGE_BONUS_UNARMOURED = "extra_damage_unarmoured",
						TYPE_DAMAGE_BONUS_UNDEAD = "extra_damage_undead",
						TYPE_DAMAGE_BONUS_BACKSTAB = "extra_damage_backstab",
						TYPE_DAMAGE_ABSORB = "damage_absorb",
						TYPE_REACH = "reach",
						TYPE_SWEEP_DAMAGE = "sweep_damage",
						TYPE_KNOCKBACK = "knockback",
						TYPE_NAUSEA = "nausea",
						TYPE_ARMOUR_PIERCING = "armour_piercing",
						TYPE_SHIELD_BREACH = "shield_breach",
						TYPE_VERSATILE = "versatile",
						TYPE_QUICK_STRIKE = "quick_strike",
						TYPE_FIREPROOF = "fireproof",
						TYPE_LIGHTWEIGHT = "lightweight",
						TYPE_HEAVY = "heavy",
						TYPE_DECAPITATE = "decapitate",
						TYPE_HARVESTER = "harvester",
						TYPE_HAMMER_SLAM = "hammer_slam";
	
	public static final DeferredHolder<WeaponTrait, WeaponTrait> THROWABLE = REGISTRY.register("throwable", () -> new ThrowableMeleeWeaponTrait(TYPE_THROWABLE, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> BLOCK_MELEE = REGISTRY.register("block_melee", () -> new MeleeBlockWeaponTrait(TYPE_BLOCK_MELEE, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> TWO_HANDED_1 = REGISTRY.register("two_handed_1", () -> new TwoHandedWeaponTrait(TYPE_TWO_HANDED, SpartanWeaponryAPI.MOD_ID).setLevel(1).setMagnitude(0.5f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> TWO_HANDED_2 = REGISTRY.register("two_handed_2", () -> new TwoHandedWeaponTrait(TYPE_TWO_HANDED, SpartanWeaponryAPI.MOD_ID).setLevel(2).setMagnitude(0.75f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_CHEST = REGISTRY.register("chest_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_CHEST, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_CHEST).setMagnitude(2.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_HEAD = REGISTRY.register("head_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_HELMET, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_HELMET).setMagnitude(1.5f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_RIDING = REGISTRY.register("riding_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_RIDING, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_RIDING).setMagnitude(2.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_THROWN_1 = REGISTRY.register("thrown_damage_bonus_1", () -> new ThrowingDamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_THROWN, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_DEFAULT).setLevel(1).setMagnitude(2.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_THROWN_2 = REGISTRY.register("thrown_damage_bonus_2", () -> new ThrowingDamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_THROWN, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_DEFAULT).setLevel(2).setMagnitude(3.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_UNARMOURED = REGISTRY.register("unarmoured_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_UNARMOURED, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_UNARMOURED).setMagnitude(3.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_UNDEAD = REGISTRY.register("undead_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_UNDEAD, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_UNDEAD).setMagnitude(1.5f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_BONUS_BACKSTAB = REGISTRY.register("backstab_damage_bonus", () -> new DamageBonusWeaponTrait(TYPE_DAMAGE_BONUS_BACKSTAB, SpartanWeaponryAPI.MOD_ID, DamageBonusWeaponTrait.DAMAGE_BACKSTAB).setMagnitude(3.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DAMAGE_ABSORB = REGISTRY.register("damage_absorb", () -> new DamageAbsorbWeaponTrait(TYPE_DAMAGE_ABSORB, SpartanWeaponryAPI.MOD_ID).setMagnitude(0.25f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> REACH_1 = REGISTRY.register("reach_1", () -> new ReachWeaponTrait(TYPE_REACH, SpartanWeaponryAPI.MOD_ID).setLevel(1).setMagnitude(6.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> REACH_2 = REGISTRY.register("reach_2", () -> new ReachWeaponTrait(TYPE_REACH, SpartanWeaponryAPI.MOD_ID).setLevel(2).setMagnitude(7.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> SWEEP_1 = REGISTRY.register("sweep_1", () -> new SweepWeaponTrait(TYPE_SWEEP_DAMAGE, SpartanWeaponryAPI.MOD_ID).setLevel(1).setMagnitude(0.01f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> SWEEP_2 = REGISTRY.register("sweep_2", () -> new SweepWeaponTrait(TYPE_SWEEP_DAMAGE, SpartanWeaponryAPI.MOD_ID).setLevel(2).setMagnitude(0.5f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> SWEEP_3 = REGISTRY.register("sweep_3", () -> new SweepWeaponTrait(TYPE_SWEEP_DAMAGE, SpartanWeaponryAPI.MOD_ID).setLevel(3).setMagnitude(1.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> KNOCKBACK = REGISTRY.register("extra_knockback", () -> new KnockbackWeaponTrait(TYPE_KNOCKBACK, SpartanWeaponryAPI.MOD_ID));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> NAUSEA = REGISTRY.register("nauseous_blow", () -> new NauseaWeaponTrait(TYPE_NAUSEA, SpartanWeaponryAPI.MOD_ID).setMagnitude(10.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> ARMOUR_PIERCING = REGISTRY.register("armour_piercing", () -> new WeaponTraitWithMagnitude(TYPE_ARMOUR_PIERCING, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE).setMelee().setMagnitude(50.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> SHIELD_BREACH = REGISTRY.register("shield_breach", () -> new WeaponTrait(TYPE_SHIELD_BREACH, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE).setMelee());
	public static final DeferredHolder<WeaponTrait, WeaponTrait> VERSATILE_PICKAXE = REGISTRY.register("versatile_pickaxe", () -> new VersatileWeaponTrait(TYPE_VERSATILE, SpartanWeaponryAPI.MOD_ID, BlockTags.MINEABLE_WITH_PICKAXE, "pickaxe"));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> VERSATILE_AXE = REGISTRY.register("versatile_axe", () -> new VersatileWeaponTrait(TYPE_VERSATILE, SpartanWeaponryAPI.MOD_ID, BlockTags.MINEABLE_WITH_AXE, "axe"));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> VERSATILE_SHOVEL = REGISTRY.register("versatile_shovel", () -> new VersatileWeaponTrait(TYPE_VERSATILE, SpartanWeaponryAPI.MOD_ID, BlockTags.MINEABLE_WITH_SHOVEL, "shovel"));
//	public static final RegistryObject<WeaponTrait> VERSATILE_HOE = REGISTRY.register("versatile_hoe", () -> new VersatileWeaponTrait(TYPE_VERSATILE, SpartanWeaponryAPI.MOD_ID, BlockTags.MINEABLE_WITH_HOE, "hoe").setMelee());
	public static final DeferredHolder<WeaponTrait, WeaponTrait> QUICK_STRIKE = REGISTRY.register("quick_strike", () -> new QuickStrikeWeaponTrait(TYPE_QUICK_STRIKE, SpartanWeaponryAPI.MOD_ID).setMagnitude(14.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> FIREPROOF = REGISTRY.register("fireproof", () -> new WeaponTrait(TYPE_FIREPROOF, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE).setUniversal());
	public static final DeferredHolder<WeaponTrait, WeaponTrait> LIGHTWEIGHT_1 = REGISTRY.register("lightweight_1", () -> new SpeedModifierWeaponTrait(TYPE_LIGHTWEIGHT, WeaponTrait.TraitQuality.POSITIVE).setMagnitude(0.1f).setLevel(1));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> LIGHTWEIGHT_2 = REGISTRY.register("lightweight_2", () -> new SpeedModifierWeaponTrait(TYPE_LIGHTWEIGHT, WeaponTrait.TraitQuality.POSITIVE).setMagnitude(0.2f).setLevel(2));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> LIGHTWEIGHT_3 = REGISTRY.register("lightweight_3", () -> new SpeedModifierWeaponTrait(TYPE_LIGHTWEIGHT, WeaponTrait.TraitQuality.POSITIVE).setMagnitude(0.3f).setLevel(3));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> HEAVY_1 = REGISTRY.register("heavy_1", () -> new SpeedModifierWeaponTrait(TYPE_HEAVY, WeaponTrait.TraitQuality.NEGATIVE).setMagnitude(-0.1f).setLevel(1));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> HEAVY_2 = REGISTRY.register("heavy_2", () -> new SpeedModifierWeaponTrait(TYPE_HEAVY, WeaponTrait.TraitQuality.NEGATIVE).setMagnitude(-0.2f).setLevel(2));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> HEAVY_3 = REGISTRY.register("heavy_3", () -> new SpeedModifierWeaponTrait(TYPE_HEAVY, WeaponTrait.TraitQuality.NEGATIVE).setMagnitude(-0.3f).setLevel(3));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> DECAPITATE = REGISTRY.register("decapitate", () -> new WeaponTraitWithMagnitude(TYPE_DECAPITATE, SpartanWeaponryAPI.MOD_ID, TraitQuality.POSITIVE).setMelee().setMagnitude(25.0f));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> HARVESTER = REGISTRY.register("harvester", () -> new HarvesterWeaponTrait(TYPE_HARVESTER, SpartanWeaponryAPI.MOD_ID));
	public static final DeferredHolder<WeaponTrait, WeaponTrait> HAMMER_SLAM = REGISTRY.register("hammer_slam", () -> new HammerSlamWeaponTrait(TYPE_HAMMER_SLAM, SpartanWeaponryAPI.MOD_ID));
}
