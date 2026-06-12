# Mixin Risk Notes

This document records why the current mixins exist and which areas should not be refactored
casually. It is intended for maintainers, not addon authors.

History note: before 1.2.1 the mixin config declared a wrong package and was not referenced from
`neoforge.mods.toml`, so no mixin had ever been applied at runtime. Since 1.2.1 all mixins listed
here load and apply; behavior depending on them is live for the first time.

## High-risk mixins

| Mixin | Purpose | Risk | Replacement direction |
| --- | --- | --- | --- |
| `PlayerMixin` | Replaces player attack damage source for armor-piercing weapon traits. Sweep bonuses moved to the vanilla `SWEEPING_DAMAGE_RATIO` attribute (no sweep hook since 1.21). | High: attack internals are common conflict points for combat mods. | Only replace if NeoForge exposes an equivalent player attack hook. |
| `LivingEntityMixin` | Applies Spartan armor-piercing damage absorption behavior. | High: armor calculation is shared by many combat and RPG mods. | Only replace with an equivalent armor calculation event or damage pipeline hook. |
| `AbstractSkeletonMixin` | Adds longbow compatibility to skeleton ranged AI and spawn equipment. | High: changes AI weapon checks and default equipment flow. | Prefer a stable NeoForge ranged-weapon compatibility hook if one becomes available. |
| `AbstractArrowMixin` | Redirects eligible arrow pickup into quiver storage before vanilla pickup completes. | High: pickup behavior can overlap with inventory, quiver, and projectile mods. | Replace only if a projectile pickup event exposes the same owner, pickup, and inventory state. |
| `ItemModelGeneratorMixin` | Preserves oil coating custom texture metadata during generated item model creation. | Medium-high: client model generation has resource-pack and loader compatibility risk. | Keep scoped to oil-coated models unless NeoForge exposes a custom geometry-safe model generation hook. |

## Spawn equipment mixins

`ZombieMixin`, `WitherSkeletonMixin`, `PiglinMixin`, and `PiglinBruteMixin` inject at the tail of
vanilla spawn equipment population to optionally replace main-hand weapons from configured tags.
The shared implementation lives in `MobMixin`.

These are behavior-affecting but relatively localized. Avoid changing injection timing, tag
selection, or config checks without in-game spawn validation.

## Shared shadow bases

`EntityMixin`, `ProjectileMixin`, and `MobMixin` are shared shadow/helper bases for other mixins.
They do not independently inject behavior, but changes to their shadows can break dependent mixins.

## Current maintenance rule

Do not replace mixins in the same change as formatting or registry/data-gen cleanup. Any future
mixin replacement should be a dedicated change with before/after behavior notes and game-side
validation for combat, projectile pickup, and mob spawn equipment.
