package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;

import java.util.List;
import java.util.Optional;

public class QuiverArrowItem extends QuiverBaseItem {
    public static final ResourceLocation TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_small.png");
    public static final ResourceLocation TEXTURE_MEDIUM = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_medium.png");
    public static final ResourceLocation TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_large.png");
    public static final ResourceLocation TEXTURE_HUGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_huge.png");

    public QuiverArrowItem(int inventorySize) {
        super(inventorySize);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.ammo.type",
                Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.ammo.arrow").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }

    @Override
    public void openGui(ItemStack stack, Player player, SlotType slotType, int slot) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return;

        serverPlayer.openMenu(new ContainerProvider(Component.translatable("gui." + ModSpartanWeaponry.ID + ".quiver_arrow.title"), stack), buf ->
        {
            buf.writeEnum(slotType);
            buf.writeInt(slot);
        });
    }

    protected static class ContainerProvider implements MenuProvider {
        private final Component displayName;
        private final ItemStack quiverStack;

        protected ContainerProvider(Component displayName, ItemStack quiverStack) {
            this.displayName = displayName;
            this.quiverStack = quiverStack;
        }

        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory,
                                                @NotNull Player player) {
            return new QuiverArrowMenu(id, inventory, this.quiverStack);
        }

        @Override
        public @NotNull Component getDisplayName() {
            return this.displayName;
        }
    }

    @Override
    public boolean isAmmoValid(ItemStack pickedUpStack, ItemStack quiver) {
        return pickedUpStack.is(ItemTags.ARROWS);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stackIn) {
        return this.makeTooltipImage(stackIn, false);
    }
}
