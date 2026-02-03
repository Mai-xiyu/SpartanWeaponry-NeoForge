package org.xiyu.spartanweaponryunofficial.inventory;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModMenus;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

public class QuiverBoltMenu extends QuiverBaseMenu {
    public static final ResourceLocation EMPTY_BOLT_SLOT = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "slots/empty_bolt_slot");

    public QuiverBoltMenu(int id, Inventory inventory, ItemStack quiverStack) {
        super(ModMenus.QUIVER_BOLT.get(), id, inventory, quiverStack, HeavyCrossbowItem.BOLT, EMPTY_BOLT_SLOT);
    }

    public static QuiverBoltMenu createFromNetwork(int id, Inventory inventory, RegistryFriendlyByteBuf buf) {
        QuiverBaseItem.SlotType slotType = buf.readEnum(QuiverBaseItem.SlotType.class);
        int slot = buf.readInt();

        ItemStack quiverStack = findQuiverStack(inventory, slotType, slot);
        return new QuiverBoltMenu(id, inventory, quiverStack);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return true;
    }

}
