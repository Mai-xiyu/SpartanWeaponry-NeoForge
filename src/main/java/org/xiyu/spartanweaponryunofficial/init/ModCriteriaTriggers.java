package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.advancement.criterion.BrewOilTrigger;

import java.util.function.Supplier;

public class ModCriteriaTriggers {
    // NeoForge 1.21: Must use DeferredRegister for trigger types
    public static final DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(Registries.TRIGGER_TYPE, ModSpartanWeaponry.ID);

    public static final Supplier<BrewOilTrigger> BREW_OIL = REGISTRY.register("brew_oil", BrewOilTrigger::new);
}
