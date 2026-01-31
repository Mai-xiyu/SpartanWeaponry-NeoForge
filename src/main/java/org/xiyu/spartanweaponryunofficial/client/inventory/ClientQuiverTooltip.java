package org.xiyu.spartanweaponryunofficial.client.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBoltMenu;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.QuiverTooltip;
import org.xiyu.spartanweaponryunofficial.util.Defaults;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class ClientQuiverTooltip implements ClientTooltipComponent 
{
	private static final ResourceLocation TEXTURE = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/gui/tooltip/quiver.png");
	private static final int SLOT_SIZE_X = 18;
	private static final int SLOT_SIZE_Y = 18;
	private static final int BORDER_WIDTH_LEFT = 7;
	private static final int BORDER_WIDTH_RIGHT = 9;
	private static final int BORDER_HEIGHT = 2;
	private static final int MAX_SLOTS_X = 9;
	private static final int TEXTURE_SIZE = 128;
	private static final int BLIT_OFFSET = 0;
	
	private final NonNullList<ItemStack> items;
	private final int itemsSize;
	private final int prioritySlot;
	private final int slotsX;
	private final boolean isBoltQuiver;
	
	public ClientQuiverTooltip(QuiverTooltip quiverIn)
	{
		items = quiverIn.getItems();
		itemsSize = items.size();
		prioritySlot = quiverIn.getPrioritySlot();
		slotsX = itemsSize == Defaults.SlotsQuiverHuge ? 6 : MAX_SLOTS_X;
		isBoltQuiver = quiverIn.isBoltQuiver();
	}

	@Override
	public int getHeight() 
	{
		return gridSizeY() * SLOT_SIZE_Y + BORDER_HEIGHT + BORDER_HEIGHT + 2;
	}

	@Override
	public int getWidth(Font font) 
	{
		return gridSizeX() * SLOT_SIZE_X + BORDER_WIDTH_LEFT + BORDER_WIDTH_RIGHT;
	}
	
	private int gridSizeX()
	{
		return Math.min(slotsX, itemsSize);
	}
	
	private int gridSizeY()
	{
		return Mth.ceil((float)itemsSize / (float)gridSizeX());
	}

	@Override
	public void renderImage(Font fontIn, int posXIn, int posYIn, GuiGraphics guiGraphics) 
	{
		
		// Left Border
		
		for(int i = 0; i < itemsSize; i++)
		{
			renderSlot(i, fontIn, posXIn, posYIn, guiGraphics);
		}

		boolean drawLongVertBorders = itemsSize == Defaults.SlotsQuiverHuge;
		blitPart(guiGraphics, posXIn, posYIn, drawLongVertBorders ? TexturePart.BORDER_LONG_LEFT : TexturePart.BORDER_LEFT);
		
		for(int i = 0; i < gridSizeX(); i++)
		{
			blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (i * SLOT_SIZE_X), posYIn, TexturePart.BORDER_UP);
			blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (i * SLOT_SIZE_X), posYIn + BORDER_HEIGHT + (gridSizeY() * SLOT_SIZE_Y), TexturePart.BORDER_DOWN);
		}
		
		blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (gridSizeX() * SLOT_SIZE_X), posYIn, 
				drawLongVertBorders ? TexturePart.BORDER_LONG_RIGHT : TexturePart.BORDER_RIGHT);
	}
	
	public void renderSlot(int slotIdxIn, Font fontIn, int posXIn, int posYIn, GuiGraphics guiGraphics)
	{
		
		ItemStack stackToDraw = items.get(slotIdxIn);
		
		int slotX = posXIn + BORDER_WIDTH_LEFT + ((slotIdxIn % gridSizeX()) * SLOT_SIZE_X);
		int slotY = posYIn + BORDER_HEIGHT + (Mth.floor((float)slotIdxIn / (float)slotsX) * SLOT_SIZE_Y);

		blitPart(guiGraphics, slotX, slotY, TexturePart.SLOT);
		
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft mc = Minecraft.getInstance();
		TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(isBoltQuiver ? QuiverBoltMenu.EMPTY_BOLT_SLOT : QuiverArrowMenu.EMPTY_ARROW_SLOT);
//		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
		guiGraphics.blit(slotX + 1, slotY + 1, BLIT_OFFSET, 16, 16, sprite);
		
		if(slotIdxIn == prioritySlot)
			AbstractContainerScreen.renderSlotHighlight(guiGraphics, slotX + 1, slotY + 1, BLIT_OFFSET + 10, 0x8040C040);
		if(!stackToDraw.isEmpty())
		{
			guiGraphics.renderItem(stackToDraw, slotX + 1, slotY + 1);
			guiGraphics.renderItemDecorations(fontIn, stackToDraw, slotX + 1, slotY + 1);
		}
	}
	
	protected void blitPart(GuiGraphics guiGraphics, int posXIn, int posYIn, TexturePart partIn)
	{
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		guiGraphics.blit(TEXTURE, posXIn, posYIn, BLIT_OFFSET, partIn.x, partIn.y, partIn.width, partIn.height, TEXTURE_SIZE, TEXTURE_SIZE);
	}
	
	protected enum TexturePart
	{
		SLOT(0, 0, 18, 18),
		BORDER_LEFT(0, 25, BORDER_WIDTH_LEFT, 22),
		BORDER_RIGHT(9, 25, BORDER_WIDTH_RIGHT, 22),
		BORDER_LONG_LEFT(0, 48, BORDER_WIDTH_LEFT, 40),
		BORDER_LONG_RIGHT(9, 48, BORDER_WIDTH_RIGHT, 40),
		BORDER_UP(0, 19, 18, BORDER_HEIGHT),
		BORDER_DOWN(0, 22, 18, BORDER_HEIGHT);
		
		public final int x, y, width, height;
		
		private TexturePart(int xIn, int yIn, int widthIn, int heightIn)
		{
			x = xIn;
			y = yIn;
			width = widthIn;
			height = heightIn;
		}
	}
}
