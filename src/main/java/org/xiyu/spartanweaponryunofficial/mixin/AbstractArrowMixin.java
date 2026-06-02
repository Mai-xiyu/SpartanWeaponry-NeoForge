package org.xiyu.spartanweaponryunofficial.mixin;

import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.QuiverHelper;

/**
 * Redirects eligible arrow pickup into quiver storage before vanilla inventory pickup runs. TODO:
 * Re-check if a future NeoForge pickup event exposes enough arrow pickup state to replace this
 * injection.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends ProjectileMixin {
    /** Mixin method to collect the arrow entity and put the item in a Quiver */
    @Inject(
            at = @At("HEAD"),
            method = "playerTouch(Lnet/minecraft/world/entity/player/Player;)V",
            cancellable = true)
    private void playerTouch(Player entityIn, CallbackInfo callback) {
        Level level = this.level();
        if (!level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            //            Log.debug("Player collision with arrow entity intercepted!");
            //            boolean pickupItem = pickup == Pickup.ALLOWED || pickup ==
            // Pickup.CREATIVE_ONLY &&
            // entityIn.abilities.isCreativeMode || getNoClip() && getShooter().getUniqueID() ==
            // entityIn.getUniqueID();

            if (this.pickup == Pickup.ALLOWED) {
                // Log.debug("Arrow can be picked up! Finding a home for it!");
                // Attempt to pickup and put into the quiver first; if that fails, then do nothing
                List<ItemStack> quivers = QuiverHelper.findValidQuivers(entityIn);
                ItemStack arrowStack = this.getPickupItem();

                // Check if the player has the same arrow type and space to store more in one of
                // their hands first before placing it in the quiver
                ItemStack mainHand = entityIn.getMainHandItem();
                ItemStack offHand = entityIn.getOffhandItem();
                if ((offHand.getItem() == arrowStack.getItem()
                                && offHand.getCount() < offHand.getMaxStackSize())
                        || mainHand.getItem() == arrowStack.getItem()
                                && mainHand.getCount() < mainHand.getMaxStackSize()) return;

                if (!quivers.isEmpty()) {
                    // Log.debug("Detected quiver to check!");
                    // Loop through all valid quivers to place the item into...
                    for (ItemStack quiver : quivers) {
                        if (!arrowStack.isEmpty()
                                && !quiver.isEmpty()
                                && ((QuiverBaseItem) quiver.getItem())
                                        .isAmmoValid(arrowStack, quiver)) {
                            // Log.debug("Found a quiver to place the arrow into!");
                            // Make sure auto-collect is enabled.
                            if (ItemStackDataHelper.getTag(quiver)
                                    .getBoolean(QuiverBaseItem.NBT_AMMO_COLLECT)) {
                                // Log.debug("Inserting arrows into a quiver!");
                                // Attempt to place the arrows into the quiver.
                                IQuiverItemHandler quiverHandler =
                                        quiver.getCapability(
                                                ModCapabilities.QUIVER_ITEM_CAPABILITY);
                                if (quiverHandler == null) continue;
                                for (int i = 0; i < quiverHandler.getSlots(); i++) {
                                    arrowStack = quiverHandler.insertItem(i, arrowStack, false);
                                }
                            }
                        }
                        if (arrowStack.isEmpty()) {
                            //                            Log.debug("Picked up arrow on the ground
                            // and placed it in the
                            // quiver!");
                            Entity thisEntity = level.getEntity(this.getId());
                            assert thisEntity != null;
                            entityIn.take(thisEntity, 1);
                            this.discard();
                            level.playSound(
                                    null,
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    SoundEvents.ITEM_PICKUP,
                                    SoundSource.PLAYERS,
                                    0.2F,
                                    (this.random.nextFloat() - this.random.nextFloat()) * 0.7F
                                            + 0.0F);
                            // Cancel running the underlying method if the arrow is directly picked
                            // up
                            callback.cancel();
                            break;
                        }
                    }
                }
            }
        }
        //        callback.cancel();
    }

    @Shadow protected boolean inGround;
    @Shadow public int shakeTime;
    @Shadow public Pickup pickup;

    @Shadow
    public boolean isNoPhysics() {
        throw new IllegalStateException(
                "Mixin failed to shadow the \"AbstractArrow.isNoPhysics()\" method!");
    }

    @Shadow
    protected ItemStack getPickupItem() {
        throw new IllegalStateException(
                "Mixin failed to shadow the \"AbstractArrow.getPickupItem()\" method!");
    }
}
