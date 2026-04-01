package org.xiyu.spartanweaponryunofficial.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class ItemStackDataHelper {
    private ItemStackDataHelper() {
    }

    public static boolean hasTag(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    public static CompoundTag getTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void updateTag(ItemStack stack, Consumer<CompoundTag> updater) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, updater);
    }

    @Nullable
    public static CompoundTag getTagElement(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag.getCompound(key).orElse(null);
    }

    public static CompoundTag getOrCreateTagElement(ItemStack stack, String key) {
        final CompoundTag[] result = new CompoundTag[1];
        updateTag(stack, tag -> {
            CompoundTag element = tag.getCompound(key).orElse(null);
            if (element == null || element.isEmpty()) {
                element = new CompoundTag();
                tag.put(key, element);
            }
            result[0] = element;
        });
        return result[0] == null ? new CompoundTag() : result[0];
    }
}
