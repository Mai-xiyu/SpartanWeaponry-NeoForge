package org.xiyu.spartanweaponryunofficial.api.trait;

import java.util.Optional;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import org.xiyu.spartanweaponryunofficial.api.ModToolActions;

public class MeleeBlockWeaponTrait extends WeaponTrait implements IActionTraitCallback {
    public MeleeBlockWeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
        this.isMelee = true;
    }

    @Override
    public Optional<IActionTraitCallback> getActionCallback() {
        return Optional.of(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            ItemStack usingStackIn, Level levelIn, Player playerIn, InteractionHand handIn) {
        if (playerIn.isCrouching()) return InteractionResultHolder.fail(usingStackIn);
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.consume(usingStackIn);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stackIn) {
        return UseAnim.BLOCK;
    }

    @Override
    public boolean canPerformToolAction(ItemStack stack, ItemAbility action) {
        return action == ModToolActions.MELEE_BLOCK;
    }

    public static void onBlockEvent(LivingShieldBlockEvent ev) {
        LivingEntity living = ev.getEntity();
        //        if(living.getUseItem().getItem() instanceof IWeaponTraitContainer<?> container &&
        // container.hasWeaponTrait(WeaponTraits.BLOCK_MELEE.get()))
        ItemStack stack = living.getUseItem();
        if (stack.getItem().canPerformAction(stack, ModToolActions.MELEE_BLOCK)) {
            DamageSource source = ev.getDamageSource();

            // Block Melee attacks only! Explosion, Fire, Magic, Projectile and unblockable damage
            // won't be blocked!
            // NOTE: Changes in Minecraft version 1.20.x means that it can only block specific
            // damage sources, rather than block melee damage sources only! Maybe provide a tag for
            // damage sources?
            if (!source.is(DamageTypes.PLAYER_ATTACK)
                    && !source.is(DamageTypes.MOB_ATTACK)
                    && !source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) ev.setCanceled(true);
        }
    }
}
