package org.xiyu.spartanweaponryunofficial.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ModSpartanWeaponry.ID);

    private static ResourceKey<EntityType<?>> key(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, path));
    }

    public static final DeferredHolder<EntityType<?>, EntityType<ArrowBaseEntity>> ARROW_SW = REGISTRY.register("arrow", () -> EntityType.Builder.<ArrowBaseEntity>of(ArrowBaseEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("arrow_sw")));
    public static final DeferredHolder<EntityType<?>, EntityType<ArrowExplosiveEntity>> ARROW_EXPLOSIVE = REGISTRY.register("explosive_arrow", () -> EntityType.Builder.<ArrowExplosiveEntity>of(ArrowExplosiveEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("arrow_explosive")));
    public static final DeferredHolder<EntityType<?>, EntityType<BoltEntity>> BOLT = REGISTRY.register("bolt", () -> EntityType.Builder.<BoltEntity>of(BoltEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("bolt")));
    public static final DeferredHolder<EntityType<?>, EntityType<BoltSpectralEntity>> BOLT_SPECTRAL = REGISTRY.register("spectral_bolt", () -> EntityType.Builder.<BoltSpectralEntity>of(BoltSpectralEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("bolt_spectral")));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowingWeaponEntity>> THROWING_WEAPON = REGISTRY.register("throwing_weapon", () -> EntityType.Builder.<ThrowingWeaponEntity>of(ThrowingWeaponEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("throwing_weapon")));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowingKnifeEntity>> THROWING_KNIFE = REGISTRY.register("throwing_knife", () -> EntityType.Builder.<ThrowingKnifeEntity>of(ThrowingKnifeEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("throwing_knife")));
    public static final DeferredHolder<EntityType<?>, EntityType<TomahawkEntity>> TOMAHAWK = REGISTRY.register("tomahawk", () -> EntityType.Builder.<TomahawkEntity>of(TomahawkEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("tomahawk")));
    public static final DeferredHolder<EntityType<?>, EntityType<JavelinEntity>> JAVELIN = REGISTRY.register("javelin", () -> EntityType.Builder.<JavelinEntity>of(JavelinEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("javelin")));
    public static final DeferredHolder<EntityType<?>, EntityType<BoomerangEntity>> BOOMERANG = REGISTRY.register("boomerang", () -> EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("boomerang")));
    public static final DeferredHolder<EntityType<?>, EntityType<DynamiteEntity>> DYNAMITE = REGISTRY.register("dynamite", () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setShouldReceiveVelocityUpdates(true)
            .sized(0.5f, 0.5f)
            .build(key("dynamite")));
}