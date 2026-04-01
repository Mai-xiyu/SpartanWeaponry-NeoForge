package org.xiyu.spartanweaponryunofficial.client.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.NonNullList;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBoltMenu;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.QuiverTooltip;
import org.xiyu.spartanweaponryunofficial.util.Defaults;

public class ClientQuiverTooltip implements ClientTooltipComponent {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/gui/tooltip/quiver.png");
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

    public ClientQuiverTooltip(QuiverTooltip quiverIn) {
        this.items = quiverIn.getItems();
        this.itemsSize = this.items.size();
        this.prioritySlot = quiverIn.getPrioritySlot();
        this.slotsX = this.itemsSize == Defaults.SlotsQuiverHuge ? 6 : MAX_SLOTS_X;
        this.isBoltQuiver = quiverIn.isBoltQuiver();
    }

    @Override
    public int getHeight(@NotNull Font font) {
        return this.gridSizeY() * SLOT_SIZE_Y + BORDER_HEIGHT + BORDER_HEIGHT + 2;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return this.gridSizeX() * SLOT_SIZE_X + BORDER_WIDTH_LEFT + BORDER_WIDTH_RIGHT;
    }

    private int gridSizeX() {
        return Math.min(this.slotsX, this.itemsSize);
    }

    private int gridSizeY() {
        return Mth.ceil((float) this.itemsSize / (float) this.gridSizeX());
    }

    @Override
    public void extractImage(@NotNull Font fontIn, int posXIn, int posYIn, int widthIn, int heightIn, @NotNull GuiGraphicsExtractor guiGraphics) {

        // Left Border

        for (int i = 0; i < this.itemsSize; i++) {
            this.renderSlot(i, fontIn, posXIn, posYIn, guiGraphics);
        }

        boolean drawLongVertBorders = this.itemsSize == Defaults.SlotsQuiverHuge;
        this.blitPart(guiGraphics, posXIn, posYIn, drawLongVertBorders ? TexturePart.BORDER_LONG_LEFT : TexturePart.BORDER_LEFT);

        for (int i = 0; i < this.gridSizeX(); i++) {
            this.blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (i * SLOT_SIZE_X), posYIn, TexturePart.BORDER_UP);
            this.blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (i * SLOT_SIZE_X), posYIn + BORDER_HEIGHT + (this.gridSizeY() * SLOT_SIZE_Y), TexturePart.BORDER_DOWN);
        }

        this.blitPart(guiGraphics, posXIn + BORDER_WIDTH_LEFT + (this.gridSizeX() * SLOT_SIZE_X), posYIn,
                drawLongVertBorders ? TexturePart.BORDER_LONG_RIGHT : TexturePart.BORDER_RIGHT);
    }

    public void renderSlot(int slotIdxIn, Font fontIn, int posXIn, int posYIn, GuiGraphicsExtractor guiGraphics) {

        ItemStack stackToDraw = this.items.get(slotIdxIn);

        int slotX = posXIn + BORDER_WIDTH_LEFT + ((slotIdxIn % this.gridSizeX()) * SLOT_SIZE_X);
        int slotY = posYIn + BORDER_HEIGHT + (Mth.floor((float) slotIdxIn / (float) this.slotsX) * SLOT_SIZE_Y);

        this.blitPart(guiGraphics, slotX, slotY, TexturePart.SLOT);

        Minecraft mc = Minecraft.getInstance();
        TextureAtlasSprite sprite = mc.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(this.isBoltQuiver ? QuiverBoltMenu.EMPTY_BOLT_SLOT : QuiverArrowMenu.EMPTY_ARROW_SLOT);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, slotX + 1, slotY + 1, 16, 16);

        if (slotIdxIn == this.prioritySlot)
            guiGraphics.fill(slotX + 1, slotY + 1, slotX + 1 + 16, slotY + 1 + 16, 0x8040C040);
        if (!stackToDraw.isEmpty()) {
            guiGraphics.item(stackToDraw, slotX + 1, slotY + 1);
            guiGraphics.itemDecorations(fontIn, stackToDraw, slotX + 1, slotY + 1);
        }
    }

    protected void blitPart(GuiGraphicsExtractor guiGraphics, int posXIn, int posYIn, TexturePart partIn) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, posXIn, posYIn, (float) partIn.x, (float) partIn.y, partIn.width, partIn.height, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    protected enum TexturePart {
        SLOT(0, 0, 18, 18),
        BORDER_LEFT(0, 25, BORDER_WIDTH_LEFT, 22),
        BORDER_RIGHT(9, 25, BORDER_WIDTH_RIGHT, 22),
        BORDER_LONG_LEFT(0, 48, BORDER_WIDTH_LEFT, 40),
        BORDER_LONG_RIGHT(9, 48, BORDER_WIDTH_RIGHT, 40),
        BORDER_UP(0, 19, 18, BORDER_HEIGHT),
        BORDER_DOWN(0, 22, 18, BORDER_HEIGHT);

        public final int x, y, width, height;

        TexturePart(int xIn, int yIn, int widthIn, int heightIn) {
            this.x = xIn;
            this.y = yIn;
            this.width = widthIn;
            this.height = heightIn;
        }
    }
}
