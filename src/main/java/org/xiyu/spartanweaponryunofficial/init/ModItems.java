package org.xiyu.spartanweaponryunofficial.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI.WeaponItemType;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.item.*;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;
import org.xiyu.spartanweaponryunofficial.util.WeaponFactory;
import org.xiyu.spartanweaponryunofficial.util.WeaponFactory.WeaponFunction;

public class ModItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(Registries.ITEM, ModSpartanWeaponry.ID);

    private static <T extends Item> T createClassified(
            WeaponFunction<T> factory,
            WeaponMaterial material,
            Item.Properties property,
            WeaponItemType weaponType) {
        return SpartanWeaponryAPI.classifyWeapon(
                factory.create(material, property), weaponType, material);
    }

    private static <T extends Item> WeaponMaterialEntry<T> entry(
            WeaponMaterial material, DeferredHolder<Item, ? extends T> holder) {
        return new WeaponMaterialEntry<>(material, holder);
    }

    private static <T extends Item>
            ImmutableMap<String, DeferredHolder<Item, ? extends T>> indexByMaterial(
                    ImmutableList<WeaponMaterialEntry<T>> vanillaItems,
                    ImmutableList<WeaponMaterialEntry<T>> moddedItems) {
        ImmutableMap.Builder<String, DeferredHolder<Item, ? extends T>> builder =
                ImmutableMap.builder();
        vanillaItems.forEach(entry -> builder.put(entry.materialName(), entry.holder()));
        moddedItems.forEach(entry -> builder.put(entry.materialName(), entry.holder()));
        return builder.build();
    }

    private static <T extends Item> Optional<Item> getItemForMaterial(
            ImmutableMap<String, DeferredHolder<Item, ? extends T>> itemsByMaterialName,
            WeaponMaterial material) {
        DeferredHolder<Item, ? extends T> holder =
                itemsByMaterialName.get(material.getMaterialName());
        return holder == null ? Optional.empty() : Optional.of(holder.get());
    }

    private static <T extends Item> ImmutableList<T> resolveItems(
            ImmutableList<WeaponMaterialEntry<T>> vanillaItems,
            ImmutableList<WeaponMaterialEntry<T>> moddedItems) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        vanillaItems.forEach(entry -> builder.add(entry.get()));
        moddedItems.forEach(entry -> builder.add(entry.get()));
        return builder.build();
    }

    private static <T extends Item> ImmutableList<ItemStack> itemStacks(
            ImmutableList<WeaponMaterialEntry<T>> entries) {
        return entries.stream()
                .map(entry -> new ItemStack(entry.get()))
                .collect(ImmutableList.toImmutableList());
    }

    private static ImmutableList<ItemStack> throwingItemStacks(
            ImmutableList<WeaponMaterialEntry<ThrowingWeaponItem>> entries) {
        return entries.stream()
                .map(entry -> entry.get().makeTabStack())
                .collect(ImmutableList.toImmutableList());
    }

    private record WeaponMaterialEntry<T extends Item>(
            WeaponMaterial material, DeferredHolder<Item, ? extends T> holder) {
        String materialName() {
            return this.material.getMaterialName();
        }

        T get() {
            return this.holder.get();
        }
    }

    @ApiStatus.Internal
    public interface WeaponItemGroup<T extends Item> {
        WeaponItemType getWeaponType();

        ImmutableList<ItemStack> getVanillaItemStacks();

        ImmutableList<ItemStack> getModdedItemStacks();

        ImmutableList<T> getAsList();

        Optional<Item> getItemForMaterial(WeaponMaterial material);
    }

    public static class WeaponItemsMelee implements WeaponItemGroup<SwordBaseItem> {
        private final WeaponItemType weaponType;
        private final ImmutableList<WeaponMaterialEntry<SwordBaseItem>> vanillaItems;
        private final ImmutableList<WeaponMaterialEntry<SwordBaseItem>> moddedItems;
        private final ImmutableMap<String, DeferredHolder<Item, ? extends SwordBaseItem>>
                itemsByMaterialName;
        public final DeferredHolder<Item, SwordBaseItem> wood,
                stone,
                copper,
                iron,
                gold,
                diamond,
                netherite;
        public final DeferredHolder<Item, SwordBaseItem> tin,
                bronze,
                steel,
                silver,
                lead,
                nickel,
                invar,
                constantan,
                platinum,
                electrum,
                aluminum;

        public WeaponItemsMelee(
                DeferredRegister<Item> register,
                String weaponName,
                WeaponItemType weaponType,
                WeaponFunction<SwordBaseItem> factory) {
            this.weaponType = weaponType;
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            this.wood =
                    register.register(
                            weaponName + "_wooden",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.WOOD, propVanilla, weaponType));
            this.stone =
                    register.register(
                            "stone_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.STONE,
                                            propVanilla,
                                            weaponType));
            this.copper =
                    register.register(
                            "copper_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.COPPER,
                                            propVanilla,
                                            weaponType));
            this.iron =
                    register.register(
                            "iron_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.IRON, propVanilla, weaponType));
            this.gold =
                    register.register(
                            "golden_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.GOLD, propVanilla, weaponType));
            this.diamond =
                    register.register(
                            "diamond_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.DIAMOND,
                                            propVanilla,
                                            weaponType));
            this.netherite =
                    register.register(
                            "netherite_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.NETHERITE,
                                            new Item.Properties().fireResistant(),
                                            weaponType));

            this.tin =
                    register.register(
                            "tin_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.TIN, propModded, weaponType));
            this.bronze =
                    register.register(
                            "bronze_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.BRONZE,
                                            propModded,
                                            weaponType));
            this.steel =
                    register.register(
                            "steel_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.STEEL, propModded, weaponType));
            this.silver =
                    register.register(
                            "silver_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.SILVER,
                                            propModded,
                                            weaponType));
            this.electrum =
                    register.register(
                            "electrum_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.ELECTRUM,
                                            propModded,
                                            weaponType));
            this.lead =
                    register.register(
                            "lead_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.LEAD, propModded, weaponType));
            this.nickel =
                    register.register(
                            "nickel_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.NICKEL,
                                            propModded,
                                            weaponType));
            this.invar =
                    register.register(
                            "invar_" + weaponName,
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.INVAR, propModded, weaponType));
            this.constantan =
                    register.register(
                            "constantan_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.CONSTANTAN,
                                            propModded,
                                            weaponType));
            this.platinum =
                    register.register(
                            "platinum_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.PLATINUM,
                                            propModded,
                                            weaponType));
            this.aluminum =
                    register.register(
                            "aluminum_" + weaponName,
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.ALUMINUM,
                                            propModded,
                                            weaponType));

            this.vanillaItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.WOOD, this.wood),
                            entry(WeaponMaterial.STONE, this.stone),
                            entry(WeaponMaterial.COPPER, this.copper),
                            entry(WeaponMaterial.IRON, this.iron),
                            entry(WeaponMaterial.GOLD, this.gold),
                            entry(WeaponMaterial.DIAMOND, this.diamond),
                            entry(WeaponMaterial.NETHERITE, this.netherite));
            this.moddedItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.TIN, this.tin),
                            entry(WeaponMaterial.BRONZE, this.bronze),
                            entry(WeaponMaterial.STEEL, this.steel),
                            entry(WeaponMaterial.SILVER, this.silver),
                            entry(WeaponMaterial.ELECTRUM, this.electrum),
                            entry(WeaponMaterial.LEAD, this.lead),
                            entry(WeaponMaterial.NICKEL, this.nickel),
                            entry(WeaponMaterial.INVAR, this.invar),
                            entry(WeaponMaterial.CONSTANTAN, this.constantan),
                            entry(WeaponMaterial.PLATINUM, this.platinum),
                            entry(WeaponMaterial.ALUMINUM, this.aluminum));
            this.itemsByMaterialName = indexByMaterial(this.vanillaItems, this.moddedItems);
        }

        /*        public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed)
        {
            getAsList().forEach((weapon) -> weapon.setAttackDamageAndSpeed(baseDamage, damageMultiplier, speed));
        }*/

        @Override
        public WeaponItemType getWeaponType() {
            return this.weaponType;
        }

        @Override
        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return itemStacks(this.vanillaItems);
        }

        @Override
        public ImmutableList<ItemStack> getModdedItemStacks() {
            return itemStacks(this.moddedItems);
        }

        @Override
        public ImmutableList<SwordBaseItem> getAsList() {
            return resolveItems(this.vanillaItems, this.moddedItems);
        }

        @Override
        public Optional<Item> getItemForMaterial(WeaponMaterial material) {
            return ModItems.getItemForMaterial(this.itemsByMaterialName, material);
        }
    }

    public static class WeaponItemsRanged implements WeaponItemGroup<Item> {
        private final WeaponItemType weaponType;
        private final ImmutableList<WeaponMaterialEntry<Item>> vanillaItems;
        private final ImmutableList<WeaponMaterialEntry<Item>> moddedItems;
        private final ImmutableMap<String, DeferredHolder<Item, ? extends Item>>
                itemsByMaterialName;
        public final DeferredHolder<Item, Item> wood,
                leather,
                copper,
                iron,
                gold,
                diamond,
                netherite;
        public final DeferredHolder<Item, Item> tin,
                bronze,
                steel,
                silver,
                electrum,
                lead,
                nickel,
                invar,
                constantan,
                platinum,
                aluminum;

        public WeaponItemsRanged(
                DeferredRegister<Item> register,
                String weaponName,
                WeaponItemType weaponType,
                WeaponFunction<? extends Item> factory) {
            this.weaponType = weaponType;
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            // Determine if this weapon type needs 'strengthened' in the name
            boolean isStrengthenedWeapon =
                    weaponName.equals("longbow") || weaponName.equals("heavy_crossbow");
            String strengthenedSuffix = isStrengthenedWeapon ? "_strengthened" : "";

            this.wood =
                    register.register(
                            weaponName + "_wooden" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.WOOD, propVanilla),
                                            weaponType,
                                            WeaponMaterial.WOOD));
            this.leather =
                    register.register(
                            weaponName + "_leather" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.LEATHER, propVanilla),
                                            weaponType,
                                            WeaponMaterial.LEATHER));
            this.copper =
                    register.register(
                            weaponName + "_copper" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.COPPER, propVanilla),
                                            weaponType,
                                            WeaponMaterial.COPPER));
            this.iron =
                    register.register(
                            weaponName + "_iron" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.IRON, propVanilla),
                                            weaponType,
                                            WeaponMaterial.IRON));
            this.gold =
                    register.register(
                            weaponName + "_golden" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.GOLD, propVanilla),
                                            weaponType,
                                            WeaponMaterial.GOLD));
            this.diamond =
                    register.register(
                            weaponName + "_diamond" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.DIAMOND, propVanilla),
                                            weaponType,
                                            WeaponMaterial.DIAMOND));
            this.netherite =
                    register.register(
                            weaponName + "_netherite" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(
                                                    WeaponMaterial.NETHERITE,
                                                    new Item.Properties().fireResistant()),
                                            weaponType,
                                            WeaponMaterial.NETHERITE));

            this.tin =
                    register.register(
                            weaponName + "_tin" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.TIN, propModded),
                                            weaponType,
                                            WeaponMaterial.TIN));
            this.bronze =
                    register.register(
                            weaponName + "_bronze" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.BRONZE, propModded),
                                            weaponType,
                                            WeaponMaterial.BRONZE));
            this.steel =
                    register.register(
                            weaponName + "_steel" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.STEEL, propModded),
                                            weaponType,
                                            WeaponMaterial.STEEL));
            this.silver =
                    register.register(
                            weaponName + "_silver" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.SILVER, propModded),
                                            weaponType,
                                            WeaponMaterial.SILVER));
            this.electrum =
                    register.register(
                            weaponName + "_electrum" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.ELECTRUM, propModded),
                                            weaponType,
                                            WeaponMaterial.ELECTRUM));
            this.lead =
                    register.register(
                            weaponName + "_lead" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.LEAD, propModded),
                                            weaponType,
                                            WeaponMaterial.LEAD));
            this.nickel =
                    register.register(
                            weaponName + "_nickel" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.NICKEL, propModded),
                                            weaponType,
                                            WeaponMaterial.NICKEL));
            this.invar =
                    register.register(
                            weaponName + "_invar" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.INVAR, propModded),
                                            weaponType,
                                            WeaponMaterial.INVAR));
            this.constantan =
                    register.register(
                            weaponName + "_constantan" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.CONSTANTAN, propModded),
                                            weaponType,
                                            WeaponMaterial.CONSTANTAN));
            this.platinum =
                    register.register(
                            weaponName + "_platinum" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.PLATINUM, propModded),
                                            weaponType,
                                            WeaponMaterial.PLATINUM));
            this.aluminum =
                    register.register(
                            weaponName + "_aluminum" + strengthenedSuffix,
                            () ->
                                    SpartanWeaponryAPI.classifyWeapon(
                                            factory.create(WeaponMaterial.ALUMINUM, propModded),
                                            weaponType,
                                            WeaponMaterial.ALUMINUM));

            this.vanillaItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.WOOD, this.wood),
                            entry(WeaponMaterial.LEATHER, this.leather),
                            entry(WeaponMaterial.COPPER, this.copper),
                            entry(WeaponMaterial.IRON, this.iron),
                            entry(WeaponMaterial.GOLD, this.gold),
                            entry(WeaponMaterial.DIAMOND, this.diamond),
                            entry(WeaponMaterial.NETHERITE, this.netherite));
            this.moddedItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.TIN, this.tin),
                            entry(WeaponMaterial.BRONZE, this.bronze),
                            entry(WeaponMaterial.STEEL, this.steel),
                            entry(WeaponMaterial.SILVER, this.silver),
                            entry(WeaponMaterial.ELECTRUM, this.electrum),
                            entry(WeaponMaterial.LEAD, this.lead),
                            entry(WeaponMaterial.NICKEL, this.nickel),
                            entry(WeaponMaterial.INVAR, this.invar),
                            entry(WeaponMaterial.CONSTANTAN, this.constantan),
                            entry(WeaponMaterial.PLATINUM, this.platinum),
                            entry(WeaponMaterial.ALUMINUM, this.aluminum));
            this.itemsByMaterialName = indexByMaterial(this.vanillaItems, this.moddedItems);
        }

        @Override
        public WeaponItemType getWeaponType() {
            return this.weaponType;
        }

        @Override
        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return itemStacks(this.vanillaItems);
        }

        @Override
        public ImmutableList<ItemStack> getModdedItemStacks() {
            return itemStacks(this.moddedItems);
        }

        @Override
        public ImmutableList<Item> getAsList() {
            return resolveItems(this.vanillaItems, this.moddedItems);
        }

        @Override
        public Optional<Item> getItemForMaterial(WeaponMaterial material) {
            return ModItems.getItemForMaterial(this.itemsByMaterialName, material);
        }
    }

    public static class WeaponItemsThrowing implements WeaponItemGroup<ThrowingWeaponItem> {
        private final WeaponItemType weaponType;
        private final ImmutableList<WeaponMaterialEntry<ThrowingWeaponItem>> vanillaItems;
        private final ImmutableList<WeaponMaterialEntry<ThrowingWeaponItem>> moddedItems;
        private final ImmutableMap<String, DeferredHolder<Item, ? extends ThrowingWeaponItem>>
                itemsByMaterialName;
        public DeferredHolder<Item, ThrowingWeaponItem> wood,
                stone,
                copper,
                iron,
                gold,
                diamond,
                netherite;
        public DeferredHolder<Item, ThrowingWeaponItem> tin,
                bronze,
                steel,
                silver,
                electrum,
                lead,
                nickel,
                invar,
                constantan,
                platinum,
                aluminum;

        public WeaponItemsThrowing(
                DeferredRegister<Item> register,
                String weaponName,
                WeaponItemType weaponType,
                WeaponFunction<ThrowingWeaponItem> factory) {
            this.weaponType = weaponType;
            Item.Properties propVanilla = new Item.Properties();
            Item.Properties propModded = new Item.Properties();

            this.wood =
                    register.register(
                            weaponName + "_wooden",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.WOOD, propVanilla, weaponType));
            this.stone =
                    register.register(
                            weaponName + "_stone",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.STONE,
                                            propVanilla,
                                            weaponType));
            this.copper =
                    register.register(
                            weaponName + "_copper",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.COPPER,
                                            propVanilla,
                                            weaponType));
            this.iron =
                    register.register(
                            weaponName + "_iron",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.IRON, propVanilla, weaponType));
            this.gold =
                    register.register(
                            weaponName + "_golden",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.GOLD, propVanilla, weaponType));
            this.diamond =
                    register.register(
                            weaponName + "_diamond",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.DIAMOND,
                                            propVanilla,
                                            weaponType));
            this.netherite =
                    register.register(
                            weaponName + "_netherite",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.NETHERITE,
                                            new Item.Properties().fireResistant(),
                                            weaponType));

            this.tin =
                    register.register(
                            weaponName + "_tin",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.TIN, propModded, weaponType));
            this.bronze =
                    register.register(
                            weaponName + "_bronze",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.BRONZE,
                                            propModded,
                                            weaponType));
            this.steel =
                    register.register(
                            weaponName + "_steel",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.STEEL, propModded, weaponType));
            this.silver =
                    register.register(
                            weaponName + "_silver",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.SILVER,
                                            propModded,
                                            weaponType));
            this.electrum =
                    register.register(
                            weaponName + "_electrum",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.ELECTRUM,
                                            propModded,
                                            weaponType));
            this.lead =
                    register.register(
                            weaponName + "_lead",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.LEAD, propModded, weaponType));
            this.nickel =
                    register.register(
                            weaponName + "_nickel",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.NICKEL,
                                            propModded,
                                            weaponType));
            this.invar =
                    register.register(
                            weaponName + "_invar",
                            () ->
                                    createClassified(
                                            factory, WeaponMaterial.INVAR, propModded, weaponType));
            this.constantan =
                    register.register(
                            weaponName + "_constantan",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.CONSTANTAN,
                                            propModded,
                                            weaponType));
            this.platinum =
                    register.register(
                            weaponName + "_platinum",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.PLATINUM,
                                            propModded,
                                            weaponType));
            this.aluminum =
                    register.register(
                            weaponName + "_aluminum",
                            () ->
                                    createClassified(
                                            factory,
                                            WeaponMaterial.ALUMINUM,
                                            propModded,
                                            weaponType));

            this.vanillaItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.WOOD, this.wood),
                            entry(WeaponMaterial.STONE, this.stone),
                            entry(WeaponMaterial.COPPER, this.copper),
                            entry(WeaponMaterial.IRON, this.iron),
                            entry(WeaponMaterial.GOLD, this.gold),
                            entry(WeaponMaterial.DIAMOND, this.diamond),
                            entry(WeaponMaterial.NETHERITE, this.netherite));
            this.moddedItems =
                    ImmutableList.of(
                            entry(WeaponMaterial.TIN, this.tin),
                            entry(WeaponMaterial.BRONZE, this.bronze),
                            entry(WeaponMaterial.STEEL, this.steel),
                            entry(WeaponMaterial.SILVER, this.silver),
                            entry(WeaponMaterial.ELECTRUM, this.electrum),
                            entry(WeaponMaterial.LEAD, this.lead),
                            entry(WeaponMaterial.NICKEL, this.nickel),
                            entry(WeaponMaterial.INVAR, this.invar),
                            entry(WeaponMaterial.CONSTANTAN, this.constantan),
                            entry(WeaponMaterial.PLATINUM, this.platinum),
                            entry(WeaponMaterial.ALUMINUM, this.aluminum));
            this.itemsByMaterialName = indexByMaterial(this.vanillaItems, this.moddedItems);
        }

        /*        public void updateSettingsFromConfig(float baseDamage, float damageMultiplier, double speed, int chargeTicks)
        {
            getAsList().forEach((weapon) -> weapon.updateFromConfig(baseDamage, damageMultiplier, speed, chargeTicks));
        }*/

        @Override
        public WeaponItemType getWeaponType() {
            return this.weaponType;
        }

        @Override
        public ImmutableList<ItemStack> getVanillaItemStacks() {
            return throwingItemStacks(this.vanillaItems);
        }

        @Override
        public ImmutableList<ItemStack> getModdedItemStacks() {
            return throwingItemStacks(this.moddedItems);
        }

        @Override
        public ImmutableList<ThrowingWeaponItem> getAsList() {
            return resolveItems(this.vanillaItems, this.moddedItems);
        }

        @Override
        public Optional<Item> getItemForMaterial(WeaponMaterial material) {
            return ModItems.getItemForMaterial(this.itemsByMaterialName, material);
        }
    }

    // Basic Items
    public static final DeferredHolder<Item, Item> SIMPLE_HANDLE =
            REGISTRY.register("simple_handle", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HANDLE =
            REGISTRY.register("handle", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SIMPLE_POLE =
            REGISTRY.register("simple_pole", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> POLE =
            REGISTRY.register("pole", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXPLOSIVE_CHARGE =
            REGISTRY.register("explosive_charge", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GREASE_BALL =
            REGISTRY.register("grease_ball", () -> new BasicItem(new Item.Properties()));

    // Weapons
    public static final WeaponItemsMelee DAGGERS =
            new WeaponItemsMelee(REGISTRY, "dagger", WeaponItemType.DAGGER, WeaponFactory.DAGGER);
    public static final WeaponItemsMelee PARRYING_DAGGERS =
            new WeaponItemsMelee(
                    REGISTRY,
                    "parrying_dagger",
                    WeaponItemType.PARRYING_DAGGER,
                    WeaponFactory.PARRYING_DAGGER);
    public static final WeaponItemsMelee LONGSWORDS =
            new WeaponItemsMelee(
                    REGISTRY, "longsword", WeaponItemType.LONGSWORD, WeaponFactory.LONGSWORD);
    public static final WeaponItemsMelee KATANAS =
            new WeaponItemsMelee(REGISTRY, "katana", WeaponItemType.KATANA, WeaponFactory.KATANA);
    public static final WeaponItemsMelee SABERS =
            new WeaponItemsMelee(REGISTRY, "saber", WeaponItemType.SABER, WeaponFactory.SABER);
    public static final WeaponItemsMelee RAPIERS =
            new WeaponItemsMelee(REGISTRY, "rapier", WeaponItemType.RAPIER, WeaponFactory.RAPIER);
    public static final WeaponItemsMelee GREATSWORDS =
            new WeaponItemsMelee(
                    REGISTRY, "greatsword", WeaponItemType.GREATSWORD, WeaponFactory.GREATSWORD);

    public static final DeferredHolder<Item, SwordBaseItem> WOODEN_CLUB =
            REGISTRY.register(
                    "wooden_club",
                    () ->
                            new SwordBaseItem(
                                    new Item.Properties(),
                                    WeaponMaterial.WOOD,
                                    WeaponArchetype.CLUB,
                                    Defaults.DamageBaseClub,
                                    Defaults.DamageMultiplierClub,
                                    Defaults.SpeedClub));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CLUB =
            REGISTRY.register(
                    "studded_club",
                    () ->
                            new SwordBaseItem(
                                    new Item.Properties(),
                                    WeaponMaterial.IRON,
                                    WeaponArchetype.CLUB,
                                    Defaults.DamageBaseClub,
                                    Defaults.DamageMultiplierClub,
                                    Defaults.SpeedClub));

    public static final DeferredHolder<Item, SwordBaseItem> CESTUS =
            REGISTRY.register(
                    "cestus",
                    () ->
                            new SwordBaseItem(
                                    new Item.Properties(),
                                    WeaponMaterial.LEATHER,
                                    WeaponArchetype.CESTUS,
                                    Defaults.DamageBaseCestus,
                                    Defaults.DamageMultiplierCestus,
                                    Defaults.SpeedCestus));
    public static final DeferredHolder<Item, SwordBaseItem> STUDDED_CESTUS =
            REGISTRY.register(
                    "studded_cestus",
                    () ->
                            new SwordBaseItem(
                                    new Item.Properties(),
                                    WeaponMaterial.IRON,
                                    WeaponArchetype.CESTUS,
                                    Defaults.DamageBaseCestus,
                                    Defaults.DamageMultiplierCestus,
                                    Defaults.SpeedCestus));

    public static final WeaponItemsMelee BATTLE_HAMMERS =
            new WeaponItemsMelee(
                    REGISTRY,
                    "battle_hammer",
                    WeaponItemType.BATTLE_HAMMER,
                    WeaponFactory.BATTLE_HAMMER);
    public static final WeaponItemsMelee WARHAMMERS =
            new WeaponItemsMelee(
                    REGISTRY, "warhammer", WeaponItemType.WARHAMMER, WeaponFactory.WARHAMMER);
    public static final WeaponItemsMelee SPEARS =
            new WeaponItemsMelee(REGISTRY, "spear", WeaponItemType.SPEAR, WeaponFactory.SPEAR);
    public static final WeaponItemsMelee HALBERDS =
            new WeaponItemsMelee(
                    REGISTRY, "halberd", WeaponItemType.HALBERD, WeaponFactory.HALBERD);
    public static final WeaponItemsMelee PIKES =
            new WeaponItemsMelee(REGISTRY, "pike", WeaponItemType.PIKE, WeaponFactory.PIKE);
    public static final WeaponItemsMelee LANCES =
            new WeaponItemsMelee(REGISTRY, "lance", WeaponItemType.LANCE, WeaponFactory.LANCE);

    public static final WeaponItemsRanged LONGBOWS =
            new WeaponItemsRanged(
                    REGISTRY, "longbow", WeaponItemType.LONGBOW, WeaponFactory.LONGBOW);
    public static final WeaponItemsRanged HEAVY_CROSSBOWS =
            new WeaponItemsRanged(
                    REGISTRY,
                    "heavy_crossbow",
                    WeaponItemType.HEAVY_CROSSBOW,
                    WeaponFactory.HEAVY_CROSSBOW);

    public static final WeaponItemsThrowing THROWING_KNIVES =
            new WeaponItemsThrowing(
                    REGISTRY,
                    "throwing_knife",
                    WeaponItemType.THROWING_KNIFE,
                    WeaponFactory.THROWING_KNIFE);
    public static final WeaponItemsThrowing TOMAHAWKS =
            new WeaponItemsThrowing(
                    REGISTRY, "tomahawk", WeaponItemType.TOMAHAWK, WeaponFactory.TOMAHAWK);
    public static final WeaponItemsThrowing JAVELINS =
            new WeaponItemsThrowing(
                    REGISTRY, "javelin", WeaponItemType.JAVELIN, WeaponFactory.JAVELIN);
    public static final WeaponItemsThrowing BOOMERANGS =
            new WeaponItemsThrowing(
                    REGISTRY, "boomerang", WeaponItemType.BOOMERANG, WeaponFactory.BOOMERANG);

    public static final WeaponItemsMelee BATTLEAXES =
            new WeaponItemsMelee(
                    REGISTRY, "battleaxe", WeaponItemType.BATTLEAXE, WeaponFactory.BATTLEAXE);
    public static final WeaponItemsMelee FLANGED_MACES =
            new WeaponItemsMelee(
                    REGISTRY,
                    "flanged_mace",
                    WeaponItemType.FLANGED_MACE,
                    WeaponFactory.FLANGED_MACE);
    public static final WeaponItemsMelee GLAIVES =
            new WeaponItemsMelee(REGISTRY, "glaive", WeaponItemType.GLAIVE, WeaponFactory.GLAIVE);
    public static final WeaponItemsMelee QUARTERSTAVES =
            new WeaponItemsMelee(
                    REGISTRY,
                    "quarterstaff",
                    WeaponItemType.QUARTERSTAFF,
                    WeaponFactory.QUARTERSTAFF);
    public static final WeaponItemsMelee SCYTHES =
            new WeaponItemsMelee(REGISTRY, "scythe", WeaponItemType.SCYTHE, WeaponFactory.SCYTHE);

    @ApiStatus.Internal
    public static ImmutableList<WeaponItemsMelee> getMeleeWeaponGroups() {
        return ImmutableList.of(
                DAGGERS,
                PARRYING_DAGGERS,
                LONGSWORDS,
                KATANAS,
                SABERS,
                RAPIERS,
                GREATSWORDS,
                BATTLE_HAMMERS,
                WARHAMMERS,
                SPEARS,
                HALBERDS,
                PIKES,
                LANCES,
                BATTLEAXES,
                FLANGED_MACES,
                GLAIVES,
                QUARTERSTAVES,
                SCYTHES);
    }

    @ApiStatus.Internal
    public static ImmutableList<WeaponItemsRanged> getRangedWeaponGroups() {
        return ImmutableList.of(LONGBOWS, HEAVY_CROSSBOWS);
    }

    @ApiStatus.Internal
    public static ImmutableList<WeaponItemsThrowing> getThrowingWeaponGroups() {
        return ImmutableList.of(THROWING_KNIVES, TOMAHAWKS, JAVELINS, BOOMERANGS);
    }

    @ApiStatus.Internal
    public static ImmutableList<WeaponItemGroup<? extends Item>> getWeaponGroups() {
        return ImmutableList.<WeaponItemGroup<? extends Item>>builder()
                .addAll(getMeleeWeaponGroups())
                .addAll(getRangedWeaponGroups())
                .addAll(getThrowingWeaponGroups())
                .build();
    }

    // Arrows
    public static final DeferredHolder<Item, ArrowBaseItem> WOODEN_ARROW =
            REGISTRY.register(
                    "wooden_arrow",
                    () ->
                            new ArrowBaseItem(
                                    Defaults.BaseDamageArrowWood,
                                    Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_WOODEN_ARROW =
            REGISTRY.register(
                    "tipped_wooden_arrow",
                    () ->
                            new ArrowBaseTippedItem(
                                    "wooden_arrow",
                                    Defaults.BaseDamageArrowWood,
                                    Defaults.RangeMultiplierArrowWood));
    public static final DeferredHolder<Item, ArrowBaseItem> COPPER_ARROW =
            REGISTRY.register(
                    "copper_arrow",
                    () ->
                            new ArrowBaseItem(
                                    Defaults.BaseDamageArrowCopper,
                                    Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_COPPER_ARROW =
            REGISTRY.register(
                    "tipped_copper_arrow",
                    () ->
                            new ArrowBaseTippedItem(
                                    "copper_arrow",
                                    Defaults.BaseDamageArrowCopper,
                                    Defaults.RangeMultiplierArrowCopper));
    public static final DeferredHolder<Item, ArrowBaseItem> IRON_ARROW =
            REGISTRY.register(
                    "iron_arrow",
                    () ->
                            new ArrowBaseItem(
                                    Defaults.BaseDamageArrowIron,
                                    Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_IRON_ARROW =
            REGISTRY.register(
                    "tipped_iron_arrow",
                    () ->
                            new ArrowBaseTippedItem(
                                    "iron_arrow",
                                    Defaults.BaseDamageArrowIron,
                                    Defaults.RangeMultiplierArrowIron));
    public static final DeferredHolder<Item, ArrowBaseItem> DIAMOND_ARROW =
            REGISTRY.register(
                    "diamond_arrow",
                    () ->
                            new ArrowBaseItem(
                                    Defaults.BaseDamageArrowDiamond,
                                    Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_DIAMOND_ARROW =
            REGISTRY.register(
                    "tipped_diamond_arrow",
                    () ->
                            new ArrowBaseTippedItem(
                                    "diamond_arrow",
                                    Defaults.BaseDamageArrowDiamond,
                                    Defaults.RangeMultiplierArrowDiamond));
    public static final DeferredHolder<Item, ArrowBaseItem> NETHERITE_ARROW =
            REGISTRY.register(
                    "netherite_arrow",
                    () ->
                            new ArrowBaseItem(
                                    Defaults.BaseDamageArrowNetherite,
                                    Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, ArrowBaseItem> TIPPED_NETHERITE_ARROW =
            REGISTRY.register(
                    "tipped_netherite_arrow",
                    () ->
                            new ArrowBaseTippedItem(
                                    "netherite_arrow",
                                    Defaults.BaseDamageArrowNetherite,
                                    Defaults.RangeMultiplierArrowNetherite));
    public static final DeferredHolder<Item, Item> EXPLOSIVE_ARROW =
            REGISTRY.register(
                    "explosive_arrow",
                    () -> new ArrowExplosiveItem(Defaults.RangeMultiplierArrowExplosive));

    public static final DeferredHolder<Item, BoltItem> BOLT =
            REGISTRY.register(
                    "bolt",
                    () ->
                            new BoltItem(
                                    Defaults.BaseDamageBolt,
                                    Defaults.RangeMultiplierBolt,
                                    Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> TIPPED_BOLT =
            REGISTRY.register(
                    "tipped_bolt",
                    () ->
                            new BoltTippedItem(
                                    "bolt",
                                    Defaults.BaseDamageBolt,
                                    Defaults.RangeMultiplierBolt,
                                    Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> SPECTRAL_BOLT =
            REGISTRY.register(
                    "spectral_bolt",
                    () ->
                            new BoltSpectralItem(
                                    Defaults.BaseDamageBolt,
                                    Defaults.RangeMultiplierBolt,
                                    Defaults.ArmorPiercingFactorBolt));
    public static final DeferredHolder<Item, BoltItem> COPPER_BOLT =
            REGISTRY.register(
                    "copper_bolt",
                    () ->
                            new BoltItem(
                                    Defaults.BaseDamageBoltCopper,
                                    Defaults.RangeMultiplierBoltCopper,
                                    Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> TIPPED_COPPER_BOLT =
            REGISTRY.register(
                    "tipped_copper_bolt",
                    () ->
                            new BoltTippedItem(
                                    "copper_bolt",
                                    Defaults.BaseDamageBoltCopper,
                                    Defaults.RangeMultiplierBoltCopper,
                                    Defaults.ArmorPiercingFactorBoltCopper));
    public static final DeferredHolder<Item, BoltItem> DIAMOND_BOLT =
            REGISTRY.register(
                    "diamond_bolt",
                    () ->
                            new BoltItem(
                                    Defaults.BaseDamageBoltDiamond,
                                    Defaults.RangeMultiplierBoltDiamond,
                                    Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> TIPPED_DIAMOND_BOLT =
            REGISTRY.register(
                    "tipped_diamond_bolt",
                    () ->
                            new BoltTippedItem(
                                    "diamond_bolt",
                                    Defaults.BaseDamageBoltDiamond,
                                    Defaults.RangeMultiplierBoltDiamond,
                                    Defaults.ArmorPiercingFactorBoltDiamond));
    public static final DeferredHolder<Item, BoltItem> NETHERITE_BOLT =
            REGISTRY.register(
                    "netherite_bolt",
                    () ->
                            new BoltItem(
                                    Defaults.BaseDamageBoltNetherite,
                                    Defaults.RangeMultiplierBoltNetherite,
                                    Defaults.ArmorPiercingFactorBoltNetherite));
    public static final DeferredHolder<Item, BoltItem> TIPPED_NETHERITE_BOLT =
            REGISTRY.register(
                    "tipped_netherite_bolt",
                    () ->
                            new BoltTippedItem(
                                    "netherite_bolt",
                                    Defaults.BaseDamageBoltNetherite,
                                    Defaults.RangeMultiplierBoltNetherite,
                                    Defaults.ArmorPiercingFactorBoltNetherite));

    public static final DeferredHolder<Item, Item> SMALL_ARROW_QUIVER =
            REGISTRY.register(
                    "small_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_ARROW_QUIVER =
            REGISTRY.register(
                    "medium_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_ARROW_QUIVER =
            REGISTRY.register(
                    "large_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_ARROW_QUIVER =
            REGISTRY.register(
                    "huge_arrow_quiver", () -> new QuiverArrowItem(Defaults.SlotsQuiverHuge));
    public static final DeferredHolder<Item, Item> SMALL_BOLT_QUIVER =
            REGISTRY.register(
                    "small_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverSmall));
    public static final DeferredHolder<Item, Item> MEDIUM_BOLT_QUIVER =
            REGISTRY.register(
                    "medium_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverMedium));
    public static final DeferredHolder<Item, Item> LARGE_BOLT_QUIVER =
            REGISTRY.register(
                    "large_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverLarge));
    public static final DeferredHolder<Item, Item> HUGE_BOLT_QUIVER =
            REGISTRY.register(
                    "huge_bolt_quiver", () -> new QuiverBoltItem(Defaults.SlotsQuiverHuge));

    public static final DeferredHolder<Item, Item> QUIVER_COMPARTMENT =
            REGISTRY.register("quiver_compartment", QuiverSmithingTemplateItem::new);
    public static final DeferredHolder<Item, Item> MEDIUM_QUIVER_BRACE =
            REGISTRY.register("medium_quiver_brace", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LARGE_QUIVER_BRACE =
            REGISTRY.register("large_quiver_brace", () -> new BasicItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HUGE_QUIVER_BRACE =
            REGISTRY.register("huge_quiver_brace", () -> new BasicItem(new Item.Properties()));

    /*    public static final RegistryObject<Item> MEDIUM_QUIVER_UPGRADE_KIT = REGISTRY.register("medium_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
    public static final RegistryObject<Item> LARGE_QUIVER_UPGRADE_KIT = REGISTRY.register("large_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));
    public static final RegistryObject<Item> HUGE_QUIVER_UPGRADE_KIT = REGISTRY.register("huge_quiver_upgrade_kit", () -> new BasicItem(new Item.Properties()));*/

    public static final DeferredHolder<Item, Item> DYNAMITE =
            REGISTRY.register("dynamite", () -> new DynamiteItem(new Item.Properties()));

    public static final DeferredHolder<Item, WeaponOilItem> WEAPON_OIL =
            REGISTRY.register("weapon_oil", WeaponOilItem::new);

    public static final DeferredHolder<Item, Item> BLAZE_HEAD =
            REGISTRY.register(
                    "blaze_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.BLAZE_HEAD.get(),
                                    ModBlocks.BLAZE_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> ENDERMAN_HEAD =
            REGISTRY.register(
                    "enderman_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.ENDERMAN_HEAD.get(),
                                    ModBlocks.ENDERMAN_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> SPIDER_HEAD =
            REGISTRY.register(
                    "spider_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.SPIDER_HEAD.get(),
                                    ModBlocks.SPIDER_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> CAVE_SPIDER_HEAD =
            REGISTRY.register(
                    "cave_spider_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.CAVE_SPIDER_HEAD.get(),
                                    ModBlocks.CAVE_SPIDER_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> ZOMBIFIED_PIGLIN_HEAD =
            REGISTRY.register(
                    "zombified_piglin_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(),
                                    ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> HUSK_HEAD =
            REGISTRY.register(
                    "husk_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.HUSK_HEAD.get(),
                                    ModBlocks.HUSK_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> STRAY_SKULL =
            REGISTRY.register(
                    "stray_skull",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.STRAY_SKULL.get(),
                                    ModBlocks.STRAY_WALL_SKULL.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> DROWNED_HEAD =
            REGISTRY.register(
                    "drowned_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.DROWNED_HEAD.get(),
                                    ModBlocks.DROWNED_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> ILLAGER_HEAD =
            REGISTRY.register(
                    "illager_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.ILLAGER_HEAD.get(),
                                    ModBlocks.ILLAGER_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
    public static final DeferredHolder<Item, Item> WITCH_HEAD =
            REGISTRY.register(
                    "witch_head",
                    () ->
                            new ExtendedSkullItem(
                                    ModBlocks.WITCH_HEAD.get(),
                                    ModBlocks.WITCH_WALL_HEAD.get(),
                                    new Item.Properties().rarity(Rarity.UNCOMMON),
                                    Direction.DOWN));
}
