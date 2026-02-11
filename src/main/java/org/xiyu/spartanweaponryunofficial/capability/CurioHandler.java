package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

/**
 * Curios integration handler - disabled for Forge 1.21.1 as Curios doesn't support this version.
 * TODO: Re-enable when Curios adds Forge 1.21.1 support
 */
public class CurioHandler {
    protected final QuiverBaseItem quiverItem;
    protected final ItemStack quiverStack;

    public CurioHandler(QuiverBaseItem item, ItemStack stack) {
        this.quiverItem = item;
        this.quiverStack = stack;
    }

    public ItemStack getStack() {
        return this.quiverStack;
    }

    public boolean canSync() {
        return true;
    }

    public void readSyncData(CompoundTag compound) {
        if (compound == null)
            return;
        IQuiverItemHandler handler = ModCapabilities.getQuiverHandler(this.quiverStack);
        if (handler instanceof QuiverItemStackHandler quiverHandler) {
            CompoundTag ammo = compound.getCompound(QuiverBaseItem.NBT_AMMO);
            if (!ammo.isEmpty())
                quiverHandler.deserializeNBT(getRegistryAccess(), ammo);
        }
    }

    private CompoundTag writeSyncDataInternal() {
        CompoundTag tag = new CompoundTag();
        CompoundTag ammo = ItemStackDataHelper.getTag(this.quiverStack).getCompound(QuiverBaseItem.NBT_AMMO);
        if (!ammo.isEmpty())
            tag.put(QuiverBaseItem.NBT_AMMO, ammo);
        return tag;
    }

    public @NotNull CompoundTag writeSyncData() {
        return this.writeSyncDataInternal();
    }

    private static RegistryAccess getRegistryAccess() {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}
