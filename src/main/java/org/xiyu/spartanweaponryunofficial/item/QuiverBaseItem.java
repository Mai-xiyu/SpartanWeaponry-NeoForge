package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.client.ClientHelper;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.QuiverTooltip;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class QuiverBaseItem extends Item {
    public enum SlotType {
        UNDEFINED,
        MAIN_HAND,
        OFF_HAND,
        HOTBAR,
        CURIO
    }

    public static final String NBT_AMMO_COLLECT = "AmmoCollect";
    public static final String NBT_AMMO = "Ammo";
    public static final String NBT_OFFHAND_MOVED = "OffhandMoved";
    public static final String NBT_ITEM_ID = "Id";
    public static final String NBT_ITEM_SLOT = "Slot";
    public static final String NBT_PROIRITY_SLOT = "PrioritySlot";

    protected int ammoSlots = Defaults.SlotsQuiverSmall;

    public QuiverBaseItem(Item.Properties properties, int inventorySize) {
        super(properties);

        if (FMLEnvironment.getDist().isClient()) {}
            // TODO: ItemProperties removed in 26.1
            // ClientHelper.registerQuiverPropertyOverrides(this);

        this.ammoSlots = inventorySize;
    }

    public int getAmmoCount(ItemStack stack) {
        int ammo = 0;
        ListTag list;

        list = ItemStackDataHelper.getTag(stack).getCompoundOrEmpty(NBT_AMMO).getListOrEmpty("Items");

        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, getRegistryAccess());
        for (int i = 0; i < list.size(); i++) {
            ItemStack item = ItemStack.OPTIONAL_CODEC.parse(ops, list.getCompoundOrEmpty(i)).result().orElse(ItemStack.EMPTY);
            if (!item.isEmpty())
                ammo++;
        }

        // Have 6 separate states for the Heavy Arrow Quiver, instead of 4
        if (this.ammoSlots >= Defaults.SlotsQuiverLarge)
            ammo = Mth.clamp(ammo, 0, 5);
        else
            ammo = Mth.clamp(ammo, 0, 3);

        return ammo;
    }

    public int getAmmoSlots() {
        return this.ammoSlots;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level levelIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        IQuiverItemHandler handler = heldItem.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
        if (handler == null)
            return InteractionResult.FAIL;
        // Check current size of Quiver and correct it if needed
        int size = ItemStackDataHelper.getOrCreateTagElement(heldItem, NBT_AMMO).getIntOr("Size", 0);
        if (size != this.ammoSlots)
            handler.resize(this.ammoSlots);

        if (!levelIn.isClientSide()) {
            if (!playerIn.isCrouching()) {
                SlotType slotType = handIn == InteractionHand.OFF_HAND ? SlotType.OFF_HAND : SlotType.MAIN_HAND;
                this.openGui(heldItem, playerIn, slotType, -1);
                return InteractionResult.CONSUME;
            } else {
                // Toggle ammo collection
                boolean ammoCollect = !ItemStackDataHelper.getTag(heldItem).getBooleanOr(NBT_AMMO_COLLECT, false);
                ItemStackDataHelper.updateTag(heldItem, tag -> tag.putBoolean(NBT_AMMO_COLLECT, ammoCollect));

                String collectStatus = ammoCollect ? "enabled" : "disabled";
                ChatFormatting collectColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
                playerIn.sendOverlayMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".ammo_collect_toggle", Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + collectStatus).withStyle(collectColour)));
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (ItemStackDataHelper.getTag(stack).contains("ClientInventory"))
            ItemStackDataHelper.updateTag(stack, tag -> tag.remove("ClientInventory"));

        super.appendHoverText(stack, tooltipContext, tooltipDisplay, tooltip, flagIn);

        boolean ammoCollect = ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_AMMO_COLLECT, false);
        String collectStatus = ammoCollect ? "enabled" : "disabled";
        ChatFormatting statusColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
        tooltip.accept(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".quiver_collect_status").append(Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + collectStatus).withStyle(statusColour)).withStyle(ChatFormatting.DARK_AQUA));

        if (this.ammoSlots != Defaults.SlotsQuiverHuge)
            tooltip.accept(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".quiver_upgrade").withStyle(ChatFormatting.YELLOW));
    }

    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (ItemStackDataHelper.getTag(stack).contains("ClientInventory"))
            ItemStackDataHelper.updateTag(stack, tag -> tag.remove("ClientInventory"));

        boolean ammoCollect = ItemStackDataHelper.getTag(stack).getBooleanOr(NBT_AMMO_COLLECT, false);
        String collectStatus = ammoCollect ? "enabled" : "disabled";
        ChatFormatting statusColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".quiver_collect_status").append(Component.translatable("tooltip." + ModSpartanWeaponry.ID + "." + collectStatus).withStyle(statusColour)).withStyle(ChatFormatting.DARK_AQUA));

        if (this.ammoSlots != Defaults.SlotsQuiverHuge)
            tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".quiver_upgrade").withStyle(ChatFormatting.YELLOW));
    }

    public Optional<TooltipComponent> makeTooltipImage(ItemStack stackIn, boolean isBoltQuiver) {
        ListTag list = ItemStackDataHelper.getTag(stackIn).getCompoundOrEmpty(NBT_AMMO).getListOrEmpty("Items");
        int prioritySlot = ItemStackDataHelper.getTag(stackIn).getIntOr(NBT_PROIRITY_SLOT, 0);

        NonNullList<ItemStack> items = NonNullList.withSize(this.ammoSlots, ItemStack.EMPTY);
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, getRegistryAccess());
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompoundOrEmpty(i);
            ItemStack slotStack = ItemStack.OPTIONAL_CODEC.parse(ops, tag).result().orElse(ItemStack.EMPTY);
            int slot = tag.getIntOr(NBT_ITEM_SLOT, 0);
            items.set(slot, slotStack);
        }
        return Optional.of(new QuiverTooltip(items, prioritySlot, isBoltQuiver));
    }

    private static RegistryAccess getRegistryAccess() {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }

    @Override
    public abstract @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stackIn);

    public abstract void openGui(ItemStack stack, Player player, SlotType slotType, int slot);

    public abstract boolean isAmmoValid(ItemStack pickedUpStack, ItemStack quiver);

}
