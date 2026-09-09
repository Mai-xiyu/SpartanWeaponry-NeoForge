package org.xiyu.spartanweaponryunofficial.inventory;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.capability.QuiverItemStackHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public abstract class QuiverBaseMenu extends AbstractContainerMenu {
    protected final ItemStack quiverStack;

    protected final IQuiverItemHandler handler;
    protected final Predicate<ItemStack> slotFilter;
    protected final ResourceLocation emptySlotTexture;

    protected int playerInvStart, playerInvEnd, hotbarStart, hotbarEnd;

    protected QuiverBaseMenu(
            MenuType<?> type,
            int id,
            Inventory inventory,
            ItemStack quiverStackIn,
            Predicate<ItemStack> slotFilterIn,
            ResourceLocation emptySlotTextureIn) {
        super(type, id);
        this.slotFilter = slotFilterIn;
        this.quiverStack = quiverStackIn;
        this.emptySlotTexture = emptySlotTextureIn;
        IQuiverItemHandler handlerTmp =
                this.quiverStack.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
        if (handlerTmp == null && this.quiverStack.getItem() instanceof QuiverBaseItem quiverItem)
            handlerTmp = new QuiverItemStackHandler(this.quiverStack, quiverItem.getAmmoSlots());

        if (handlerTmp != null && this.quiverStack.getItem() instanceof QuiverBaseItem quiverItem) {
            int currentSize =
                    ItemStackDataHelper.getOrCreateTagElement(
                                    this.quiverStack, QuiverBaseItem.NBT_AMMO)
                            .getInt("Size");
            if (currentSize != quiverItem.getAmmoSlots())
                handlerTmp.resize(quiverItem.getAmmoSlots());
        }
        this.handler = handlerTmp;

        this.playerInvStart = this.handler.getSlots();
        this.playerInvEnd = this.playerInvStart + 26;
        this.hotbarStart = this.playerInvEnd + 1;
        this.hotbarEnd = this.hotbarStart + 8;

        this.addQuiverSlots();
        this.addPlayerSlots(inventory);
    }

    protected void addQuiverSlots() {
        // Default starting slot positions for the Small Quiver
        int slotStartX = 53, slotStartY = 20;

        int columns = 1; // Used to determine when to place a slot in a new line

        slotStartX =
                switch (this.handler.getSlots()) {
                    case Defaults.SlotsQuiverSmall -> {
                        columns = 4;
                        yield 53;
                    }
                    case Defaults.SlotsQuiverMedium, Defaults.SlotsQuiverHuge -> {
                        columns = 6;
                        yield 35;
                    }
                    case Defaults.SlotsQuiverLarge -> {
                        columns = 9;
                        yield 8;
                    }
                    default -> slotStartX;
                };

        // Quiver inventory
        for (int i = 0; i < this.handler.getSlots(); i++) {
            this.addSlot(
                    new SlotFiltered(
                                    this.handler,
                                    i,
                                    slotStartX + (18 * (i % columns)),
                                    slotStartY + (i / columns * 18),
                                    this.slotFilter)
                            .setBackground(InventoryMenu.BLOCK_ATLAS, this.emptySlotTexture));
            // 52, 19
        }
    }

    protected void addPlayerSlots(Inventory inventory) {
        int yOffset = this.handler.getSlots() == Defaults.SlotsQuiverHuge ? 18 : 0;

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(
                        new Slot(
                                inventory, 9 + (i * 9) + j, 8 + (j * 18), 51 + yOffset + (i * 18)));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + (i * 18), 109 + yOffset));
        }

        // Offhand slot
        this.addSlot(
                new Slot(
                                inventory,
                                40,
                                -21,
                                this.handler.getSlots() == Defaults.SlotsQuiverHuge ? 127 : 109)
                        .setBackground(
                                InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIdx) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIdx);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            // Take arrows out of the quiver,
            if (slotIdx >= 0 && slotIdx < this.handler.getSlots()) {
                // Prioritise the hotbar next, then the main inventory
                if (!this.moveItemStackTo(
                        slotStack, this.playerInvStart, this.hotbarEnd + 2, false) /*&&
                        !this.mergeItemStack(slotStack, playerInvStart, playerInvEnd + 1, false)*/)
                    return ItemStack.EMPTY;
            }
            // Attempt to place arrows into the quiver
            else if (slotIdx >= this.playerInvStart
                    && slotIdx <= this.hotbarEnd + 1
                    && slot.mayPlace(stack)) {
                if (!this.moveItemStackTo(slotStack, 0, this.playerInvStart, false))
                    return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (slotStack.getCount() == stack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, slotStack);
        }

        return stack;
    }

    @Override
    public void clicked(
            int slot, int dragType, @NotNull ClickType clickType, @NotNull Player player) {
        if (slot >= 0) {
            this.getSlot(slot);
            if (ItemStack.isSameItemSameComponents(this.getSlot(slot).getItem(), this.quiverStack))
                return;
        }
        super.clicked(slot, dragType, clickType, player);
    }

    public ItemStack getQuiverStack() {
        return this.quiverStack;
    }

    /** Returns the number of ammo slots provided by the open quiver. */
    public int getQuiverSlotCount() {
        return this.handler.getSlots();
    }

    protected static ItemStack findQuiverStack(
            Inventory inventory, QuiverBaseItem.SlotType slotType, int slot) {
        ItemStack quiverStack = ItemStack.EMPTY;
        switch (slotType) {
            case HOTBAR:
                quiverStack = inventory.getItem(slot);
                break;
            case CURIO:
                Optional<SlotResult> opt =
                        CuriosApi.getCuriosInventory(inventory.player)
                                .flatMap(
                                        handler ->
                                                handler.findFirstCurio(
                                                        (stack) ->
                                                                stack.getItem()
                                                                        instanceof QuiverBaseItem));
                if (opt.isPresent()) {
                    quiverStack = opt.get().stack();
                }
                break;
            case MAIN_HAND:
                quiverStack = inventory.player.getMainHandItem();
                break;
            case OFF_HAND:
                quiverStack = inventory.player.getOffhandItem();
                break;
            default:
                break;
        }
        return quiverStack;
    }
}
