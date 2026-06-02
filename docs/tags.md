# Tags

Spartan Weaponry Unofficial keeps the original flat tags and also provides grouped tags for datapacks,
modpacks, and addon data generation. New tags are additive; existing tag paths remain valid.

## Grouped weapon tags

All classified weapons should be included in:

```text
#spartan_weaponry_unofficial:weapons
```

Weapon type tags use plural paths:

```text
#spartan_weaponry_unofficial:weapons/longswords
#spartan_weaponry_unofficial:weapons/spears
#spartan_weaponry_unofficial:weapons/heavy_crossbows
#spartan_weaponry_unofficial:weapons/throwing_knives
```

Use `SpartanWeaponryAPI.getWeaponTag(WeaponItemType)` or `ModItemTags.weaponType(...)` from Java.

## Material tags

Material tags use the material logical name, not the item registry prefix:

```text
#spartan_weaponry_unofficial:materials/wood
#spartan_weaponry_unofficial:materials/steel
#spartan_weaponry_unofficial:materials/diamond
```

Use `SpartanWeaponryAPI.getMaterialTag(material)` or `ModItemTags.material(...)` from Java.

## Namespace tags

Source namespace tags group weapons by registry namespace:

```text
#spartan_weaponry_unofficial:mods/spartan_weaponry_unofficial
#spartan_weaponry_unofficial:mods/your_mod_id
```

Use `SpartanWeaponryAPI.getNamespaceTag(namespace)` or `ModItemTags.namespace(namespace)` from Java.

## Legacy tags

Legacy flat tags are retained for compatibility:

```text
#spartan_weaponry_unofficial:longsword
#spartan_weaponry_unofficial:spear
#spartan_weaponry_unofficial:steel_weapons
#spartan_weaponry_unofficial:contantan_weapons
```

The misspelled `contantan_weapons` path is intentionally kept because changing it would break existing
datapacks.

## Datapack example

```json
{
  "replace": false,
  "values": [
    "your_mod_id:mythril_longsword"
  ]
}
```

Place the file at:

```text
data/spartan_weaponry_unofficial/tags/item/weapons/longswords.json
```

Add the same item to material and namespace tags if your datapack needs those groups:

```text
data/spartan_weaponry_unofficial/tags/item/materials/mythril.json
data/spartan_weaponry_unofficial/tags/item/mods/your_mod_id.json
```

## KubeJS example

No KubeJS dependency is required by this mod. Use normal item tags:

```js
ServerEvents.tags("item", event => {
  event.add("spartan_weaponry_unofficial:weapons", "your_mod_id:mythril_longsword");
  event.add("spartan_weaponry_unofficial:weapons/longswords", "your_mod_id:mythril_longsword");
  event.add("spartan_weaponry_unofficial:materials/mythril", "your_mod_id:mythril_longsword");
  event.add("spartan_weaponry_unofficial:mods/your_mod_id", "your_mod_id:mythril_longsword");
});
```
