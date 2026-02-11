package org.xiyu.spartanweaponryunofficial.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SlotFiltered extends SlotItemHandler {
    protected final Predicate<ItemStack> filter;

    public SlotFiltered(IItemHandler handlerIn, int indexIn, int xPositionIn, int yPositionIn, Predicate<ItemStack> filterIn) {
        super(handlerIn, indexIn, xPositionIn, yPositionIn);
        this.filter = filterIn;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.filter.test(stack);
    }
}
