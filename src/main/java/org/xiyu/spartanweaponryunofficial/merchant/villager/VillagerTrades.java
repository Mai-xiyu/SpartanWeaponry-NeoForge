package org.xiyu.spartanweaponryunofficial.merchant.villager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

public class VillagerTrades {
    public static class BaseTrade implements ItemListing {
        protected ItemStack wantedStack;
        protected ItemStack wantedStack2;
        protected ItemStack offeredStack;
        protected int maxUses;
        protected int xpGiven; // Not to be confused with player XP
        protected float priceMultiplier;

        public BaseTrade(
                ItemStack wantedStack,
                ItemStack wantedStack2,
                ItemStack offeredStack,
                int maxUses,
                int xpGiven,
                float priceMultiplier) {
            this.wantedStack = wantedStack;
            this.wantedStack2 = wantedStack2;
            this.offeredStack = offeredStack;
            this.maxUses = maxUses;
            this.xpGiven = xpGiven;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemCost cost1 = new ItemCost(this.wantedStack.getItem(), this.wantedStack.getCount());
            Optional<ItemCost> cost2 =
                    this.wantedStack2.isEmpty()
                            ? Optional.empty()
                            : Optional.of(
                                    new ItemCost(
                                            this.wantedStack2.getItem(),
                                            this.wantedStack2.getCount()));
            return new MerchantOffer(
                    cost1,
                    cost2,
                    this.offeredStack,
                    this.maxUses,
                    this.xpGiven,
                    this.priceMultiplier);
        }
    }

    public static class BuyItemWithEmeraldsTrade extends BaseTrade {
        public BuyItemWithEmeraldsTrade(
                int emeraldCost,
                ItemStack offeredStack,
                int maxUses,
                int xpGiven,
                float priceMultiplier) {
            super(
                    new ItemStack(Items.EMERALD, emeraldCost),
                    ItemStack.EMPTY,
                    offeredStack,
                    maxUses,
                    xpGiven,
                    priceMultiplier);
        }
    }

    public static class BuyEnchantedItemWithEmeraldsTrade extends BuyItemWithEmeraldsTrade {

        public BuyEnchantedItemWithEmeraldsTrade(
                int emeraldCost,
                ItemStack offeredStack,
                int maxUses,
                int xpGiven,
                float priceMultiplier) {
            super(emeraldCost, offeredStack, maxUses, xpGiven, priceMultiplier);
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            int level = 5 + rand.nextInt(15);
            Stream<Holder<Enchantment>> enchantments =
                    trader.level()
                            .registryAccess()
                            .registryOrThrow(Registries.ENCHANTMENT)
                            .holders()
                            .map(h -> h);
            ItemStack enchantedStack =
                    EnchantmentHelper.enchantItem(
                            rand, new ItemStack(this.offeredStack.getItem()), level, enchantments);
            ItemCost cost = new ItemCost(this.wantedStack.getItem(), this.wantedStack.getCount());
            return new MerchantOffer(
                    cost,
                    Optional.empty(),
                    enchantedStack,
                    this.maxUses,
                    this.xpGiven,
                    this.priceMultiplier);
        }
    }

    public static class RandomisedTradeItem {
        private final ItemStack stack;
        private final int emeraldCost;

        public RandomisedTradeItem(Item item, int emeraldCost) {
            this.stack = new ItemStack(item);
            this.emeraldCost = emeraldCost;
        }

        public ItemStack getItemStack() {
            return this.stack;
        }

        public int getEmeraldCost() {
            return this.emeraldCost;
        }
    }

    public static class RandomisedBuyItemWithEmeraldsTrade implements ItemListing {
        protected List<RandomisedTradeItem> offeredItems;
        protected int maxUses;
        protected int xpGiven; // Not to be confused with player XP
        protected float priceMultiplier;

        public RandomisedBuyItemWithEmeraldsTrade(
                List<RandomisedTradeItem> items, int maxUses, int xpGiven, float priceMultiplier) {
            this.offeredItems = items;
            this.maxUses = maxUses;
            this.xpGiven = xpGiven;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            RandomisedTradeItem offeredItem =
                    this.offeredItems.get(
                            this.offeredItems.size() == 1
                                    ? 0
                                    : rand.nextInt(this.offeredItems.size() - 1));
            ItemCost cost = new ItemCost(Items.EMERALD, offeredItem.getEmeraldCost());
            return new MerchantOffer(
                    cost,
                    Optional.empty(),
                    offeredItem.getItemStack(),
                    this.maxUses,
                    this.xpGiven,
                    this.priceMultiplier);
        }
    }

    public static class RandomisedBuyEnchantedItemWithEmeraldsTrade
            extends RandomisedBuyItemWithEmeraldsTrade {

        public RandomisedBuyEnchantedItemWithEmeraldsTrade(
                List<RandomisedTradeItem> items, int maxUses, int xpGiven, float priceMultiplier) {
            super(items, maxUses, xpGiven, priceMultiplier);
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            RandomisedTradeItem offeredItem =
                    this.offeredItems.get(rand.nextInt(this.offeredItems.size() - 1));
            int level = 5 + rand.nextInt(15);
            Stream<Holder<Enchantment>> enchantments =
                    trader.level()
                            .registryAccess()
                            .registryOrThrow(Registries.ENCHANTMENT)
                            .holders()
                            .map(h -> h);
            ItemStack enchantedStack =
                    EnchantmentHelper.enchantItem(
                            rand,
                            new ItemStack(offeredItem.getItemStack().getItem()),
                            level,
                            enchantments);
            ItemCost cost = new ItemCost(Items.EMERALD, offeredItem.getEmeraldCost());
            return new MerchantOffer(
                    cost,
                    Optional.empty(),
                    enchantedStack,
                    this.maxUses,
                    this.xpGiven,
                    this.priceMultiplier);
        }
    }
}
