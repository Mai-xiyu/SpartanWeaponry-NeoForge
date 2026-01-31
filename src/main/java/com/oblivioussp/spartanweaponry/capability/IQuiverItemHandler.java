package com.oblivioussp.spartanweaponry.capability;

import net.neoforged.neoforge.items.IItemHandler;

public interface IQuiverItemHandler extends IItemHandler 
{
	/**
	 * Resizes the stack list to the specified size. NOTE: If reducing the size of the stack list, any items over the specified size will be LOST
	 * @param size
	 */
	public void resize(int size);
	
	public boolean isEmpty();
}
