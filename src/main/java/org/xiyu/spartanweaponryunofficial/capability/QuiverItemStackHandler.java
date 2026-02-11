package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class QuiverItemStackHandler extends ItemStackHandler implements IQuiverItemHandler {
    private final ItemStack quiverStack;

    public QuiverItemStackHandler(ItemStack stack, int size) {
        super(size);
        this.quiverStack = stack;
        CompoundTag tag = ItemStackDataHelper.getTag(stack).getCompound(QuiverBaseItem.NBT_AMMO);
        if (!tag.isEmpty())
            this.deserializeNBT(getRegistryAccess(), tag);
    }

    /**
     * Resizes the stack list to the specified size. NOTE: If reducing the size of the stack list, any items over the specified size will be LOST
     *
     */
    public void resize(int size) {
        NonNullList<ItemStack> newStacks = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < newStacks.size(); i++) {
            if (i < this.stacks.size())
                newStacks.set(i, this.stacks.get(i));
        }
        this.stacks = newStacks;
        this.syncToStack();
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.syncToStack();
    }

    private void syncToStack() {
        ItemStackDataHelper.updateTag(this.quiverStack, tag -> tag.put(QuiverBaseItem.NBT_AMMO, this.serializeNBT(getRegistryAccess())));
    }

    private static RegistryAccess getRegistryAccess() {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (stack.isEmpty())
                return false;
        }
        return true;
    }
}
