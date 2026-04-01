package org.xiyu.spartanweaponryunofficial.client.gui.container;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
    protected final Identifier GUI_TEXTURE_SMALL = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_small.png");
    protected final Identifier GUI_TEXTURE_MEDIUM = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_medium.png");
    protected final Identifier GUI_TEXTURE_LARGE = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_large.png");
    protected final Identifier GUI_TEXTURE_HUGE = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_huge.png");

    protected final Component PRIORITY_BUTTON_TOOLTIP = Component.literal("[").append(Component.translatable("gui." + ModSpartanWeaponry.ID + ".set_priority_slot")).append(Component.literal("]"));
    protected final Component AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP = Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_enabled");
    protected final Component AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP = Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_disabled");

    protected final Identifier texture;
    protected final ItemStack quiver;
    protected final int ammoSlots;
    protected int prioritySlot;
    protected boolean isAmmoCollectEnabled;

    public QuiverBaseScreen(T screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.quiver = screenContainer.getQuiverStack();
        this.prioritySlot = ItemStackDataHelper.getTag(this.quiver).getIntOr(QuiverBaseItem.NBT_PROIRITY_SLOT, 0);
        this.isAmmoCollectEnabled = ItemStackDataHelper.getTag(this.quiver).getBooleanOr(QuiverBaseItem.NBT_AMMO_COLLECT, false);

        IQuiverItemHandler handler = this.quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
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
                this.texture = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/missingno.png");
                Log.error("Missing texture for GUI for quiver: " + this.quiver.getHoverName());
                break;
        }
    }

    @Override
    protected void init() {
        super.init();

        // TODO: Combine tooltips for priority slot
        ToggleImageButton ammoCollectButton = new ToggleImageButton(this.isAmmoCollectEnabled, this.leftPos - 18, this.topPos + 20, 16, 16, 177, 39, 17, 17, this.texture, 256, 256, (button) ->
        {
            this.isAmmoCollectEnabled = !this.isAmmoCollectEnabled;
            button.setTooltip(Tooltip.create(this.isAmmoCollectEnabled ? this.AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP : this.AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
            NetworkHandler.sendPacketToServer(new QuiverButtonPacket(this.isAmmoCollectEnabled));
        }, Component.empty());
        ammoCollectButton.setTooltip(Tooltip.create(this.isAmmoCollectEnabled ? this.AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP : this.AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
        this.addRenderableWidget(ammoCollectButton);
        for (int i = 0; i < this.ammoSlots; i++) {
            Slot slot = this.menu.getSlot(i);
            final int slotX = this.leftPos + slot.x - 1;
            final int slotY = this.topPos + slot.y - 1;
            Button priorityButton = Button.builder(Component.empty(), (button) ->
            {
                // Do button pushing actions here
                this.prioritySlot = this.hoveredSlot.getContainerSlot();
                NetworkHandler.sendPacketToServer(new QuiverPrioritySlotPacket(this.hoveredSlot.getContainerSlot()));
            }).bounds(slotX, slotY, 7, 7).build();
//			priorityButton.setTooltip(Tooltip.create(PRIORITY_BUTTON_TOOLTIP));
            this.addRenderableWidget(priorityButton);
        }
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, this.leftPos, this.topPos, 0f, 0f, this.imageWidth, this.imageHeight, 256, 256);
        int offhandY = this.ammoSlots == Defaults.SlotsQuiverHuge ? 122 : 104;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, this.leftPos - 27, this.topPos + offhandY, 178f, (float) offhandY, 27, 29, 256, 256);

        Slot highlightedSlot = this.menu.slots.get(this.prioritySlot);
        guiGraphics.fill(this.leftPos + highlightedSlot.x, this.topPos + highlightedSlot.y, this.leftPos + highlightedSlot.x + 16, this.topPos + highlightedSlot.y + 16, 0x8040C040);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        String name = this.quiver.getHoverName().getString();
        guiGraphics.text(this.font, this.quiver.getHoverName(), this.imageWidth / 2 - this.font.width(name) / 2, 5, 0x404040, false);
        guiGraphics.text(this.font, this.playerInventoryTitle, 8, 42 + (this.ammoSlots == Defaults.SlotsQuiverHuge ? 18 : 0), 0x404040, false);
    }
	
/*	protected void drawButtonTooltip(Button button, PoseStack poseStack, int x, int y)
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
