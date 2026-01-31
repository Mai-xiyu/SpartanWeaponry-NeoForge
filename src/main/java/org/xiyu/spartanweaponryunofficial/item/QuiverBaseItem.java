package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.capability.QuiverItemStackHandler;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.QuiverTooltip;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;

public abstract class QuiverBaseItem extends Item
{
	public enum SlotType
	{
		UNDEFINED,
		MAIN_HAND,
		OFF_HAND,
		HOTBAR,
		CURIO
	}
	
	public static final String NBT_AMMO_COLLECT = "AmmoCollect";
	public static final String NBT_AMMO = "Ammo";
	public static final String NBT_OFFHAND_MOVED = "OffhandMoved";
	public static final String NBT_ITEM_ID = "Id";
	public static final String NBT_ITEM_SLOT = "Slot";
	public static final String NBT_PROIRITY_SLOT = "PrioritySlot";
	
	protected int ammoSlots = Defaults.SlotsQuiverSmall;

	public QuiverBaseItem(int inventorySize)
	{
		super(new Item.Properties().stacksTo(1));
		
		if(FMLEnvironment.dist.isClient())
			ClientHelper.registerQuiverPropertyOverrides(this);
		
		ammoSlots = inventorySize;
	}

	public int getAmmoCount(ItemStack stack)
	{
		int ammo = 0;
		ListTag list = null;
		
		list = ItemStackDataHelper.getTag(stack).getCompound(NBT_AMMO).getList("Items", Tag.TAG_COMPOUND);
		if(list == null)	return 0;
		
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack item = ItemStack.parseOptional(getRegistryAccess(), list.getCompound(i));
			if(!item.isEmpty())
				ammo++;
		}
		
		// Have 6 separate states for the Heavy Arrow Quiver, instead of 4
		if(ammoSlots >= Defaults.SlotsQuiverLarge)
			ammo = Mth.clamp(ammo, 0, 5);
		else
			ammo = Mth.clamp(ammo, 0, 3);
		
		return ammo;
	}

	public int getAmmoSlots()
	{
		return ammoSlots;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) 
	{
		ItemStack heldItem = playerIn.getItemInHand(handIn);
		
		IQuiverItemHandler handler = heldItem.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
		if(handler == null)
			return InteractionResultHolder.fail(heldItem);
		// Check current size of Quiver and correct it if needed
		int size = ItemStackDataHelper.getOrCreateTagElement(heldItem, NBT_AMMO).getInt("Size");
		if(size != ammoSlots)
			handler.resize(ammoSlots);
			
		if(!levelIn.isClientSide)
		{
			if(!playerIn.isCrouching())
			{
				SlotType slotType = handIn == InteractionHand.OFF_HAND ? SlotType.OFF_HAND : SlotType.MAIN_HAND;
				openGui(heldItem, playerIn, slotType, -1);
				return InteractionResultHolder.consume(heldItem);
			}
			else
			{
				// Toggle ammo collection
				boolean ammoCollect = !ItemStackDataHelper.getTag(heldItem).getBoolean(NBT_AMMO_COLLECT);
				ItemStackDataHelper.updateTag(heldItem, tag -> tag.putBoolean(NBT_AMMO_COLLECT, ammoCollect));
				
				String collectStatus = ammoCollect ? "enabled" : "disabled";
				ChatFormatting collectColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
				playerIn.displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".ammo_collect_toggle", Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + collectStatus).withStyle(collectColour)), true);
				return InteractionResultHolder.fail(heldItem);
			}
		}
        return InteractionResultHolder.pass(heldItem);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) 
	{
		if(ItemStackDataHelper.getTag(stack).contains("ClientInventory"))
			ItemStackDataHelper.updateTag(stack, tag -> tag.remove("ClientInventory"));
		
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
		
		boolean ammoCollect = ItemStackDataHelper.getTag(stack).getBoolean(NBT_AMMO_COLLECT);
		String collectStatus = ammoCollect ? "enabled" : "disabled";
		ChatFormatting statusColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".quiver_collect_status").append(Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + collectStatus).withStyle(statusColour)).withStyle(ChatFormatting.DARK_AQUA));
		
		if(ammoSlots != Defaults.SlotsQuiverHuge)
			tooltip.add(Component.translatable("tooltip."+ ModSpartanWeaponry.ID + ".quiver_upgrade").withStyle(ChatFormatting.YELLOW));
	}
	
	public Optional<TooltipComponent> makeTooltipImage(ItemStack stackIn, boolean isBoltQuiver)
	{
		ListTag list = ItemStackDataHelper.getTag(stackIn).getCompound(NBT_AMMO).getList("Items", Tag.TAG_COMPOUND);
		int prioritySlot = ItemStackDataHelper.getTag(stackIn).getInt(NBT_PROIRITY_SLOT);
		
		NonNullList<ItemStack> items = NonNullList.withSize(ammoSlots, ItemStack.EMPTY);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag tag = list.getCompound(i);
			ItemStack slotStack = ItemStack.parseOptional(getRegistryAccess(), tag);
			int slot = tag.getInt(NBT_ITEM_SLOT);
			items.set(slot, slotStack);
		}
		return Optional.of(new QuiverTooltip(items, prioritySlot, isBoltQuiver));
	}

	private static RegistryAccess getRegistryAccess()
	{
		return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	}
	
	@Override
	public abstract Optional<TooltipComponent> getTooltipImage(ItemStack stackIn);
	
	public abstract void openGui(ItemStack stack, Player player, SlotType slotType, int slot);
	
	public abstract boolean isAmmoValid(ItemStack pickedUpStack, ItemStack quiver);
	
}
