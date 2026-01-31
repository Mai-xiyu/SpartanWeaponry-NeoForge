package com.oblivioussp.spartanweaponry.client.gui.container;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.capability.IQuiverItemHandler;
import com.oblivioussp.spartanweaponry.client.gui.components.ToggleImageButton;
import com.oblivioussp.spartanweaponry.init.ModCapabilities;
import com.oblivioussp.spartanweaponry.inventory.QuiverBaseMenu;
import com.oblivioussp.spartanweaponry.item.QuiverBaseItem;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;
import com.oblivioussp.spartanweaponry.network.NetworkHandler;
import com.oblivioussp.spartanweaponry.network.QuiverButtonPacket;
import com.oblivioussp.spartanweaponry.network.QuiverPrioritySlotPacket;
import com.oblivioussp.spartanweaponry.util.Defaults;
import com.oblivioussp.spartanweaponry.util.Log;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class QuiverBaseScreen<T extends QuiverBaseMenu> extends AbstractContainerScreen<T> 
{
	protected final ResourceLocation GUI_TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_small.png");
	protected final ResourceLocation GUI_TEXTURE_MEDIUM = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_medium.png");
	protected final ResourceLocation GUI_TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_large.png");
	protected final ResourceLocation GUI_TEXTURE_HUGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/quiver_huge.png");
	
	protected final Component PRIORITY_BUTTON_TOOLTIP = Component.literal("[").append(Component.translatable("gui." + ModSpartanWeaponry.ID + ".set_priority_slot")).append(Component.literal("]"));
	protected final Component AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP = Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_enabled");
	protected final Component AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP = Component.translatable("gui." + ModSpartanWeaponry.ID + ".ammo_collect_disabled");
	
	protected final ResourceLocation texture;
	protected final ItemStack quiver;
	protected final int ammoSlots;
	protected int prioritySlot = 0;
	protected boolean isAmmoCollectEnabled;

	public QuiverBaseScreen(T screenContainer, Inventory inv, Component title) 
	{
		super(screenContainer, inv, title);
		quiver = screenContainer.getQuiverStack();
		prioritySlot = ItemStackDataHelper.getTag(quiver).getInt(QuiverBaseItem.NBT_PROIRITY_SLOT);
		isAmmoCollectEnabled = ItemStackDataHelper.getTag(quiver).getBoolean(QuiverBaseItem.NBT_AMMO_COLLECT);
		
		IQuiverItemHandler handler = quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
		ammoSlots = handler != null ? handler.getSlots() : Defaults.SlotsQuiverSmall;

		switch(ammoSlots)
		{
		case Defaults.SlotsQuiverHuge:
			texture = GUI_TEXTURE_HUGE;
			break;
		case Defaults.SlotsQuiverLarge:
			texture = GUI_TEXTURE_LARGE;
			break;
		case Defaults.SlotsQuiverMedium:
			texture = GUI_TEXTURE_MEDIUM;
			break;
		case Defaults.SlotsQuiverSmall:
			texture = GUI_TEXTURE_SMALL;
			break;
		default:
			texture = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/missingno.png");
			Log.error("Missing texture for GUI for quiver: " + quiver.getHoverName().toString());
			break;
		}
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		// TODO: Combine tooltips for priority slot
		ToggleImageButton ammoCollectButton = new ToggleImageButton(isAmmoCollectEnabled, leftPos - 18, topPos + 20, 16, 16, 177, 39, 17, 17, texture, 256, 256, (button) ->
		{
			isAmmoCollectEnabled = !isAmmoCollectEnabled;
			button.setTooltip(Tooltip.create(isAmmoCollectEnabled ? AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP : AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
			NetworkHandler.sendPacketToServer(new QuiverButtonPacket(isAmmoCollectEnabled));
		}, Component.empty());
		ammoCollectButton.setTooltip(Tooltip.create(isAmmoCollectEnabled ? AMMO_COLLECT_ENABLED_BUTTON_TOOLTIP : AMMO_COLLECT_DISABLED_BUTTON_TOOLTIP));
		addRenderableWidget(ammoCollectButton);
		for(int i = 0; i < ammoSlots; i++)
		{
			Slot slot = menu.getSlot(i);
			final int slotX = leftPos + slot.x - 1;
			final int slotY = topPos + slot.y - 1;
			Button priorityButton = Button.builder(Component.empty(), (button) -> 
			{
				// Do button pushing actions here
				prioritySlot = hoveredSlot.getContainerSlot();
				NetworkHandler.sendPacketToServer(new QuiverPrioritySlotPacket(hoveredSlot.getContainerSlot()));
			}).bounds(slotX, slotY, 7, 7).build();
//			priorityButton.setTooltip(Tooltip.create(PRIORITY_BUTTON_TOOLTIP));
			addRenderableWidget(priorityButton);
		}
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) 
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		if(menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem())
		{
			List<Component> tooltipList = getTooltipFromItem(minecraft, hoveredSlot.getItem());
			
			// Show the priority button tooltip if the button is being hovered over
			if(hoveredSlot.index < ammoSlots && 
					mouseX > leftPos + hoveredSlot.x - 1 && mouseX < leftPos + hoveredSlot.x + 6 &&
					mouseY > topPos + hoveredSlot.y - 1 && mouseY < topPos + hoveredSlot.y + 6)
				tooltipList.add(0, PRIORITY_BUTTON_TOOLTIP);
			
			renderTooltip(guiGraphics, mouseX, mouseY);
		}
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		int offhandY = ammoSlots == Defaults.SlotsQuiverHuge ? 122 : 104;
		guiGraphics.blit(texture, leftPos - 27, topPos + offhandY, 178, offhandY, 27, 29);

		Slot highlightedSlot = menu.slots.get(prioritySlot);
		renderSlotHighlight(guiGraphics, leftPos + highlightedSlot.x, topPos + highlightedSlot.y, 0, 0x8040C040);
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) 
	{
		String name = quiver.getHoverName().getString();
		guiGraphics.drawString(font, quiver.getHoverName(), imageWidth / 2 - font.width(name) / 2, 5, 0x404040, false);
		guiGraphics.drawString(font, playerInventoryTitle, 8, 42 + (ammoSlots == Defaults.SlotsQuiverHuge ? 18 : 0), 0x404040, false);
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
