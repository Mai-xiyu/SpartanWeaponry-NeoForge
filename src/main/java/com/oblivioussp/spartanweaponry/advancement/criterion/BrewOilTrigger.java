package com.oblivioussp.spartanweaponry.advancement.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.OilEffects;
import com.oblivioussp.spartanweaponry.api.oil.OilEffect;
import com.oblivioussp.spartanweaponry.init.ModCriteriaTriggers;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class BrewOilTrigger extends SimpleCriterionTrigger<BrewOilTrigger.TriggerInstance> 
{
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "brew_oil");

	@Override
	public Codec<BrewOilTrigger.TriggerInstance> codec()
	{
		return BrewOilTrigger.TriggerInstance.CODEC;
	}
	
	public void trigger(ServerPlayer playerIn, OilEffect oilEffectIn)
	{
		trigger(playerIn, (triggerInstance) -> triggerInstance.matches(oilEffectIn));
	}
	
	public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceLocation> oilEffect)
			implements SimpleCriterionTrigger.SimpleInstance
	{
		public static final Codec<BrewOilTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
							EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(BrewOilTrigger.TriggerInstance::player),
							ResourceLocation.CODEC.optionalFieldOf("oil_effect").forGetter(BrewOilTrigger.TriggerInstance::oilEffect)
					).apply(instance, BrewOilTrigger.TriggerInstance::new)
		);

		public static Criterion<BrewOilTrigger.TriggerInstance> brewedOil()
		{
			return ModCriteriaTriggers.BREW_OIL.createCriterion(new BrewOilTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
		}

		// TODO: Possibly make a advancement for brewing all the oils
		public boolean matches(OilEffect oilEffectIn)
		{
			if(oilEffect.isEmpty())
				return true;
			RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
			Registry<OilEffect> registry = registryAccess.registry(OilEffects.REGISTRY_KEY).orElse(null);
			ResourceLocation key = registry != null ? registry.getKey(oilEffectIn) : null;
			return key != null && key.equals(oilEffect.get());
		}
	}
}
