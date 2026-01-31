package org.xiyu.spartanweaponryunofficial.capability;

import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class QuiverItemStackHandler extends ItemStackHandler implements IQuiverItemHandler
{
	private final ItemStack quiverStack;

	public QuiverItemStackHandler(ItemStack stack, int size)
	{
		super(size);
		this.quiverStack = stack;
		CompoundTag tag = ItemStackDataHelper.getTag(stack).getCompound(QuiverBaseItem.NBT_AMMO);
		if(!tag.isEmpty())
			deserializeNBT(getRegistryAccess(), tag);
	}
	
	/**
	 * Resizes the stack list to the specified size. NOTE: If reducing the size of the stack list, any items over the specified size will be LOST
	 * @param size
	 */
	public void resize(int size)
	{
		NonNullList<ItemStack> newStacks = NonNullList.withSize(size, ItemStack.EMPTY);
		
		for(int i = 0; i < newStacks.size(); i++)
		{
			if(i < stacks.size())
				newStacks.set(i, stacks.get(i));
		}
		stacks = newStacks;
		syncToStack();
	}

	@Override
	protected void onContentsChanged(int slot)
	{
		syncToStack();
	}

	private void syncToStack()
	{
		ItemStackDataHelper.updateTag(quiverStack, tag -> tag.put(QuiverBaseItem.NBT_AMMO, serializeNBT(getRegistryAccess())));
	}

	private static RegistryAccess getRegistryAccess()
	{
		return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	}

	@Override
	public boolean isEmpty() 
	{
		for(ItemStack stack : stacks)
		{
			if(stack.isEmpty())
				return false;
		}
		return true;
	}
}
