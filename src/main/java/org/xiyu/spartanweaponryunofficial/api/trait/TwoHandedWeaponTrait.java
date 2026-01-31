package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.List;

import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class TwoHandedWeaponTrait extends MeleeCallbackWeaponTrait
{
	public TwoHandedWeaponTrait(String typeIn, String modIdIn) 
	{
		super(typeIn, modIdIn, TraitQuality.NEGATIVE);
		isMelee = true;
	}
	
	@Override
	protected void addTooltipDescription(ItemStack stack, List<Component> tooltip)
	{
		tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", modId, this.type), magnitude * 100.0f).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
	}

	@Override
	public void onItemUpdate(WeaponMaterial material, ItemStack stack, Level level, LivingEntity entity, int itemSlot, boolean isSelected)
	{
		// Only process on server side
		if (level.isClientSide() || !(entity instanceof Player player))
			return;
		
		ItemStack mainHand = entity.getMainHandItem();
		ItemStack offHand = entity.getOffhandItem();
		
		// Case 1: Two-handed weapon in main hand - auto-clear offhand
		if (isSelected && ItemStack.isSameItem(stack, mainHand) && !offHand.isEmpty())
		{
			moveItemToInventoryOrDrop(player, level, EquipmentSlot.OFFHAND);
		}
		// Case 2: Two-handed weapon in off hand - auto-clear main hand
		else if (ItemStack.isSameItem(stack, offHand) && !mainHand.isEmpty())
		{
			moveItemToInventoryOrDrop(player, level, EquipmentSlot.MAINHAND);
		}
	}
	
	/**
	 * Moves the item from the specified slot to player's inventory, or drops it if inventory is full.
	 */
	private void moveItemToInventoryOrDrop(Player player, Level level, EquipmentSlot slotToClear)
	{
		ItemStack itemToMove = player.getItemBySlot(slotToClear);
		if (itemToMove.isEmpty())
			return;
		
		// Create a copy to work with
		ItemStack remaining = itemToMove.copy();
		
		// Step 1: Try to add to player's inventory
		Inventory inventory = player.getInventory();
		inventory.add(remaining);
		if (remaining.isEmpty())
		{
			// Successfully added all items
			player.setItemSlot(slotToClear, ItemStack.EMPTY);
			return;
		}
		
		// Step 2: Try to add remaining items to container items in inventory (like Sophisticated Backpacks)
		if (!remaining.isEmpty())
		{
			remaining = tryAddToContainersInInventory(player, remaining);
		}
		
		// Step 3: If still not empty, drop the remaining items
		if (!remaining.isEmpty())
		{
			ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remaining);
			itemEntity.setPickUpDelay(40); // 2 second pickup delay
			level.addFreshEntity(itemEntity);
		}
		
		// Clear the slot
		player.setItemSlot(slotToClear, ItemStack.EMPTY);
	}
	
	/**
	 * Tries to add an item to container items in the player's inventory.
	 * This supports mods like Sophisticated Backpacks that expose IItemHandler capability.
	 * @param player The player
	 * @param stack The item stack to add
	 * @return The remaining items that couldn't be added
	 */
	private ItemStack tryAddToContainersInInventory(Player player, ItemStack stack)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		
		Inventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++)
		{
			ItemStack containerStack = inventory.getItem(i);
			if (containerStack.isEmpty())
				continue;
			
			// Check if this item has an inventory capability (like Sophisticated Backpacks)
			IItemHandler itemHandler = containerStack.getCapability(Capabilities.ItemHandler.ITEM);
			if (itemHandler != null)
			{
				// Try to insert into this container
				for (int slot = 0; slot < itemHandler.getSlots(); slot++)
				{
					stack = itemHandler.insertItem(slot, stack, false);
					if (stack.isEmpty())
						return ItemStack.EMPTY;
				}
			}
		}
		return stack;
	}
	
	@Override
	public float modifyDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) 
	{
		float resultDamage = baseDamage;
		ItemStack mainHand = attacker.getMainHandItem();
		ItemStack offHand = attacker.getOffhandItem();
		
		if(!mainHand.isEmpty() && !offHand.isEmpty())
		{
			resultDamage *= (1.0f - magnitude);
		}
		return resultDamage;
	}
}
