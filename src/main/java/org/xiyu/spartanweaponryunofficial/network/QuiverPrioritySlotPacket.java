package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBaseMenu;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;

public class QuiverPrioritySlotPacket {
    private final int prioritySlot;

    public QuiverPrioritySlotPacket(int prioritySlot) {
        this.prioritySlot = prioritySlot;
    }

    public int prioritySlot() {
        return prioritySlot;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(prioritySlot);
    }

    public static QuiverPrioritySlotPacket decode(FriendlyByteBuf buf) {
        return new QuiverPrioritySlotPacket(buf.readVarInt());
    }

    public static void handle(QuiverPrioritySlotPacket packet, CustomPayloadEvent.Context context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof QuiverBaseMenu quiverMenu) {
                ItemStack quiver = quiverMenu.getQuiverStack();
                ItemStackDataHelper.updateTag(quiver, tag -> tag.putInt(QuiverBaseItem.NBT_PROIRITY_SLOT, packet.prioritySlot));

                // Do nothing if the priority slot is empty
                Slot slot = quiverMenu.getSlot(packet.prioritySlot);
                if (!slot.hasItem())
                    return;

                for (IQuiverInfo quiverInfo : QuiverHelper.info) {
                    if (quiverInfo.isQuiver(quiver)) {
                        InteractionHand ammoHand = quiverInfo.isWeapon(player.getMainHandItem()) ? InteractionHand.OFF_HAND : quiverInfo.isWeapon(player.getOffhandItem()) ? InteractionHand.MAIN_HAND : null;

                        if (ammoHand != null) {
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
        context.setPacketHandled(true);
    }
}
