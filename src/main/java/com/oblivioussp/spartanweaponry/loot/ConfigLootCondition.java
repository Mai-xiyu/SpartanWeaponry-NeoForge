package com.oblivioussp.spartanweaponry.loot;

import com.mojang.serialization.MapCodec;
import com.oblivioussp.spartanweaponry.init.ModLootModifiers;
import com.oblivioussp.spartanweaponry.util.Config;

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
		return ModLootModifiers.CONFIG_ENABLED;
	}
	
	public static LootItemCondition.Builder builder()
	{
		return () -> INSTANCE;
	}

}
