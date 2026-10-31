package org.xiyu.spartanweaponryunofficial.gametest;

import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ArrowBaseEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoltEntity;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.item.HeavyCrossbowItem;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

@GameTestHolder(ModSpartanWeaponry.ID)
@PrefixGameTestTemplate(false)
public final class HeavyCrossbowGameTests {
    private HeavyCrossbowGameTests() {}

    @GameTest(
            templateNamespace = ModSpartanWeaponry.ID,
            template = "empty",
            timeoutTicks = 240,
            batch = "heavyCrossbow")
    public static void preserves_special_crossbow_projectiles(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setGameMode(GameType.CREATIVE);
        boolean originalMode = Config.INSTANCE.useBoltsForCrossbows.get();

        Config.INSTANCE.useBoltsForCrossbows.set(true);
        runScenario(
                helper,
                player,
                ModItems.NETHERITE_BOLT.get(),
                "Bolt",
                () ->
                        runScenario(
                                helper,
                                player,
                                ModItems.TIPPED_NETHERITE_BOLT.get(),
                                "Bolt",
                                () -> {
                                    Config.INSTANCE.useBoltsForCrossbows.set(false);
                                    runScenario(
                                            helper,
                                            player,
                                            ModItems.NETHERITE_ARROW.get(),
                                            "Arrow",
                                            () ->
                                                    runScenario(
                                                            helper,
                                                            player,
                                                            ModItems.TIPPED_NETHERITE_ARROW.get(),
                                                            "Arrow",
                                                            () -> {
                                                                Config.INSTANCE
                                                                        .useBoltsForCrossbows
                                                                        .set(originalMode);
                                                                helper.succeed();
                                                            }));
                                }));
    }

    private static void runScenario(
            GameTestHelper helper,
            ServerPlayer player,
            Item ammoItem,
            String entityNbtKey,
            Runnable next) {
        player.stopUsingItem();
        HeavyCrossbowItem crossbowItem =
                (HeavyCrossbowItem) ModItems.HEAVY_CROSSBOWS.netherite.get();
        ItemStack crossbow = crossbowItem.getDefaultInstance();
        ItemStack ammo = new ItemStack(ammoItem);
        player.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
        player.setItemInHand(InteractionHand.OFF_HAND, ammo);

        if (!crossbowItem.use(player.level(), player, InteractionHand.MAIN_HAND).getResult().consumesAction()) {
            helper.fail("Heavy crossbow refused supported projectile " + ammoItem);
            return;
        }

        int useDuration = crossbowItem.getUseDuration(crossbow, player);
        setUseItemState(
                player,
                crossbow,
                useDuration - crossbowItem.getFullLoadTicks(crossbow, player.level()));
        crossbowItem.releaseUsing(
                crossbow,
                player.level(),
                player,
                useDuration - crossbowItem.getFullLoadTicks(crossbow, player.level()));
        assertStoredProjectile(helper, crossbow, ammoItem);

        crossbowItem.releaseUsing(crossbow, player.level(), player, useDuration);
        helper.runAfterDelay(
                1,
                () -> {
                    AbstractArrow projectile = findProjectile(helper, player, ammoItem);
                    if (projectile == null) return;

                    CompoundTag entityData = new CompoundTag();
                    projectile.addAdditionalSaveData(entityData);
                    ItemStack fired =
                            ItemStack.parseOptional(
                                    player.level().registryAccess(),
                                    entityData.getCompound(entityNbtKey));
                    if (!fired.is(ammoItem)) {
                        helper.fail(
                                "Crossbow fired "
                                        + fired.getItem()
                                        + " instead of "
                                        + ammoItem);
                        return;
                    }
                    projectile.discard();
                    next.run();
                });
    }

    private static void assertStoredProjectile(GameTestHelper helper, ItemStack crossbow, Item item) {
        CompoundTag crossbowTag = ItemStackDataHelper.getTag(crossbow);
        ItemStack stored =
                ItemStack.parseOptional(
                        helper.getLevel().registryAccess(),
                        crossbowTag.getCompound(HeavyCrossbowItem.NBT_PROJECTILE));
        if (!stored.is(item))
            helper.fail(
                    "Crossbow stored "
                            + stored.getItem()
                            + " instead of "
                            + item
                            + "; tag="
                            + crossbowTag);
    }

    private static AbstractArrow findProjectile(
            GameTestHelper helper, ServerPlayer player, Item ammoItem) {
        AABB searchBox = player.getBoundingBox().inflate(64.0);
        if (ammoItem.getClass().getSimpleName().contains("Bolt")) {
            List<BoltEntity> bolts =
                    helper.getLevel().getEntitiesOfClass(BoltEntity.class, searchBox, Entity::isAlive);
            if (!bolts.isEmpty()) return bolts.get(0);
        } else {
            List<ArrowBaseEntity> arrows =
                    helper.getLevel()
                            .getEntitiesOfClass(ArrowBaseEntity.class, searchBox, Entity::isAlive);
            if (!arrows.isEmpty()) return arrows.get(0);
        }
        helper.fail("Crossbow did not spawn a projectile for " + ammoItem);
        return null;
    }

    private static void setUseItemState(
            ServerPlayer player, ItemStack useItem, int useItemRemainingTicks) {
        try {
            Field useItemField = LivingEntity.class.getDeclaredField("useItem");
            Field remainingField = LivingEntity.class.getDeclaredField("useItemRemaining");
            useItemField.setAccessible(true);
            remainingField.setAccessible(true);
            useItemField.set(player, useItem);
            remainingField.setInt(player, useItemRemainingTicks);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to prepare mock player use state", exception);
        }
    }
}
