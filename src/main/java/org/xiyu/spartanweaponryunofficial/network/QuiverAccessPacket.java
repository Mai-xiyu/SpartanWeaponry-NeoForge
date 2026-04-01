package org.xiyu.spartanweaponryunofficial.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
// TODO: Curios API not available for 26.1 yet
// import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;
// TODO: Curios API not available for 26.1 yet
// import top.theillusivec4.curios.api.SlotResult;

// import java.util.Optional;

public record QuiverAccessPacket() implements CustomPacketPayload {
    public static final Type<QuiverAccessPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_access"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverAccessPacket> STREAM_CODEC = StreamCodec.unit(new QuiverAccessPacket());

    @Override
    public @NotNull Type<QuiverAccessPacket> type() {
        return TYPE;
    }

    public static void handle(final QuiverAccessPacket packet, IPayloadContext context) {
        if (packet == null)
            return;

        context.enqueueWork(() ->
        {
            ServerPlayer player = (ServerPlayer) context.player();

            ItemStack quiver = ItemStack.EMPTY;
            QuiverBaseItem quiverItem = null;
            QuiverBaseItem.SlotType slotType = QuiverBaseItem.SlotType.UNDEFINED;
            int slot = -1;

            // TODO: Merge Quiver searching functionality to helper methods
            // Look in the weapon slot to find the appropriate quiver type to look for first.
            for (IQuiverInfo info : QuiverHelper.info) {
                if (info.isWeapon(player.getMainHandItem())) {
                    // Find a quiver, if possible.
                    // TODO: Curios slot support disabled for 26.1
                    /*
                    if (quiver.isEmpty() && CuriosHelper.LOADED) {
                        Optional<SlotResult> opt = QuiverHelper.getQuiverCurio(player);
                        if (opt.isPresent() && info.isQuiver(opt.get().stack())) {
                            quiver = opt.get().stack();
                            quiverItem = (QuiverBaseItem) quiver.getItem();
                            slotType = QuiverBaseItem.SlotType.CURIO;
                            break;
                        }
                    }
                    */
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
            // TODO: Curios slot support disabled for 26.1
            /*
            if (quiver.isEmpty() && CuriosHelper.LOADED) {
                Optional<SlotResult> opt = QuiverHelper.getQuiverCurio(player);
                if (opt.isPresent()) {
                    quiver = opt.get().stack();
                    quiverItem = (QuiverBaseItem) quiver.getItem();
                    slotType = QuiverBaseItem.SlotType.CURIO;
                }
            }
            */
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
                player.sendOverlayMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".quiver_not_found").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                return;
            }

            quiverItem.openGui(quiver, player, slotType, slot);
        });
    }
}
