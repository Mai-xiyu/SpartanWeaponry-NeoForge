package org.xiyu.spartanweaponryunofficial.inventory;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SlotFiltered extends SlotItemHandler {
    protected final Predicate<ItemStack> filter;
    protected final Identifier noItemIcon;

    public SlotFiltered(IItemHandler handlerIn, int indexIn, int xPositionIn, int yPositionIn, Predicate<ItemStack> filterIn) {
        this(handlerIn, indexIn, xPositionIn, yPositionIn, filterIn, null);
    }

    public SlotFiltered(IItemHandler handlerIn, int indexIn, int xPositionIn, int yPositionIn, Predicate<ItemStack> filterIn, @Nullable Identifier noItemIconIn) {
        super(handlerIn, indexIn, xPositionIn, yPositionIn);
        this.filter = filterIn;
        this.noItemIcon = noItemIconIn;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.filter.test(stack);
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return this.noItemIcon;
    }
}
