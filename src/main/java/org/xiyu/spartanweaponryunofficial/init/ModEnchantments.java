package org.xiyu.spartanweaponryunofficial.init;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
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

	public static java.util.Optional<Holder.Reference<Enchantment>> getHolder(RegistryAccess access, ResourceKey<Enchantment> key)
	{
		if(access == null)
			return java.util.Optional.empty();
		var registry = access.lookup(Registries.ENCHANTMENT);
		if(registry.isEmpty())
			return java.util.Optional.empty();
		return registry.get().get(key);
	}

	public static int getLevel(RegistryAccess access, ResourceKey<Enchantment> key, ItemStack stack)
	{
		var holder = getHolder(access, key);
		return holder.map(h -> EnchantmentHelper.getItemEnchantmentLevel(h, stack)).orElse(0);
	}

	public static int getLevel(RegistryAccess access, ResourceKey<Enchantment> key, LivingEntity living)
	{
		var holder = getHolder(access, key);
		return holder.map(h -> EnchantmentHelper.getEnchantmentLevel(h, living)).orElse(0);
	}
}
