package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBaseMenu;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;

public record QuiverPrioritySlotPacket(int prioritySlot) implements CustomPacketPayload {
    public static final Type<QuiverPrioritySlotPacket> TYPE = new Type<>(Identifier.tryBuild(ModSpartanWeaponry.ID, "quiver_priority_slot"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverPrioritySlotPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            QuiverPrioritySlotPacket::prioritySlot,
            QuiverPrioritySlotPacket::new
    );

    @Override
    public @NotNull Type<QuiverPrioritySlotPacket> type() {
        return TYPE;
    }

    public static void handle(final QuiverPrioritySlotPacket packet, IPayloadContext context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            AbstractContainerMenu menu = context.player().containerMenu;
            if (menu instanceof QuiverBaseMenu quiverMenu) {
                ItemStack quiver = quiverMenu.getQuiverStack();
                ItemStackDataHelper.updateTag(quiver, tag -> tag.putInt(QuiverBaseItem.NBT_PROIRITY_SLOT, packet.prioritySlot));

                // Do nothing if the priority slot is empty
                Slot slot = quiverMenu.getSlot(packet.prioritySlot);
                if (!slot.hasItem())
                    return;

                for (IQuiverInfo quiverInfo : QuiverHelper.info) {
                    Player player = context.player();
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
    }
}
