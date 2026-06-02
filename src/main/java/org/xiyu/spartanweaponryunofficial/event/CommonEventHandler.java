package org.xiyu.spartanweaponryunofficial.event;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.api.trait.IMeleeTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.*;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.item.SwordBaseItem;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.loot.ModLootTables;
import org.xiyu.spartanweaponryunofficial.merchant.villager.FletcherTrades;
import org.xiyu.spartanweaponryunofficial.merchant.villager.WeaponsmithTrades;
import org.xiyu.spartanweaponryunofficial.util.*;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper.IQuiverInfo;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEventHandler {
    public static Random rand = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent ev) {
        DamageSource source = ev.getSource();
        float dmgDealt = ev.getAmount();
        LivingEntity target = ev.getEntity();

        // Debug crap (code doesn't seem to be called anymore, even in an IDE)
        //        if(SharedConstants.IS_RUNNING_IN_IDE)
        //            Log.info("Damage: Entity: " + target.getDisplayName().getString() + " - Armour
        // value: "
        // + target.getArmorValue() + " - Damage value: " + dmgDealt + " - Source: " +
        // source.getMsgId());

        if (dmgDealt == 0.0f
                || source.is(DamageTypeTags.IS_PROJECTILE)
                || source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.IS_EXPLOSION)
                || /*src.isMagic() ||*/ (!source.getMsgId().equals("player")
                        && !source.getMsgId().equals("mob"))) return;

        // Ensure that the source of damage is direct (not from projectiles, etc)
        if (source.getDirectEntity() == source.getEntity()
                && source.getEntity() instanceof LivingEntity attacker) {
            Level level = attacker.level();

            ItemStack attackerStack = attacker.getMainHandItem();
            ItemStack targetStack = target.getMainHandItem();

            boolean doTraitDamageParticles = false;
            boolean doOilDamageParticles = false;

            if (!attackerStack.isEmpty()
                    && attackerStack.getItem() instanceof IWeaponTraitContainer<?> container) {
                float dmgUnmodified = dmgDealt;

                for (WeaponTrait trait : container.getAllWeaponTraits()) {
                    Optional<IMeleeTraitCallback> opt = trait.getMeleeCallback();
                    if (opt.isPresent())
                        dmgDealt =
                                opt.get()
                                        .modifyDamageDealt(
                                                container.getMaterial(),
                                                dmgDealt,
                                                source,
                                                attacker,
                                                target);
                }

                if (dmgDealt > dmgUnmodified) doTraitDamageParticles = true;
            }
            if (attackerStack.getItem() instanceof ThrowingWeaponItem
                    && ItemStackDataHelper.hasTag(attackerStack)
                    && ItemStackDataHelper.getTag(attackerStack)
                                    .getInt(ThrowingWeaponItem.NBT_AMMO_USED)
                            >= ((ThrowingWeaponItem) attackerStack.getItem())
                                    .getMaxAmmo(attackerStack, attacker.level()))
                // Only do punching damage when melee attacking using a throwing weapon without ammo
                dmgDealt = 1.0f;

            // Apply any valid oil effects
            if (WeaponOilConfig.isEnabled() && attackerStack.is(ModItemTags.OILABLE_WEAPONS)) {
                IOilHandler oilHandler =
                        attackerStack.getCapability(ModCapabilities.OIL_CAPABILITY);
                if (oilHandler != null && oilHandler.isOiled()) {
                    float dmgUnmodified = dmgDealt;
                    dmgDealt =
                            oilHandler.useEffect(dmgDealt, level, target, attacker, attackerStack);
                    if (dmgDealt != dmgUnmodified || oilHandler.getPotion().isPresent())
                        doOilDamageParticles = true;
                }
            }

            if (!targetStack.isEmpty()
                    && targetStack.getItem() instanceof IWeaponTraitContainer<?> container) {
                for (WeaponTrait trait : container.getAllWeaponTraits()) {
                    Optional<IMeleeTraitCallback> opt = trait.getMeleeCallback();
                    if (opt.isPresent())
                        dmgDealt =
                                opt.get()
                                        .modifyDamageTaken(
                                                container.getMaterial(),
                                                dmgDealt,
                                                source,
                                                attacker,
                                                target);
                }
            }

            if (dmgDealt != ev.getAmount()) {
                // Log.info(String.format("Changed damage dealt! %f -> %f", ev.getAmount(),
                // dmgDealt));
                ev.setAmount(dmgDealt);
            }

            if (!level.isClientSide) {
                // Emit particles when damage has been enhanced or mitigated, depending on what has
                // happened
                if (doTraitDamageParticles && dmgDealt > ev.getAmount())
                    ((ServerLevel) level)
                            .sendParticles(
                                    ModParticles.DAMAGE_BOOSTED.get(),
                                    target.getX(),
                                    target.getY() + (target.getBbHeight() / 2.0f),
                                    target.getZ(),
                                    8,
                                    0.2d,
                                    0.2d,
                                    0.2d,
                                    0.5d);
                else if (dmgDealt < ev.getAmount())
                    ((ServerLevel) level)
                            .sendParticles(
                                    ModParticles.DAMAGE_REDUCED.get(),
                                    target.getX(),
                                    target.getY() + (target.getBbHeight() / 2.0f),
                                    target.getZ(),
                                    8,
                                    0.2d,
                                    0.2d,
                                    0.2d,
                                    0.5d);
                if (doOilDamageParticles)
                    ((ServerLevel) level)
                            .sendParticles(
                                    ModParticles.OIL_DAMAGE_BOOSTED.get(),
                                    target.getX(),
                                    target.getY() + (target.getBbHeight() / 2.0f),
                                    target.getZ(),
                                    8,
                                    0.2d,
                                    0.2d,
                                    0.2d,
                                    0.5d);
            }
        }
    }

    @SubscribeEvent
    public static void attackEvent(LivingIncomingDamageEvent ev) {
        if (ev.getEntity() instanceof Player player
                && player.isUsingItem()
                && !player.getUseItem().isEmpty()) {
            ItemStack activeStack = player.getUseItem();

            if (activeStack.getItem() instanceof SwordBaseItem weapon
                    && weapon.hasWeaponTrait(WeaponTraits.BLOCK_MELEE.get())) {
                DamageSource source = ev.getSource();
                boolean damageItem = false;

                // Block Melee attacks only! Explosion, Fire, Magic, Projectile and unblockable
                // damage won't be blocked!
                if (source.getMsgId().equals("player")
                        || source.getMsgId().equals("mob")
                                && !source.is(DamageTypeTags.IS_EXPLOSION)
                                && !source.is(DamageTypeTags.IS_FIRE)
                                && !source.is(DamageTypeTags.IS_PROJECTILE)
                                && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                    // Do knockback due to damage.
                    Entity trueSourceEntity = source.getEntity();

                    if (trueSourceEntity instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) source.getEntity();
                        living.knockback(
                                0.3F, player.getX() - living.getX(), player.getZ() - living.getZ());
                    }
                    damageItem = true;
                }
                if (damageItem) {
                    int itemDamage = 1 + Mth.floor(ev.getAmount());
                    EquipmentSlot breakSlot =
                            player.getUsedItemHand() == InteractionHand.MAIN_HAND
                                    ? EquipmentSlot.MAINHAND
                                    : EquipmentSlot.OFFHAND;
                    activeStack.hurtAndBreak(itemDamage, player, breakSlot);
                    player.level()
                            .playSound(
                                    null,
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    SoundEvents.PLAYER_ATTACK_KNOCKBACK,
                                    SoundSource.PLAYERS,
                                    0.8f,
                                    0.8f);
                    ev.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent ev) {
        DamageSource source = ev.getSource();

        Entity e = source.getDirectEntity();
        if (e instanceof ThrowingWeaponEntity throwingWeapon) {
            int luckLevel =
                    ModEnchantments.getLevel(
                            throwingWeapon.level().registryAccess(),
                            ModEnchantments.LUCKY_THROW,
                            throwingWeapon.getWeaponItem());
            if (luckLevel > 0) {
                for (ItemEntity drop : ev.getDrops()) {
                    ItemStack dropStack = drop.getItem();
                    if (!dropStack.isEmpty()) {
                        int extra = drop.level().random.nextInt(luckLevel + 1);
                        if (extra > 0) dropStack.grow(extra);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent ev) {
        // What should be done.
        // - Check to see if a Bow or Heavy Crossbow of any kind is equipped, in main-hand or
        // off-hand.
        // - If true, get the opposite hand of the equipped weapon, and check if it's empty
        // - If it's empty, check to see if the appropriate quiver is in either the hotbar or the
        // appropriate Curios slot.
        // - If found, then place a stack of ammo in the appropriate opposite hand slot, and take
        // that ammo from the quiver.
        // -- If the weapon is unequiped, do the opposite and place the arrow stack back into the
        // quiver as appropriate.

        if ((ev.getSlot() == EquipmentSlot.MAINHAND || ev.getSlot() == EquipmentSlot.OFFHAND)
                && ev.getEntity() instanceof Player player) {
            ItemStack fromStack = ev.getFrom();
            ItemStack toStack = ev.getTo();
            EquipmentSlot oppositeHand;
            ItemStack oppositeStack;

            // Get the opposite hand for equipping ammo in.
            if (ev.getSlot() == EquipmentSlot.OFFHAND) oppositeHand = EquipmentSlot.MAINHAND;
            else oppositeHand = EquipmentSlot.OFFHAND;

            oppositeStack = player.getItemBySlot(oppositeHand);

            // Check and see if the bow has been unequipped and if the opposite hand slot has valid
            // ammo in it.
            if (!ItemStack.isSameItem(fromStack, toStack) && !oppositeStack.isEmpty()) {
                // Check if the item being switched to is blacklisted in the config
                boolean toStackBlacklisted = false;
                String toName = BuiltInRegistries.ITEM.getKey(toStack.getItem()).toString();

                // If the item being switched to is blacklisted, it will allow the quiver to put the
                // arrows away when equipped
                if (Config.INSTANCE.quiverBowBlacklist.get().contains(toName))
                    toStackBlacklisted = true;

                for (IQuiverInfo quiverInfo : QuiverHelper.info) {
                    if (quiverInfo.isWeapon(fromStack)
                            && (!quiverInfo.isWeapon(toStack) || toStackBlacklisted)
                            && quiverInfo.isAmmo(oppositeStack)) {
                        ItemStack quiver = QuiverHelper.findFirstOfType(player, quiverInfo);
                        placeAmmoIntoQuiver(player, quiver, oppositeHand);

                        oppositeStack = player.getItemBySlot(oppositeHand);
                        if (oppositeStack.isEmpty()) {
                            // If there is any offhand item data in the quiver, find it and put it
                            // back
                            CompoundTag nbt =
                                    ItemStackDataHelper.getTagElement(
                                            quiver, QuiverBaseItem.NBT_OFFHAND_MOVED);
                            if (nbt != null) {
                                String itemId = nbt.getString(QuiverBaseItem.NBT_ITEM_ID);
                                int itemSlot = nbt.getInt(QuiverBaseItem.NBT_ITEM_SLOT);
                                ItemStack offhandStack = player.getInventory().getItem(itemSlot);
                                // Check to see if the item in the slot is a match
                                if (BuiltInRegistries.ITEM
                                        .getKey(offhandStack.getItem())
                                        .toString()
                                        .equals(itemId)) {
                                    // Now move the item to the offhand from the appropriate
                                    // inventory slot
                                    player.setItemSlot(oppositeHand, offhandStack);
                                    player.getInventory().setItem(itemSlot, ItemStack.EMPTY);
                                }
                                // Delete the NBT data stored in the quiver, regardless of whether
                                // or not anything happened
                                ItemStackDataHelper.updateTag(
                                        quiver,
                                        tag -> tag.remove(QuiverBaseItem.NBT_OFFHAND_MOVED));
                            }
                            break;
                        }
                    }
                }
            }
            // Check to see if a bow has been equipped
            if (!ItemStack.isSameItem(toStack, fromStack)) {
                // Check to see if the weapon being equipped is blacklisted in the config
                String regName = BuiltInRegistries.ITEM.getKey(toStack.getItem()).toString();
                // If so, then the quiver will *NOT* take any arrows out. However, arrows will be
                // put into the Quiver
                if (Config.INSTANCE.quiverBowBlacklist.get().contains(regName)) return;

                for (IQuiverInfo quiverInfo : QuiverHelper.info) {
                    if (quiverInfo.isWeapon(toStack)) {
                        ItemStack quiver = QuiverHelper.findFirstOfType(player, quiverInfo);

                        if (!quiver.isEmpty()) {
                            // TODO: Possibly make an isEmpty() method for a custom item handler
                            IQuiverItemHandler quiverHandler =
                                    quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
                            if (quiverHandler == null) continue;
                            boolean isQuiverEmpty = quiverHandler.isEmpty();
                            /*for(int i = 0; i < quiverHandler.getSlots(); i++)
                            {
                                ItemStack arrowStack = quiverHandler.getStackInSlot(i);
                                if(!arrowStack.isEmpty())
                                {
                                    isQuiverEmpty = false;
                                    break;
                                }
                            }*/

                            // Check to see if the opposite hand slot is not empty; attempt to move
                            // it somewhere else
                            if (!isQuiverEmpty
                                    && !oppositeStack.isEmpty()
                                    && !quiverInfo.isAmmo(oppositeStack)) {
                                // Find the nearest empty slot...
                                int emptySlot = -1;
                                for (int i = 0; i < player.getInventory().items.size(); i++) {
                                    ItemStack playerStack = player.getInventory().items.get(i);
                                    if (playerStack.isEmpty()) {
                                        emptySlot = i;
                                        break;
                                    }
                                }
                                // If found, place it in that empty slot
                                if (emptySlot != -1) {
                                    String itemId =
                                            BuiltInRegistries.ITEM
                                                    .getKey(oppositeStack.getItem())
                                                    .toString();
                                    // Store the relevant data to find the offhand item in the
                                    // Quiver NBT Tag
                                    CompoundTag nbt =
                                            ItemStackDataHelper.getOrCreateTagElement(
                                                    quiver, QuiverBaseItem.NBT_OFFHAND_MOVED);
                                    nbt.putString(QuiverBaseItem.NBT_ITEM_ID, itemId);
                                    nbt.putInt(QuiverBaseItem.NBT_ITEM_SLOT, emptySlot);

                                    // Now move the item from the offhand to the appropriate
                                    // inventory slot
                                    player.getInventory().setItem(emptySlot, oppositeStack);
                                    player.setItemSlot(oppositeHand, ItemStack.EMPTY);
                                }
                            }

                            if (player.getItemBySlot(oppositeHand).isEmpty())
                                takeAmmoFromQuiver(player, quiver, oppositeHand);

                            oppositeStack = player.getItemBySlot(oppositeHand);
                            if (!oppositeStack.isEmpty()) break;
                        }
                    }
                }
            }
        }
    }

    /** Find the first empty stack in the quiver, then place the arrow item in that slot. */
    protected static void placeAmmoIntoQuiver(
            Player player, ItemStack quiver, EquipmentSlot oppositeHandSlot) {
        if (!quiver.isEmpty()) {
            IQuiverItemHandler quiverHandler =
                    quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
            if (quiverHandler == null) return;
            ItemStack arrowStack = player.getItemBySlot(oppositeHandSlot);

            int prioritySlot =
                    ItemStackDataHelper.getTag(quiver).getInt(QuiverBaseItem.NBT_PROIRITY_SLOT);
            arrowStack = quiverHandler.insertItem(prioritySlot, arrowStack, false);
            if (!arrowStack.isEmpty()) {
                for (int j = 0; j < quiverHandler.getSlots(); j++) {
                    if (j == prioritySlot)
                        continue; // Skip the priority slot, since it's been checked already

                    arrowStack = quiverHandler.insertItem(j, arrowStack, false);
                    if (arrowStack.isEmpty()) break;
                }
            }
            player.setItemSlot(oppositeHandSlot, arrowStack);
        }
    }

    /** Find the first stack in the quiver, then place it in the opposite hand. */
    protected static void takeAmmoFromQuiver(
            Player player, ItemStack quiver, EquipmentSlot oppositeHandSlot) {
        if (!quiver.isEmpty()) {
            IQuiverItemHandler quiverHandler =
                    quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
            if (quiverHandler == null) return;
            ItemStack arrowStack;

            int prioritySlot =
                    ItemStackDataHelper.getTag(quiver).getInt(QuiverBaseItem.NBT_PROIRITY_SLOT);
            if (prioritySlot <= quiverHandler.getSlots()) {
                arrowStack = quiverHandler.extractItem(prioritySlot, 64, false);
                if (!arrowStack.isEmpty()) {
                    player.setItemSlot(oppositeHandSlot, arrowStack);
                    return;
                }

                for (int j = 0; j < quiverHandler.getSlots(); j++) {
                    if (j == prioritySlot)
                        continue; // Skip the priority slot, since it's been checked already

                    arrowStack = quiverHandler.extractItem(j, 64, false);
                    if (!arrowStack.isEmpty()) {
                        player.setItemSlot(oppositeHandSlot, arrowStack);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPickup(ItemEntityPickupEvent.Pre ev) {
        if (ev.getItemEntity().hasPickUpDelay()) return; // Cancel when in delay

        ItemStack pickedUpStack = ev.getItemEntity().getItem().copy();
        int beforeCount = pickedUpStack.getCount(), afterCount;
        Player player = ev.getPlayer();
        Level level = player.level();
        List<ItemStack> quivers = QuiverHelper.findValidQuivers(player);

        if (!quivers.isEmpty()) {
            // Loop through all valid quivers to place the item into...
            for (ItemStack quiver : quivers) {
                if (!pickedUpStack.isEmpty()
                        && !quiver.isEmpty()
                        && ((QuiverBaseItem) quiver.getItem()).isAmmoValid(pickedUpStack, quiver)) {
                    // Make sure auto-collect is enabled.
                    if (ItemStackDataHelper.getTag(quiver)
                            .getBoolean(QuiverBaseItem.NBT_AMMO_COLLECT)) {
                        // Attempt to place the arrows into the quiver.
                        IQuiverItemHandler quiverHandler =
                                quiver.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
                        if (quiverHandler == null) continue;
                        for (int i = 0; i < quiverHandler.getSlots(); i++) {
                            pickedUpStack = quiverHandler.insertItem(i, pickedUpStack, false);
                        }
                    }
                }
                if (pickedUpStack.isEmpty()) break;
            }
            afterCount = pickedUpStack.getCount();
            if (afterCount < beforeCount) {
                player.take(ev.getItemEntity(), beforeCount - afterCount);
                ev.getItemEntity().getItem().setCount(afterCount);
                level.playSound(
                        null,
                        ev.getItemEntity().getX(),
                        ev.getItemEntity().getY(),
                        ev.getItemEntity().getZ(),
                        SoundEvents.ITEM_PICKUP,
                        player.getSoundSource(),
                        0.2F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.7F + 0.0F);
            }
        }
        // Merge compatible itemstacks for throwing weapons (ammo restoration on pickup)
        if (pickedUpStack.getItem() instanceof ThrowingWeaponItem throwingWeapon) {
            boolean recoveredThrowingWeapon =
                    ItemStackDataHelper.hasTag(pickedUpStack)
                            && ItemStackDataHelper.getTag(pickedUpStack)
                                    .getBoolean(ThrowingWeaponItem.NBT_RECOVERED);
            if (tryMergeThrowingWeaponPickup(player, pickedUpStack, throwingWeapon, level)) {
                player.take(ev.getItemEntity(), 1);
                level.playSound(
                        null,
                        ev.getItemEntity().getX(),
                        ev.getItemEntity().getY(),
                        ev.getItemEntity().getZ(),
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.PLAYERS,
                        0.2F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.7F + 0.0F);
                ev.getItemEntity().getItem().setCount(0);
                ev.setCanPickup(TriState.FALSE);
            } else if (recoveredThrowingWeapon) {
                ev.setCanPickup(TriState.FALSE);
            }
        }
    }

    private static boolean tryMergeThrowingWeaponPickup(
            Player player,
            ItemStack pickedUpStack,
            ThrowingWeaponItem throwingWeapon,
            Level level) {
        if (!ItemStackDataHelper.hasTag(pickedUpStack)
                || !ItemStackDataHelper.getTag(pickedUpStack).hasUUID(ThrowingWeaponItem.NBT_UUID))
            return false;

        CompoundTag pickedUpTag = ItemStackDataHelper.getTag(pickedUpStack);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (!canMergeThrowingWeaponStacks(slotStack, pickedUpStack, pickedUpTag)) continue;

            int maxAmmo = throwingWeapon.getMaxAmmo(slotStack, player.level());
            int currentAmmo =
                    maxAmmo
                            - ItemStackDataHelper.getTag(slotStack)
                                    .getInt(ThrowingWeaponItem.NBT_AMMO_USED);
            if (currentAmmo >= maxAmmo) return true;

            int itemDamage = slotStack.getDamageValue() + pickedUpStack.getDamageValue();
            if (itemDamage > slotStack.getMaxDamage()) {
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ITEM_BREAK,
                        player.getSoundSource(),
                        0.8f,
                        0.8f + player.getRandom().nextFloat() * 0.4f);
                itemDamage -= slotStack.getMaxDamage() + 1;
            } else {
                int ammoToRestore =
                        Math.max(1, maxAmmo - pickedUpTag.getInt(ThrowingWeaponItem.NBT_AMMO_USED));
                int restoredAmmo = Mth.clamp(currentAmmo + ammoToRestore, 0, maxAmmo);
                ItemStackDataHelper.updateTag(
                        slotStack,
                        tag ->
                                tag.putInt(
                                        ThrowingWeaponItem.NBT_AMMO_USED, maxAmmo - restoredAmmo));
            }
            slotStack.setDamageValue(Mth.clamp(itemDamage, 0, slotStack.getMaxDamage()));
            return true;
        }
        return false;
    }

    private static boolean canMergeThrowingWeaponStacks(
            ItemStack slotStack, ItemStack pickedUpStack, CompoundTag pickedUpTag) {
        if (slotStack.isEmpty()
                || !ItemStack.isSameItem(slotStack, pickedUpStack)
                || !ItemStackDataHelper.hasTag(slotStack)) return false;

        CompoundTag slotTag = ItemStackDataHelper.getTag(slotStack);
        return slotTag.hasUUID(ThrowingWeaponItem.NBT_UUID)
                && slotTag.getUUID(ThrowingWeaponItem.NBT_UUID)
                        .equals(pickedUpTag.getUUID(ThrowingWeaponItem.NBT_UUID));
    }

    /**
     * Prevent dropping throwing weapons when all ammo is used (i.e. the weapon is in-flight).
     * Without this, Q-dropping the placeholder item would cause the returning projectile to lose
     * its inventory target for ammo restoration.
     */
    @SubscribeEvent
    public static void onItemToss(ItemTossEvent ev) {
        ItemStack stack = ev.getEntity().getItem();
        if (stack.getItem() instanceof ThrowingWeaponItem throwingWeapon
                && ItemStackDataHelper.hasTag(stack)) {
            int ammoUsed =
                    ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
            int maxAmmo = throwingWeapon.getMaxAmmo(stack, ev.getPlayer().level());
            if (ammoUsed >= maxAmmo) {
                // Cancel the toss - but ItemTossEvent cancellation does NOT restore the item to
                // inventory,
                // so we must add it back manually
                ev.setCanceled(true);
                ev.getPlayer().getInventory().add(stack);
            }
        }
    }

    /** Inject loot tables with weapons from this mod */
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent ev) {
        if (Config.INSTANCE.addIronWeaponsToVillageWeaponsmith.get()
                && ev.getName().equals(BuiltInLootTables.VILLAGE_WEAPONSMITH.location())) {
            Log.info("Adding Iron Weapons to the Village Weaponsmith Loot Table!");
            ev.getTable().addPool(generateLootPool(ModLootTables.INJECT_VILLAGE_WEAPONSMITH));
        } else if (Config.INSTANCE.addBowAndCrossbowLootToVillageFletcher.get()
                && ev.getName().equals(BuiltInLootTables.VILLAGE_FLETCHER.location())) {
            Log.info(
                    "Adding Longbow and Heavy Crossbow related loot to the Village Fletcher Loot Table!");
            ev.getTable().addPool(generateLootPool(ModLootTables.INJECT_VILLAGE_FLETCHER));
        } else if (Config.INSTANCE.addDiamondWeaponsToEndCity.get()
                && ev.getName().equals(BuiltInLootTables.END_CITY_TREASURE.location())) {
            Log.info("Adding Diamond Weapons to the End City Treasure Loot Table!");
            ev.getTable().addPool(generateLootPool(ModLootTables.INJECT_END_CITY_TREASURE));
        }
    }

    private static LootPool generateLootPool(ResourceKey<LootTable> lootKey) {
        return LootPool.lootPool()
                .add(generateLootEntry(lootKey))
                .setBonusRolls(UniformGenerator.between(0, 1))
                .name(ModSpartanWeaponry.ID + "_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> generateLootEntry(
            ResourceKey<LootTable> tableKey) {
        return NestedLootTable.lootTableReference(tableKey).setWeight(1);
    }

    /**
     * Allow Mobs to spawn with weapons from this mod; Zombies with most melee weapons and Skeletons
     * with Longbows
     */
    /*@SubscribeEvent
    public static void onJoinWorld(SpecialSpawn ev)
    {
        if(!Config.INSTANCE.disableSpawningZombieWithWeapon.get() && ev.getEntity() instanceof Zombie zombie)
        {
            float rand = zombie.level.random.nextFloat();
            float chance = zombie.level.getDifficulty() == Difficulty.HARD ?
                    Config.INSTANCE.zombieWithMeleeSpawnChanceHard.get().floatValue() :
                    Config.INSTANCE.zombieWithMeleeSpawnChanceNormal.get().floatValue();

            if(rand > 1 - chance)
            {
                ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(ModItemTags.ZOMBIE_SPAWN_WEAPONS);
                if(!tag.isEmpty())
                {
                    ItemStack weapon = ItemStack.EMPTY;
                    List<Item> possibleWeapons = tag.stream().toList();

                    weapon = generateRandomItem(zombie.level, possibleWeapons);

                    zombie.setItemSlot(EquipmentSlot.MAINHAND, weapon);
                }
            }
        }
        if(!Config.INSTANCE.disableSpawningSkeletonWithLongbow.get() && ev.getEntity() instanceof AbstractSkeleton skeleton)
        {
            float rand = skeleton.level.random.nextFloat();
            float chance = skeleton.level.getDifficulty() == Difficulty.HARD ?
                    Config.INSTANCE.skeletonWithLongbowSpawnChanceHard.get().floatValue() :
                    Config.INSTANCE.skeletonWithLongbowSpawnChanceNormal.get().floatValue();

            if(rand > 1 - chance)
            {
                ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(ModItemTags.SKELETON_SPAWN_LONGBOWS);
                if(!tag.isEmpty())
                {
                    ItemStack weapon = ItemStack.EMPTY;
                    List<Item> possibleWeapons = tag.stream().toList();
                    weapon = generateRandomItem(skeleton.level, possibleWeapons);
                    skeleton.setItemSlot(EquipmentSlot.MAINHAND, weapon);
                }
            }
        }
    }

    private static ItemStack generateRandomItem(Level level, List<Item> items)
    {
        float weaponRand = level.random.nextFloat();
        float divider = 1.0f / items.size();
        int idx = Mth.floor(weaponRand / divider);
        idx = idx > items.size() - 1 ? items.size() - 1 : idx;

        return new ItemStack(items.get(idx));
    }*/
    @SubscribeEvent
    public static void addVillagerTrades(VillagerTradesEvent ev) {
        if (Config.INSTANCE.disableVillagerTrading.get()) return;

        if (ev.getType() == VillagerProfession.WEAPONSMITH) {
            List<ItemListing> tradesLv1 = ev.getTrades().get(1);
            List<ItemListing> tradesLv2 = ev.getTrades().get(2);
            List<ItemListing> tradesLv3 = ev.getTrades().get(3);
            List<ItemListing> tradesLv4 = ev.getTrades().get(4);
            List<ItemListing> tradesLv5 = ev.getTrades().get(5);
            if (!WeaponsmithTrades.LVL1_ITEMS.isEmpty())
                tradesLv1.add(WeaponsmithTrades.LVL1_TRADE);
            if (!WeaponsmithTrades.LVL2_ITEMS.isEmpty())
                tradesLv2.add(WeaponsmithTrades.LVL2_TRADE);
            if (!WeaponsmithTrades.LVL3_ITEMS.isEmpty())
                tradesLv3.add(WeaponsmithTrades.LVL3_TRADE);
            if (!WeaponsmithTrades.LVL4_ITEMS.isEmpty())
                tradesLv4.add(WeaponsmithTrades.LVL4_TRADE);
            if (!WeaponsmithTrades.LVL5_ITEMS.isEmpty())
                tradesLv5.add(WeaponsmithTrades.LVL5_TRADE);
        } else if (ev.getType() == VillagerProfession.FLETCHER) {
            List<ItemListing> tradesLv1 = ev.getTrades().get(1);
            List<ItemListing> tradesLv3 = ev.getTrades().get(3);
            List<ItemListing> tradesLv5 = ev.getTrades().get(5);
            if (!Config.INSTANCE.longbows.disableRecipes.get())
                tradesLv1.add(FletcherTrades.LONGBOW_WOOD_TRADE);
            if (!Config.INSTANCE.longbows.disableRecipes.get())
                tradesLv3.add(FletcherTrades.LONGBOW_IRON_TRADE);
            if (!Config.INSTANCE.heavyCrossbows.disableRecipes.get())
                tradesLv3.add(FletcherTrades.HEAVY_CROSSBOW_TRADE);
            if (!Config.INSTANCE.heavyCrossbows.disableRecipes.get())
                tradesLv3.add(FletcherTrades.BOLT_TRADE);
            if (!Config.INSTANCE.longbows.disableRecipes.get())
                tradesLv5.add(FletcherTrades.ENCHANTED_DIAMOND_LONGBOW_TRADE);
            if (!Config.INSTANCE.heavyCrossbows.disableRecipes.get())
                tradesLv5.add(FletcherTrades.ENCHANTED_DIAMOND_HEAVY_CROSSBOW_TRADE);
        }
    }

    /** Events to supress Ender Teleportation using the Ender Disruption Mob Effect */
    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderEntity ev) {
        ev.setCanceled(checkToCancelTeleport(ev.getEntityLiving()));
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderPearl ev) {
        ev.setCanceled(checkToCancelTeleport(ev.getPlayer()));
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.ChorusFruit ev) {
        ev.setCanceled(checkToCancelTeleport(ev.getEntityLiving()));
    }

    public static boolean checkToCancelTeleport(LivingEntity teleportingEntity) {
        return teleportingEntity.hasEffect(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModMobEffects.ENDER_DISRPUTION.get()));
    }

    /**
     * Repair Throwing Weapons with other Throwing Weapons of the same type<br>
     * Allows replenishing of ammo for enchanted Throwing Weapons at an XP cost per enchantment
     */
    @SubscribeEvent
    public static void handleAnvilUpdate(AnvilUpdateEvent ev) {
        ItemStack left = ev.getLeft();
        ItemStack right = ev.getRight();
        if (left.getItem() instanceof ThrowingWeaponItem throwingWeapon
                && ItemStackDataHelper.hasTag(left)
                && ItemStackDataHelper.getTag(left).getBoolean(ThrowingWeaponItem.NBT_ORIGINAL)
                && ItemStack.isSameItem(left, right)) {
            int leftAmmo =
                    ItemStackDataHelper.getTag(left).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
            int rightAmmo =
                    ItemStackDataHelper.getTag(right).getInt(ThrowingWeaponItem.NBT_AMMO_USED);

            if (leftAmmo == 0) // Used ammo is zero when ammo is full
            return;

            // Combine ammo and durability
            int maxAmmo =
                    ((ThrowingWeaponItem) left.getItem()).getMaxAmmo(left, ev.getPlayer().level());
            int durability = left.getDamageValue() + right.getDamageValue();
            int combinedAmmo =
                    Mth.clamp(
                            (maxAmmo - leftAmmo) + (maxAmmo - rightAmmo),
                            0,
                            throwingWeapon.getMaxAmmo(left, ev.getPlayer().level()));
            // Reduce ammo count if combined durability value exceeds maximum durability value
            if (durability > left.getMaxDamage()) {
                combinedAmmo = Math.max(combinedAmmo - 1, 0);
                durability -= left.getMaxDamage();
            }
            ItemStack resultStack = ev.getLeft().copy();
            final int finalCombinedAmmo = combinedAmmo;
            ItemStackDataHelper.updateTag(
                    resultStack,
                    tag ->
                            tag.putInt(
                                    ThrowingWeaponItem.NBT_AMMO_USED, maxAmmo - finalCombinedAmmo));
            resultStack.setDamageValue(durability);

            // Calculate enchantment level to set the XP cost (This should help discourage potential
            // duping)
            ItemEnchantments enchantmentMap = EnchantmentHelper.getEnchantmentsForCrafting(left);
            int cost = 1; // 1 Level of cost per total levels of enchantment
            for (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Holder<Enchantment>> entry :
                    enchantmentMap.entrySet()) {
                cost += entry.getIntValue();
            }

            ev.setCost(cost);
            ev.setOutput(resultStack);
        }
    }

    /** Trigger Oil brewing Advancement when appropriate */
    @SubscribeEvent
    public static void onBrewPotion(PlayerBrewedPotionEvent ev) {
        if (!WeaponOilConfig.isEnabled()) return;

        ItemStack stack = ev.getStack();
        OilEffects.NONE.get();
        OilEffect oil;
        if (stack.is(ModItems.WEAPON_OIL.get())
                && (oil = OilHelper.getOilFromStack(stack)) != OilEffects.NONE.get())
            // NeoForge 1.21: BREW_OIL is now a Supplier
            ModCriteriaTriggers.BREW_OIL.get().trigger((ServerPlayer) ev.getEntity(), oil);
    }

    /** Simple Handle in-world conversion -> Stick in hand + Use on Grass */

    // Conversion of blocks when Simple Handles are crafted. Tall blocks turn into their smaller
    // variants
    private static final ImmutableMap<Block, Block> conversionMap =
            ImmutableMap.of(
                    Blocks.SHORT_GRASS,
                    Blocks.AIR,
                    Blocks.TALL_GRASS,
                    Blocks.SHORT_GRASS,
                    Blocks.SEAGRASS,
                    Blocks.WATER,
                    Blocks.TALL_SEAGRASS,
                    Blocks.SEAGRASS,
                    Blocks.FERN,
                    Blocks.AIR,
                    Blocks.LARGE_FERN,
                    Blocks.FERN);

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickBlock ev) {
        // Skip if the item is not some form of stick or if the stick is on a cooldown
        Player player = ev.getEntity();
        InteractionHand hand = ev.getHand();
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Tags.Items.RODS_WOODEN)
                || player.getCooldowns().isOnCooldown(stack.getItem())) return;

        Level level = ev.getLevel();
        BlockPos pos = ev.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        Block blockTo;

        // Check the conversion map to determine what the block turns into
        if ((blockTo = conversionMap.get(block)) != null) {
            if (block == Blocks.TALL_GRASS
                    || block == Blocks.TALL_SEAGRASS
                    || block == Blocks.LARGE_FERN) {
                // Check to see what half of the tall block has been clicked to get the proper block
                // to convert
                if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF)
                        == DoubleBlockHalf.UPPER) {
                    pos = pos.below();
                    state = level.getBlockState(pos);
                }
            }
            // Remove an item of the main stack
            player.getCooldowns().addCooldown(stack.getItem(), 5);
            stack.shrink(1);
            if (stack.getCount() <= 0) {
                stack = ItemStack.EMPTY;
                player.setItemInHand(hand, stack);
            }
            // Now spawn the converted item on the ground
            stack = new ItemStack(ModItems.SIMPLE_HANDLE.get());
            Block.popResource(level, pos, stack);

            // Now change the block appropriately
            level.setBlock(pos, blockTo.defaultBlockState(), 35);
            level.levelEvent(player, 2001, pos, Block.getId(state));
            ev.setCancellationResult(InteractionResult.CONSUME);
        }
    }
}
