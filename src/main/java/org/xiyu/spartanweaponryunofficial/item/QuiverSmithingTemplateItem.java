package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import java.util.List;

public class QuiverSmithingTemplateItem extends SmithingTemplateItem {
    private static final Identifier EMPTY_SMALL_ARROW_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_small_arrow_quiver_slot");
    private static final Identifier EMPTY_MEDIUM_ARROW_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_medium_arrow_quiver_slot");
    private static final Identifier EMPTY_LARGE_ARROW_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_large_arrow_quiver_slot");
    private static final Identifier EMPTY_SMALL_BOLT_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_small_bolt_quiver_slot");
    private static final Identifier EMPTY_MEDIUM_BOLT_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_medium_bolt_quiver_slot");
    private static final Identifier EMPTY_LARGE_BOLT_QUIVER_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_large_bolt_quiver_slot");

    private static final Identifier EMPTY_MEDIUM_QUIVER_BRACE_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_medium_quiver_brace_slot");
    private static final Identifier EMPTY_LARGE_QUIVER_BRACE_SLOT = Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "slots/empty_large_quiver_brace_slot");

    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    private static final Component QUIVER_APPLIES_TO = Component.translatable(Util.makeDescriptionId("tooltip", Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_compartment.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component QUIVER_INGREDIENTS = Component.translatable(Util.makeDescriptionId("tooltip", Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_compartment.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component QUIVER_UPGRADE_DESC = Component.translatable(Util.makeDescriptionId("tooltip", Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_compartment.upgrade_description"))).withStyle(TITLE_FORMAT);
    private static final Component QUIVER_BASE_SLOT_DESC = Component.translatable(Util.makeDescriptionId("tooltip", Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_compartment.base_slot_description")));
    private static final Component QUIVER_ADDITIONAL_SLOT_DESC = Component.translatable(Util.makeDescriptionId("tooltip", Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_compartment.additonal_slot_description")));

    public QuiverSmithingTemplateItem(Item.Properties properties) {
        super(QUIVER_APPLIES_TO,
                QUIVER_INGREDIENTS,
                QUIVER_BASE_SLOT_DESC,
                QUIVER_ADDITIONAL_SLOT_DESC,
                List.of(EMPTY_SMALL_ARROW_QUIVER_SLOT, EMPTY_MEDIUM_ARROW_QUIVER_SLOT, EMPTY_LARGE_ARROW_QUIVER_SLOT,
                        EMPTY_SMALL_BOLT_QUIVER_SLOT, EMPTY_MEDIUM_BOLT_QUIVER_SLOT, EMPTY_LARGE_BOLT_QUIVER_SLOT),
                List.of(EMPTY_MEDIUM_QUIVER_BRACE_SLOT, EMPTY_LARGE_QUIVER_BRACE_SLOT),
                properties);
    }
}
