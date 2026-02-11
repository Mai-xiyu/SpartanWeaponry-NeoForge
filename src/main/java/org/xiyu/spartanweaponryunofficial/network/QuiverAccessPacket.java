package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;

public class QuiverAccessPacket {

    public QuiverAccessPacket() {
    }

    public void encode(FriendlyByteBuf buf) {
        // No data to encode
    }

    public static QuiverAccessPacket decode(FriendlyByteBuf buf) {
        return new QuiverAccessPacket();
    }

    public static void handle(QuiverAccessPacket packet, CustomPayloadEvent.Context context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            ItemStack quiver = ItemStack.EMPTY;
            QuiverBaseItem quiverItem = null;
            QuiverBaseItem.SlotType slotType = QuiverBaseItem.SlotType.UNDEFINED;
            int slot = -1;

            // TODO: Merge Quiver searching functionality to helper methods
            // Look in the weapon slot to find the appropriate quiver type to look for first.
            for (IQuiverInfo info : QuiverHelper.info) {
                if (info.isWeapon(player.getMainHandItem())) {
                    // Find a quiver, if possible.
                    // Via the Curios slots - disabled for Forge 1.21.1
                    // ... or via the hotbar
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (!stack.isEmpty() && info.isQuiver(stack)) {
                            quiver = stack;
                            quiverItem = (QuiverBaseItem) quiver.getItem();
                            slotType = QuiverBaseItem.SlotType.HOTBAR;
                            slot = i;
                            break;
                        }
                    }
                    break;
                }
            }

            // Otherwise, Find a quiver, if possible.
            // Via the Curios slots - disabled for Forge 1.21.1
            if (quiver.isEmpty() || quiverItem == null) {
                // ... or via the hotbar
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && (stack.getItem() instanceof QuiverBaseItem)) {
                        quiver = stack;
                        quiverItem = (QuiverBaseItem) quiver.getItem();
                        slotType = QuiverBaseItem.SlotType.HOTBAR;
                        slot = i;
                        break;
                    }
                }
            }

            if (quiver.isEmpty() || quiverItem == null) {
                player.displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".quiver_not_found").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
                return;
            }

            quiverItem.openGui(quiver, player, slotType, slot);
        });
        context.setPacketHandled(true);
    }
}
