# Addon Author Notes

This document describes the stable addon-facing API surface for Spartan Weaponry Unofficial on
Minecraft 1.21.1 NeoForge.

## Dependency

Addons should depend on the mod id `spartan_weaponry_unofficial` in `META-INF/neoforge.mods.toml`:

```toml
[[dependencies.your_mod_id]]
modId = "spartan_weaponry_unofficial"
type = "required"
versionRange = "[1.1.2,)"
ordering = "AFTER"
side = "BOTH"
```

Use the same jar version that your addon is tested against. The Java package for the public API is
`org.xiyu.spartanweaponryunofficial.api`.

## API version

The current API version is `15`.

Call `SpartanWeaponryAPI.assertAPIVersion` from your mod constructor or early setup code:

```java
SpartanWeaponryAPI.assertAPIVersion("your_mod_id", 15);
```

API 15 adds `WeaponTraits.registry()` / `OilEffects.registry()` accessors,
`WeaponMaterial.colorRGB(int, int, int)`, `WeaponMaterial` incorrect-blocks-for-drops control, and
`WeaponTrait.getModId()` / `getQuality()`. Request `14` if you only need classification metadata
and the `createWeapon` descriptor entry point.

Request the lowest API version that contains the features your addon needs. Older addons can continue
to request older versions.

## Creating weapons

The legacy creation methods remain supported:

```java
public static final DeferredItem<Item> STEEL_LONGSWORD = ITEMS.register(
        "steel_longsword",
        () -> SpartanWeaponryAPI.createLongsword(WeaponMaterial.STEEL)
);
```

New code can use the descriptor-based entry point when the weapon type is data-driven or selected from
a shared table:

```java
public static final DeferredItem<Item> STEEL_LONGSWORD = ITEMS.register(
        "steel_longsword",
        () -> SpartanWeaponryAPI.createWeapon(
                SpartanWeaponryAPI.WeaponItemType.LONGSWORD,
                WeaponMaterial.STEEL
        )
);
```

Both forms create the same item type and automatically attach weapon classification metadata.

## Defining materials

Existing `WeaponMaterial` constructors are still supported. For new addon code, prefer the builder when
named fields make the material easier to audit:

```java
public static final WeaponMaterial MYTHRIL = WeaponMaterial.builder("mythril", "your_mod_id")
        .colours(0x8CC8FF, 0xDDF6FF)
        .durability(900)
        .speed(7.0f)
        .baseDamage(3.0f)
        .enchantability(18)
        .repairTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/mythril")))
        .traitsTag(ModWeaponTraitTags.STEEL)
        .build();
```

Material names are used by grouped material tags such as
`spartan_weaponry_unofficial:materials/mythril`.

Materials built from a vanilla `Tier` (constructor or `Builder.tier(...)`) inherit the tier's
`getIncorrectBlocksForDrops` tag, which controls mining-level checks for versatile weapons.
Materials built from raw numbers default to `BlockTags.INCORRECT_FOR_WOODEN_TOOL`; override it with
`Builder.incorrectBlocksForDrops(...)` or `WeaponMaterial.setIncorrectBlocksForDrops(...)`.

## Registry access

Custom weapon traits and oil effects are registered against the mod's registries. Create your own
`DeferredRegister` against the exposed keys:

```java
public static final DeferredRegister<WeaponTrait> TRAITS =
        DeferredRegister.create(WeaponTraits.REGISTRY_KEY, "your_mod_id");
public static final DeferredRegister<OilEffect> OILS =
        DeferredRegister.create(OilEffects.REGISTRY_KEY, "your_mod_id");
```

For lookups after registry creation, use `WeaponTraits.registry()` and `OilEffects.registry()`.

## Classification

Weapons created through `SpartanWeaponryAPI.createWeapon` or legacy `createXxx` methods are classified
automatically. If an addon creates a compatible item without these factories, it can opt in manually:

```java
Item item = new CustomSpearItem(properties);
return SpartanWeaponryAPI.classifyWeapon(
        item,
        SpartanWeaponryAPI.WeaponItemType.SPEAR,
        MYTHRIL
);
```

Runtime lookup:

```java
SpartanWeaponryAPI.getWeaponClassification(item).ifPresent(classification -> {
    String weaponType = classification.weaponTypeName();
    String material = classification.materialName();
});
```

Classification metadata is runtime metadata. Datapacks and KubeJS can still add items to the same tags
without appearing in the runtime classification snapshot.

## Stable and internal APIs

Stable addon entry points:

- `SpartanWeaponryAPI`
- `WeaponMaterial`
- `WeaponClassification`
- `WeaponTraits` and `api.trait` callback/trait types
- `api.tags.*`
- `api.data.model.*` and `api.data.recipe.*` datagen helpers

Internal or implementation-facing types should not be depended on directly:

- `IInternalMethodHandler`
- `org.xiyu.spartanweaponryunofficial.init.ModItems` and its internal weapon group views
- `org.xiyu.spartanweaponryunofficial.item.*` helper classes
- `org.xiyu.spartanweaponryunofficial.util.*` unless a type is explicitly documented as API
- mixin classes
