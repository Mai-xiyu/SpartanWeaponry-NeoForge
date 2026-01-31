package com.oblivioussp.spartanweaponry.network;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.inventory.QuiverBaseMenu;
import com.oblivioussp.spartanweaponry.item.QuiverBaseItem;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;
import com.oblivioussp.spartanweaponry.util.QuiverHelper;
import com.oblivioussp.spartanweaponry.util.QuiverHelper.IQuiverInfo;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record QuiverPrioritySlotPacket(int prioritySlot) implements CustomPacketPayload
{
	public static final Type<QuiverPrioritySlotPacket> TYPE = new Type<>(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "quiver_priority_slot"));
	public static final StreamCodec<RegistryFriendlyByteBuf, QuiverPrioritySlotPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			QuiverPrioritySlotPacket::prioritySlot,
			QuiverPrioritySlotPacket::new
	);

	@Override
	public Type<QuiverPrioritySlotPacket> type()
	{
		return TYPE;
	}

	public static void handle(final QuiverPrioritySlotPacket packet, IPayloadContext context)
	{
		if(packet == null)
			return;
		
		context.enqueueWork(() ->
		{
			AbstractContainerMenu menu = context.player().containerMenu;
			if(menu instanceof QuiverBaseMenu quiverMenu)
			{
				ItemStack quiver = quiverMenu.getQuiverStack();
				ItemStackDataHelper.updateTag(quiver, tag -> tag.putInt(QuiverBaseItem.NBT_PROIRITY_SLOT, packet.prioritySlot));
				
				// Do nothing if the priority slot is empty
				Slot slot = quiverMenu.getSlot(packet.prioritySlot);
				if(!slot.hasItem())
					return;
				
				for(IQuiverInfo quiverInfo : QuiverHelper.info)
				{
					Player player = context.player();
					if(quiverInfo.isQuiver(quiver))
					{
						InteractionHand ammoHand = quiverInfo.isWeapon(player.getMainHandItem()) ? InteractionHand.OFF_HAND : quiverInfo.isWeapon(player.getOffhandItem()) ? InteractionHand.MAIN_HAND : null;
						
						if(ammoHand != null)
						{
							ItemStack priorityStack = slot.getItem();
							ItemStack ammoStack = player.getItemInHand(ammoHand);
							
							// Swap out priority stack with ammo stack
							player.setItemInHand(ammoHand, priorityStack);
							slot.set(ammoStack);
						}
					}
				}
			}
		});
	}
}
