package org.xiyu.spartanweaponryunofficial.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.init.ModCriteriaTriggers;

import java.util.Optional;

public class BrewOilTrigger extends SimpleCriterionTrigger<BrewOilTrigger.TriggerInstance> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "brew_oil");

    @Override
    public @NotNull Codec<BrewOilTrigger.TriggerInstance> codec() {
        return BrewOilTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer playerIn, OilEffect oilEffectIn) {
        this.trigger(playerIn, (triggerInstance) -> triggerInstance.matches(oilEffectIn));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Identifier> oilEffect)
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<BrewOilTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(BrewOilTrigger.TriggerInstance::player),
                        Identifier.CODEC.optionalFieldOf("oil_effect").forGetter(BrewOilTrigger.TriggerInstance::oilEffect)
                ).apply(instance, BrewOilTrigger.TriggerInstance::new)
        );

        public static Criterion<BrewOilTrigger.TriggerInstance> brewedOil() {
            // NeoForge 1.21: BREW_OIL is now a Supplier
            return ModCriteriaTriggers.BREW_OIL.get().createCriterion(new BrewOilTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
        }

        // TODO: Possibly make a advancement for brewing all the oils
        public boolean matches(OilEffect oilEffectIn) {
            if (this.oilEffect.isEmpty())
                return true;
            RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
            Registry<OilEffect> registry = registryAccess.lookup(OilEffects.REGISTRY_KEY).orElse(null);
            Identifier key = registry != null ? registry.getKey(oilEffectIn) : null;
            return key != null && key.equals(this.oilEffect.get());
        }
    }
}
