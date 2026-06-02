package org.xiyu.spartanweaponryunofficial.inventory;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModMenus;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

public class QuiverArrowMenu extends QuiverBaseMenu {
    public static final ResourceLocation EMPTY_ARROW_SLOT =
            ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "slots/empty_arrow_slot");

    public QuiverArrowMenu(int id, Inventory inventory, ItemStack quiverStack) {
        super(
                ModMenus.QUIVER_ARROW.get(),
                id,
                inventory,
                quiverStack,
                BowItem.ARROW_ONLY,
                EMPTY_ARROW_SLOT);
    }

    public static QuiverArrowMenu createFromNetwork(
            int id, Inventory inventory, RegistryFriendlyByteBuf buf) {
        QuiverBaseItem.SlotType slotType = buf.readEnum(QuiverBaseItem.SlotType.class);
        int slot = buf.readInt();

        ItemStack quiverStack = findQuiverStack(inventory, slotType, slot);
        return new QuiverArrowMenu(id, inventory, quiverStack);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return true;
    }
}
