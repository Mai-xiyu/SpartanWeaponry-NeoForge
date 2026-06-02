# Data Generation

This document covers addon-facing datagen helpers. These helpers generate JSON; they do not change
runtime behavior by themselves.

## Item model helpers

Use `org.xiyu.spartanweaponryunofficial.api.data.model.ModelGenerator` from an
`ItemModelProvider`:

```java
@Override
protected void registerModels() {
    ModelGenerator models = new ModelGenerator(this);
    models.createLongswordModel(ModItems.MYTHRIL_LONGSWORD.get());
    models.createSpearModel(ModItems.MYTHRIL_SPEAR.get());
}
```

Use the helper that matches the registered weapon type. The helper derives model and texture names
from the item registry name used by your addon.

## Recipe helpers

Use `org.xiyu.spartanweaponryunofficial.api.data.recipe.RecipeProviderHelper` from a recipe provider:

```java
RecipeProviderHelper.recipeLongsword(
        output,
        YourItems.HANDLE.get(),
        ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/mythril")),
        ModItems.MYTHRIL_LONGSWORD.get(),
        "has_mythril_ingot"
);
```

Recipe helpers preserve the same recipe semantics as the built-in Spartan Weaponry recipes. Addons
remain responsible for recipe ids, unlock criteria, and conditional loading decisions in their own
provider.

## Classification tags

If your addon creates weapons through `SpartanWeaponryAPI.createWeapon` or legacy `createXxx` methods,
classification metadata is attached automatically. A tag provider can emit the standard grouped tags:

```java
SpartanWeaponryAPI.forEachKnownWeaponTag("your_mod_id", (tag, item) -> this.tag(tag).add(item));
```

For custom items that do not use the factories, call `classifyWeapon` in the registration lambda before
datagen queries the classification snapshot:

```java
public static final DeferredItem<Item> MYTHRIL_SPEAR = ITEMS.register(
        "mythril_spear",
        () -> SpartanWeaponryAPI.classifyWeapon(
                new CustomSpearItem(new Item.Properties()),
                SpartanWeaponryAPI.WeaponItemType.SPEAR,
                MYTHRIL
        )
);
```

Datapacks can add or remove tag entries independently of runtime classification metadata. If a datapack
adds an item to `#spartan_weaponry_unofficial:weapons/spears`, Java runtime classification lookup will
not automatically know about that item.

## Avoiding resource drift

When updating datagen code, compare generated output before committing:

```powershell
.\gradlew.bat runData --console=plain --no-daemon
git diff -- src/generated/resources
```

Generated JSON should only change when a tag, recipe, model, loot table, or translation key is intended
to change.
