package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.particle.DamageModifiedParticle;

@EventBusSubscriber(
        modid = ModSpartanWeaponry.ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY =
            DeferredRegister.create(Registries.PARTICLE_TYPE, ModSpartanWeaponry.ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DAMAGE_BOOSTED =
            REGISTRY.register("damage_boosted", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DAMAGE_REDUCED =
            REGISTRY.register("damage_reduced", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OIL_DAMAGE_BOOSTED =
            REGISTRY.register("oil_damage_boosted", () -> new SimpleParticleType(false));

    // TODO: Also consider adding boomerang trail particle and possibly for other throwing weapons
    // too

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent ev) {
        ev.registerSpriteSet(
                DAMAGE_BOOSTED.get(), DamageModifiedParticle.DamageBoostedProvider::new);
        ev.registerSpriteSet(
                DAMAGE_REDUCED.get(), DamageModifiedParticle.DamageReducedProvider::new);
        ev.registerSpriteSet(
                OIL_DAMAGE_BOOSTED.get(), DamageModifiedParticle.OilDamageBoostedProvider::new);
    }
}
