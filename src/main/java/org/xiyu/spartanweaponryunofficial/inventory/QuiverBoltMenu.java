package org.xiyu.spartanweaponryunofficial.inventory;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.init.ModMenus;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

public class QuiverBoltMenu extends QuiverBaseMenu {
    public static final ResourceLocation EMPTY_BOLT_SLOT =
            ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "slots/empty_bolt_slot");

    public QuiverBoltMenu(int id, Inventory inventory, ItemStack quiverStack) {
        this(id, inventory, quiverStack, QuiverBaseItem.SlotType.UNDEFINED, -1);
    }

    public QuiverBoltMenu(
            int id,
            Inventory inventory,
            ItemStack quiverStack,
            QuiverBaseItem.SlotType sourceType,
            int sourceSlot) {
        this(id, inventory, quiverStack, sourceType, sourceSlot, null, -1);
    }

    public QuiverBoltMenu(
            int id,
            Inventory inventory,
            ItemStack quiverStack,
            QuiverBaseItem.SlotType sourceType,
            int sourceSlot,
            String curioIdentifier,
            int curioIndex) {
        super(
                ModMenus.QUIVER_BOLT.get(),
                id,
                inventory,
                quiverStack,
                HeavyCrossbowItem.BOLT,
                EMPTY_BOLT_SLOT,
                sourceType,
                sourceSlot,
                curioIdentifier,
                curioIndex);
    }

    public static QuiverBoltMenu createFromNetwork(
            int id, Inventory inventory, RegistryFriendlyByteBuf buf) {
        QuiverBaseItem.SlotType slotType = buf.readEnum(QuiverBaseItem.SlotType.class);
        int slot = buf.readInt();
        String curioIdentifier = null;
        int curioIndex = -1;
        if (buf.readBoolean()) {
            curioIdentifier = buf.readUtf(64);
            curioIndex = buf.readVarInt();
        }

        ItemStack quiverStack =
                findQuiverStack(
                        inventory,
                        slotType,
                        slot,
                        curioIdentifier,
                        curioIndex,
                        stack -> stack.is(ModItemTags.BOLT_QUIVERS));
        return new QuiverBoltMenu(
                id,
                inventory,
                quiverStack,
                slotType,
                slot,
                curioIdentifier,
                curioIndex);
    }
}
