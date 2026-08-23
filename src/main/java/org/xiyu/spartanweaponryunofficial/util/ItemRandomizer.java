package org.xiyu.spartanweaponryunofficial.util;

import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRandomizer {
    public static ItemStack generate(RandomSource random, List<Item> items) {
        return new ItemStack(items.get(random.nextInt(items.size())));
    }
}
