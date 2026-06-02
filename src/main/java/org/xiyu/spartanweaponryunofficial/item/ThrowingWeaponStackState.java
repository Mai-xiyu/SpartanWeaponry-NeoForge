package org.xiyu.spartanweaponryunofficial.item;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

final class ThrowingWeaponStackState {
    private ThrowingWeaponStackState() {}

    static void init(ItemStack stack, boolean initUuid) {
        ItemStackDataHelper.updateTag(
                stack,
                tag -> {
                    if (!tag.contains(ThrowingWeaponItem.NBT_AMMO_USED)) {
                        tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, 0);
                    }
                    if (initUuid && !tag.hasUUID(ThrowingWeaponItem.NBT_UUID)) {
                        tag.putUUID(ThrowingWeaponItem.NBT_UUID, UUID.randomUUID());
                        tag.putBoolean(ThrowingWeaponItem.NBT_ORIGINAL, true);
                    }
                });
    }

    static int getAmmoUsed(ItemStack stack) {
        return ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
    }

    static int getAmmoRemaining(ItemStack stack, int maxAmmo) {
        return maxAmmo - getAmmoUsed(stack);
    }

    static boolean hasAmmoRemaining(ItemStack stack, int maxAmmo) {
        return getAmmoUsed(stack) < maxAmmo;
    }

    static void incrementAmmoUsed(ItemStack stack) {
        ItemStackDataHelper.updateTag(
                stack,
                tag ->
                        tag.putInt(
                                ThrowingWeaponItem.NBT_AMMO_USED,
                                tag.getInt(ThrowingWeaponItem.NBT_AMMO_USED) + 1));
    }

    static boolean isNotOriginal(ItemStack stack) {
        CompoundTag tag = ItemStackDataHelper.getTag(stack);
        return tag.contains(ThrowingWeaponItem.NBT_ORIGINAL)
                && !tag.getBoolean(ThrowingWeaponItem.NBT_ORIGINAL);
    }

    static Optional<UUID> getUuid(ItemStack stack) {
        CompoundTag tag = ItemStackDataHelper.getTag(stack);
        return tag.hasUUID(ThrowingWeaponItem.NBT_UUID)
                ? Optional.of(tag.getUUID(ThrowingWeaponItem.NBT_UUID))
                : Optional.empty();
    }
}
