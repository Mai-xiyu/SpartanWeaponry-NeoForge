package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

public class QuickStrikeWeaponTrait extends MeleeCallbackWeaponTrait {
    public QuickStrikeWeaponTrait(String type, String modId) {
        super(type, modId, TraitQuality.POSITIVE);
    }

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker,
                            Entity projectile) {
        target.invulnerableTime = (int) this.getMagnitude();
    }
}
