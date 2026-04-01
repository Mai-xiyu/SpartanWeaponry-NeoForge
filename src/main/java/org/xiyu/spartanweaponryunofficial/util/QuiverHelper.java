package org.xiyu.spartanweaponryunofficial.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
// TODO: Curios API not available for 26.1 yet
// import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
// import top.theillusivec4.curios.api.CuriosApi;
// import top.theillusivec4.curios.api.SlotResult;
// import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class QuiverHelper {
    public interface IQuiverInfo {
        boolean isQuiver(ItemStack stack);

        boolean isWeapon(ItemStack stack);

        boolean isAmmo(ItemStack stack);
    }

    public static final Predicate<ItemStack> ARROW_QUIVER = (stack) -> stack.is(ModItemTags.ARROW_QUIVERS);
    public static final Predicate<ItemStack> BOLT_QUIVER = (stack) -> stack.is(ModItemTags.BOLT_QUIVERS);
    public static final Predicate<ItemStack> HEAVY_CROSSBOW = (stack) -> stack.is(ModItemTags.HEAVY_CROSSBOWS);

    public static List<IQuiverInfo> info = ImmutableList.of(
            new IQuiverInfo() {
                @Override
                public boolean isQuiver(ItemStack stack) {
                    return !stack.isEmpty() && BOLT_QUIVER.test(stack);
                }

                @Override
                public boolean isWeapon(ItemStack stack) {
                    return !stack.isEmpty() && HEAVY_CROSSBOW.test(stack);
                }

                @Override
                public boolean isAmmo(ItemStack stack) {
                    return !stack.isEmpty() && HeavyCrossbowItem.BOLT.test(stack);
                }

            },
            new IQuiverInfo() {
                @Override
                public boolean isQuiver(ItemStack stack) {
                    return !stack.isEmpty() && ARROW_QUIVER.test(stack);
                }

                @Override
                public boolean isWeapon(ItemStack stack) {
                    return !stack.isEmpty() && (stack.getItem() instanceof ProjectileWeaponItem && !(stack.getItem() instanceof HeavyCrossbowItem));
                }

                @Override
                public boolean isAmmo(ItemStack stack) {
                    return !stack.isEmpty() && ProjectileWeaponItem.ARROW_ONLY.test(stack);
                }
            });

    public static ItemStack findFirstOfType(Player player, IQuiverInfo info) {
        // Find a quiver, if possible.
        // TODO: Curios slot support disabled for 26.1
        /*
        if (CuriosHelper.LOADED) {
            Optional<SlotResult> opt = getQuiverCurio(player);
            if (opt.isPresent() && info.isQuiver(opt.get().stack()))
                return opt.get().stack();
        }
        */
        // ... or via the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && info.isQuiver(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Find all valid Quivers in the player's hotbar or Curios slot (if Curios is installed), regardless of quiver type
     */
    public static List<ItemStack> findValidQuivers(Player player) {
        List<ItemStack> result = new ArrayList<>();

        // Find a quiver, if possible.
        // TODO: Curios back slot support disabled for 26.1
        /*
        if (CuriosHelper.LOADED) {
            Optional<SlotResult> opt = getQuiverCurio(player);
            opt.ifPresent(slotResult -> result.add(slotResult.stack()));
        }
        */
        // ... or via the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof QuiverBaseItem))
                result.add(stack);
        }

        return result;
    }

    /**
     * Find the first found Quiver in the player's hotbar or Curios slot (if Curios is installed), regardless of quiver type
     */
    public static ItemStack findFirstQuiver(Player player) {
        // Find a quiver, if possible.
        // TODO: Curios back slot support disabled for 26.1
        /*
        if (CuriosHelper.LOADED) {
            Optional<SlotResult> opt = getQuiverCurio(player);
            if (opt.isPresent())
                return opt.get().stack();
        }
        */
        // ... or via the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof QuiverBaseItem))
                return stack;
        }

        return ItemStack.EMPTY;
    }

    /* TODO: Curios API not available for 26.1 yet
    public static Optional<SlotResult> getQuiverCurio(Player player) {
        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player);
        return handler.isPresent() ? handler.orElseThrow().findFirstCurio((stack) -> stack.getItem() instanceof QuiverBaseItem) : Optional.empty();
    }
    */
}
