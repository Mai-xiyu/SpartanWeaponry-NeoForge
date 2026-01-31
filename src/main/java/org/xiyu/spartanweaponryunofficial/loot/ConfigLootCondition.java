package org.xiyu.spartanweaponryunofficial.loot;

import com.mojang.serialization.MapCodec;
import org.xiyu.spartanweaponryunofficial.init.ModLootModifiers;
import org.xiyu.spartanweaponryunofficial.util.Config;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ConfigLootCondition implements LootItemCondition 
{
	public static final ConfigLootCondition INSTANCE = new ConfigLootCondition();
	public static final MapCodec<ConfigLootCondition> CODEC = MapCodec.unit(INSTANCE);
	
	protected ConfigLootCondition() {}
	
	@Override
	public boolean test(LootContext t) 
	{
		return !Config.INSTANCE.disableNewHeadDrops.get();
	}

	@Override
	public LootItemConditionType getType()
	{
		// NeoForge 1.21: CONFIG_ENABLED is now a Supplier
		return ModLootModifiers.CONFIG_ENABLED.get();
	}
	
	public static LootItemCondition.Builder builder()
	{
		return () -> INSTANCE;
	}

}
