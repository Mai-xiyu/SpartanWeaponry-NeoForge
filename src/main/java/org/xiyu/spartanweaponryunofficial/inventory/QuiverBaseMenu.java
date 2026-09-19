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
import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.capability.QuiverItemStackHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public abstract class QuiverBaseMenu extends AbstractContainerMenu {
    protected final ItemStack quiverStack;
    protected final QuiverBaseItem.SlotType sourceType;
    protected final int sourceSlot;
    protected final String sourceCurioIdentifier;
    protected final int sourceCurioIndex;

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
        this(
                type,
                id,
                inventory,
                quiverStackIn,
                slotFilterIn,
                emptySlotTextureIn,
                QuiverBaseItem.SlotType.UNDEFINED,
                -1,
                null,
                -1);
    }

    protected QuiverBaseMenu(
            MenuType<?> type,
            int id,
            Inventory inventory,
            ItemStack quiverStackIn,
            Predicate<ItemStack> slotFilterIn,
            ResourceLocation emptySlotTextureIn,
            QuiverBaseItem.SlotType sourceTypeIn,
            int sourceSlotIn) {
        this(
                type,
                id,
                inventory,
                quiverStackIn,
                slotFilterIn,
                emptySlotTextureIn,
                sourceTypeIn,
                sourceSlotIn,
                null,
                -1);
    }

    protected QuiverBaseMenu(
            MenuType<?> type,
            int id,
            Inventory inventory,
            ItemStack quiverStackIn,
            Predicate<ItemStack> slotFilterIn,
            ResourceLocation emptySlotTextureIn,
            QuiverBaseItem.SlotType sourceTypeIn,
            int sourceSlotIn,
            String sourceCurioIdentifierIn,
            int sourceCurioIndexIn) {
        super(type, id);
        this.slotFilter = slotFilterIn;
        this.quiverStack = quiverStackIn == null ? ItemStack.EMPTY : quiverStackIn;
        this.sourceType =
                sourceTypeIn == null ? QuiverBaseItem.SlotType.UNDEFINED : sourceTypeIn;
        this.sourceSlot = sourceSlotIn;
        this.sourceCurioIdentifier = sourceCurioIdentifierIn;
        this.sourceCurioIndex = sourceCurioIndexIn;
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
        if (handlerTmp == null) handlerTmp = new QuiverItemStackHandler(ItemStack.EMPTY, 0);
        this.handler = handlerTmp;

        this.playerInvStart = this.handler.getSlots();
        this.playerInvEnd = this.playerInvStart + 26;
        this.hotbarStart = this.playerInvEnd + 1;
        this.hotbarEnd = this.hotbarStart + 8;

        this.addQuiverSlots();
        this.addPlayerSlots(inventory);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (!(this.quiverStack.getItem() instanceof QuiverBaseItem)) return false;

        if (this.sourceType == QuiverBaseItem.SlotType.UNDEFINED) {
            if (isQuiverStack(player.getMainHandItem())
                    || isQuiverStack(player.getOffhandItem())) return true;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (isQuiverStack(player.getInventory().getItem(i))) return true;
            }
            if (CuriosHelper.LOADED) {
                Optional<SlotResult> opt =
                        CuriosApi.getCuriosInventory(player)
                                .flatMap(
                                        handler ->
                                                handler.findFirstCurio(
                                                        stack -> isQuiverStack(stack)));
                if (opt.isPresent()) return true;
            }
            return false;
        }

        ItemStack currentStack =
                switch (this.sourceType) {
                    case HOTBAR ->
                            this.sourceSlot >= 0
                                            && this.sourceSlot
                                                    < 9
                                    ? player.getInventory().getItem(this.sourceSlot)
                                    : ItemStack.EMPTY;
                    case MAIN_HAND -> player.getMainHandItem();
                    case OFF_HAND -> player.getOffhandItem();
                    case CURIO ->
                            CuriosHelper.LOADED
                                    ? CuriosApi.getCuriosInventory(player)
                                            .flatMap(
                                                    handler ->
                                                            this.sourceCurioIdentifier != null
                                                                            && this.sourceCurioIndex
                                                                                    >= 0
                                                                    ? handler.findCurio(
                                                                            this.sourceCurioIdentifier,
                                                                            this.sourceCurioIndex)
                                                                    : handler.findFirstCurio(
                                                                            this::isQuiverStack))
                                            .map(SlotResult::stack)
                                            .filter(this::isQuiverStack)
                                            .orElse(ItemStack.EMPTY)
                                    : ItemStack.EMPTY;
                    case UNDEFINED -> ItemStack.EMPTY;
                };
        return isQuiverStack(currentStack);
    }

    private boolean isQuiverStack(ItemStack candidate) {
        return candidate == this.quiverStack;
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
        if (slotIdx < 0 || slotIdx >= this.slots.size()) return ItemStack.EMPTY;

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
        if (slot >= this.slots.size()) return;
        if (slot >= 0) {
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
        return findQuiverStack(
                inventory,
                slotType,
                slot,
                null,
                -1,
                candidate -> candidate.getItem() instanceof QuiverBaseItem);
    }

    protected static ItemStack findQuiverStack(
            Inventory inventory,
            QuiverBaseItem.SlotType slotType,
            int slot,
            String curioIdentifier,
            int curioIndex,
            Predicate<ItemStack> quiverFilter) {
        ItemStack quiverStack = ItemStack.EMPTY;
        switch (slotType) {
            case HOTBAR:
                if (slot >= 0 && slot < 9)
                    quiverStack = inventory.getItem(slot);
                break;
            case CURIO:
                if (CuriosHelper.LOADED) {
                    Optional<SlotResult> opt =
                            CuriosApi.getCuriosInventory(inventory.player)
                                    .flatMap(
                                            handler ->
                                                    curioIdentifier != null && curioIndex >= 0
                                                            ? handler.findCurio(
                                                                    curioIdentifier, curioIndex)
                                                            : handler.findFirstCurio(quiverFilter));
                    if (opt.isPresent()) {
                        quiverStack = opt.get().stack();
                    }
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
        return !quiverStack.isEmpty() && quiverFilter.test(quiverStack)
                ? quiverStack
                : ItemStack.EMPTY;
    }
}
