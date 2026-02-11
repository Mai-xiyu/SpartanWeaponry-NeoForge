package org.xiyu.spartanweaponryunofficial.client.gui;

import net.minecraft.client.Minecraft;

public class AlignmentHelper {
    /**
     * Sub-alignment for use for vertical alignments
     */
    public enum VerticalAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    /**
     * Sub-alignment for use for horizontal alignments
     */
    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Exact alignment settings that comprise of the two different sub-alignments (horizontal and vertical)
     */
    public enum Alignment {
        TOP_LEFT(VerticalAlignment.TOP, HorizontalAlignment.LEFT),
        TOP_CENTER(VerticalAlignment.TOP, HorizontalAlignment.CENTER),
        TOP_RIGHT(VerticalAlignment.TOP, HorizontalAlignment.RIGHT),
        CENTER_LEFT(VerticalAlignment.CENTER, HorizontalAlignment.LEFT),
        CENTER(VerticalAlignment.CENTER, HorizontalAlignment.CENTER),
        CENTER_RIGHT(VerticalAlignment.CENTER, HorizontalAlignment.RIGHT),
        BOTTOM_LEFT(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT),
        BOTTOM_CENTER(VerticalAlignment.BOTTOM, HorizontalAlignment.CENTER),
        BOTTOM_RIGHT(VerticalAlignment.BOTTOM, HorizontalAlignment.RIGHT);

        private final VerticalAlignment vertical;
        private final HorizontalAlignment horizontal;

        Alignment(VerticalAlignment verticalIn, HorizontalAlignment horizontalIn) {
            this.vertical = verticalIn;
            this.horizontal = horizontalIn;
        }

        public VerticalAlignment getVertical() {
            return this.vertical;
        }

        public HorizontalAlignment getHorizontal() {
            return this.horizontal;
        }
    }

    public static int getAlignedX(Alignment align, int offset, int width) {
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        return switch (align.getHorizontal()) {
            case LEFT -> offset;
            case CENTER -> (scaledWidth / 2) - (width / 2) + offset;
            case RIGHT -> scaledWidth - width + offset;
        };
    }

    public static int getAlignedY(Alignment align, int offset, int height) {
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        return switch (align) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> offset;
            case CENTER_LEFT, CENTER_RIGHT -> (scaledHeight / 2) - (height / 2) + offset;
            case CENTER -> (scaledHeight / 2) - (height / 2) + 26 + offset;
            case BOTTOM_CENTER -> scaledHeight - height - 65 + offset;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> scaledHeight - height + offset;
        };
    }
}
