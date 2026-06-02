package org.xiyu.spartanweaponryunofficial.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.client.gui.components.ToggleImageButton;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBaseMenu;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.network.NetworkHandler;
import org.xiyu.spartanweaponryunofficial.network.QuiverButtonPacket;
import org.xiyu.spartanweaponryunofficial.network.QuiverPrioritySlotPacket;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.Log;

public class QuiverBaseScreen<T extends QuiverBaseMenu> extends AbstractContainerScreen<T> {
    protected final ResourceLocation GUI_TEXTURE_SMALL =
            ResourceLocation.fromNamespaceAndPath(
                    ModSpartanWeaponry.ID, "textures/gui/quiver_small.png");
    protected final ResourceLocation GUI_TEXTURE_MEDIUM =
            ResourceLocation.fromNamespaceAndPath(
                    ModSpartanWeaponry.ID, "textures/gui/quiver_medium.png");
    protected final ResourceLocation GUI_TEXTURE_LARGE =
            ResourceLocation.fromNamespaceAndPath(
                    ModSpartanWeaponry.ID, "textures/gui/quiver_large.png");
    protected final ResourceLocation GUI_TEXTURE_HUGE =
            ResourceLocation.fromNamespaceAndPath(
                    ModSpartanWeaponry.ID, "textures/gui/quiver_huge.png");

    protected final Component PRIORITY_BUTTON_TOOLTIP =
            Component.literal("[")
                    .append(
                            Component.translatable(
                                    "gui." + ModSpartanWeaponry.ID + ".set_priority_slot"))
                    .append(Component.literal("]"));
    protected final Component AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP =
            Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_enabled");
    protected final Component AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP =
            Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_disabled");

    protected final ResourceLocation texture;
    protected final ItemStack quiver;
    protected final int ammoSlots;
    protected int prioritySlot;
    protected boolean isAmmoCollectEnabled;

    public QuiverBaseScreen(T screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.quiver = screenContainer.getQuiverStack();
        this.prioritySlot =
                ItemStackDataHelper.getTag(this.quiver).getInt(QuiverBaseItem.NBT_PROIRITY_SLOT);
        this.isAmmoCollectEnabled =
                ItemStackDataHelper.getTag(this.quiver).getBoolean(QuiverBaseItem.NBT_AMMO_COLLECT);

        IQuiverItemHandler handler =
                this.quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
        this.ammoSlots = handler != null ? handler.getSlots() : Defaults.SlotsQuiverSmall;

        switch (this.ammoSlots) {
            case Defaults.SlotsQuiverHuge:
                this.texture = this.GUI_TEXTURE_HUGE;
                break;
            case Defaults.SlotsQuiverLarge:
                this.texture = this.GUI_TEXTURE_LARGE;
                break;
            case Defaults.SlotsQuiverMedium:
                this.texture = this.GUI_TEXTURE_MEDIUM;
                break;
            case Defaults.SlotsQuiverSmall:
                this.texture = this.GUI_TEXTURE_SMALL;
                break;
            default:
                this.texture =
                        ResourceLocation.fromNamespaceAndPath(
                                ModSpartanWeaponry.ID, "textures/gui/missingno.png");
                Log.error("Missing texture for GUI for quiver: " + this.quiver.getHoverName());
                break;
        }
    }

    @Override
    protected void init() {
        super.init();

        // TODO: Combine tooltips for priority slot
        ToggleImageButton ammoCollectButton =
                new ToggleImageButton(
                        this.isAmmoCollectEnabled,
                        this.leftPos - 18,
                        this.topPos + 20,
                        16,
                        16,
                        177,
                        39,
                        17,
                        17,
                        this.texture,
                        256,
                        256,
                        (button) -> {
                            this.isAmmoCollectEnabled = !this.isAmmoCollectEnabled;
                            button.setTooltip(
                                    Tooltip.create(
                                            this.isAmmoCollectEnabled
                                                    ? this.AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP
                                                    : this.AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
                            NetworkHandler.sendPacketToServer(
                                    new QuiverButtonPacket(this.isAmmoCollectEnabled));
                        },
                        Component.empty());
        ammoCollectButton.setTooltip(
                Tooltip.create(
                        this.isAmmoCollectEnabled
                                ? this.AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP
                                : this.AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
        this.addRenderableWidget(ammoCollectButton);
        for (int i = 0; i < this.ammoSlots; i++) {
            Slot slot = this.menu.getSlot(i);
            final int slotX = this.leftPos + slot.x - 1;
            final int slotY = this.topPos + slot.y - 1;
            Button priorityButton =
                    Button.builder(
                                    Component.empty(),
                                    (button) -> {
                                        // Do button pushing actions here
                                        this.prioritySlot = this.hoveredSlot.getContainerSlot();
                                        NetworkHandler.sendPacketToServer(
                                                new QuiverPrioritySlotPacket(
                                                        this.hoveredSlot.getContainerSlot()));
                                    })
                            .bounds(slotX, slotY, 7, 7)
                            .build();
            //            priorityButton.setTooltip(Tooltip.create(PRIORITY_BUTTON_TOOLTIP));
            this.addRenderableWidget(priorityButton);
        }
    }

    @Override
    public void render(
            @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (this.menu.getCarried().isEmpty()
                && this.hoveredSlot != null
                && this.hoveredSlot.hasItem()) {
            List<Component> tooltipList =
                    getTooltipFromItem(this.minecraft, this.hoveredSlot.getItem());

            // Show the priority button tooltip if the button is being hovered over
            if (this.hoveredSlot.index < this.ammoSlots
                    && mouseX > this.leftPos + this.hoveredSlot.x - 1
                    && mouseX < this.leftPos + this.hoveredSlot.x + 6
                    && mouseY > this.topPos + this.hoveredSlot.y - 1
                    && mouseY < this.topPos + this.hoveredSlot.y + 6)
                tooltipList.addFirst(this.PRIORITY_BUTTON_TOOLTIP);

            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        guiGraphics.blit(
                this.texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int offhandY = this.ammoSlots == Defaults.SlotsQuiverHuge ? 122 : 104;
        guiGraphics.blit(
                this.texture, this.leftPos - 27, this.topPos + offhandY, 178, offhandY, 27, 29);

        Slot highlightedSlot = this.menu.slots.get(this.prioritySlot);
        renderSlotHighlight(
                guiGraphics,
                this.leftPos + highlightedSlot.x,
                this.topPos + highlightedSlot.y,
                0,
                0x8040C040);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String name = this.quiver.getHoverName().getString();
        guiGraphics.drawString(
                this.font,
                this.quiver.getHoverName(),
                this.imageWidth / 2 - this.font.width(name) / 2,
                5,
                0x404040,
                false);
        guiGraphics.drawString(
                this.font,
                this.playerInventoryTitle,
                8,
                42 + (this.ammoSlots == Defaults.SlotsQuiverHuge ? 18 : 0),
                0x404040,
                false);
    }

    /*    protected void drawButtonTooltip(Button button, PoseStack poseStack, int x, int y)
    {
        if(menu.getCarried().isEmpty() && hoveredSlot != null && !hoveredSlot.hasItem())
            renderTooltip(poseStack, PRIORITY_BUTTON_TOOLTIP, x, y);
    }

    protected void drawAmmoCollectTooltip(Button button, PoseStack poseStack, int x, int y)
    {
        if(menu.getCarried().isEmpty())
            renderTooltip(poseStack, isAmmoCollectEnabled ? AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP : AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP, x, y);
    }*/

}
