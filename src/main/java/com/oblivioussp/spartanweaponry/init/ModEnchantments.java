package com.oblivioussp.spartanweaponry.init;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ModEnchantments
{
	public static final ResourceKey<Enchantment> PROPEL = createKey("propel");
	public static final ResourceKey<Enchantment> RAZORS_EDGE = createKey("razors_edge");
	public static final ResourceKey<Enchantment> INCENDIARY = createKey("incendiary");
	public static final ResourceKey<Enchantment> LUCKY_THROW = createKey("lucky_throw");
	public static final ResourceKey<Enchantment> HYDRODYNAMIC = createKey("hydrodynamic");
	public static final ResourceKey<Enchantment> SUPERCHARGE = createKey("supercharge");
	public static final ResourceKey<Enchantment> EXPANSE = createKey("expanse");
	public static final ResourceKey<Enchantment> SHARPSHOOTER = createKey("sharpshooter");
	public static final ResourceKey<Enchantment> COLLECTORANG = createKey("collectorang");

	private static ResourceKey<Enchantment> createKey(String path)
	{
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.tryBuild(ModSpartanWeaponry.ID, path));
	}

	public static Holder<Enchantment> getHolder(RegistryAccess access, ResourceKey<Enchantment> key)
	{
		return access.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key);
	}

	public static int getLevel(RegistryAccess access, ResourceKey<Enchantment> key, ItemStack stack)
	{
		return EnchantmentHelper.getItemEnchantmentLevel(getHolder(access, key), stack);
	}

	public static int getLevel(RegistryAccess access, ResourceKey<Enchantment> key, LivingEntity living)
	{
		return EnchantmentHelper.getEnchantmentLevel(getHolder(access, key), living);
	}
}
