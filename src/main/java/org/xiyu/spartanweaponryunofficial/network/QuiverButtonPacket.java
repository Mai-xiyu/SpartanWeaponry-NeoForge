package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBaseMenu;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public record QuiverButtonPacket(boolean state) implements CustomPacketPayload {
    public static final Type<QuiverButtonPacket> TYPE = new Type<>(Identifier.tryBuild(ModSpartanWeaponry.ID, "quiver_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverButtonPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            QuiverButtonPacket::state,
            QuiverButtonPacket::new
    );

    @Override
    public @NotNull Type<QuiverButtonPacket> type() {
        return TYPE;
    }

    public static void handle(final QuiverButtonPacket packet, IPayloadContext context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            AbstractContainerMenu menu = context.player().containerMenu;
            if (menu instanceof QuiverBaseMenu quiverMenu) {
                ItemStackDataHelper.updateTag(quiverMenu.getQuiverStack(), tag -> tag.putBoolean(QuiverBaseItem.NBT_AMMO_COLLECT, packet.state));
            }
        });
    }
}