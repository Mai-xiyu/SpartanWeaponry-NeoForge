package org.xiyu.spartanweaponryunofficial.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;
import org.xiyu.spartanweaponryunofficial.util.WeaponOilConfig;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModSpartanWeaponry.ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASIC = REGISTRY.register("basic", () ->
            CreativeModeTab.builder().
                    title(Component.translatable("itemGroup." + ModSpartanWeaponry.ID + ".basic")).
                    icon(() -> new ItemStack(ModItems.LONGSWORDS.diamond.get())).
                    displayItems((itemDisplayParams, output) -> {
                        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
                        builder.add(new ItemStack(ModItems.SIMPLE_HANDLE.get()), new ItemStack(ModItems.HANDLE.get()),
                                new ItemStack(ModItems.SIMPLE_POLE.get()), new ItemStack(ModItems.POLE.get()),
                                new ItemStack(ModItems.EXPLOSIVE_CHARGE.get()), new ItemStack(ModItems.GREASE_BALL.get()));
                        builder.addAll(ModItems.DAGGERS.getVanillaItemStacks());
                        builder.addAll(ModItems.PARRYING_DAGGERS.getVanillaItemStacks());
                        builder.addAll(ModItems.LONGSWORDS.getVanillaItemStacks());
                        builder.addAll(ModItems.KATANAS.getVanillaItemStacks());
                        builder.addAll(ModItems.SABERS.getVanillaItemStacks());
                        builder.addAll(ModItems.RAPIERS.getVanillaItemStacks());
                        builder.addAll(ModItems.GREATSWORDS.getVanillaItemStacks());
                        builder.add(new ItemStack(ModItems.WOODEN_CLUB.get()), new ItemStack(ModItems.STUDDED_CLUB.get()),
                                new ItemStack(ModItems.CESTUS.get()), new ItemStack(ModItems.STUDDED_CESTUS.get()));
                        builder.addAll(ModItems.BATTLE_HAMMERS.getVanillaItemStacks());
                        builder.addAll(ModItems.WARHAMMERS.getVanillaItemStacks());
                        builder.addAll(ModItems.SPEARS.getVanillaItemStacks());
                        builder.addAll(ModItems.HALBERDS.getVanillaItemStacks());
                        builder.addAll(ModItems.PIKES.getVanillaItemStacks());
                        builder.addAll(ModItems.LANCES.getVanillaItemStacks());
                        builder.addAll(ModItems.LONGBOWS.getVanillaItemStacks());
                        builder.addAll(ModItems.HEAVY_CROSSBOWS.getVanillaItemStacks());
                        builder.addAll(ModItems.THROWING_KNIVES.getVanillaItemStacks());
                        builder.addAll(ModItems.TOMAHAWKS.getVanillaItemStacks());
                        builder.addAll(ModItems.JAVELINS.getVanillaItemStacks());
                        builder.addAll(ModItems.BOOMERANGS.getVanillaItemStacks());
                        builder.addAll(ModItems.BATTLEAXES.getVanillaItemStacks());
                        builder.addAll(ModItems.FLANGED_MACES.getVanillaItemStacks());
                        builder.addAll(ModItems.GLAIVES.getVanillaItemStacks());
                        builder.addAll(ModItems.QUARTERSTAVES.getVanillaItemStacks());
                        builder.addAll(ModItems.SCYTHES.getVanillaItemStacks());
                        output.acceptAll(builder.build());
                        output.accept(ModItems.SMALL_ARROW_QUIVER.get());
                        output.accept(ModItems.MEDIUM_ARROW_QUIVER.get());
                        output.accept(ModItems.LARGE_ARROW_QUIVER.get());
                        output.accept(ModItems.HUGE_ARROW_QUIVER.get());
                        output.accept(ModItems.SMALL_BOLT_QUIVER.get());
                        output.accept(ModItems.MEDIUM_BOLT_QUIVER.get());
                        output.accept(ModItems.LARGE_BOLT_QUIVER.get());
                        output.accept(ModItems.HUGE_BOLT_QUIVER.get());
                        output.accept(ModItems.QUIVER_COMPARTMENT.get());
                        output.accept(ModItems.MEDIUM_QUIVER_BRACE.get());
                        output.accept(ModItems.LARGE_QUIVER_BRACE.get());
                        output.accept(ModItems.HUGE_QUIVER_BRACE.get());
                        output.accept(ModItems.DYNAMITE.get());
                        makeWeaponOilVariants(output);
                        output.accept(ModItems.BLAZE_HEAD.get());
                        output.accept(ModItems.ENDERMAN_HEAD.get());
                        output.accept(ModItems.SPIDER_HEAD.get());
                        output.accept(ModItems.CAVE_SPIDER_HEAD.get());
                        output.accept(ModItems.ZOMBIFIED_PIGLIN_HEAD.get());
                        output.accept(ModItems.HUSK_HEAD.get());
                        output.accept(ModItems.STRAY_SKULL.get());
                        output.accept(ModItems.DROWNED_HEAD.get());
                        output.accept(ModItems.ILLAGER_HEAD.get());
                        output.accept(ModItems.WITCH_HEAD.get());
                    }).
                    build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MODDED = REGISTRY.register("modded", () ->
            CreativeModeTab.builder().
                    title(Component.translatable("itemGroup." + ModSpartanWeaponry.ID + ".modded")).
                    icon(() -> new ItemStack(ModItems.GREATSWORDS.bronze.get())).
                    displayItems((itemDisplayParams, output) -> {
                        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
                        builder.addAll(ModItems.DAGGERS.getModdedItemStacks());
                        builder.addAll(ModItems.PARRYING_DAGGERS.getModdedItemStacks());
                        builder.addAll(ModItems.LONGSWORDS.getModdedItemStacks());
                        builder.addAll(ModItems.KATANAS.getModdedItemStacks());
                        builder.addAll(ModItems.SABERS.getModdedItemStacks());
                        builder.addAll(ModItems.RAPIERS.getModdedItemStacks());
                        builder.addAll(ModItems.GREATSWORDS.getModdedItemStacks());
                        builder.addAll(ModItems.BATTLE_HAMMERS.getModdedItemStacks());
                        builder.addAll(ModItems.WARHAMMERS.getModdedItemStacks());
                        builder.addAll(ModItems.SPEARS.getModdedItemStacks());
                        builder.addAll(ModItems.HALBERDS.getModdedItemStacks());
                        builder.addAll(ModItems.PIKES.getModdedItemStacks());
                        builder.addAll(ModItems.LANCES.getModdedItemStacks());
                        builder.addAll(ModItems.LONGBOWS.getModdedItemStacks());
                        builder.addAll(ModItems.HEAVY_CROSSBOWS.getModdedItemStacks());
                        builder.addAll(ModItems.THROWING_KNIVES.getModdedItemStacks());
                        builder.addAll(ModItems.TOMAHAWKS.getModdedItemStacks());
                        builder.addAll(ModItems.JAVELINS.getModdedItemStacks());
                        builder.addAll(ModItems.BOOMERANGS.getModdedItemStacks());
                        builder.addAll(ModItems.BATTLEAXES.getModdedItemStacks());
                        builder.addAll(ModItems.FLANGED_MACES.getModdedItemStacks());
                        builder.addAll(ModItems.GLAIVES.getModdedItemStacks());
                        builder.addAll(ModItems.QUARTERSTAVES.getModdedItemStacks());
                        builder.addAll(ModItems.SCYTHES.getModdedItemStacks());
                        output.acceptAll(builder.build());
                    }).withTabsBefore(BASIC.getKey()).
                    build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARROWS_BOLTS = REGISTRY.register("arrows_bolts", () ->
            CreativeModeTab.builder().
                    title(Component.translatable("itemGroup." + ModSpartanWeaponry.ID + ".arrows_bolts")).
                    icon(() -> new ItemStack(ModItems.DIAMOND_ARROW.get())).
                    displayItems((itemDisplayParams, output) -> {
                        output.accept(ModItems.WOODEN_ARROW.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_WOODEN_ARROW.get());
                        output.accept(ModItems.COPPER_ARROW.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_COPPER_ARROW.get());
                        output.accept(ModItems.IRON_ARROW.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_IRON_ARROW.get());
                        output.accept(ModItems.DIAMOND_ARROW.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_DIAMOND_ARROW.get());
                        output.accept(ModItems.NETHERITE_ARROW.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_NETHERITE_ARROW.get());
                        output.accept(ModItems.EXPLOSIVE_ARROW.get());
                        output.accept(ModItems.BOLT.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_BOLT.get());
                        output.accept(ModItems.SPECTRAL_BOLT.get());
                        output.accept(ModItems.COPPER_BOLT.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_COPPER_BOLT.get());
                        output.accept(ModItems.DIAMOND_BOLT.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_DIAMOND_BOLT.get());
                        output.accept(ModItems.NETHERITE_BOLT.get());
                        makeTippedProjectiles(output, ModItems.TIPPED_NETHERITE_BOLT.get());
                    }).withTabsBefore(MODDED.getKey()).
                    build());

    private static void makeTippedProjectiles(CreativeModeTab.Output output, Item item) {
        BuiltInRegistries.POTION.stream()
                .filter((potion) -> !(potion.getEffects().isEmpty()))
                .forEach((potion) -> output.accept(PotionContents.createItemStack(item, BuiltInRegistries.POTION.wrapAsHolder(potion))));
    }

    private static void makeWeaponOilVariants(CreativeModeTab.Output output) {
        if (!WeaponOilConfig.isEnabled())
            return;

        OilEffects.REGISTRY.getEntries().stream().filter((oil) -> oil.get() != OilEffects.POTION.get()).forEach((oil) -> output.accept(OilHelper.makeOilStack(oil.get())));
        BuiltInRegistries.POTION.stream().filter(OilHelper::isValidPotion).forEach((potion) -> output.accept(OilHelper.makePotionOilStack(potion)));
    }
}
