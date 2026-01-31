package com.oblivioussp.spartanweaponry.network;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.inventory.QuiverBaseMenu;
import com.oblivioussp.spartanweaponry.item.QuiverBaseItem;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record QuiverButtonPacket(boolean state) implements CustomPacketPayload
{
	public static final Type<QuiverButtonPacket> TYPE = new Type<>(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "quiver_button"));
	public static final StreamCodec<RegistryFriendlyByteBuf, QuiverButtonPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			QuiverButtonPacket::state,
			QuiverButtonPacket::new
	);

	@Override
	public Type<QuiverButtonPacket> type()
	{
		return TYPE;
	}

	public static void handle(final QuiverButtonPacket packet, IPayloadContext context)
	{
		if(packet == null)
			return;
		
		context.enqueueWork(() ->
		{
			AbstractContainerMenu menu = context.player().containerMenu;
			if(menu instanceof QuiverBaseMenu quiverMenu)
			{
				ItemStackDataHelper.updateTag(quiverMenu.getQuiverStack(), tag -> tag.putBoolean(QuiverBaseItem.NBT_AMMO_COLLECT, packet.state));
			}
		});
	}
}