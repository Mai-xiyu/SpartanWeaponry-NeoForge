package org.xiyu.spartanweaponryunofficial.capability;

// TODO: Curios API not available for 26.1 yet - stub out
/*
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CurioHandler implements ICurio {
    protected final QuiverBaseItem quiverItem;
    protected final ItemStack quiverStack;

    public CurioHandler(QuiverBaseItem item, ItemStack stack) {
        this.quiverItem = item;
        this.quiverStack = stack;
    }

    @Override
    public ItemStack getStack() {
        return this.quiverStack;
    }

    @Override
    public boolean canSync(SlotContext slotContext) {
        return true;
    }

    @Override
    public void readSyncData(SlotContext slotContext, CompoundTag compound) {
        if (compound == null)
            return;
        IQuiverItemHandler handler = this.quiverStack.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
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

    @Override
    public @NotNull CompoundTag writeSyncData(SlotContext slotContext) {
        return this.writeSyncDataInternal();
    }

    private static RegistryAccess getRegistryAccess() {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}
*/
public class CurioHandler {
}
