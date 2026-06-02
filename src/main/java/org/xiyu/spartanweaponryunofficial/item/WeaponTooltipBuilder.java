package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.util.ClientConfig;

final class WeaponTooltipBuilder {
    private WeaponTooltipBuilder() {}

    static boolean checkBuiltInMaterialCraftability(WeaponMaterial material, boolean canBeCrafted) {
        if (!ClientConfig.INSTANCE.forceDisableUncraftableTooltips.get()
                && material.getModId().equals(ModSpartanWeaponry.ID)) {
            var tag = BuiltInRegistries.ITEM.getTag(material.getRepairTag());
            if (tag.isEmpty() || tag.get().size() == 0) return false;
        }
        return canBeCrafted;
    }

    static void addUncraftableMaterialTooltip(WeaponMaterial material, List<Component> tooltip) {
        tooltip.add(
                Component.translatable(
                                String.format(
                                        "tooltip.%s.uncraftable_missing_material",
                                        ModSpartanWeaponry.ID),
                                material.getRepairTagName())
                        .withStyle(ChatFormatting.RED));
    }

    static void addTraitHeader(
            List<Component> tooltip, boolean isShiftPressed, ChatFormatting shiftKeyColor) {
        if (isShiftPressed) {
            tooltip.add(
                    Component.translatable(
                                    String.format("tooltip.%s.traits", ModSpartanWeaponry.ID),
                                    Component.translatable(
                                                    "tooltip."
                                                            + ModSpartanWeaponry.ID
                                                            + ".showing_details")
                                            .withStyle(ChatFormatting.DARK_GRAY))
                            .withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(
                    Component.translatable(
                                    String.format("tooltip.%s.traits", ModSpartanWeaponry.ID),
                                    Component.translatable(
                                                    "tooltip."
                                                            + ModSpartanWeaponry.ID
                                                            + ".show_details",
                                                    shiftKeyColor + "SHIFT")
                                            .withStyle(ChatFormatting.DARK_GRAY))
                            .withStyle(ChatFormatting.GOLD));
        }
    }
}
