package org.xiyu.spartanweaponryunofficial.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

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
        // Via a Curios slot... (Disabled - Curios not available for Forge 1.21.1)
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
        // Via the Curios back slot (Disabled - Curios not available for Forge 1.21.1)
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
        // Via the Curios back slot (Disabled - Curios not available for Forge 1.21.1)
        // ... or via the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof QuiverBaseItem))
                return stack;
        }

        return ItemStack.EMPTY;
    }

    // Disabled - Curios not available for Forge 1.21.1
    public static Optional<Object> getQuiverCurio(Player player) {
        return Optional.empty();
    }
}
