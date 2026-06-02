package org.xiyu.spartanweaponryunofficial.item;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.xiyu.spartanweaponryunofficial.api.trait.IActionTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

final class WeaponActionDispatcher {
    private WeaponActionDispatcher() {}

    static InteractionResult useOn(
            Optional<WeaponTrait> actionTrait,
            UseOnContext context,
            Supplier<InteractionResult> fallback) {
        Optional<IActionTraitCallback> callback =
                actionTrait.flatMap(WeaponTrait::getActionCallback);
        return callback.map(actionCallback -> actionCallback.useOn(context)).orElseGet(fallback);
    }

    static InteractionResultHolder<ItemStack> use(
            Optional<WeaponTrait> actionTrait,
            ItemStack stack,
            Level level,
            Player player,
            InteractionHand hand,
            Supplier<InteractionResultHolder<ItemStack>> fallback) {
        Optional<IActionTraitCallback> callback =
                actionTrait.flatMap(WeaponTrait::getActionCallback);
        return callback.map(actionCallback -> actionCallback.use(stack, level, player, hand))
                .orElseGet(fallback);
    }

    static void releaseUsing(
            Optional<WeaponTrait> actionTrait,
            ItemStack stack,
            Level level,
            LivingEntity entityLiving,
            int timeLeft,
            float attackDamage) {
        actionTrait
                .flatMap(WeaponTrait::getActionCallback)
                .ifPresent(
                        callback ->
                                callback.releaseUsing(
                                        stack, level, entityLiving, timeLeft, attackDamage));
    }

    static void onUseTick(
            Optional<WeaponTrait> actionTrait,
            ItemStack stack,
            LivingEntity entity,
            int count,
            float attackDamage) {
        actionTrait
                .flatMap(WeaponTrait::getActionCallback)
                .ifPresent(callback -> callback.onUsingTick(stack, entity, count, attackDamage));
    }

    static int getUseDuration(
            Optional<WeaponTrait> actionTrait,
            ItemStack stack,
            LivingEntity entity,
            IntSupplier fallback) {
        Optional<IActionTraitCallback> callback =
                actionTrait.flatMap(WeaponTrait::getActionCallback);
        return callback.map(actionCallback -> actionCallback.getUseDuration(stack, entity))
                .orElseGet(fallback::getAsInt);
    }

    static UseAnim getUseAnimation(
            Optional<WeaponTrait> actionTrait, ItemStack stack, Supplier<UseAnim> fallback) {
        Optional<IActionTraitCallback> callback =
                actionTrait.flatMap(WeaponTrait::getActionCallback);
        return callback.map(actionCallback -> actionCallback.getUseAnimation(stack))
                .orElseGet(fallback);
    }

    static boolean doesSneakBypassUse(
            Optional<WeaponTrait> actionTrait,
            ItemStack stack,
            LevelReader level,
            BlockPos pos,
            Player player,
            BooleanSupplier fallback) {
        Optional<IActionTraitCallback> callback =
                actionTrait.flatMap(WeaponTrait::getActionCallback);
        return callback.map(
                        actionCallback ->
                                actionCallback.doesSneakBypassUse(stack, level, pos, player))
                .orElseGet(fallback::getAsBoolean);
    }
}
