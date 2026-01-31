package org.xiyu.spartanweaponryunofficial.init;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class ModDamageTypes 
{
	
/*	public static final DeferredRegister<DamageType> REGISTRY = DeferredRegister.create(Registries.DAMAGE_TYPE, ModSpartanWeaponry.ID);
	
	public static final RegistryObject<DamageType> THROWN_WEAPON_PLAYER = REGISTRY.register("thrown_weapon_player", () -> new DamageType("player", 0.1f));
	public static final RegistryObject<DamageType> THROWN_WEAPON_MOB = REGISTRY.register("thrown_weapon_mob", () -> new DamageType("mob", 0.1f));
	public static final RegistryObject<DamageType> ARMOR_PIERCING_MELEE = REGISTRY.register("armor_piercing_melee", () -> new DamageType("player", 0.1f));
	public static final RegistryObject<DamageType> ARMOR_PIERCING_BOLT = REGISTRY.register("armor_piercing_bolt", () -> new DamageType("arrow", 0.1f));*/
	
	public static final ResourceKey<DamageType> KEY_THROWN_WEAPON_PLAYER = createKey("thrown_weapon_player");
	public static final ResourceKey<DamageType> KEY_THROWN_WEAPON_MOB = createKey("thrown_weapon_mob");
	public static final ResourceKey<DamageType> KEY_ARMOR_PIERCING_MELEE = createKey("armor_piercing_melee");
	public static final ResourceKey<DamageType> KEY_ARMOR_PIERCING_BOLT = createKey("armor_piercing_bolt");
	
	public static DamageSource armorPiercingMelee(Entity source)
	{
		return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KEY_ARMOR_PIERCING_MELEE), source);
	}
	
	public static DamageSource armorPiercingProjectile(Entity source, Entity indirect)
	{
		return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KEY_ARMOR_PIERCING_BOLT), source, indirect);
	}
	
	public static DamageSource thrownWeaponPlayer(Entity source, Entity thrownWeapon)
	{
		return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KEY_THROWN_WEAPON_PLAYER), source, thrownWeapon);
	}
	
	public static DamageSource thrownWeaponMob(Entity source, Entity thrownWeapon)
	{
		return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KEY_THROWN_WEAPON_MOB), source, thrownWeapon);
	}
	
	private static ResourceKey<DamageType> createKey(String keyName)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.tryBuild(ModSpartanWeaponry.ID, keyName));
	}
}