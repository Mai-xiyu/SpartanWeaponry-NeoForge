package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBaseMenu;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

public class QuiverButtonPacket {
    private final boolean state;

    public QuiverButtonPacket(boolean state) {
        this.state = state;
    }

    public boolean state() {
        return state;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(state);
    }

    public static QuiverButtonPacket decode(FriendlyByteBuf buf) {
        return new QuiverButtonPacket(buf.readBoolean());
    }

    public static void handle(QuiverButtonPacket packet, CustomPayloadEvent.Context context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof QuiverBaseMenu quiverMenu) {
                ItemStackDataHelper.updateTag(quiverMenu.getQuiverStack(), tag -> tag.putBoolean(QuiverBaseItem.NBT_AMMO_COLLECT, packet.state));
            }
        });
        context.setPacketHandled(true);
    }
}