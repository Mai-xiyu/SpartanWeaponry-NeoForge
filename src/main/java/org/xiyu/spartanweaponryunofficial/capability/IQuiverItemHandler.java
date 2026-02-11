package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraftforge.items.IItemHandler;

public interface IQuiverItemHandler extends IItemHandler {
    /**
     * Resizes the stack list to the specified size. NOTE: If reducing the size of the stack list, any items over the specified size will be LOST
     *
     */
    void resize(int size);

    boolean isEmpty();
}
