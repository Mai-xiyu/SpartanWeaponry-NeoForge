package org.xiyu.spartanweaponryunofficial.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.util.Config;

public class ConfigLootCondition implements LootItemCondition {
    public static final ConfigLootCondition INSTANCE = new ConfigLootCondition();
    public static final MapCodec<ConfigLootCondition> CODEC = MapCodec.unit(INSTANCE);

    protected ConfigLootCondition() {
    }

    @Override
    public boolean test(LootContext t) {
        return !Config.INSTANCE.disableNewHeadDrops.get();
    }

    @Override
    public @NotNull MapCodec<ConfigLootCondition> codec() {
        return CODEC;
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }

}