package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.particle.DamageModifiedParticle;

@Mod.EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, ModSpartanWeaponry.ID);

    public static final RegistryObject<SimpleParticleType> DAMAGE_BOOSTED = REGISTRY.register("damage_boosted", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> DAMAGE_REDUCED = REGISTRY.register("damage_reduced", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> OIL_DAMAGE_BOOSTED = REGISTRY.register("oil_damage_boosted", () -> new SimpleParticleType(false));
    // TODO: Also consider adding boomerang trail particle and possibly for other throwing weapons too

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent ev) {
        ev.registerSpriteSet(DAMAGE_BOOSTED.get(), DamageModifiedParticle.DamageBoostedProvider::new);
        ev.registerSpriteSet(DAMAGE_REDUCED.get(), DamageModifiedParticle.DamageReducedProvider::new);
        ev.registerSpriteSet(OIL_DAMAGE_BOOSTED.get(), DamageModifiedParticle.OilDamageBoostedProvider::new);
    }
}
