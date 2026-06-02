package org.xiyu.spartanweaponryunofficial.item;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

final class ThrowingWeaponStackState {
    private ThrowingWeaponStackState() {}

    static void init(ItemStack stack, boolean initUuid) {
        normalize(stack, Integer.MAX_VALUE, initUuid);
    }

    static void normalize(ItemStack stack, int maxAmmo, boolean ensureUuid) {
        int safeMaxAmmo = Math.max(0, maxAmmo);
        ItemStackDataHelper.updateTag(
                stack,
                tag -> {
                    int ammoUsed =
                            Mth.clamp(tag.getInt(ThrowingWeaponItem.NBT_AMMO_USED), 0, safeMaxAmmo);
                    tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, ammoUsed);

                    if (ensureUuid && !tag.hasUUID(ThrowingWeaponItem.NBT_UUID)) {
                        tag.putUUID(ThrowingWeaponItem.NBT_UUID, UUID.randomUUID());
                        if (shouldMarkOriginal(tag)) {
                            tag.putBoolean(ThrowingWeaponItem.NBT_ORIGINAL, true);
                        }
                    }
                });
    }

    static int getAmmoUsed(ItemStack stack) {
        return ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
    }

    static int getAmmoRemaining(ItemStack stack, int maxAmmo) {
        int safeMaxAmmo = Math.max(0, maxAmmo);
        return Mth.clamp(safeMaxAmmo - getAmmoUsed(stack), 0, safeMaxAmmo);
    }

    static boolean hasAmmoRemaining(ItemStack stack, int maxAmmo) {
        return getAmmoUsed(stack) < Math.max(0, maxAmmo);
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

    private static boolean shouldMarkOriginal(CompoundTag tag) {
        return !tag.getBoolean(ThrowingWeaponItem.NBT_RECOVERED)
                && (!tag.contains(ThrowingWeaponItem.NBT_ORIGINAL)
                        || tag.getBoolean(ThrowingWeaponItem.NBT_ORIGINAL));
    }
}
