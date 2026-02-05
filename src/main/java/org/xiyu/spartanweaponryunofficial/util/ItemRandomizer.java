package org.xiyu.spartanweaponryunofficial.util;

import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemRandomizer {
    public static ItemStack generate(Level level, List<Item> items) {
        float weaponRand = level.random.nextFloat();
        float divider = 1.0f / items.size();
        int idx = Mth.floor(weaponRand / divider);
        idx = Math.min(idx, items.size() - 1);

        return new ItemStack(items.get(idx));
    }
}
